package com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.model;

import java.net.SocketAddress;

/**
 *
 * Author:FunFun
 * Function:配置队列，存放mac和对应的网络IP
 * Date:2016/4/12 8:48
 */
public class ConfigQueue {
    private byte[] mac;
    private String ip;
    private SocketAddress socket;
    /**
     * 构造方法名称：	ConfigQueue
     * 创建时间：		2015-4-14下午12:09:13
     * 方法描述：		TODO
     */
    public ConfigQueue()
    {
        super();
        this.mac = new byte[6];
        this.ip = "";
        this.socket = null;
    }

    /**
     * 返回值： socket
     */
    public SocketAddress getSocket()
    {
        return socket;
    }

    /**
     * 参数设置： socket to set
     */
    public void setSocket(SocketAddress socket)
    {
        this.socket = socket;
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
     * 返回值： ip
     */
    public String getIp()
    {
        return ip;
    }
    /**
     * 参数设置： ip to set
     */
    public void setIp(String ip)
    {
        this.ip = ip;
    }
}
