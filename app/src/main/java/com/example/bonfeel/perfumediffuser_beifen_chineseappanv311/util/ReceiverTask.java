package com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.util;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;

import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.model.BonfeelFrame;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.model.Device;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.model.NetElectricsConst;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.model.TimeScale;

/**
 *
 * Author:FunFun
 * Function:接收者任务，用于对广播接收器接收到的数据进行异步处理
 * Date:2016/4/12 9:48
 */
public class ReceiverTask extends AsyncTask<byte[], Integer, Integer> {
    private String tag = "ReceiverAsyncTask";

    private Device device;
    private Handler handler;
    private static long Diffuser_Volume = 18000;
    private long volume;
    private TimeScale tempTimeScale;

    private Message message;

    private static final int QUERY_SUCCESS = 11;
    private static final int CONTROL_SUCCESS = 12;
    private static final int CAST_ERROR = 13;
    private static final int DEVICE_LEAVE = 14;

    /**
     * 构造方法名称：	ReceiverTask
     * 创建时间：		2015-2-7上午11:22:47
     * 方法描述：		TODO
     */
    public ReceiverTask(Device device, Handler handler) {
        super();
        this.device = device;
        this.handler = handler;
        tempTimeScale = new TimeScale();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected Integer doInBackground(byte[]... params) {
        int result = 0;
        // 先进行数据解析（拆帧）
        DataFrameUtil.analyzeFrame(params[0]);
        // 对拆帧后的数据进行帧头，帧尾，长度，验证码的验证
        switch (DataFrameUtil.showResult()) {
            // 验证成功
            case 1:
                // 根据控制码的判断，来确定数据是哪种数据
                switch (DataFrameUtil.frameFunc()) {
                    case 1:
                        result = QUERY_SUCCESS;
                        break;
                    case 2:
                        result = CONTROL_SUCCESS;
                        break;
                    case 3:
                        result = CAST_ERROR;
                        break;
                    case 4:
                        result = DEVICE_LEAVE;
                        break;
                }
                break;
            case 2:
                result = 20;
                break;
            case 3:
                result = 30;
                break;
            case 4:
                result = 40;
                break;
            case 5:
                result = 50;
                break;
            case 6:
                result = 60;
                break;
            case 7:
                result = 70;
                break;
            default:
                break;
        }
        return result;
    }

    @Override
    protected void onPostExecute(Integer result) {
        LogUtil.logDebug(tag,
                "analyzing data has finished, now start update info");
        message = handler.obtainMessage();
        switch (result) {
            // 外层控制码：0xC0,内层控制功能码：0xC9，查询反馈
            case QUERY_SUCCESS:
                // 要将数据取出来更新设备
                LogUtil.logDebug(tag, "====>DIFFUSER HAS QUERY SUCCESS");
                calculateResultForA();
                message.what = QUERY_SUCCESS;
                break;
            // 外层控制码：0xC0,内层控制功能码：0xD9，控制反馈
            case CONTROL_SUCCESS:
                LogUtil.logDebug(tag, "====>DIFFUSER HAS CONTROLED SUCCESS");
                if (DataFrameUtil.getDataFrame().getSubDataFrame().getData() != null) {
                    for (int i = 0; i < DataFrameUtil.getDataFrame()
                            .getSubDataFrame().getData().length; i++) {
                        if (0x00 == DataFrameUtil.getDataFrame().getSubDataFrame()
                                .getData()[i]) {
                            NetCommunicationUtil.setCompetable(false);
                            message.arg1 = 1;
                            LogUtil.logDebug(tag,
                                    "----------------unCompetable---------------");
                        }else{//fun add
                            message.arg1 = 0;
                        }
                    }
                }
                if (DataFrameUtil.getMac() != null) {
                    message.obj = DataFrameUtil.getMac();
                } else {
                    message.obj = null;
                }
                message.what = CONTROL_SUCCESS;
                break;
            // 外层控制码：0x80,内层控制功能码：0x98，主动上报反馈
            case CAST_ERROR:
                LogUtil.logDebug(tag, "====>DIFFUSER HAS SOME ERROR");
                calculateResultForError();
                message.what = CAST_ERROR;
                break;
            // 外层控制码：0xC0,内层控制功能码：0xA9，设备离线
            case DEVICE_LEAVE:
                LogUtil.logDebug(tag, "====>DIFFUSER IS LEAVE");
                message.what = DEVICE_LEAVE;
                break;
            case 20:
                LogUtil.logDebug(tag, "内帧校验位出错");
                break;
            case 30:
                LogUtil.logDebug(tag, "内帧长度出错");
                break;
            case 40:
                LogUtil.logDebug(tag, "内帧帧头或帧尾出错");
                break;
            case 50:
                LogUtil.logDebug(tag, "外帧校验位出错");
                break;
            case 60:
                LogUtil.logDebug(tag, "外帧长度出错");
                break;
            case 70:
                LogUtil.logDebug(tag, "外帧帧头或帧尾出错");
                break;
            default:
                break;
        }
        handler.sendMessage(message);
        super.onPostExecute(result);
    }

    /**
     * 方法名称：	calculateResultForError
     * 方法描述：	对于主动上报的数据单独处理
     * 参数：
     * 返回值类型：	void
     * 创建时间：	2015-3-21下午3:30:31
     */
    private void calculateResultForError() {
        byte[] content = DataFrameUtil.getDataFrame().getSubDataFrame()
                .getData();
        int count = content.length / 3;
        int index = 0;
        if (content.length % 3 == 0) {
            for (int i = 0; i < count; i++) {
                upDateState(content[index], content[index + 1],
                        content[index + 2]);
                index += 3;
            }
        } else {
            LogUtil.logDebug(tag, "脏数据");
        }
    }

    /**
     * 方法名称：	calculateResult
     * 方法描述：	处理A类的查询结果
     * 参数：
     * 返回值类型：	void
     * 创建时间：	2015-2-7上午10:24:47
     */
    private void calculateResultForA() {
        byte[] content = DataFrameUtil.getDataFrame().getSubDataFrame()
                .getData();
        int count = content.length / 3;
        int index = 0;
        if (content.length % 3 == 0) {
            for (int i = 0; i < count; i++) {
                upDateState(content[index], content[index + 1],
                        content[index + 2]);
                index += 3;
            }
        } else {
            LogUtil.logDebug(tag, "脏数据");
        }
    }

    /**
     * 方法名称：	upDateState
     * 方法描述：	根据查询回来的数据更新设备
     * 参数：			@param param
     * 参数：			@param value1
     * 参数：			@param value2
     * 返回值类型：	void
     * 创建时间：	2015-1-28上午10:15:45
     */
    private void upDateState(byte param, byte value1, byte value2) {
        switch (param) {
            case 0x01:// 设备开关
                if ((byte) 0x00 == value1) {
                    device.setPower(false);
                } else {
                    device.setPower(true);
                }
                LogUtil.logDebug(tag, "the diffuser's power is：" + device.isPower());
                break;
            case 0x02:// 当前年份
                device.getCurTime()
                        .setYear(DataFrameUtil.byteToInt(value1, value2));
                LogUtil.logDebug(tag, "the diffuser's year is："
                        + device.getCurTime().getYear());
                break;
            case 0x03:// 当前日期
                device.getCurTime().setMonth(DataFrameUtil.byteToInt(value1));
                device.getCurTime().setDate(DataFrameUtil.byteToInt(value2));
                LogUtil.logDebug(tag, "the diffuser's date is："
                        + device.getCurTime().getMonth() + " "
                        + device.getCurTime().getDate());
                break;
            // 时间段1的开始时间，结束时间，风速设置，运行时间，暂停时间，定时周期
            case 0x04:
                tempTimeScale.setStartTime(correct(DataFrameUtil.byteToInt(value1))
                        + ":" + correct(DataFrameUtil.byteToInt(value2)));
                LogUtil.logDebug(
                        tag,
                        "timescale 1's startTime is："
                                + tempTimeScale.getStartTime());
                break;
            case 0x05:
                tempTimeScale.setEndTime(correct(DataFrameUtil.byteToInt(value1))
                        + ":" + correct(DataFrameUtil.byteToInt(value2)));
                LogUtil.logDebug(tag,
                        "timescale 1's stopTime is：" + tempTimeScale.getEndTime());
                break;
            case 0x06:// (风速,暂不操作)
                break;
            case 0x07:
                tempTimeScale.setWorkTime(DataFrameUtil.byteToInt(value1));
                LogUtil.logDebug(tag,
                        "timescale 1's workTime is：" + tempTimeScale.getWorkTime());
                break;
            case 0x08:
                tempTimeScale.setRestTime(DataFrameUtil.byteToInt(value1));
                LogUtil.logDebug(tag,
                        "timescale 1's restTime is：" + tempTimeScale.getRestTime());
                break;
            case 0x09:
                tempTimeScale.setCircle(value1);
                LogUtil.logDebug(tag,
                        "timescale 1's circle is：" + tempTimeScale.getCircle());
			/*
			 * 判断该时间段是否有效，有效则存入device's timescale 否则不存
			 */
                if (isFuncTimeScale()) {
                    tempTimeScale.setId(0);
                    tempTimeScale.setWork(true);
                    if (isNewTimeScale()) {
                        device.addTimeScale(tempTimeScale);
                        LogUtil.logDebug(tag, "add timeScale 1");
                    } else {
                        device.updateTimeScale(tempTimeScale);
                        LogUtil.logDebug(tag, "update timeScale 1");
                    }
                } else {
                    LogUtil.logDebug(tag, "timeScale 1 is not aviliable");
                }
                // 接着查询下一个时间段
                queryTimeScale(2);
                break;
            // 时间段2的开始时间，结束时间，风速设置，运行时间，暂停时间，定时周期
            case 0x0A:
                tempTimeScale.setStartTime(correct(DataFrameUtil.byteToInt(value1))
                        + ":" + correct(DataFrameUtil.byteToInt(value2)));
                LogUtil.logDebug(
                        tag,
                        "timescale 2's startTime is："
                                + tempTimeScale.getStartTime());
                break;
            case 0x0B:
                tempTimeScale.setEndTime(correct(DataFrameUtil.byteToInt(value1))
                        + ":" + correct(DataFrameUtil.byteToInt(value2)));
                LogUtil.logDebug(tag,
                        "timescale 2's stopTime is：" + tempTimeScale.getEndTime());
                break;
            case 0x0C:
                break;
            case 0x0D:
                tempTimeScale.setWorkTime(DataFrameUtil.byteToInt(value1));
                LogUtil.logDebug(tag,
                        "timescale 2's workTime is：" + tempTimeScale.getWorkTime());
                break;
            case 0x0E:
                tempTimeScale.setRestTime(DataFrameUtil.byteToInt(value1));
                LogUtil.logDebug(tag,
                        "timescale 2's restTime is：" + tempTimeScale.getRestTime());
                break;
            case 0x0F:
                tempTimeScale.setCircle(value1);
                LogUtil.logDebug(tag,
                        "timescale 2's circle is：" + tempTimeScale.getCircle());
                if (isFuncTimeScale()) {
                    tempTimeScale.setId(1);
                    tempTimeScale.setWork(true);
                    if (isNewTimeScale()) {
                        device.addTimeScale(tempTimeScale);
                        LogUtil.logDebug(tag, "add timeScale 2");
                    } else {
                        device.updateTimeScale(tempTimeScale);
                        LogUtil.logDebug(tag, "update timeScale 2");
                    }
                } else {
                    LogUtil.logDebug(tag, "timeScale 2 is not aviliable");
                }
                // 接着查询下一个时间段
                queryTimeScale(3);
                break;
            // 时间段3的开始时间，结束时间，风速设置，运行时间，暂停时间，定时周期
            case 0x10:
                tempTimeScale.setStartTime(correct(DataFrameUtil.byteToInt(value1))
                        + ":" + correct(DataFrameUtil.byteToInt(value2)));
                LogUtil.logDebug(
                        tag,
                        "timescale 3's startTime is："
                                + tempTimeScale.getStartTime());
                break;
            case 0x11:
                tempTimeScale.setEndTime(correct(DataFrameUtil.byteToInt(value1))
                        + ":" + correct(DataFrameUtil.byteToInt(value2)));
                LogUtil.logDebug(tag,
                        "timescale 3's stopTime is：" + tempTimeScale.getEndTime());
                break;
            case 0x12:
                break;
            case 0x13:
                tempTimeScale.setWorkTime(DataFrameUtil.byteToInt(value1));
                LogUtil.logDebug(tag,
                        "timescale 3's workTime is：" + tempTimeScale.getWorkTime());
                break;
            case 0x14:
                tempTimeScale.setRestTime(DataFrameUtil.byteToInt(value1));
                LogUtil.logDebug(tag,
                        "timescale 3's restTime is：" + tempTimeScale.getRestTime());
                break;
            case 0x15:
                tempTimeScale.setCircle(value1);
                LogUtil.logDebug(tag,
                        "timescale 3's circle is：" + tempTimeScale.getCircle());
                if (isFuncTimeScale()) {
                    tempTimeScale.setId(2);
                    tempTimeScale.setWork(true);
                    if (isNewTimeScale()) {
                        device.addTimeScale(tempTimeScale);
                        LogUtil.logDebug(tag, "add timeScale 3");
                    } else {
                        device.updateTimeScale(tempTimeScale);
                        LogUtil.logDebug(tag, "update timeScale 3");
                    }
                } else {
                    LogUtil.logDebug(tag, "timeScale 3 is not aviliable");
                }
                // 接着查询下一个时间段
                queryTimeScale(4);
                break;
            // 时间段4的开始时间，结束时间，风速设置，运行时间，暂停时间，定时周期
            case 0x16:
                tempTimeScale.setStartTime(correct(DataFrameUtil.byteToInt(value1))
                        + ":" + correct(DataFrameUtil.byteToInt(value2)));
                LogUtil.logDebug(
                        tag,
                        "timescale 4's startTime is："
                                + tempTimeScale.getStartTime());
                break;
            case 0x17:
                tempTimeScale.setEndTime(correct(DataFrameUtil.byteToInt(value1))
                        + ":" + correct(DataFrameUtil.byteToInt(value2)));
                LogUtil.logDebug(tag,
                        "timescale 4's stopTime is：" + tempTimeScale.getEndTime());
                break;
            case 0x18:
                break;
            case 0x19:
                tempTimeScale.setWorkTime(DataFrameUtil.byteToInt(value1));
                LogUtil.logDebug(tag,
                        "timescale 4's workTime is：" + tempTimeScale.getWorkTime());
                break;
            case 0x1A:
                tempTimeScale.setRestTime(DataFrameUtil.byteToInt(value1));
                LogUtil.logDebug(tag,
                        "timescale 4's restTime is：" + tempTimeScale.getRestTime());
                break;
            case 0x1B:
                tempTimeScale.setCircle(value1);
                LogUtil.logDebug(tag,
                        "timescale 4's circle is：" + tempTimeScale.getCircle());
                if (isFuncTimeScale()) {
                    tempTimeScale.setId(3);
                    tempTimeScale.setWork(true);
                    if (isNewTimeScale()) {
                        device.addTimeScale(tempTimeScale);
                        LogUtil.logDebug(tag, "add timeScale 4");
                    } else {
                        device.updateTimeScale(tempTimeScale);
                        LogUtil.logDebug(tag, "update timeScale 4");
                    }
                } else {
                    LogUtil.logDebug(tag, "timeScale 4 is not aviliable");
                }
                // 查询最后一个时间段
                queryTimeScale(5);
                break;
            // 时间段5的开始时间，结束时间，风速设置，运行时间，暂停时间，定时周期
            case 0x1C:
                tempTimeScale.setStartTime(correct(DataFrameUtil.byteToInt(value1))
                        + ":" + correct(DataFrameUtil.byteToInt(value2)));
                LogUtil.logDebug(
                        tag,
                        "timescale 5's startTime is："
                                + tempTimeScale.getStartTime());
                break;
            case 0x1D:
                tempTimeScale.setEndTime(correct(DataFrameUtil.byteToInt(value1))
                        + ":" + correct(DataFrameUtil.byteToInt(value2)));
                LogUtil.logDebug(tag,
                        "timescale 5's stopTime is：" + tempTimeScale.getEndTime());
                break;
            case 0x1E:
                break;
            case 0x1F:
                tempTimeScale.setWorkTime(DataFrameUtil.byteToInt(value1));
                LogUtil.logDebug(tag,
                        "timescale 5's workTime is：" + tempTimeScale.getWorkTime());
                break;
            case 0x20:
                tempTimeScale.setRestTime(DataFrameUtil.byteToInt(value1));
                LogUtil.logDebug(tag,
                        "timescale 5's restTime is：" + tempTimeScale.getRestTime());
                break;
            case 0x21:
                tempTimeScale.setCircle(value1);
                LogUtil.logDebug(tag,
                        "timescale 5's circle is：" + tempTimeScale.getCircle());
                if (isFuncTimeScale()) {
                    tempTimeScale.setId(4);
                    tempTimeScale.setWork(true);
                    if (isNewTimeScale()) {
                        device.addTimeScale(tempTimeScale);
                        LogUtil.logDebug(tag, "add timeScale 5");
                    } else {
                        device.updateTimeScale(tempTimeScale);
                        LogUtil.logDebug(tag, "update timeScale 5");
                    }
                } else {
                    LogUtil.logDebug(tag, "timeScale 5 is not aviliable");
                }
                // 全查成功
                message.arg1 = 1;
                NetCommunicationUtil.setStepForQuery(3);
                break;
            case 0x22:// 香水剩余量
                volume = DataFrameUtil.byteToLong(value1, value2);
                device.setVolume(((double) volume / (double) Diffuser_Volume) * 100.000);
                LogUtil.logDebug(tag,
                        "the diffuser's volume is：" + device.getVolume() + "%");
                break;
            case 0x23:// 当前时间
                device.getCurTime().setTime(
                        DataFrameUtil.byteToInt(value1) + ":"
                                + DataFrameUtil.byteToInt(value2));
                LogUtil.logDebug(tag, "the diffuser's time is："
                        + device.getCurTime().getTime());
                break;
            case 0x24:// 故障
                getError(value1);
                break;
            case 0x25:// 蜂鸣器设置(不做操作)
                break;
            case 0x26:// 经销商信息
                break;
            case 0x2A:// 温度
                device.setTemp(DataFrameUtil.byteToInt(value1));
                LogUtil.logDebug(tag, "the diffuser's surround temperature is："
                        + device.getTemp() + "℃");
                break;
            case 0x2B:// 湿度
                device.setWeat(DataFrameUtil.byteToInt(value1));
                LogUtil.logDebug(tag,
                        "the diffuser's surround weat is：" + device.getWeat() + "%");
                break;
            case 0x2C:// 负离子发送器
                if (value1 == 0x00) {
                    device.setNIG(false);
                } else {
                    device.setNIG(true);
                }
                break;
            case 0x2D:// 设备当前工作的时间段ID
                // value1为0表示还不是正确的当前时间段ID ，默认设置第一个时间段
                if (DataFrameUtil.byteToInt(value1) == 0) {
                    device.setCurIndex(0);
                } else {
                    device.setCurIndex(DataFrameUtil.byteToInt(value1) - 1);
                }
                LogUtil.logDebug(tag,
                        "the working timescale's index is: " + device.getCurIndex());
                break;
            case 0x2E:// 设备时间的秒
                LogUtil.logDebug(
                        tag,
                        "the diffuser's second is："
                                + DataFrameUtil.byteToInt(value1));
                break;
            case 0x2F:// 温度传感器故障
                getTempError(value1);
			/*
			 * 判断是否需要进行全查还是故障重查还是定时查询
			 */
                identifyStep();
                break;
            case 0x31:// 时钟故障
                getTimeError(value1);
                break;
            default:
                break;
        }
    }

    private void identifyStep() {
        switch (NetCommunicationUtil.getStepForQuery()) {
            case 1:
                message.arg1 = 2;
                NetCommunicationUtil.setStepForQuery(3);
                break;
            case 2:
                queryTimeScale(1);
                break;
            default:
                break;
        }
    }

    private void getError(byte value) {
        LogUtil.logDebug(tag, "analyze diffuser's error");
        LogUtil.logDebug(tag, "the select device's mac" + device.getMac());
        LogUtil.logDebug(tag,
                "the uptell device's mac" + DataFrameUtil.getMac());
		/*
		 * 首先要判断是否为当前控制设备出现故障
		 */
        if (device.getMac().equals(DataFrameUtil.getMac())) {
            switch (value) {
                case (byte) 0x00:
                    // 正常
                    device.setError(0);
                    break;
                case (byte) 0x01:
                    // 精油瓶不匹配
                    device.setError(1);
                    break;
                case (byte) 0x02:
                    // 电机故障
                    device.setError(2);
                    break;
                case (byte) 0x04:
                    // 精油剩余量为0
                    device.setError(4);
                    break;
                default:
                    break;
            }
            LogUtil.logDebug(tag, "the diffuser" + device.getMac()
                    + " has problem：" + device.getError());
        } else {
            // 从设备列表中取出该设备
            for (int i = 0; i < DeviceManager.getInstance().getCount(); i++) {
                Device d = (Device) DeviceManager.getInstance().get(i);
                if (d.getMac().equals(DataFrameUtil.getMac())) {
                    switch (value) {
                        case (byte) 0x00:
                            // 正常
                            d.setError(0);
                            break;
                        case (byte) 0x01:
                            // 精油瓶不匹配
                            d.setError(1);
                            break;
                        case (byte) 0x02:
                            // 电机故障
                            d.setError(2);
                            break;
                        case (byte) 0x04:
                            // 精油剩余量为0
                            d.setError(4);
                            break;
                        default:
                            break;
                    }
                    LogUtil.logDebug(tag, "the diffuser" + d.getMac()
                            + " has problem：" + d.getError());
                    break;
                }
            }
        }
    }

    private void getTempError(byte value) {
        if (device.getMac().equals(DataFrameUtil.getMac())) {
            switch (value) {
                case (byte) 0x00:
                    // 温度传感器正常
                    device.setTempError(0);
                    break;
                case (byte) 0x01:
                    // 温度传感器故障
                    device.setTempError(1);
                    break;
                default:
                    break;
            }
            LogUtil.logDebug(
                    tag,
                    "the diffuser's temperature sensor has problem："
                            + device.getTempError());
        } else {
            // 从设备列表中取出该设备
            for (int i = 0; i < DeviceManager.getInstance().getCount(); i++) {
                Device d = (Device) DeviceManager.getInstance().get(i);
                if (d.getMac().equals(DataFrameUtil.getMac())) {
                    switch (value) {
                        case (byte) 0x00:
                            // 温度传感器正常
                            device.setTempError(0);
                            break;
                        case (byte) 0x01:
                            // 温度传感器故障
                            device.setTempError(1);
                            break;
                        default:
                            break;
                    }
                    LogUtil.logDebug(
                            tag,
                            "the diffuser" + d.getMac()
                                    + "'s temperature sensor has problem："
                                    + d.getTempError());
                    break;
                }
            }
        }
    }

    private void getTimeError(byte value) {
        if (device.getMac().equals(DataFrameUtil.getMac())) {
            switch (value) {
                case (byte) 0x00:
                    // 时钟正常
                    device.setTimeError(0);
                    break;
                case (byte) 0x01:
                    // 时钟故障
                    device.setTimeError(1);
                    break;
                default:
                    break;
            }
            LogUtil.logDebug(
                    tag,
                    "the diffuser's clock  has problem："
                            + device.getTempError());
        } else {
            // 从设备列表中取出该设备
            for (int i = 0; i < DeviceManager.getInstance().getCount(); i++) {
                Device d = (Device) DeviceManager.getInstance().get(i);
                if (d.getMac().equals(DataFrameUtil.getMac())) {
                    switch (value) {
                        case (byte) 0x00:
                            // 时钟正常
                            device.setTimeError(0);
                            break;
                        case (byte) 0x01:
                            // 时钟故障
                            device.setTimeError(1);
                            break;
                        default:
                            break;
                    }
                    LogUtil.logDebug(tag, "the diffuser" + d.getMac()
                            + "'s clock has problem：" + d.getTimeError());
                    break;
                }
            }
        }
    }

    /**
     * 方法名称：	isFuncTimeScale
     * 方法描述：	判断时间段是否有效
     * 参数：			@return
     * 返回值类型：	boolean
     * 创建时间：	2015-5-20下午4:52:22
     */
    private boolean isFuncTimeScale() {
        if (tempTimeScale.getStartTime().equals(tempTimeScale.getEndTime())) {
            return false;
        }
        return true;
    }

    private boolean isNewTimeScale() {
        boolean isNew = true;
        // for (int i = 0; i < device.getTimeScales().size(); i++)
        // {
        // if ( device.getTimeScale(i).getId() == tempTimeScale.getId() )
        // {
        // isNew = false;
        // break;
        // }
        // }
        if (device.getTimeScale(tempTimeScale.getId()) != null) {
            isNew = false;
        }
        return isNew;
    }

    /**
     * 方法名称：	queryTimeScale
     * 方法描述：	查询5个时间段的数据
     * 参数：
     * 返回值类型：	void
     * 创建时间：	2015-2-1上午9:09:27
     */
    private void queryTimeScale(int index) {
        switch (index) {
            case 1:
                // 查询时间段1
                NetCommunicationUtil.queryDevice(BonfeelFrame.queryTimeScale1,
                        device.getMac(), NetElectricsConst.QueryTimeScale);
                break;
            case 2:
                // 查询时间段2
                NetCommunicationUtil.queryDevice(BonfeelFrame.queryTimeScale2,
                        device.getMac(), NetElectricsConst.QueryTimeScale);
                break;
            case 3:
                // 查询时间段3
                NetCommunicationUtil.queryDevice(BonfeelFrame.queryTimeScale3,
                        device.getMac(), NetElectricsConst.QueryTimeScale);
                break;
            case 4:
                // 查询时间段4
                NetCommunicationUtil.queryDevice(BonfeelFrame.queryTimeScale4,
                        device.getMac(), NetElectricsConst.QueryTimeScale);
                break;
            case 5:
                // 查询时间段5
                NetCommunicationUtil.queryDevice(BonfeelFrame.queryTimeScale5,
                        device.getMac(), NetElectricsConst.QueryTimeScale);
                break;
        }
    }

    /**
     * 方法名称：	correct
     * 方法描述：	对于时间的修正,时间的时，分，日期的月，日，都用两位表示
     * 参数：			@param date
     * 参数：			@return
     * 返回值类型：	String
     * 创建时间：	2015-2-6下午6:17:17
     */
    private String correct(int date) {
        String str = "";
        if (date < 10) {
            str = "0" + date;
        } else {
            str = date + "";
        }
        return str;
    }
}
