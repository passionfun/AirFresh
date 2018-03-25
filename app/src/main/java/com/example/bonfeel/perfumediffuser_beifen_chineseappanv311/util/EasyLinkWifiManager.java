package com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.util;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;

import java.util.List;
/**
 *
 * Author:FunFun
 * Function:Wifi manager class for getting the WIFI details and SSID gateway parameters
 * Date:2016/4/12 9:29
 */
public class EasyLinkWifiManager {
    /**
     * Default wifimanager instance
     */
    private WifiManager mWifiManager=null;

    /**
     * Wifi info instance 
     */
    private WifiInfo mWifiInfo=null;
    /**
     * Called activity context
     */
    private Context mContext=null;

    /**
     * Integer constant to check if this build is currently running under Jellybean and above
     */
    private static final int BUILD_VERSION_JELLYBEAN=17;
    /**
     * Constructor for custom alert dialog.accepting context of called activity
     * @param mContext
     */
    public EasyLinkWifiManager(Context mContext)
    {
        this.mContext=mContext;
        mWifiManager=(WifiManager)mContext.getSystemService(Context.WIFI_SERVICE);
        mWifiInfo=mWifiManager.getConnectionInfo();
    }

    /**
     * 方法名称：	getWifiList
     * 方法描述：	获取wifi信号
     * 参数：			@return
     * 返回值类型：	List<ScanResult>
     * 创建时间：	2015-3-20上午9:58:19
     */
    public List<ScanResult> getWifiList()
    {
        return mWifiManager.getScanResults();
    }
    /**
     * 方法名称：	getScanWifi
     * 方法描述：	进行wifi热点的扫描
     * 参数：			@return
     * 返回值类型：	boolean
     * 创建时间：	2015-4-28下午5:37:39
     */
    public boolean getScanWifi()
    {
        return mWifiManager.startScan();
    }
    /**
     * returns BASE ssid 
     * @return BASE ssid 
     */
    public String getBaseSSID()
    {
        return mWifiInfo.getBSSID();
    }
    /**
     * returns current  ssid  connected to
     * @return current ssid 
     */
    public  String getCurrentSSID()
    {
        return 	getSSIDAfterFilting(mWifiInfo.getSSID());
//		return mWifiInfo.getSSID();
    }

    /**
     *method to check wifi
     *
     * @return true if wifi is connected in our device else false
     */
    public boolean isWifiConnected()
    {
        ConnectivityManager connManager = (ConnectivityManager)mContext. getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if (mWifi.isConnected())
        {
            return true;
        }
        else
            return false;
    }

    /**
     * Returns the current IP address connected to
     * @return
     */
    public  String getCurrentIpAddressConnected()
    {
        int ipval=	mWifiInfo.getIpAddress();
        String ipString = String.format("%d.%d.%d.%d", (ipval & 0xff),(ipval >> 8 & 0xff),(ipval >> 16 & 0xff),	(ipval >> 24 & 0xff));

        return ipString.toString();
    }

    public  int getCurrentIpAddressConnectedInt()
    {
        int ipval=	mWifiInfo.getIpAddress();

        return ipval;
    }

    /**
     * Returns the current GatewayIP address connected to
     * @return
     */
    public  String getGatewayIpAddress()
    {
        int gatwayVal=	mWifiManager.getDhcpInfo().gateway;
        return (String.format("%d.%d.%d.%d", (gatwayVal & 0xff),(gatwayVal >> 8 & 0xff),(gatwayVal >> 16 & 0xff),	(gatwayVal >> 24 & 0xff))).toString();
    }

    /**
     * Filters the double Quotations occuring in Jellybean and above devices.
     * This is only occuring in SDK 17 and above this is documented in SDK as   http://developer.android.com/reference/android/net/wifi/WifiConfiguration.html#SSID
     *
     * @param connectedSSID
     * @return
     */
    public static String getSSIDAfterFilting(String connectedSSID)
    {
        int currentVersion= Build.VERSION.SDK_INT;

        if (currentVersion >= BUILD_VERSION_JELLYBEAN)
        {
            if (connectedSSID.startsWith("\"") && connectedSSID.endsWith("\""))
            {
                connectedSSID = connectedSSID.substring(1, connectedSSID.length()-1);
            }
        }
        return connectedSSID;
    }
}
