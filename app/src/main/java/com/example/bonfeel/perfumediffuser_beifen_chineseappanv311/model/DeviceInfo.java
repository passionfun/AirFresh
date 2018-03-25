package com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.model;
/**
 *
 * Author:FunFun
 * Function:新设备的信息
 * Date:2016/4/12 8:53
 */
public class DeviceInfo {
    private String name;
    private byte[] mac;
    /**
     * 返回值： name
     */
    public String getName()
    {
        return name;
    }
    /**
     * 参数设置： name to set
     */
    public void setName(String name)
    {
        this.name = name;
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
}
