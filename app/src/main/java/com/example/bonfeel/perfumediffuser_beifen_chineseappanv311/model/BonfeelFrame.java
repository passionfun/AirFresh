package com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.model;

import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.util.DataFrameUtil;

/**
 *
 * Author:FunFun
 * Function:百芬扩香仪设备通信用到的所有查询，控制等需要手机端下发的数据帧子帧数据域
 * 					其他项目通讯帧可以此建立类似的文档，便于通信的数据统一整理和处理，避免了
 * 					在其他代码文件中出现具体数据
 * Date:2016/4/12 8:45
 */
public class BonfeelFrame {
    /*
	 * 控制数据域帧
	 */
    public static byte[] DeviceOn = new byte[]{0x01, 0x01, 0x00};
    public static byte[] DeviceOff = new byte[]{0x01, 0x00, 0x00};
    public static byte[] SetCurrentYear(int year)
    {
        byte[] CurYear = new byte[3];
        CurYear[0] = 0x02;
        CurYear[1] = DataFrameUtil.intToByteArray(year)[0];
        CurYear[2] = DataFrameUtil.intToByteArray(year)[1];
        return CurYear;
    }
    public static byte[] SetCurrentDate(int month, int day)
    {
        byte[] CurDate = new byte[3];
        CurDate[0] = 0x03;
        CurDate[1] = DataFrameUtil.intToByteArray(month, day)[0];
        CurDate[2] = DataFrameUtil.intToByteArray(month, day)[1];
        return CurDate;
    }

    //风速控制
    public static byte[] setWindRank(int windRank){
        byte[] windStatus=new byte[3];
        windStatus[0]=0x35;
        windStatus[2]=0x00;
        switch (windRank) {
            case 0:
                windStatus[1]=0x00;
                break;
            case 1:
                windStatus[1]=0x01;
                break;
            case 2:
                windStatus[1]=0x02;
                break;
            case 3:
                windStatus[1]=0x03;
                break;
            case 4:
                windStatus[1]=0x04;
                break;
            case 5:
                windStatus[1]=0x05;
                break;
            case 6:
                windStatus[1]=0x06;
                break;
            case 7:
                windStatus[1]=0x07;
                break;
            case 8:
                windStatus[1]=0x08;
                break;
            case 9:
                windStatus[1]=0x09;
                break;
            case 10:
                windStatus[1]=0x0a;
                break;

            default:
                break;
        }

        return windStatus;


    }
    public static byte[] SetStartTime(int index, int hour, int min)
    {
        byte[] StartTime = new byte[3];
        switch (index)
        {
            case 0:
                StartTime[0] = 0x04;
                break;
            case 1:
                StartTime[0] = 0x0A;
                break;
            case 2:
                StartTime[0] = 0x10;
                break;
            case 3:
                StartTime[0] = 0x16;
                break;
            case 4:
                StartTime[0] = 0x1C;
                break;
        }
        StartTime[1] = DataFrameUtil.intToByteArray(hour, min)[0];
        StartTime[2] = DataFrameUtil.intToByteArray(hour, min)[1];
        return StartTime;
    }
    public static byte[] SetStopTime(int index, int hour, int min)
    {
        byte[] StopTime = new byte[3];
        switch (index)
        {
            case 0:
                StopTime[0] = 0x05;
                break;
            case 1:
                StopTime[0] = 0x0B;
                break;
            case 2:
                StopTime[0] = 0x11;
                break;
            case 3:
                StopTime[0] = 0x17;
                break;
            case 4:
                StopTime[0] = 0x1D;
                break;
        }
        StopTime[1] = DataFrameUtil.intToByteArray(hour, min)[0];
        StopTime[2] = DataFrameUtil.intToByteArray(hour, min)[1];
        return StopTime;
    }
    public static byte[] SetWorkTime(int index, int time)
    {
        byte[] WorkTime = new byte[3];
        switch (index)
        {
            case 0:
                WorkTime[0] = 0x07;
                break;
            case 1:
                WorkTime[0] = 0x0D;
                break;
            case 2:
                WorkTime[0] = 0x13;
                break;
            case 3:
                WorkTime[0] = 0x19;
                break;
            case 4:
                WorkTime[0] = 0x1F;
                break;
        }
        WorkTime[1] = DataFrameUtil.intToByte(time);
        WorkTime[2] = 0x00;
        return WorkTime;
    }
    public static byte[] SetRestTime(int index, int time)
    {
        byte[] RestTime = new byte[3];
        switch (index)
        {
            case 0:
                RestTime[0] = 0x08;
                break;
            case 1:
                RestTime[0] = 0x0E;
                break;
            case 2:
                RestTime[0] = 0x14;
                break;
            case 3:
                RestTime[0] = 0x1A;
                break;
            case 4:
                RestTime[0] = 0x20;
                break;
        }
        RestTime[1] = DataFrameUtil.intToByte(time);
        RestTime[2] = 0x00;
        return RestTime;
    }
    public static byte[] SetCircle(int index, byte circle)
    {
        byte[] Circle = new byte[3];
        switch (index)
        {
            case 0:
                Circle[0] = 0x09;
                break;
            case 1:
                Circle[0] = 0x0F;
                break;
            case 2:
                Circle[0] = 0x15;
                break;
            case 3:
                Circle[0] = 0x1B;
                break;
            case 4:
                Circle[0] = 0x21;
                break;
        }
        Circle[1] = circle;
        Circle[2] = 0x00;
        return Circle;
    }
    public static byte[] setTimeScale(int index, String startHour, String startMin, String stopHour, String stopMin, int workTime, int restTime, byte circle)
    {
        byte[] frame = new byte[15];
        switch (index)
        {
            case 0:
                frame[6] = 0x04;
                frame[7] = DataFrameUtil.intToByteArray(Integer.parseInt(startHour), Integer.parseInt(startMin))[0];
                frame[8] = DataFrameUtil.intToByteArray(Integer.parseInt(startHour), Integer.parseInt(startMin))[1];

                frame[9] = 0x05;
                frame[10] = DataFrameUtil.intToByteArray(Integer.parseInt(stopHour), Integer.parseInt(stopMin))[0];
                frame[11] = DataFrameUtil.intToByteArray(Integer.parseInt(stopHour), Integer.parseInt(stopMin))[1];

                frame[0] = 0x07;
                frame[1] = DataFrameUtil.intToByte(workTime);
                frame[2] = 0x00;

                frame[3] = 0x08;
                frame[4] = DataFrameUtil.intToByte(restTime);
                frame[5] = 0x00;

                frame[12] = 0x09;
                frame[13] = circle;
                frame[14] = 0x00;
                break;
            case 1:
                frame[6] = 0x0A;
                frame[7] = DataFrameUtil.intToByteArray(Integer.parseInt(startHour), Integer.parseInt(startMin))[0];
                frame[8] = DataFrameUtil.intToByteArray(Integer.parseInt(startHour), Integer.parseInt(startMin))[1];

                frame[9] = 0x0B;
                frame[10] = DataFrameUtil.intToByteArray(Integer.parseInt(stopHour), Integer.parseInt(stopMin))[0];
                frame[11] = DataFrameUtil.intToByteArray(Integer.parseInt(stopHour), Integer.parseInt(stopMin))[1];

                frame[0] = 0x0D;
                frame[1] = DataFrameUtil.intToByte(workTime);
                frame[2] = 0x00;

                frame[3] = 0x0E;
                frame[4] = DataFrameUtil.intToByte(restTime);
                frame[5] = 0x00;

                frame[12] = 0x0F;
                frame[13] = circle;
                frame[14] = 0x00;
                break;
            case 2:

                frame[6] = 0x10;
                frame[7] = DataFrameUtil.intToByteArray(Integer.parseInt(startHour), Integer.parseInt(startMin))[0];
                frame[8] = DataFrameUtil.intToByteArray(Integer.parseInt(startHour), Integer.parseInt(startMin))[1];

                frame[9] = 0x11;
                frame[10] = DataFrameUtil.intToByteArray(Integer.parseInt(stopHour), Integer.parseInt(stopMin))[0];
                frame[11] = DataFrameUtil.intToByteArray(Integer.parseInt(stopHour), Integer.parseInt(stopMin))[1];

                frame[0] = 0x13;
                frame[1] = DataFrameUtil.intToByte(workTime);
                frame[2] = 0x00;

                frame[3] = 0x14;
                frame[4] = DataFrameUtil.intToByte(restTime);
                frame[5] = 0x00;

                frame[12] = 0x15;
                frame[13] = circle;
                frame[14] = 0x00;
                break;
            case 3:
                frame[6] = 0x16;
                frame[7] = DataFrameUtil.intToByteArray(Integer.parseInt(startHour), Integer.parseInt(startMin))[0];
                frame[8] = DataFrameUtil.intToByteArray(Integer.parseInt(startHour), Integer.parseInt(startMin))[1];

                frame[9] = 0x17;
                frame[10] = DataFrameUtil.intToByteArray(Integer.parseInt(stopHour), Integer.parseInt(stopMin))[0];
                frame[11] = DataFrameUtil.intToByteArray(Integer.parseInt(stopHour), Integer.parseInt(stopMin))[1];

                frame[0] = 0x19;
                frame[1] = DataFrameUtil.intToByte(workTime);
                frame[2] = 0x00;

                frame[3] = 0x1A;
                frame[4] = DataFrameUtil.intToByte(restTime);
                frame[5] = 0x00;

                frame[12] = 0x1B;
                frame[13] = circle;
                frame[14] = 0x00;
                break;
            case 4:
                frame[6] = 0x1C;
                frame[7] = DataFrameUtil.intToByteArray(Integer.parseInt(startHour), Integer.parseInt(startMin))[0];
                frame[8] = DataFrameUtil.intToByteArray(Integer.parseInt(startHour), Integer.parseInt(startMin))[1];

                frame[9] = 0x1D;
                frame[10] = DataFrameUtil.intToByteArray(Integer.parseInt(stopHour), Integer.parseInt(stopMin))[0];
                frame[11] = DataFrameUtil.intToByteArray(Integer.parseInt(stopHour), Integer.parseInt(stopMin))[1];

                frame[0] = 0x1F;
                frame[1] = DataFrameUtil.intToByte(workTime);
                frame[2] = 0x00;

                frame[3] = 0x20;
                frame[4] = DataFrameUtil.intToByte(restTime);
                frame[5] = 0x00;

                frame[12] = 0x21;
                frame[13] = circle;
                frame[14] = 0x00;
                break;
        }
        return frame;
    }
    public static byte[] setTimeScale(int index, String startHour, String startMin, String stopHour, String stopMin)
    {
        byte[] frame = new byte[6];
        switch (index)
        {
            case 0:
                frame[0] = 0x04;
                frame[1] = DataFrameUtil.intToByteArray(Integer.parseInt(startHour), Integer.parseInt(startMin))[0];
                frame[2] = DataFrameUtil.intToByteArray(Integer.parseInt(startHour), Integer.parseInt(startMin))[1];

                frame[3] = 0x05;
                frame[4] = DataFrameUtil.intToByteArray(Integer.parseInt(stopHour), Integer.parseInt(stopMin))[0];
                frame[5] = DataFrameUtil.intToByteArray(Integer.parseInt(stopHour), Integer.parseInt(stopMin))[1];
                break;
            case 1:
                frame[0] = 0x0A;
                frame[1] = DataFrameUtil.intToByteArray(Integer.parseInt(startHour), Integer.parseInt(startMin))[0];
                frame[2] = DataFrameUtil.intToByteArray(Integer.parseInt(startHour), Integer.parseInt(startMin))[1];

                frame[3] = 0x0B;
                frame[4] = DataFrameUtil.intToByteArray(Integer.parseInt(stopHour), Integer.parseInt(stopMin))[0];
                frame[5] = DataFrameUtil.intToByteArray(Integer.parseInt(stopHour), Integer.parseInt(stopMin))[1];
                break;
            case 2:

                frame[0] = 0x10;
                frame[1] = DataFrameUtil.intToByteArray(Integer.parseInt(startHour), Integer.parseInt(startMin))[0];
                frame[2] = DataFrameUtil.intToByteArray(Integer.parseInt(startHour), Integer.parseInt(startMin))[1];

                frame[3] = 0x11;
                frame[4] = DataFrameUtil.intToByteArray(Integer.parseInt(stopHour), Integer.parseInt(stopMin))[0];
                frame[5] = DataFrameUtil.intToByteArray(Integer.parseInt(stopHour), Integer.parseInt(stopMin))[1];
                break;
            case 3:
                frame[0] = 0x16;
                frame[1] = DataFrameUtil.intToByteArray(Integer.parseInt(startHour), Integer.parseInt(startMin))[0];
                frame[2] = DataFrameUtil.intToByteArray(Integer.parseInt(startHour), Integer.parseInt(startMin))[1];

                frame[3] = 0x17;
                frame[4] = DataFrameUtil.intToByteArray(Integer.parseInt(stopHour), Integer.parseInt(stopMin))[0];
                frame[5] = DataFrameUtil.intToByteArray(Integer.parseInt(stopHour), Integer.parseInt(stopMin))[1];
                break;
            case 4:
                frame[0] = 0x1C;
                frame[1] = DataFrameUtil.intToByteArray(Integer.parseInt(startHour), Integer.parseInt(startMin))[0];
                frame[2] = DataFrameUtil.intToByteArray(Integer.parseInt(startHour), Integer.parseInt(startMin))[1];
                frame[3] = 0x1D;
                frame[4] = DataFrameUtil.intToByteArray(Integer.parseInt(stopHour), Integer.parseInt(stopMin))[0];
                frame[5] = DataFrameUtil.intToByteArray(Integer.parseInt(stopHour), Integer.parseInt(stopMin))[1];
                break;
        }
        return frame;
    }
    public static byte[] SetCurrentTime(int hour, int min)
    {
        byte[] CurTime = new byte[3];
        CurTime[0] = 0x23;
        CurTime[1] = DataFrameUtil.intToByteArray(hour, min)[0];
        CurTime[2] = DataFrameUtil.intToByteArray(hour, min)[1];
        return CurTime;
    }
    public static byte[] SetCurrentSec(int second)
    {
        byte[] CurSec = new byte[3];
        CurSec[0] = 0x2E;
        CurSec[1] = DataFrameUtil.intToByte(second);
        CurSec[2] = 0x00;
        return CurSec;
    }
    public static byte[] NIGOn = new byte[]{0x2C, 0x01, 0x00};
    public static byte[] NIGOff = new byte[]{0x2C, 0x00, 0x00};

    /*
     * 查询电源，剩余量，温度，湿度，故障，时钟故障，温度传感器故障
     */
    public static byte[] queryStateV2 = new byte[]{0x01, 0x22, 0x2A, 0x2B, 0x2D, 0x24, 0x31, 0x2F};
    public static byte[] queryStateV1 = new byte[]{0x01, 0x22, 0x2A, 0x2B, 0x2D, 0x24, 0x2F};
    /*
     * 查询时间段1的开始，结束，运行，暂停，周期
     */
    public static byte[] queryTimeScale1 = new byte[]{0x04, 0x05, 0x07, 0x08, 0x09};
    /*
     * 查询时间段2的开始，结束，运行，暂停，周期
     */
    public static byte[] queryTimeScale2 = new byte[]{0x0A, 0x0B, 0x0D, 0x0E, 0x0F};
    /*
     * 查询时间段3的开始，结束，运行，暂停，周期
     */
    public static byte[] queryTimeScale3 = new byte[]{0x10, 0x11, 0x13, 0x14, 0x15};
    /*
     * 查询时间段4的开始，结束，运行，暂停，周期
     */
    public static byte[] queryTimeScale4 = new byte[]{0x16, 0x17, 0x19, 0x1A, 0x1B};
    /*
     * 查询时间段5的开始，结束，运行，暂停，周期
     */
    public static byte[] queryTimeScale5 = new byte[]{0x1C, 0x1D, 0x1F, 0x20, 0x21};
    /*
     * 查询设备时间
     */
    public static byte[] queryCurDate = new byte[]{0x02, 0x03, 0x23, 0x2E};
    /*
     * 定时查询设备状态
     */
    public static byte[] queryAlarmTaskV2 = new byte[]{0x01, 0x22, 0x2A, 0x2B, 0x2D, 0x24, 0x31, 0x2F};
    public static byte[] queryAlarmTaskV1 = new byte[]{0x01, 0x22, 0x2A, 0x2B, 0x2D, 0x24, 0x2F};

    public static byte[] queryNIG = new byte[]{0x2C};
    //查询设备温度
    public static byte[] queryTemp = new byte[]{0x2A};
    //查询硬件版本日期
//	public static byte[] queryTemp = new byte[]{(byte) 0xCC};
    //查询设备湿度
    public static byte[] queryWeat = new byte[]{0x2B};
    //设备故障，温度传感器故障
    public static byte[] queryErrorV2 = new byte[]{0x24, 0x31, 0x2F};
    public static byte[] queryErrorV1 = new byte[]{0x24, 0x2F};

}
