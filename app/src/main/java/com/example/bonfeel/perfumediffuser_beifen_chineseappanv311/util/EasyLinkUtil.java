package com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.util;

import android.content.Context;
import android.net.wifi.WifiManager;

import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.model.ConfigQueue;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.model.DeviceInfo;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.model.NetInfo;

import java.net.SocketAddress;
/**
 *
 * Author:FunFun
 * Function:一键配置操作
 * Date:2016/4/12 9:27
 */
public class EasyLinkUtil {
    private static NetInfo netInfo;
    private static WifiManager.MulticastLock multicastLock;
    private static DeviceInfo deviceInfo;

    private static final int INITIAL_QUEUE_SIZE = 8;
    private static final int INCREASE_SIZE = 4;
    private static ConfigQueue[] queue;
    private static ConfigQueue[] existQueue;

    /************************************************
     * 方法名称：	setNetInfo
     * 说明：		设置网络的ssid，key，和gatewayIp
     * 创建时间：	2015-3-12
     * 作者：		jkwen
     ***********************************************/
    public static void setNetInfo(String key, String ip, int port, Context context)
    {
        EasyLinkWifiManager elWifiManager = new EasyLinkWifiManager(context);
        if (netInfo == null)
        {
            netInfo = new NetInfo();
        }
        netInfo.setSsid(elWifiManager.getCurrentSSID());
        netInfo.setKey(key);
        netInfo.setGatewayIp(elWifiManager.getGatewayIpAddress());
        netInfo.setPhone_ip(elWifiManager.getCurrentIpAddressConnectedInt());
        netInfo.setServerIp(ip);
        netInfo.setServerPort(port);
    }
    public static NetInfo getNetInfo()
    {
        return netInfo;
    }
    /************************************************
     * 方法名称：	getNetSsid
     * 说明：		获取网络SSID
     * 创建时间：	2015-3-12
     * 作者：		jkwen
     ***********************************************/
    public static String getNetSsid(Context context)
    {
        EasyLinkWifiManager elWifiManager = new EasyLinkWifiManager(context);
        return elWifiManager.getCurrentSSID();
    }
    /**
     * 方法名称：	MulticastLockAcquire
     * 方法描述：	获取资源
     * 参数：
     * 返回值类型：	void
     * 创建时间：	2015-3-13上午10:53:50
     */
    public static void MulticastLockAcquire(Context context)
    {
        if (multicastLock == null)
        {
            multicastLock = ((WifiManager) context.getSystemService(Context.WIFI_SERVICE)).createMulticastLock("multicast.test");
        }
        multicastLock.acquire();
    }
    /**
     * 方法名称：	MulticastLockRelease
     * 方法描述：	释放资源
     * 参数：
     * 返回值类型：	void
     * 创建时间：	2015-3-13上午10:56:36
     */
    public static void MulticastLockRelease(Context context)
    {
        if (multicastLock == null)
        {
            multicastLock = ((WifiManager) context.getSystemService(Context.WIFI_SERVICE)).createMulticastLock("multicast.test");
        }
        multicastLock.release();
    }
    /**
     * 返回值： deviceInfo
     */
    public static DeviceInfo getDeviceInfo()
    {
        return deviceInfo;
    }
    /**
     * 参数设置： deviceInfo to set
     */
    public static void setDeviceInfo(byte[] mac)
    {
        if ( deviceInfo == null )
        {
            deviceInfo = new DeviceInfo();
        }
        deviceInfo.setMac(mac);
        //mac地址即初始设备名
        String name = "";
        for (int i = 0; i < mac.length; i++)
        {
            name = name + Integer.toHexString(mac[i] & 0xFF);
        }
        deviceInfo.setName(name);
    }
    /**
     * 方法名称：	pushQueue
     * 方法描述：	获取到新的mac入队列
     * 参数：			@param mac
     * 参数：			@param ip
     * 返回值类型：	void
     * 创建时间：	2015-4-14下午12:11:31
     */
    public static void pushQueue(byte[] mac, SocketAddress sa)
    {
        boolean isFull = true;
        boolean isExist = false;
        if ( queue == null )
        {
            queue = new ConfigQueue[INITIAL_QUEUE_SIZE];
        }
        //检查队列中是否已有该设备
        for (int i = 0; i < queue.length; i++)
        {
            //queue[i]是否为空
            if ( queue[i] != null)
            {
                if ( Helper.byteArray2String(queue[i].getMac()).equals(Helper.byteArray2String(mac)) )
                {
                    isExist = true;
                }
            }
            else
            {
                isFull = false;
                if ( !isExist )
                {
                    if ( isNewDevice(mac) )
                    {
                        queue[i] = new ConfigQueue();
                        queue[i].setMac(mac);
                        queue[i].setSocket(sa);
                    }
                }
                break;
            }
        }
        //是否需要开辟新空间
        if ( isFull )
        {
            ConfigQueue[] newQueue = new ConfigQueue[queue.length + INCREASE_SIZE];
            for (int i = 0; i < queue.length; i++)
            {
                newQueue[i] = queue[i];
            }
            if ( !isExist )
            {
                if ( isNewDevice(mac) )
                {
                    newQueue[queue.length] = new ConfigQueue();
                    newQueue[queue.length].setMac(mac);
                    newQueue[queue.length].setSocket(sa);
                }
            }
            queue = newQueue;
        }
    }
    /**
     * 方法名称：	popQueue
     * 方法描述：	从队列中取出设备
     * 参数：			@param index
     * 参数：			@return
     * 返回值类型：	ConfigQueue
     * 创建时间：	2015-4-14下午2:48:21
     */
    public static ConfigQueue popQueue(int index)
    {
        //越界
        if ( queue == null || index >= queue.length )
        {
            return null;
        }
        return queue[index];
    }
    /**
     * 方法名称：	getConfigQueues
     * 方法描述：	返回新入网的设备
     * 参数：			@return
     * 返回值类型：	ConfigQueue[]
     * 创建时间：	2015-4-15下午12:31:35
     */
    public static ConfigQueue[] getConfigQueues()
    {
        if ( queue == null )
        {
            return null;
        }
        int len = 0;
        for (int i = 0; i < queue.length; i++)
        {
            if ( queue[i] != null )
            {
                len++;
            }
            else
            {
                break;
            }
        }
        if ( len == 0 )
        {
            return null;
        }
        ConfigQueue[] configQueues = new ConfigQueue[len];
        for (int i = 0; i < configQueues.length; i++)
        {
            configQueues[i] = queue[i];
        }
        return configQueues;
    }
    private static boolean isNewDevice(byte[] mac)
    {
        boolean isNew = true;
        if ( existQueue == null )
        {
            return isNew;
        }
        for (int i = 0; i < existQueue.length; i++)
        {
            if ( existQueue[i] != null )
            {
                if ( Helper.byteArray2String(existQueue[i].getMac()).equals(Helper.byteArray2String(mac)) )
                {
                    System.out.println("the device has already in this network");
                    isNew = false;
                    break;
                }
            }
            else
            {
                break;
            }
        }
        return isNew;
    }
    /**
     * 方法名称：	pushExistQueue
     * 方法描述：	存入网络中已有的设备
     * 参数：			@param mac
     * 参数：			@param sa
     * 返回值类型：	void
     * 创建时间：	2015-4-15上午11:54:05
     */
    public static void pushExistQueue(byte[] mac, SocketAddress sa)
    {
        boolean isFull = true;
        boolean isExist = false;
        if ( existQueue == null )
        {
            existQueue = new ConfigQueue[INITIAL_QUEUE_SIZE];
        }
        //检查队列中是否已有该设备
        for (int i = 0; i < existQueue.length; i++)
        {
            //queue[i]是否为空
            if ( existQueue[i] != null)
            {
                if ( Helper.byteArray2String(existQueue[i].getMac()).equals(Helper.byteArray2String(mac)) )
                {
                    isExist = true;
                }
            }
            else
            {
                isFull = false;
                if ( !isExist )
                {
                    existQueue[i] = new ConfigQueue();
                    existQueue[i].setMac(mac);
                    existQueue[i].setSocket(sa);
                }
                break;
            }
        }
        //是否需要开辟新空间
        if ( isFull )
        {
            ConfigQueue[] newQueue = new ConfigQueue[existQueue.length + INCREASE_SIZE];
            for (int i = 0; i < existQueue.length; i++)
            {
                newQueue[i] = existQueue[i];
            }
            if ( !isExist )
            {
                newQueue[existQueue.length] = new ConfigQueue();
                newQueue[existQueue.length].setMac(mac);
                newQueue[existQueue.length].setSocket(sa);
            }
            existQueue = newQueue;
        }
    }
    public static void clean()
    {
        existQueue = null;
        queue = null;
        netInfo = null;
    }
}
