package com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.model;
/**
 *
 * Author:FunFun
 * Function:子数据帧，定义了内层数据帧,依据协议定义
 * Date:2016/4/12 9:00
 */
public class SubDataFrame {
    private byte[] header;
    private byte len;
    private byte func;
    private byte[] data;
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
    public byte getLen()
    {
        return len;
    }
    /**
     * 参数设置： len to set
     */
    public void setLen(byte len)
    {
        this.len = len;
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
     * 返回值： data
     */
    public byte[] getData()
    {
        return data;
    }
    /**
     * 参数设置： data to set
     */
    public void setData(byte[] data)
    {
        this.data = data;
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
        frameLength = header.length+1+1+data.length+end.length;
        return frameLength;
    }
}
