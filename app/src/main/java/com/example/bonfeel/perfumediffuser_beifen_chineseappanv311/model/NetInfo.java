package com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.model;

/**
 *
 * Author:FunFun
 * Function:网络信息，包括ssid,key,gatewayip
 * Date:2016/4/12 8:56
 */
public class NetInfo {
    private String ssid;
    private String key;
    private String gatewayIp;
    private int phone_ip;

    private String serverIp;
    private int serverPort;
    /**
     * @return the ssid
     */
    public String getSsid()
    {
        return ssid;
    }
    /**
     * @param ssid the ssid to set
     */
    public void setSsid(String ssid)
    {
        this.ssid = ssid;
    }
    /**
     * @return the key
     */
    public String getKey()
    {
        return key;
    }
    /**
     * @param key the key to set
     */
    public void setKey(String key)
    {
        this.key = key;
    }
    /**
     * @return the gatewayIp
     */
    public String getGatewayIp()
    {
        return gatewayIp;
    }
    /**
     * @param gatewayIp the gatewayIp to set
     */
    public void setGatewayIp(String gatewayIp)
    {
        this.gatewayIp = gatewayIp;
    }
    /**
     * @return the phone_ip
     */
    public int getPhone_ip()
    {
        return phone_ip;
    }
    /**
     * @param phone_ip the phone_ip to set
     */
    public void setPhone_ip(int phone_ip)
    {
        this.phone_ip = phone_ip;
    }
    /**
     * 返回值： serverIp
     */
    public String getServerIp()
    {
        return serverIp;
    }
    /**
     * 参数设置： serverIp to set
     */
    public void setServerIp(String serverIp)
    {
        this.serverIp = serverIp;
    }
    /**
     * 返回值： serverPort
     */
    public int getServerPort()
    {
        return serverPort;
    }
    /**
     * 参数设置： serverPort to set
     */
    public void setServerPort(int serverPort)
    {
        this.serverPort = serverPort;
    }
}
