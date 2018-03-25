package com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.model.NetElectricsConst;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.util.LogUtil;

/**
 *
 * Author:FunFun
 * Function:后台定时服务,用于定时进行时间校准和判断当前设备所运行着的时间段
 * Date:2016/4/12 10:14
 */
public class AlarmService extends Service {
    private String tag = "后台定时服务";
    private static long RUNNING_TIME = 60 * 1000;
    private AlarmManager alarmManager;
    private long triggerTime;
    private Intent receiver;
    private PendingIntent pendReceiver;

    /**
     * 返回值： rUNNING_TIME
     */
    public static long getRUNNING_TIME() {
        return RUNNING_TIME;
    }

    /**
     * 参数设置： rUNNING_TIME to set
     */
    public static void setRUNNING_TIME(long rUNNING_TIME) {
        RUNNING_TIME = rUNNING_TIME;
    }

    /* (非Javadoc)
     * 方法名称：	onBind
     * 方法描述：	TODO
     * 重写部分：	@see android.app.Service#onBind(android.content.Intent)
     */
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /* (非Javadoc)
     * 方法名称：	onCreate
     * 方法描述：	TODO
     * 重写部分：	@see android.app.Service#onCreate()
     */
    @Override
    public void onCreate() {
        LogUtil.logDebug(tag, "alarmService onCreate exetected");
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        super.onCreate();
    }

    /* (非Javadoc)
     * 方法名称：	onStartCommand
     * 方法描述：	TODO
     * 重写部分：	@see android.app.Service#onStartCommand(android.content.Intent, int, int)
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogUtil.logDebug(tag, "alarmService onStartCommand exetected");
        // 启动定时广播
        triggerTime = System.currentTimeMillis() + RUNNING_TIME;
        receiver = new Intent(NetElectricsConst.ACTION_ALARM_CAST);
        pendReceiver = PendingIntent.getBroadcast(this, 0, receiver, 0);
        alarmManager.set(AlarmManager.RTC_WAKEUP, triggerTime, pendReceiver);
        return super.onStartCommand(intent, flags, startId);
    }

    /* (非Javadoc)
     * 方法名称：	onDestroy
     * 方法描述：	TODO
     * 重写部分：	@see android.app.Service#onDestroy()
     */
    @Override
    public void onDestroy() {
        LogUtil.logDebug(tag, "alarmService onDestroy exetected");
        alarmManager.cancel(pendReceiver);
        stopSelf();
        super.onDestroy();
    }
}
