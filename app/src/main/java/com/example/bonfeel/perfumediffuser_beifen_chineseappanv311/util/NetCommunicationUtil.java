package com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.util;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;

import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.model.Device;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.model.NetElectricsConst;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.model.Problem;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.model.Scene;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.model.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * Author:FunFun
 * Function:网络通信类，提供http通信，socket通信，检测网络连接状态等静态方法。
 * Date:2016/4/12 9:43
 */
public class NetCommunicationUtil {
    private static String stag = "NetCommunicationUtil";
    // 获取全局使用的context
    private static Context context = ApplicationUtil.getContext();
    // socket通信变量
    private static Socket client;
    private static OutputStream outputStream;
    private static InputStream inputStream;
    // socket服务的intent
    private static Intent socketService;
    // 用于暂存场景中的设备列表
    private static List<Device> sceneDevices;
    // 用于设备界面的时间段修改的标志
    private static int step;
    // 根据不同值来区分是故障重查，状态全查，还是定时查
    private static int stepForQuery;
    // 退出系统的标志
    private static int exitTag = 0;
    // 区别场景的群控和单控
    private static boolean isSingle = true;
    // 判断是否是重启软件
    private static boolean isReBoot = false;
    // 用于兼容参数不支持
    private static boolean isCompetable = true;
    // 用于一键控制获取控制成功的个数
    private static int controlSucSize = 0;
    // 设备MAC列表，用于存储已控制成功的设备
    private static List<String> controlSucDevices;

    /************************************************
     * 方法名称：	clean
     * 说明：		对静态数据进行资源释放
     * 创建时间：	2015-5-18
     * 作者：		jkwen
     ***********************************************/
    public static void clean() {
        sceneDevices = null;
        exitTag = 0;
        isSingle = true;
        isReBoot = false;
        isCompetable = true;
        controlSucSize = 0;
        controlSucDevices = new ArrayList<String>();
    }

    /**
     * 返回值： controlSucDevices
     */
    public static int getControlSucDevices() {
        if (controlSucDevices == null) {
            return 0;
        }
        return controlSucDevices.size();
    }

    /**
     * 参数设置： controlSucDevices to set
     */
    public static void addControlSucDevices(String strMac) {
        LogUtil.logDebug("控制成功的设备：", "控制成功的设备：");
        if (controlSucDevices == null) {
            controlSucDevices = new ArrayList<String>();
        }
        for (int i = 0; i < controlSucDevices.size(); i++) {
            if (strMac.equals(controlSucDevices.get(i))) {
                return;
            }
        }
        LogUtil.logDebug("控制成功的设备：", "控制成功的设备：" + strMac + ",");
        controlSucDevices.add(strMac);
    }

    /**
     * clean controlsuccessdevicesformlist
     */
    public static void cleanControlSucDevices() {
        // fun add
        if (controlSucDevices == null) {
            return;
        }
        controlSucDevices.clear();
        // fun add
        controlSucDevices = null;
    }

    /**
     * 返回值： controlSucSize
     */
    public static int getControlSucSize() {
        return controlSucSize;
    }

    /**
     * 参数设置： controlSucSize to set
     */
    public static void setControlSucSize() {
        NetCommunicationUtil.controlSucSize++;
    }

    /**
     * 参数设置：controlSucSize to clean
     */
    public static void cleanControlSucSize() {
        NetCommunicationUtil.controlSucSize = 0;
    }

    /**
     * 返回值： isCompetable
     */
    public static boolean isCompetable() {
        return isCompetable;
    }

    /**
     * 参数设置： isCompetable to set
     */
    public static void setCompetable(boolean isCompetable) {
        NetCommunicationUtil.isCompetable = isCompetable;
    }

    /**
     * 返回值： isReBoot
     */
    public static boolean isReBoot() {
        return isReBoot;
    }

    /**
     * 参数设置： isReBoot to set
     */
    public static void setReBoot(boolean isReBoot) {
        NetCommunicationUtil.isReBoot = isReBoot;
    }

    /**
     * 返回值： isSingle
     */
    public static boolean isSingle() {
        return isSingle;
    }

    /**
     * 参数设置： isSingle to set
     */
    public static void setSingle(boolean isSingle) {
        NetCommunicationUtil.isSingle = isSingle;
    }

    /**
     * 返回值： exitTag
     */
    public static int getExitTag() {
        return exitTag;
    }

    /**
     * 参数设置： exitTag to set
     */
    public static void setExitTag(int exitTag) {
        NetCommunicationUtil.exitTag = exitTag;
    }

    /**
     * 返回值： stepForQuery
     */
    public static int getStepForQuery() {
        return stepForQuery;
    }

    /**
     * 参数设置： stepForQuery to set
     */
    public static void setStepForQuery(int stepForQuery) {
        NetCommunicationUtil.stepForQuery = stepForQuery;
    }

    /**
     * 返回值： step
     */
    public static int getStep() {
        return step;
    }

    /**
     * 参数设置： step to set
     */
    public static void setStep(int step) {
        NetCommunicationUtil.step = step;
    }

    /****************************************************
     *
     * 接口名称：	HttpCallbackListener
     * 作者：			jackWen
     * 创建时间：	2015-1-5
     * 说明：			http连接的回调函数
     *
     ***************************************************/
    public interface HttpCallbackListener {
        void onFinish(Bundle bundle);

        void onError(Bundle bundle);
    }

    /****************************************************
     *
     * 接口名称：	SocketCallbackListener
     * 作者：			jackWen
     * 创建时间：	2015-1-14
     * 说明：			socket连接的回调函数
     *
     ***************************************************/
    public interface SocketCallbackListener {
        void onFinish(byte[] buffer);

        void onTimeOut(SocketTimeoutException e);

        void onSocketClose(SocketException e);

        void onError(Exception e);
    }

    /****************************************************
     *
     * 接口名称：	SocketSendCallbackListener
     * 作者：			jackWen
     * 创建时间：	2015-1-14
     * 说明：			socket通信的回调函数
     *
     ***************************************************/
    public interface SocketSendCallbackListener {
        void onSucced();

        void onFailed();

        void onError(Exception e);
    }

    /**
     * 方法名称：	getNetConnectState
     * 方法描述：	静态方法，检测网络的连接状态
     * 参数：			@return
     * 返回值类型：	boolean
     * 创建时间：	2015-1-5上午11:17:02
     */
    public static boolean getNetConnectState() {
        ConnectivityManager connect = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connect != null) {
            NetworkInfo[] networkInfos = connect.getAllNetworkInfo();
            if (networkInfos != null) {
                for (int i = 0; i < networkInfos.length; i++) {
                    if (networkInfos[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 方法名称：	isWifiConnected
     * 参数：			@return true if wifi is connected in our device else false
     * 返回值类型：	boolean
     * 创建时间：	2015-5-4上午11:40:18
     */
    public static boolean isWifiConnected() {
        ConnectivityManager connManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        return mWifi.isConnected();
    }

    /**
     * 方法名称：	isMobileConnected
     * 参数：			@return true if mobile is connected in our device else false
     * 返回值类型：	boolean
     * 创建时间：	2015-5-4下午4:42:35
     */
    public static boolean isMobileConnected() {
        ConnectivityManager connManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager
                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        return mWifi.isConnected();
    }

    /**
     * @return Returns the current IP address connected to
     */
    public static String getCurrentSSID() {
        WifiManager mWifiManager = (WifiManager) context
                .getSystemService(Context.WIFI_SERVICE);
        WifiInfo mWifiInfo = mWifiManager.getConnectionInfo();
        return mWifiInfo.getSSID();
    }

    /**
     * 方法名称：	getMobileMac
     * 方法描述：	静态方法，用于获取无线网络的设备物理地址（这里的设备指运行软件的设备，一般是手机和平板）
     * 参数：			@return
     * 返回值类型：	String
     * 创建时间：	2015-1-5上午11:20:25
     */
    public static String getMobileMac() {
        WifiManager wifi = (WifiManager) context
                .getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifi.getConnectionInfo();
        return (wifiInfo == null) ? "NULL" : wifiInfo.getMacAddress();
    }

    /**
     * 方法名称：	httpConnect
     * 方法描述：	静态方法，进行http通信，可验证用户登录是否成功，设备是否在线，
     * 参数：			@param params 参数集
     * 参数：			@param method 方法名
     * 参数：			@param listener 回调方法
     * 2015-03-19 新增参数：int style 查询结果的类型
     * 返回值类型：	void
     * 创建时间：	2015-1-5下午1:26:13
     */
    public static void httpConnect(final List<PropertyInfo> params,
                                   final String method, final int style,
                                   final HttpCallbackListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpTransportSE transportSE;
                SoapSerializationEnvelope envelope;
                Bundle bundle = new Bundle();
                Log.i("inwebservice:",
                        "inwebservice1:" + bundle.get("devicemac"));
                // WSDL的地址
                transportSE = new HttpTransportSE(NetElectricsConst.HTTP_URL);
                transportSE.debug = true;
                // soap版本
                envelope = new SoapSerializationEnvelope(
                        NetElectricsConst.HTTP_VERSION);
                envelope.bodyOut = setBodyOut(method, params);
                envelope.dotNet = true;
                try {
                    // 执行webservice的方法调用
                    transportSE.call(
                            (NetElectricsConst.HTTP_NAMESPACE + method),
                            envelope);
                    SoapObject backObject = (SoapObject) envelope.bodyIn;
                    String result = backObject.getProperty(0).toString();
                    Log.i(" BonfeelWebservice", "method:" + method+",result:"+result);
                    LogUtil.logDebug(stag + "lzw=====", "" + result);
                    // 对返回结果进行判断
                    // || result.equals("[]")
                    if (result.equals("") || result.length() == 0
                            || result == null) {
                        bundle.putInt("resCode", 0);
                        bundle.putString("message", "无返回数据");
                    } else {
                        switch (style) {
                            case NetElectricsConst.STYLE_RES:
                                JSONTokener jsonTokener = new JSONTokener(result);
                                JSONObject jsonObject = (JSONObject) jsonTokener
                                        .nextValue();
                                if (jsonObject.getString("ResCode").equals("0")) {
                                    bundle.putInt("resCode", 0);
                                } else {
                                    bundle.putInt("resCode", 1);
                                }
                                break;
                            case NetElectricsConst.STYLE_OBJECT:
                                bundle.putInt("resCode", 1);
                                break;
                            default:
                                break;
                        }
                        bundle.putString("message", "有返回数据");
                        bundle.putString("result", result);
                    }
                    // 回调通信结果
                    if (listener != null) {
                        listener.onFinish(bundle);
                    }
                } catch (Exception e) {
                    bundle.putInt("resCode", 0);
                    bundle.putString("message", e.getMessage());
                    if (listener != null) {
                        listener.onError(bundle);
                    }
                }
            }
        }).start();
    }

    /**
     * 方法名称：	setBodyOut
     * 方法描述：	匹配方法和参数
     * 参数：			@param params
     * 参数：			@param method
     * 参数：			@return
     * 返回值类型：	SoapObject
     * 创建时间：	2015-1-14上午10:32:54
     */
    private static SoapObject setBodyOut(String method,
                                         List<PropertyInfo> params) {
        SoapObject soapObject = new SoapObject(
                NetElectricsConst.HTTP_NAMESPACE, method);
        for (PropertyInfo propertyInfo : params) {
            soapObject.addProperty(propertyInfo);
        }
        return soapObject;
    }

    /**
     * 方法名称：	getHttpResultInfo
     * 方法描述：	根据方法tag对http通信返回的结果进行解析,数据量较少时调用
     * 参数：			@param result
     * 参数：			@param tag
     * 参数：			@return
     * 返回值类型：	Bundle
     * 创建时间：	2015-1-31下午1:33:33
     */
    public static Bundle getHttpResultInfo(String result,
                                           NetElectricsConst.METHOD_TAG tag) {
        Bundle resultInfo = new Bundle();
        try {
            switch (tag) {
                case UserLogin:
                    JSONTokener jsonUser = new JSONTokener(result);
                    JSONObject jsonUserObject = (JSONObject) jsonUser.nextValue();
                    resultInfo.putString("resCode",
                            jsonUserObject.getString("ResCode"));
                    resultInfo.putString("message",
                            jsonUserObject.getString("Message"));
                    break;
                case MatchDevice:
                    JSONTokener jsonDevice = new JSONTokener(result);
                    JSONObject jsonDeviceObject = (JSONObject) jsonDevice
                            .nextValue();
                    resultInfo.putString("resCode",
                            jsonDeviceObject.getString("ResCode"));
                    resultInfo.putString("message",
                            jsonDeviceObject.getString("Message"));
                    break;
                case GetDate:
                    resultInfo = stringToDate(result);
                    break;
                case GetAppVersion:
                    JSONTokener jsonApp = new JSONTokener(result);
                    JSONObject jsonAppObject = (JSONObject) jsonApp.nextValue();
                    resultInfo.putString("version",
                            jsonAppObject.getString("version"));
                    resultInfo.putString("time",
                            jsonAppObject.getString("updatetime"));
                    resultInfo.putString("content",
                            jsonAppObject.getString("updatecontent"));
                    resultInfo.putString("url",
                            jsonAppObject.getString("updateurl"));
                    break;
                case GetUserInfo:
                    JSONTokener jsonUserInfo = new JSONTokener(result);
                    JSONObject jsonUserInfoObject = (JSONObject) jsonUserInfo
                            .nextValue();
                    resultInfo
                            .putString("id", jsonUserInfoObject.getString("用户编码"));
                    resultInfo.putString("name",
                            jsonUserInfoObject.getString("用户名称"));
                    resultInfo.putString("areaid",
                            jsonUserInfoObject.getString("区域编码"));
                    resultInfo.putString("areaname",
                            jsonUserInfoObject.getString("区域名称"));
                    resultInfo.putString("signdate",
                            jsonUserInfoObject.getString("注册日期"));
                    resultInfo.putString("commit_addr",
                            jsonUserInfoObject.getString("通讯地址"));
                    resultInfo.putString("jiguan",
                            jsonUserInfoObject.getString("归属地"));
                    resultInfo
                            .putString("mail", jsonUserInfoObject.getString("邮箱"));
                    resultInfo.putString("mobilephone",
                            jsonUserInfoObject.getString("手机号"));
                    resultInfo.putString("qq", jsonUserInfoObject.getString("QQ"));
                    resultInfo.putString("weixin",
                            jsonUserInfoObject.getString("微信"));
                    resultInfo.putString("iconUrl",
                            jsonUserInfoObject.getString("头像"));
                    break;
                default:
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return resultInfo;
    }

    /**
     * 方法名称：	stringToDate
     * 方法描述：	将服务器的时间字符串提取出来
     * 参数：			@param result
     * 参数：			@return
     * 返回值类型：	Bundle
     * 创建时间：	2015-2-6上午10:41:55
     */
    private static Bundle stringToDate(String result) {
        Bundle res = new Bundle();
        String yymmdd = "";
        String hhmmss = "";
        String year = "";
        String month = "";
        String day = "";
        String hour = "";
        String min = "";
        String second = "";
        String temp = "";
        int index = 0;
        // 先取出年月日和时间
        for (int i = 0; i < result.length(); i++) {
            if (result.charAt(i) == ' ') {
                yymmdd = result.substring(0, i);
                hhmmss = result.substring(i + 1, result.length());
                break;
            }
        }
        // 分别取出年、月、日
        for (int i = 0; i < yymmdd.length(); i++) {
            if (yymmdd.charAt(i) == '-') {
                index++;
            } else {
                temp += yymmdd.charAt(i);
            }
            if (i == yymmdd.length() - 1) {
                index++;
            }
            switch (index) {
                case 1:
                    year = temp;
                    temp = "";
                    index = 2;
                    break;
                case 3:
                    month = temp;
                    temp = "";
                    index = 4;
                    break;
                case 5:
                    day = temp;
                    temp = "";
                    index = 0;
                    break;
                default:
                    break;
            }
        }
        // 分别取出时、分、秒
        for (int i = 0; i < hhmmss.length(); i++) {
            if (hhmmss.charAt(i) == ':') {
                index++;
            } else {
                temp += hhmmss.charAt(i);
            }
            if (i == hhmmss.length() - 1) {
                index++;
            }
            switch (index) {
                case 1:
                    hour = temp;
                    temp = "";
                    index = 2;
                    break;
                case 3:
                    min = temp;
                    temp = "";
                    index = 4;
                    break;
                case 5:
                    second = temp;
                    temp = "";
                    index = 6;
                    break;
                default:
                    break;
            }
        }
        res.putString("year", year);
        res.putString("month", month);
        res.putString("day", day);
        res.putString("hour", hour);
        res.putString("min", min);
        res.putString("sec", second);
        return res;
    }

    /**
     * 方法名称：	getHttpResultInfo
     * 方法描述：	根据方法tag对http通信返回的结果进行解析,数据量较大时调用
     * 参数：			@param result
     * 参数：			@param tag
     * 参数：			@param manager
     * 返回值类型：	void
     * 创建时间：	2015-1-31下午3:49:07
     */
    public static void getHttpResultInfo(NetElectricsConst.METHOD_TAG tag,
                                         String result) {
        try {
            JSONArray jsonArray = new JSONArray(result);
            JSONObject item = new JSONObject();
            switch (tag) {
                // 设备列表
                case GetDevice:
                    for (int i = 0; i < jsonArray.length(); i++) {
                        Device device = (Device) DeviceManager.getInstance()
                                .create();
                        String addr = "";
                        int index = 0;
                        item = jsonArray.getJSONObject(i);
                        device.setId(item.getInt("设备系统编号"));
                        device.setName(item.getString("设备名称"));
                        device.setMac(item.getString("设备地址"));
                        device.setSelect(Integer.parseInt(item.getString("是否最近使用")) == 1 ? true
                                : false);
                        for (int j = 0; j < item.getString("安装地址").length(); j++) {
                            if (item.getString("安装地址").charAt(j) == '-') {
                                index++;
                            } else {
                                addr += item.getString("安装地址").charAt(j);
                                if (j == item.getString("安装地址").length() - 1) {
                                    index++;
                                }
                            }
                            switch (index) {
                                case 1:
                                    device.getLocation().setProvince(addr);
                                    addr = "";
                                    index = 2;
                                    break;
                                case 3:
                                    //device.getLocation().setCity(addr);
                                    device.getLocation().setDetail(addr);
                                    addr = "";
                                    index = 4;
                                    break;
                                case 5:
                                    device.getLocation().setCounty(addr);
                                    addr = "";
                                    index = 6;
                                    break;
                                case 7:
                                    device.getLocation().setDetail(addr);
                                    addr = "";
                                    break;
                                default:
                                    break;
                            }
                        }
                        device.setLeave(Integer.parseInt(item.getString("在线状态")) == 1 ? false
                                : true);
                        DeviceManager.getInstance().add(device);
                    }
                    break;
                case GetProblems:
                    for (int i = 0; i < jsonArray.length(); i++) {
                        Problem problem = (Problem) ProblemManager.getInstance()
                                .create();
                        item = jsonArray.getJSONObject(i);
                        problem.setName(item.getString("问题名称"));
                        problem.setContent(item.getString("问题解答"));
                        ProblemManager.getInstance().add(problem);
                    }
                    break;
                case GetScene:
                    for (int i = 0; i < jsonArray.length(); i++) {
                        Scene scene = (Scene) SceneManager.getInstance().create();
                        item = jsonArray.getJSONObject(i);
                        scene.setId(item.getString("模式编号"));
                        scene.setName(item.getString("模式名称"));
                        scene.setIconUrl(item.getString("图片路径"));
                        // scene.setSelect(Integer.parseInt(item.getString("是否最近使用"))
                        // == 1?true:false);
                        SceneManager.getInstance().add(scene);
                    }
                    break;
                case SceneDetail:
                    sceneDevices = new ArrayList<Device>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        Device device = (Device) DeviceManager.getInstance()
                                .create();
                        item = jsonArray.getJSONObject(i);

                        device.setSceneID(item.getString("唯一序号"));
                        device.setMac(item.getString("设备Mac地址"));
                        device.setName(item.getString("设备名称"));
                        // fun add 测试
                        // Log.i("devicename", item.getString("设备名称"));
                        if (item.getString("参数1").equals("00")) {
                            device.setPower(false);
                        } else {
                            device.setPower(true);
                        }
                        sceneDevices.add(device);
                        for (int j = 0; j < sceneDevices.size(); j++) {
                            LogUtil.logDebug(stag,
                                    "NetCommunicationUtil scenedevice:"
                                            + sceneDevices.get(j).getMac());
                        }

                    }
                    break;
                case UpdateDevice:
                    for (int i = 0; i < jsonArray.length(); i++) {
                        item = jsonArray.getJSONObject(i);
                        int index = item.getInt("设备系统编号");
                        boolean state = (Integer.parseInt(item.getString("在线状态")) == 1 ? false
                                : true);
                        DeviceManager.getInstance().update(index, state);
                    }
                    break;
                default:
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 返回值： sceneDevices
     */
    public static List<Device> getSceneDevices() {
        return sceneDevices;
    }

    /**
     * 方法名称：	socketConnect
     * 方法描述：	socket通信，用于建立socket连接
     * 参数：			@param listener
     * 返回值类型：	void
     * 创建时间：	2015-1-14下午1:09:59
     */
    public static void socketConnect(final SocketCallbackListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    LogUtil.logDebug(stag, "ready for building socket pipe");
                    // 创建socket通信的客户端点，通信通道
                    createSocketPoint();
                    // 监听循环
                    while (true) {
                        LogUtil.logDebug(stag,
                                "socket pipe is built success, listening");
                        try {
                            if (client != null) {
                                byte[] buffer = new byte[50];// 50改为1024
                                if (-1 != inputStream.read(buffer)) {
                                    String backString = "";
                                    for (int i = 0; i < buffer.length; i++) {
                                        backString += Integer
                                                .toHexString(buffer[i] & 0xff)
                                                + " ";
                                    }
                                    LogUtil.logInfo("socket:", "source data："
                                            + backString);
                                    if (listener != null) {
                                        listener.onFinish(buffer);
                                    }
                                }
                            } else {
                                break;
                            }
                        } catch (SocketException e) {
                            LogUtil.logDebug(stag, "network is shutdown");
                            if (listener != null) {
                                listener.onSocketClose(e);
                                break;
                            }
                        } catch (SocketTimeoutException e) {
                            LogUtil.logDebug(stag, "socket TimeOut");
                            if (listener != null) {
                                listener.onTimeOut(e);
                            }
                        } catch (Exception e) {
                            LogUtil.logDebug(stag, "socket监听异常");
                            if (listener != null) {
                                listener.onError(e);
                            }
                        }
                    }
                } catch (Exception e) {
                    LogUtil.logDebug(stag, "循环外的catch");
                    if (listener != null) {
                        listener.onError(e);
                    }
                }
            }
        }).start();
    }

    /**
     * 方法名称：	createSocketPoint
     * 方法描述：	创建socket通信端点
     * 参数：
     * 返回值类型：	void
     * 创建时间：	2015-1-14下午12:01:48
     * @throws Exception
     */
    private static void createSocketPoint() throws Exception {
        LogUtil.logDebug(stag, "creating socket client, waitting...");
//		client = new Socket(NetElectricsConst.SOCKET_IP,
//				NetElectricsConst.SOCKET_PORT);
//		client.setSoTimeout(NetElectricsConst.SOCKET_TIMEOUT);
//		inputStream = client.getInputStream();
//		outputStream = client.getOutputStream();

        client = new Socket();
        SocketAddress sa = new InetSocketAddress(NetElectricsConst.SOCKET_IP, NetElectricsConst.SOCKET_PORT);
        client.connect(sa, 30000);
        client.setSoTimeout(NetElectricsConst.SOCKET_TIMEOUT);//设置读取数据超时
        inputStream = client.getInputStream();
        outputStream = client.getOutputStream();
    }

    /**
     * 方法名称：	socketSend
     * 方法描述：	socket通信，查询设备状态，控制设备以及监听上行数据
     * 参数：			@param data
     * 参数：			@param listener
     * 返回值类型：	void
     * 创建时间：	2015-1-14下午1:25:15
     */
    public static void socketSend(final byte[] data,
                                  final SocketSendCallbackListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    LogUtil.logDebug(stag, "写数据的client：" + client);
                    if (client != null) {
                        outputStream.write(data);
                        outputStream.flush();
                        // 通信成功
                        if (listener != null) {
                            listener.onSucced();
                        }
                    } else {
                        LogUtil.logDebug(stag,
                                "socket client is null, need rebuild");
                        // 通信失败，通信通道不通
                        if (listener != null) {
                            listener.onFailed();
                        }
                    }
                } catch (Exception e) {
                    LogUtil.logDebug(stag, "Broken pipe or other errors");
                    // 通信错误
                    if (listener != null) {
                        listener.onError(e);
                    }
                }
            }
        }).start();
    }

    /**
     * 方法名称：	socketClose
     * 方法描述：	关闭socket连接
     * 参数：
     * 返回值类型：	void
     * 创建时间：	2015-1-14下午1:32:04
     */
    public static void socketClose() {
        try {
            if (client != null) {
                outputStream.close();
                inputStream.close();
                client.close();
            }
        } catch (Exception e) {
        }
    }

    /**
     * 方法名称：	buildListener
     * 方法描述：	建立socket通信端口
     * 参数：
     * 返回值类型：	void
     * 创建时间：	2015-2-11上午11:27:52
     */
    public static void buildListener() {
        socketService = new Intent(NetElectricsConst.ACTION_CONNECT);
        ApplicationUtil.getContext().startService(socketService);
    }

    /**
     * 方法名称：	queryDevice
     * 方法描述：	发送查询设备状态的数据帧
     * 参数：			@param queryFrame
     * 返回值类型：	void
     * 创建时间：	2015-2-7上午11:32:44
     */
    public static void queryDevice(byte[] content, String mac, int tag) {
        // 生成查询数据帧
        byte[] dataFrame = DataFrameUtil.createFrame(
                NetElectricsConst.OPERATE_QUERY, mac, content);
        // 启动后台socket服务，进行写数据操作
        socketService = new Intent(NetElectricsConst.ACTION_SEND);
        socketService.putExtra("dataFrame", dataFrame);
        socketService.putExtra("classTag", tag);
        ApplicationUtil.getContext().startService(socketService);
    }

    /**
     * 方法名称：	controlDevice
     * 方法描述：	发送控制设备状态的数据帧
     * 参数：			@param content
     * 参数：			@param mac
     * 返回值类型：	void
     * 创建时间：	2015-2-7上午11:58:46
     */
    public static void controlDevice(byte[] content, String mac, int tag) {
        byte[] dataFrame = DataFrameUtil.createFrame(
                NetElectricsConst.OPERATE_CONTROL, mac, content);
        socketService = new Intent(NetElectricsConst.ACTION_SEND);
        socketService.putExtra("dataFrame", dataFrame);
        socketService.putExtra("classTag", tag);
        ApplicationUtil.getContext().startService(socketService);
    }

    /**
     * 方法名称：	phoneSignUp
     * 方法描述：	用户手机注册时，发送的数据帧，用于用户注册服务器
     * 参数：			@param content
     * 参数：			@param tag
     * 返回值类型：	void
     * 创建时间：	2015-2-7下午4:06:20
     */
    @SuppressWarnings("null")
    public static void phoneSignUp(String phone, int tag) {
        // fun add
        if (phone == null && phone.isEmpty()) {
            return;
        }
        byte[] dataFrame = DataFrameUtil.getPhoneFrame(phone);
        socketService = new Intent(NetElectricsConst.ACTION_SEND);
        socketService.putExtra("dataFrame", dataFrame);
        socketService.putExtra("classTag", tag);
        ApplicationUtil.getContext().startService(socketService);
    }

    /**
     * 方法名称：	heartBeat
     * 方法描述：	发送心跳包
     * 参数：
     * 返回值类型：	void
     * 创建时间：	2015-4-23下午4:04:06
     */
    public static void heartBeat() {
        byte[] dataFrame = getHeartBeatFrame();
        String backString = "";
        for (int i = 0; i < dataFrame.length; i++) {
            backString += Integer.toHexString(dataFrame[i] & 0xff) + " ";
        }
        LogUtil.logInfo(stag, "心跳帧" + backString);
        Log.i("心跳帧的内容", "heart beat" + backString);// fun add
        socketService = new Intent(NetElectricsConst.ACTION_SEND);
        socketService.putExtra("dataFrame", dataFrame);
        ApplicationUtil.getContext().startService(socketService);
    }

    public static byte[] getHeartBeatFrame() {
        String phone = ((User) UserManager.getInstance().get(0)).getPhone();
        byte[] dataFrame = DataFrameUtil.getHeartBeat(phone);
        return dataFrame;
    }

    /**
     * 方法名称：	errorFeedback
     * 方法描述：	对主动上报帧（故障上报）的反馈
     * 参数：			@param content
     * 参数：			@param mac
     * 参数：			@param tag
     * 返回值类型：	void
     * 创建时间：	2015-4-8上午8:51:07
     */
    public static void errorFeedback(byte[] content, String mac, int tag) {
        byte[] dataFrame = DataFrameUtil.createFrame(
                NetElectricsConst.OPERATE_CONTROL, mac, content);
        socketService = new Intent(NetElectricsConst.ACTION_SEND);
        socketService.putExtra("dataFrame", dataFrame);
        socketService.putExtra("classTag", tag);
        ApplicationUtil.getContext().startService(socketService);
    }
}
