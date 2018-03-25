package com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.model.NetElectricsConst;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.util.LogUtil;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.util.NetCommunicationUtil;

import java.net.SocketException;
import java.net.SocketTimeoutException;

/**
 *
 * Author:FunFun
 * Function:socket后台服务，用于监听socket通信
 * Date:2016/4/12 10:17
 */
public class SocketService extends Service {
    private String tag = "socket通信服务";
    private byte[] dataFrame;
    private int classTag;
    private Intent caster;

    /*
     * 方法名称：	onBind
     * 方法描述：	TODO
     */
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /*
     * 方法名称：	onCreate
     * 方法描述：	TODO
     */
    @Override
    public void onCreate() {
        LogUtil.logDebug(tag, "socketService onCreate executed");
        super.onCreate();
    }

    /*
     * 方法名称：	onStartCommand
     * 方法描述：	TODO
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogUtil.logDebug(tag, "socketService onStartCommand intent " + intent);
        if (intent == null) {
            return super.onStartCommand(intent, flags, startId);
        } else {
            dataFrame = intent.getByteArrayExtra("dataFrame");
        }
        classTag = intent.getIntExtra("classTag", 0);
        switch (switchAction(intent.getAction())) {
            // socket连接,监听
            case 1:
                NetCommunicationUtil.socketConnect(new NetCommunicationUtil.SocketCallbackListener() {
                    // 成功接收到上行数据的回调
                    @Override
                    public void onFinish(byte[] buffer) {
                        //logSourceData(buffer);
					/*
					 * 判断是数据帧还是心跳帧
					 */
                        boolean isHeart = true;
                        byte[] tempHeart = NetCommunicationUtil.getHeartBeatFrame();
                        for (int i = 0; i < tempHeart.length; i++) {
                            if (tempHeart[i] != buffer[i]) {
                                getAction();
                                caster.putExtra("data", buffer);
                                sendBroadcast(caster);
                                isHeart = false;
                                break;
                            }
                        }
                        if (isHeart) {
                            // 发送心跳包
                            NetCommunicationUtil.heartBeat();
                        }
                    }

                    // 监听异常
                    @Override
                    public void onError(Exception e) {
                        caster = new Intent(NetElectricsConst.ACTION_DATA_FAILED);
                        sendBroadcast(caster);
                        e.printStackTrace();
                        LogUtil.logDebug(tag, "onError");
                    }

                    // 监听超时
                    @Override
                    public void onTimeOut(SocketTimeoutException e) {
                        e.printStackTrace();
                        LogUtil.logDebug(tag, "连接超时!");
                    }

                    // 监听关闭
                    @Override
                    public void onSocketClose(SocketException e) {
					/*
					 * socket closed
					 * close alarmService and heartBeatService
					 */
                        Intent heartBeat = new Intent(
                                NetElectricsConst.ACTION_HEART_BEAT);
                        stopService(heartBeat);
                        Intent alarm = new Intent(NetElectricsConst.ACTION_ALARM);
                        stopService(alarm);
                        e.printStackTrace();
                        LogUtil.logDebug(tag, "onsocket!");
                    }
                });
                break;
            // socket写，即下行数据写
            case 2:
                NetCommunicationUtil.socketSend(dataFrame,
                        new NetCommunicationUtil.SocketSendCallbackListener() {
                            // 下行数据写成功
                            @Override
                            public void onSucced() {
                                caster = new Intent(
                                        NetElectricsConst.ACTION_DATA_WRITE);
                                sendBroadcast(caster);
                                LogUtil.logDebug(tag,
                                        "send data to server is success...");
                            }

                            // socket client is null, need rebuild
                            @Override
                            public void onFailed() {
                                caster = new Intent(
                                        NetElectricsConst.ACTION_DATA_FAILED);
                                sendBroadcast(caster);
                                LogUtil.logDebug(tag,
                                        "socket disconnect,need reconnect!");
                            }

                            // Broken pipe or other errors
                            @Override
                            public void onError(Exception e) {
                                caster = new Intent(
                                        NetElectricsConst.ACTION_DATA_ERROR);
                                sendBroadcast(caster);
                                e.printStackTrace();
                            }
                        });
                break;
            // socket关
            case 3:
                NetCommunicationUtil.socketClose();
                stopSelf();
                break;
            default:
                break;
        }
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 方法名称：	logSourceData
     * 方法描述：	打印收到的源数据
     * 参数：			@param buffer
     * 返回值类型：	void
     * 创建时间：	2015-4-23下午2:49:29
     */
    private void logSourceData(byte[] buffer) {
        String backString = "";
        for (int i = 0; i < buffer.length; i++) {
            backString += Integer.toHexString(buffer[i] & 0xff) + " ";
        }
        LogUtil.logInfo(tag, "服务器返回的数据帧" + backString);
        Log.i("socket data:", "服务器返回的数据帧:" + backString);
    }

    /**
     * 方法名称：	getAction
     * 方法描述：	根据不同的tag进行不同的action广播
     * 参数：
     * 返回值类型：	void
     * 创建时间：	2015-4-23下午2:16:19
     */
    private void getAction() {
        switch (classTag) {
            case 1:
                // 手机注册成功的广播
                caster = new Intent(NetElectricsConst.ACTION_PHONE_SIGNUP);
                break;
            case 2:
                // 设备注册成功的广播
                caster = new Intent(NetElectricsConst.ACTION_DEVICE_SIGNIN);
                break;
            case 3:
                // 操作成功的广播//查询状态，时间段成功的广播
                caster = new Intent(NetElectricsConst.ACTION_DATA_RECEIVE);
                break;
            case 4:
                // 查询当前时间成功的广播
                caster = new Intent(NetElectricsConst.ACTION_CURRENT_TIME);
                break;
            case 5:
                // 设置时间段成功的广播
                caster = new Intent(NetElectricsConst.ACTION_SET_TIMESCALE);
                break;
            case 6:
                // 设置设备界面的时间段广播
                caster = new Intent(NetElectricsConst.ACTION_SET_CURTIMESCALE);
                break;
            case 7:
                // 查询温湿度的广播
                caster = new Intent(NetElectricsConst.ACTION_CHECK_TEMPWEAT);
                break;
            case 8:
                // 控制负离子发生器
                caster = new Intent(NetElectricsConst.ACTION_CONTROL_NIG);
                break;
            default:
                caster = new Intent(NetElectricsConst.ACTION_DATA_RECEIVE);
                break;
        }
        LogUtil.logDebug(tag, "" + caster);
    }

    /**
     * 方法名称：	switchAction
     * 方法描述：	根据不同的action执行不同的case
     * 参数：			@param action
     * 参数：			@return
     * 返回值类型：	int
     * 创建时间：	2015-4-23下午2:14:56
     */
    private int switchAction(String action) {
        int index = 0;
        if (action.equals(NetElectricsConst.ACTION_CONNECT)) {
            index = 1;
            System.out.println("the action is socket connect");
        } else {
            if (action.equals(NetElectricsConst.ACTION_SEND)) {
                index = 2;
                System.out.println("the action is socket send");
            } else {
                if (action.equals(NetElectricsConst.ACTION_CLOSE)) {
                    index = 3;
                    System.out.println("the action is socket close");
                } else {
                }
            }
        }
        return index;
    }

    /*
     * 方法名称：	onDestroy
     * 方法描述：	TODO
     */
    @Override
    public void onDestroy() {
        System.out.println("socketService onDestory executed");
        stopSelf();
        super.onDestroy();
    }
}
