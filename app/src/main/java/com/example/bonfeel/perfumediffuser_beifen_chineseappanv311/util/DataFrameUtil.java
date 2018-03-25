package com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.util;

import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.model.DataFrame;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.model.DataFrameLib;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.model.NetElectricsConst;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.model.SubDataFrame;

/**
 *
 * Author:FunFun
 * Function:数据帧解析和生成的工具类。该类的数据来源是数据帧库中的数据。
 * 					对外提供生成的数据帧，或者解析后的结果。该类也是单例模式
 * Date:2016/4/12 9:15
 */
public class DataFrameUtil {
    private static String tag = "DataFrameUtil";

    private static byte[] frame;
    private static byte[] subframe;
    private static DataFrame dataFrame;

    /**
     * 方法名称：	createFrame
     * 方法描述：	根据操作, 操作对象, 数据内容的不同，生成相应数据帧
     * 参数：			@param operate
     * 参数：			@param mac
     * 参数：			@return
     * 返回值类型：	byte[]
     * 创建时间：	2015-1-24上午9:38:08
     */
    public static byte[] createFrame(String operate, String mac, byte[] data) {
        byte[] frame = null;
        byte[] subframe = null;
        DataFrame dataFrame = new DataFrame();
        dataFrame.setHeader(DataFrameLib.getInstance().getHeader(
                NetElectricsConst.MobileToDevice));
        dataFrame.setLen(DataFrameLib.getInstance().getLen(
                NetElectricsConst.MobileToDevice));
        dataFrame.setMac(getDeviceMac(mac));
        dataFrame.setLogic(DataFrameLib.getInstance().getLogicalPath(
                NetElectricsConst.MobileToDevice));
        dataFrame
                .setControl(DataFrameLib.getInstance().getControlCode(operate));
        dataFrame.setFunc(DataFrameLib.getInstance().getFuncCode(
                NetElectricsConst.MobileToDevice));
        SubDataFrame subDataFrame = new SubDataFrame();
        subDataFrame.setHeader(DataFrameLib.getInstance().getSubheader(
                NetElectricsConst.MobileToDevice));
        subDataFrame.setLen(DataFrameLib.getInstance().getSublen(
                NetElectricsConst.MobileToDevice));
        subDataFrame.setFunc(DataFrameLib.getInstance().getControlFuncCode(
                operate, NetElectricsConst.MobileToDevice));
        subDataFrame.setData(data);
        subDataFrame.setEnd(DataFrameLib.getInstance().getSubend(
                NetElectricsConst.MobileToDevice));
        dataFrame.setSubDataFrame(subDataFrame);
        dataFrame.setEnd(DataFrameLib.getInstance().getEnd(
                NetElectricsConst.MobileToDevice));
        // +2的原因是内层帧的前面有两位，内容是内帧的长度，但这两位不算内层帧
        subframe = new byte[subDataFrame.getFrameLength()];
        frame = new byte[dataFrame.getFrameLength() + 2];

        for (int i = 0; i < subframe.length; i++) {
            if (i < subDataFrame.getHeader().length) {
                subframe[i] = subDataFrame.getHeader()[i];
            } else {
                if ((i - subDataFrame.getHeader().length) < 1) {
                    subframe[i] = subDataFrame.getLen();
                } else {
                    if ((i - subDataFrame.getHeader().length - 1) < 1) {
                        subframe[i] = subDataFrame.getFunc();
                    } else {
                        if ((i - subDataFrame.getHeader().length - 2) < subDataFrame
                                .getData().length) {
                            subframe[i] = subDataFrame.getData()[i
                                    - subDataFrame.getHeader().length - 2];
                        } else {
                            subframe[i] = subDataFrame.getEnd()[i
                                    - subDataFrame.getHeader().length - 2
                                    - subDataFrame.getData().length];
                        }
                    }
                }
            }
        }
        subframe[2] = Byte.parseByte(String.valueOf(subframe.length));
        subframe[subframe.length - 2] = getCheckByte(subframe);
        byte[] tempframe = new byte[subframe.length + 2];
        tempframe[0] = subframe[2];
        tempframe[1] = 0x00;
        for (int i = 2; i < tempframe.length; i++) {
            tempframe[i] = subframe[i - 2];
        }
        for (int i = 0; i < frame.length; i++) {
            if (i < dataFrame.getHeader().length) {
                frame[i] = dataFrame.getHeader()[i];
            } else {
                if ((i - dataFrame.getHeader().length) < dataFrame.getLen().length) {
                    frame[i] = dataFrame.getLen()[i
                            - dataFrame.getHeader().length];
                } else {
                    if ((i - dataFrame.getHeader().length - dataFrame.getLen().length) < dataFrame
                            .getMac().length) {
                        frame[i] = dataFrame.getMac()[i
                                - dataFrame.getHeader().length
                                - dataFrame.getLen().length];
                    } else {
                        if ((i - dataFrame.getHeader().length
                                - dataFrame.getLen().length - dataFrame
                                .getMac().length) < 1) {
                            frame[i] = dataFrame.getLogic();
                        } else {
                            if ((i - dataFrame.getHeader().length
                                    - dataFrame.getLen().length
                                    - dataFrame.getMac().length - 1) < 1) {
                                frame[i] = dataFrame.getControl();
                            } else {
                                if ((i - dataFrame.getHeader().length
                                        - dataFrame.getLen().length
                                        - dataFrame.getMac().length - 2) < 1) {
                                    frame[i] = dataFrame.getFunc();
                                } else {
                                    if ((i - dataFrame.getHeader().length
                                            - dataFrame.getLen().length
                                            - dataFrame.getMac().length - 3) < tempframe.length) {
                                        frame[i] = tempframe[i
                                                - dataFrame.getHeader().length
                                                - dataFrame.getLen().length
                                                - dataFrame.getMac().length - 3];
                                    } else {
                                        frame[i] = dataFrame.getEnd()[i
                                                - dataFrame.getHeader().length
                                                - dataFrame.getLen().length
                                                - dataFrame.getMac().length - 3
                                                - tempframe.length];
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        frame[2] = Byte.parseByte(String.valueOf(frame.length));
        frame[frame.length - 2] = getCheckByte(frame);

		/*
		 * 用于调试查看的代码段
		 */
        String backString = "";
        for (int i = 0; i < frame.length; i++) {
            backString += Integer.toHexString(frame[i] & 0xff) + " ";
        }
        LogUtil.logInfo(tag, "手机发出的数据帧" + backString);
        // **************************************************************

        return frame;
    }

    /**
     * 方法名称：	analyzeFrame
     * 方法描述：	解析设备上行的数据帧
     * 参数：			@param tag
     * 返回值类型：	void
     * 创建时间：	2015-1-4下午3:30:59
     */
    public static void analyzeFrame(byte[] data) {
        int lenData = data.length;
        if (null == data || 0 == lenData) {
            // 没有数据
        } else {
            try {
                for (int i = (data.length - 1); i >= 0; i--) {
                    if ((byte) 0xA5 == data[i]) {
                        lenData = i + 1;
                        break;
                    }
                }
                frame = new byte[lenData];
                for (int i = 0; i < lenData; i++) {
                    frame[i] = data[i];
                }
                dataFrame = new DataFrame();
                dataFrame.setHeader(new byte[] { frame[0], frame[1] });
                dataFrame.setLen(new byte[] { frame[2], frame[3] });
                dataFrame.setMac(new byte[] { frame[4], frame[5], frame[6],
                        frame[7], frame[8], frame[9], frame[10], frame[11] });
                dataFrame.setLogic(frame[12]);
                dataFrame.setControl(frame[13]);
                dataFrame.setFunc(frame[14]);
                dataFrame.setEnd(new byte[] { frame[lenData - 2],
                        frame[lenData - 1] });

                // 15,16是新增的两位,丢掉了
                dataFrame.getSubDataFrame().setHeader(
                        new byte[] { frame[17], frame[18] });
                dataFrame.getSubDataFrame().setLen(frame[19]);
                dataFrame.getSubDataFrame().setFunc(frame[20]);
                dataFrame.getSubDataFrame().setEnd(
                        new byte[] { frame[lenData - 4], frame[lenData - 3] });

                byte[] content = new byte[lenData - 25];
                for (int i = 0; i < content.length; i++) {
                    content[i] = frame[21 + i];
                }
                dataFrame.getSubDataFrame().setData(content);
                // 从DF开始到DE结尾的内帧
                subframe = new byte[content.length + 6];
                for (int i = 0; i < subframe.length; i++) {
                    subframe[i] = frame[17 + i];
                }
            } catch (Exception e) {
                // e.printStackTrace();
            }
        }
    }

    /**
     * 方法名称：	getMac
     * 方法描述：	获取到收到通信帧的设备mac，用于故障设备的判断
     * 					在通信协议的定义中mac是8位的，但是百芬的mac只有六位
     * 参数：			@return
     * 返回值类型：	String
     * 创建时间：	2015-4-28下午3:45:06
     */
    public static String getMac() {
        byte[] bMac = dataFrame.getMac();
        if (bMac == null) {
            return null;
        }
        String mac = "";
        for (int i = 7; i > 1; i--) {
            String t = Integer.toHexString(bMac[i] & 0xff);
            if (t.length() == 1) {
                t = "0" + t;
            }
            // 小写字母转大写字母
            String n = "";
            for (int j = 0; j < t.length(); j++) {
                char c = t.charAt(j);
                if (c >= 97 && c <= 122) {
                    c = (char) (c - 32);
                }
                n += c;
            }
            mac += n;
            if (i > 2) {
                mac += "-";
            }
        }
        return mac;
    }

    /**
     * 方法名称：	showResult
     * 方法描述：	将解析后的数据帧进行判别，得出结果。即判断是何种事件情况
     * 					（香水用完或故障，返回查询内容还是控制设备成功）
     * 参数：			tag标记，标记不同情况的结果
     * 返回值类型：	int
     * 创建时间：	2015-1-4下午3:28:34
     */
    public static int showResult() {
        int tag = 0;
        // 检验外层帧的帧头帧尾
        if ((byte) 0x5A == dataFrame.getHeader()[0]
                && (byte) 0x5C == dataFrame.getHeader()[1]
                && (byte) 0xA5 == dataFrame.getEnd()[1]) {
            // 检验数据帧的长度
            if (dataFrame.getLen()[0] == frame.length) {
                // 对校验位的重新校验比对
                if (dataFrame.getEnd()[0] == getCheckByte(frame)) {
                    // 内层帧的判断,判断帧头帧尾
                    if ((byte) 0xDF == dataFrame.getSubDataFrame().getHeader()[0]
                            && (byte) 0xFD == dataFrame.getSubDataFrame()
                            .getHeader()[1]
                            && (byte) 0xDE == dataFrame.getSubDataFrame()
                            .getEnd()[1]) {
                        // 内层帧长度校对
                        if (dataFrame.getSubDataFrame().getLen() == subframe.length) {
                            // 对校验位的重新校验比对
                            if (dataFrame.getSubDataFrame().getEnd()[0] == getCheckByte(subframe)) {
                                tag = 1;
                            } else {
                                // 内帧校验位出错
                                tag = 2;
                            }
                        } else {
                            // 内帧长度不错
                            tag = 3;
                        }
                    } else {
                        // 内帧帧头或帧尾出错
                        tag = 4;
                    }
                } else {
                    // 外帧校验位出错
                    tag = 5;
                }
            } else {
                // 外帧长度出错
                tag = 6;
            }
        } else {
            // 外帧帧头或帧尾出错
            tag = 7;
        }
        return tag;
    }

    /**
     * 方法名称：	frameFunc
     * 方法描述：	根据操作，解析数据帧的内帧控制功能码
     * 参数：			@return
     * 返回值类型：	int
     * 创建时间：	2015-2-5下午7:21:18
     */
    public static int frameFunc() {
        int tag = 0;
        // 上行控制码
        if (dataFrame.getControl() == DataFrameLib.getInstance()
                .getControlCode(NetElectricsConst.OPERATE_RESPOND)) {
            // 查询反馈
            if (dataFrame.getSubDataFrame().getFunc() == DataFrameLib
                    .getInstance().getControlFuncCode(
                            NetElectricsConst.OPERATE_QUERY, 1)) {
                // 内帧的数据域为设备的属性值
                tag = 1;
            } else {
                // 控制设备反馈
                if (dataFrame.getSubDataFrame().getFunc() == DataFrameLib
                        .getInstance().getControlFuncCode(
                                NetElectricsConst.OPERATE_RESPOND, 1)) {
                    // 内帧的数据域不带数据，该帧只表示设备控制成功
                    tag = 2;
                } else {
                    // 设备离线
                    if (dataFrame.getSubDataFrame().getFunc() == DataFrameLib
                            .getInstance().getControlFuncCode(
                                    NetElectricsConst.DEVICE_OFFLINE, 1)) {
                        tag = 4;
                    } else {
                    }
                }
            }
        } else {
            // 主动上报
            if (dataFrame.getControl() == DataFrameLib.getInstance()
                    .getControlCode(NetElectricsConst.OPERATE_SUBMIT)) {
                // 设备发起的写事件
                if (dataFrame.getSubDataFrame().getFunc() == DataFrameLib
                        .getInstance().getControlFuncCode(
                                NetElectricsConst.OPERATE_SUBMIT, 1)) {
                    tag = 3;
                }
            } else {
                // 下行控制码
            }
        }
        // System.out.println("外帧控制码："+Integer.toHexString(dataFrame.getControl()
        // & 0xff));
        // System.out.println("外帧功能码："+Integer.toHexString(dataFrame.getFunc() &
        // 0xff));
        // System.out.println("内帧控制功能码："
        // +Integer.toHexString(dataFrame.getSubDataFrame().getFunc() & 0xff));
        return tag;
    }

    /**
     * 返回值： dataFrame
     */
    public static DataFrame getDataFrame() {
        return dataFrame;
    }

    /**
     * 方法名称：	getCheckByte
     * 方法描述：	获取校验位
     * 参数：			@param frame
     * 参数：			@return
     * 返回值类型：	byte
     * 创建时间：	2015-1-24上午11:01:23
     */
    public static byte getCheckByte(byte[] frame) {
        int data = 0;
        for (int i = 0; i < (frame.length - 2); i++) {
            data += (int) frame[i];
        }
        byte checkByte = (byte) (data & 0x000000FF);
        return checkByte;
    }

    /**
     * 方法名称：	getDeviceMac
     * 方法描述：	生成数据帧的mac部分
     * 参数：			@param mac
     * 参数：			@return
     * 返回值类型：	byte
     * 创建时间：	2015-1-24上午11:43:21
     */
    private static byte[] getDeviceMac(String strMac) {
        byte[] mac = new byte[8];
        String bStr = "";
        // mac要倒置输入,即mac为c8:93:46:42:84:be,将要倒置为be:84:42:46:93:c8
        int j = 7;
        mac[0] = 0x00;
        mac[1] = 0x00;
        for (int i = 0; i < strMac.length(); i++) {
            if (strMac.charAt(i) != '-' && strMac.charAt(i) != ':') {
                bStr += strMac.charAt(i);
                if (bStr.length() == 2) {
                    mac[j] = Integer.decode("0x" + bStr).byteValue();
                    j--;
                    bStr = "";
                }
            }
        }
        return mac;
    }

    /**
     * 方法名称：	getPhoneFrame
     * 方法描述：	依据手机号生成手机注册帧
     * 参数：			@param phone
     * 参数：			@return
     * 返回值类型：	byte[]
     * 创建时间：	2015-2-3上午8:01:02
     */
    @SuppressWarnings("null")
    public static byte[] getPhoneFrame(String phone) {
        // 将string型的手机号转换为16进制的string
        // fun add 非空判断
        if (phone == null && phone.isEmpty()) {
            return null;
        }
        long longMobile = 0;
        // try……catch代码块，解决数据转换异常
        try {
            longMobile = Long.parseLong(phone);
        } catch (NumberFormatException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        String strMobile = Long.toHexString(longMobile);
        // 将该16进制低位补0，补足12位（取12的原因是因为手机号原本为11位）
        int strLen = strMobile.length();
        StringBuffer strBuffer = null;
        while (strLen < 12) {
            strBuffer = new StringBuffer();
            // 将这个缓冲附加0，再加16进制的string
            strBuffer.append("0").append(strMobile);
            strMobile = strBuffer.toString();
            strLen = strMobile.length();
        }
        // 将新得到的16进制string转为byte[]
        byte[] byteMobile = new byte[strMobile.length() / 2];
        int j = 5;
        for (int i = 0; i < byteMobile.length; i++) {
            byte temp = Integer.decode(
                    "0x" + strMobile.substring(2 * i, 2 * i + 2)).byteValue();
            byteMobile[j] = temp;
            j--;
        }
        // 生成手机注册整帧
        byte[] frameHead = new byte[] { Integer.decode("0x5A").byteValue(),
                Integer.decode("0x5D").byteValue(),

                Integer.decode("0x21").byteValue(),
                Integer.decode("0x00").byteValue(),

                Integer.decode("0x00").byteValue(),
                Integer.decode("0x00").byteValue(),
                Integer.decode("0x00").byteValue(),
                Integer.decode("0x00").byteValue(),
                Integer.decode("0x00").byteValue(),
                Integer.decode("0x00").byteValue(),
                Integer.decode("0x00").byteValue(),
                Integer.decode("0x00").byteValue(),

                Integer.decode("0x00").byteValue(),
                Integer.decode("0x80").byteValue(),
                Integer.decode("0xE9").byteValue(),
                Integer.decode("0x0E").byteValue(),
                Integer.decode("0x00").byteValue(),
                Integer.decode("0x04").byteValue(),
                Integer.decode("0x00").byteValue(),
                Integer.decode("0x01").byteValue(),
                Integer.decode("0x00").byteValue(),
                Integer.decode("0xA8").byteValue(),

                Integer.decode("0xC0").byteValue(),
                Integer.decode("0x06").byteValue(),
                Integer.decode("0x00").byteValue() };
        byte[] frameEnd = new byte[] { Integer.decode("0x00").byteValue(),
                Integer.decode("0xA5").byteValue() };
        byte[] phoneFrame = new byte[frameHead.length + 6 + frameEnd.length];
        for (int i = 0; i < phoneFrame.length; i++) {
            if (i < frameHead.length) {
                phoneFrame[i] = frameHead[i];
            } else {
                if ((i - frameHead.length) < 6) {
                    phoneFrame[i] = byteMobile[i - frameHead.length];
                } else {
                    phoneFrame[i] = frameEnd[i - frameHead.length - 6];
                }
            }
        }
        String backString = "";
        for (int i = 0; i < phoneFrame.length; i++) {
            backString += Integer.toHexString(phoneFrame[i] & 0xff) + " ";
        }
        LogUtil.logInfo(tag, "手机注册帧" + backString);
        // **************************************************************
        return phoneFrame;
    }

    /**
     * 方法名称：	getHeartBeat
     * 方法描述：	获取心跳帧
     * 参数：			@param phone
     * 参数：			@return
     * 返回值类型：	byte[]
     * 创建时间：	2015-4-23下午3:57:17
     */
    public static byte[] getHeartBeat(String phone) {
        // 将string型的手机号转换为16进制的string
        long longMobile = Long.parseLong(phone);
        String strMobile = Long.toHexString(longMobile);
        // 将该16进制低位补0，补足12位（取12的原因是因为手机号原本为11位）
        int strLen = strMobile.length();
        StringBuffer strBuffer = null;
        while (strLen < 12) {
            strBuffer = new StringBuffer();
            // 将这个缓冲附加0，再加16进制的string
            strBuffer.append("0").append(strMobile);
            strMobile = strBuffer.toString();
            strLen = strMobile.length();
        }
        // 将新得到的16进制string转为byte[]
        byte[] byteMobile = new byte[strMobile.length() / 2];
        int j = 5;
        for (int i = 0; i < byteMobile.length; i++) {
            byte temp = Integer.decode(
                    "0x" + strMobile.substring(2 * i, 2 * i + 2)).byteValue();
            byteMobile[j] = temp;
            j--;
        }
        byte[] frameHead = new byte[] { 0x5A };
        byte[] frameEnd = new byte[] { 0x00, (byte) 0xff, (byte) 0xA5 };
        byte[] phoneFrame = new byte[frameHead.length + 6 + frameEnd.length];
        for (int i = 0; i < phoneFrame.length; i++) {
            if (i < frameHead.length) {
                phoneFrame[i] = frameHead[i];
            } else {
                if ((i - frameHead.length) < 6) {
                    phoneFrame[i] = byteMobile[i - frameHead.length];
                } else {
                    phoneFrame[i] = frameEnd[i - frameHead.length - 6];
                }
            }
        }
        return phoneFrame;
    }

    /**
     * 方法名称：	byteToInt
     * 方法描述：	将byte转为int，用于对上行数据的处理
     * 参数：			@param b
     * 参数：			@return
     * 返回值类型：	int
     * 创建时间：	2015-2-5下午7:29:26
     */
    public static int byteToInt(byte b) {
        // byte to hexString
        String hexString = Integer.toHexString(b & 0xFF);
        // hesString to int
        int value = Integer.decode("0x" + hexString);
        return value;
    }

    /**
     * 方法名称：	byteToInt
     * 方法描述：	将byte转为int ,用于对上行数据的处理
     * 参数：			@param v1
     * 参数：			@param v2
     * 参数：			@return
     * 返回值类型：	int
     * 创建时间：	2015-2-6上午9:02:49
     */
    public static int byteToInt(byte v1, byte v2) {
        // byte to hexString
        String hexString = Integer.toHexString(v2 & 0xFF)
                + Integer.toHexString(v1 & 0xFF);
        // hexString to long
        int value = Integer.decode("0x" + hexString);
        return value;
    }

    /**
     * 方法名称：	byteToLong
     * 方法描述：	将byte转为long, 用于对上行数据的处理
     * 参数：			@param a
     * 参数：			@param b
     * 参数：			@return
     * 返回值类型：	long
     * 创建时间：	2015-2-5下午7:32:14
     */
    public static long byteToLong(byte v1, byte v2) {
        String str2 = Integer.toHexString(v2 & 0xFF);
        String str1 = Integer.toHexString(v1 & 0xFF);
        if (str2.length() == 1) {
            str2 = "0" + str2;
        }
        if (str1.length() == 1) {
            str1 = "0" + str1;
        }
        // byte to hexString
        String hexString = str2 + str1;
        // hexString to long
        long value = Long.decode("0x" + hexString);
        return value;
    }

    /**
     * 方法名称：	intToByteArray
     * 方法描述：	将int转为byte[]，用于对下行数据转换
     * 参数：			@param a
     * 参数：			@return
     * 返回值类型：	byte
     * 创建时间：	2015-2-6上午8:48:31
     */
    public static byte[] intToByteArray(int v1, int v2) {
        // int to hexString
        String hexString1 = Integer.toHexString(v1);
        String hexString2 = Integer.toHexString(v2);
        // hexString to byte
        byte[] value = new byte[2];
        value[0] = Integer.decode("0x" + hexString1).byteValue();
        value[1] = Integer.decode("0x" + hexString2).byteValue();
        return value;
    }

    /**
     * 方法名称：	intToByteArray
     * 方法描述：	将int转为byte[], 用于对下行数据转换
     * 参数：			@param v1
     * 参数：			@return
     * 返回值类型：	byte[]
     * 创建时间：	2015-2-7下午1:28:24
     */
    public static byte[] intToByteArray(int v1) {
        // int to hexString
        String hexString = Integer.toHexString(v1);
        // be sure the length is even
        if (hexString.length() % 2 != 0) {
            StringBuffer buffer = new StringBuffer();
            buffer.append("0").append(hexString);
            hexString = buffer.toString();
        }
        // hexString to byte[]
        byte[] value = new byte[hexString.length() / 2];
        // indirect byte array
        int j = 1;
        for (int i = 0; i < hexString.length() / 2; i++) {
            value[i] = Integer.decode(
                    "0x" + hexString.substring(2 * j, 2 * j + 2)).byteValue();
            j--;
        }
        return value;
    }

    /**
     * 方法名称：	intToByte
     * 方法描述：	将int转换为byte, 用于对下行数据的转换
     * 参数：			@param v1
     * 参数：			@return
     * 返回值类型：	byte
     * 创建时间：	2015-2-7下午1:55:46
     */
    public static byte intToByte(int v1) {
        // int to hexString
        String hexString = Integer.toHexString(v1);
        // hexString to byte
        byte value = Integer.decode("0x" + hexString).byteValue();
        return value;
    }
    /**
     * 方法名称：	getEasyLinkFrame
     * 方法描述：	获取新设备配置帧
     * 参数：			@param mac
     * 参数：			@return
     * 返回值类型：	byte[]
     * 创建时间：	2015-3-16下午12:19:56
     */
    // public static byte[] getEasyLinkFrame(byte[] mac)
    // {
    // byte[] frame = new byte[9];
    // frame[0] = 0x5A;
    // frame[8] = (byte) 0xA5;
    // frame[7] = 0x00;
    // for (int i = 1; i < frame.length-2; i++)
    // {
    // frame[i] = mac[i-1];
    // }
    // frame[7] = getCheckByte(frame);
    // return frame;
    // }
}
