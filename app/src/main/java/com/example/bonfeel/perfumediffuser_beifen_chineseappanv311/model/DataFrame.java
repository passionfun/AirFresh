package com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.model;

/**
 *
 * Author:FunFun
 * Function:数据帧类，该类用于软件的网络数据通信,依据协议定义
 * Date:2016/4/12 8:50
 */
public class DataFrame {
    private byte[] header;
    private byte[] len;
    private byte[] mac;
    private byte logic;
    private byte control;
    private byte func;
    private SubDataFrame subDataFrame;
    private byte[] end;

    private int frameLength;



    /**
     * 返回值： header
     */
    public byte[] getHeader()
    {
        return header;
    }
    /**
     * 参数设置： header to set
     */
    public void setHeader(byte[] header)
    {
        this.header = header;
    }
    /**
     * 返回值： len
     */
    public byte[] getLen()
    {
        return len;
    }
    /**
     * 参数设置： len to set
     */
    public void setLen(byte[] len)
    {
        this.len = len;
    }
    /**
     * 返回值： mac
     */
    public byte[] getMac()
    {
        return mac;
    }
    /**
     * 参数设置： mac to set
     */
    public void setMac(byte[] mac)
    {
        this.mac = mac;
    }
    /**
     * 返回值： logic
     */
    public byte getLogic()
    {
        return logic;
    }
    /**
     * 参数设置： logic to set
     */
    public void setLogic(byte logic)
    {
        this.logic = logic;
    }
    /**
     * 返回值： control
     */
    public byte getControl()
    {
        return control;
    }
    /**
     * 参数设置： control to set
     */
    public void setControl(byte control)
    {
        this.control = control;
    }
    /**
     * 返回值： func
     */
    public byte getFunc()
    {
        return func;
    }
    /**
     * 参数设置： func to set
     */
    public void setFunc(byte func)
    {
        this.func = func;
    }
    /**
     * 返回值： subDataFrame
     */
    public SubDataFrame getSubDataFrame()
    {
        if ( subDataFrame == null )
        {
            subDataFrame = new SubDataFrame();
        }
        return subDataFrame;
    }
    /**
     * 参数设置： subDataFrame to set
     */
    public void setSubDataFrame(SubDataFrame subDataFrame)
    {
        this.subDataFrame = subDataFrame;
    }
    /**
     * 返回值： end
     */
    public byte[] getEnd()
    {
        return end;
    }
    /**
     * 参数设置： end to set
     */
    public void setEnd(byte[] end)
    {
        this.end = end;
    }
    /**
     * 返回值： frameLength
     */
    public int getFrameLength()
    {
        frameLength = header.length+len.length+mac.length+1+1+1+subDataFrame.getFrameLength()+end.length;
        return frameLength;
    }

}
