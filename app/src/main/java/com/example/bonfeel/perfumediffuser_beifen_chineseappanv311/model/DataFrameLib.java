package com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.model;

/**
 *
 * Author:FunFun
 * Function:通信数据帧库，提供软件通信用的控制码，功能码，以及帧头，帧尾等固定格式内容。
 * 					该类采用单例模式，程序中只存在一个实例，且只能供外部使用，不能修改。
 * Date:2016/4/12 8:51
 */

public class DataFrameLib {
    private byte[][] header;
    private byte[][] len;
    private byte[] logicalPath;
    private byte[] control;
    private byte[] func;
    private byte[][] end;
    private byte[][] subheader;
    private byte[] sublen;
    private byte[] subfunc;
    private byte[][] subend;

    //单例模式
    private static class LibHolder
    {
        static final DataFrameLib DATA_FRAME_LIB = new DataFrameLib();
    }
    public static DataFrameLib getInstance()
    {
        return LibHolder.DATA_FRAME_LIB;
    }
    //在私有的构造函数中进行库的创建
    private DataFrameLib()
    {
        //种类定义
        header = new byte[10][];
        len = new byte[10][];
        logicalPath = new byte[8];
        control = new byte[10];
        func = new byte[10];
        end = new byte[10][];
        subheader = new byte[10][];
        sublen = new byte[10];
        subfunc = new byte[10];
        subend = new byte[10][];
        //帧头
        header[0] = new byte[]{
                Integer.decode("0x5A").byteValue(),
                Integer.decode("0x5D").byteValue()
        };
        //帧长度
        len[0] = new byte[]{
                Integer.decode("0x1F").byteValue(),
                Integer.decode("0x00").byteValue()
        };
        //逻辑通道
        logicalPath[0] = 0x00;
        logicalPath[1] = 0x01;
        logicalPath[2] = 0x02;
        logicalPath[3] = 0x03;
        logicalPath[4] = 0x04;
        logicalPath[5] = 0x05;
        logicalPath[6] = 0x06;
        logicalPath[7] = 0x07;
        //控制码
        control[0] = Integer.decode("0x00").byteValue();
        control[1] = Integer.decode("0xC0").byteValue();
        control[2] = Integer.decode("0x80").byteValue();
        control[3] = Integer.decode("0x40").byteValue();
        //功能码
        func[0] = Integer.decode("0x12").byteValue();
        func[1] = Integer.decode("0x0A").byteValue();
        //外层帧尾
        end[0] = new byte[]{
                Integer.decode("0xA5").byteValue(),
                Integer.decode("0xA5").byteValue()
        };
        //内帧帧头
        subheader[0] = new byte[]{
                Integer.decode("0xDF").byteValue(),
                Integer.decode("0xFD").byteValue()
        };
        //内帧长度
        sublen[0] = Integer.decode("0x20").byteValue();
        //内帧的控制功能码
        subfunc[0] = Integer.decode("0x09").byteValue();//下行查询
        subfunc[1] = Integer.decode("0x19").byteValue();//下行控制
        subfunc[2] = Integer.decode("0xC9").byteValue();//上行查询
        subfunc[3] = Integer.decode("0xD9").byteValue();//上行控制
        subfunc[4] = Integer.decode("0x98").byteValue();//上行主动上报
        subfunc[5] = Integer.decode("0xA9").byteValue();//上行设备离线
        //内帧帧尾
        subend[0] = new byte[]{
                Integer.decode("0x33").byteValue(),
                Integer.decode("0xDE").byteValue()
        };
    }
    /**
     * 返回值： header
     */
    public byte[] getHeader(int tag)
    {
        //帧头用于区别上行帧还是下行帧
        switch (tag)
        {
            case NetElectricsConst.MobileToDevice:
                return header[0];
            case NetElectricsConst.DeviceToMobile:
            default:
                return null;
        }
    }
    /**
     * 返回值： len
     */
    public byte[] getLen(int tag)
    {
        //暂时只有一种
        switch (tag)
        {
            case NetElectricsConst.MobileToDevice:
                return len[0];
            default:
                return null;
        }
    }
    /**
     * 返回值： logicalPath
     */
    public byte getLogicalPath(int tag)
    {
        switch (tag)
        {
            case NetElectricsConst.MobileToDevice:
                return logicalPath[0];
            case NetElectricsConst.DeviceToMobile:
                return logicalPath[1];
            default:
                return 0x00;
        }
    }
    /**
     * 返回值： end
     */
    public byte[] getEnd(int tag)
    {
        switch (tag)
        {
            case NetElectricsConst.MobileToDevice:
                return end[0];
            case NetElectricsConst.DeviceToMobile:
            default:
                return null;
        }
    }
    /**
     * 返回值： subheader
     */
    public byte[] getSubheader(int tag)
    {
        switch (tag)
        {
            case NetElectricsConst.MobileToDevice:
                return subheader[0];
            case NetElectricsConst.DeviceToMobile:
            default:
                return null;
        }
    }
    /**
     * 返回值： sublen
     */
    public byte getSublen(int tag)
    {
        switch (tag)
        {
            case NetElectricsConst.MobileToDevice:
                return sublen[0];
            case NetElectricsConst.DeviceToMobile:
            default:
                return 0x00;
        }
    }
    /**
     * 返回值： subend
     */
    public byte[] getSubend(int tag)
    {
        switch (tag)
        {
            case NetElectricsConst.MobileToDevice:
                return subend[0];
            case NetElectricsConst.DeviceToMobile:
            default:
                return null;
        }
    }
    /**
     * 方法名称：	getControlCode
     * 方法描述：	根据数据帧类型，选取对应的外层控制码
     * 参数：			@param operate
     * 参数：			@return
     * 返回值类型：	byte
     * 创建时间：	2015-1-24上午9:10:19
     */
    public byte getControlCode(String operate)
    {
        if ( operate.equals(NetElectricsConst.OPERATE_QUERY) || operate.equals(NetElectricsConst.OPERATE_CONTROL))
        {
            //0x00,下行
            return control[0];
        }
        else
        {
            if ( operate.equals(NetElectricsConst.OPERATE_RESPOND) )
            {
                //0xc0,上行
                return control[1];
            }
            else
            {
                if ( operate.equals(NetElectricsConst.OPERATE_SUBMIT) )
                {
                    //0x80,主动上报
                    return control[2];
                }
                else
                {
                    return 0x00;
                }
            }
        }
    }
    /**
     * 方法名称：	getFuncCode
     * 方法描述：	根据上行，还是下行来选取外层功能码
     * 参数：			@param tag
     * 参数：			@return
     * 返回值类型：	byte
     * 创建时间：	2015-1-24上午9:14:50
     */
    public byte getFuncCode(int tag)
    {
        int index = 0;
        switch (tag)
        {
            case NetElectricsConst.MobileToDevice:
                index = 0;
                break;
            case NetElectricsConst.DeviceToMobile:
                index = 1;
                break;
        }
        return func[index];
    }
    /**
     * 方法名称：	getControlFuncCode
     * 方法描述：	内层控制功能码由读写，和操作决定,tag = 0为下行，tag = 1为上行
     * 参数：			@param operate
     * 参数：			@param tag
     * 参数：			@return
     * 返回值类型：	byte
     * 创建时间：	2015-1-24上午9:28:26
     */
    public byte getControlFuncCode(String operate, int tag)
    {
        int index = 0;
        switch (tag)
        {
            //MobileToDevice,下行
            case 0:
                if ( operate.equals(NetElectricsConst.OPERATE_QUERY) )
                {
                    //0x09,下行查
                    index = 0;
                }
                else
                {
                    if ( operate.equals(NetElectricsConst.OPERATE_CONTROL) )
                    {
                        //0x19,下行控制
                        index = 1;
                    }
                    else
                    {}
                }
                break;
            //DeviceToMobile,上行
            case 1:
                if ( operate.equals(NetElectricsConst.OPERATE_QUERY) )
                {
                    //0xc9,上行查反馈
                    index = 2;
                }
                else
                {
                    if ( operate.equals(NetElectricsConst.OPERATE_RESPOND) )
                    {
                        //0xd9,上行控制反馈
                        index = 3;
                    }
                    else
                    {
                        if ( operate.equals(NetElectricsConst.OPERATE_SUBMIT) )
                        {
                            //0x98,上行主动上报
                            index = 4;
                        }
                        else
                        {
                            if ( operate.equals(NetElectricsConst.DEVICE_OFFLINE) )
                            {
                                //0xa9,上行设备离线
                                index = 5;
                            }
                            else
                            {}
                        }
                    }
                }
                break;
        }
        return subfunc[index];
    }
}
