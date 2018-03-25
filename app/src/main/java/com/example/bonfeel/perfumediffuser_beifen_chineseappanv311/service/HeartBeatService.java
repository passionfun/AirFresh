package com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.model.NetElectricsConst;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.util.LogUtil;

/**
 *
 * Author:FunFun
 * Function:心跳服务,用来告知服务，用户是在线的
 * Date:2016/4/12 10:16
 */
public class HeartBeatService extends Service {
    private String tag = "Heart Beat Service";
    private long BEAT_FREQUENCY = 30 * 1000;
    private AlarmManager alarmManager;
    private long triggerTime;
    private Intent receiver;
    private PendingIntent pendReceiver;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        LogUtil.logDebug(tag, "heart beat onCreate exetected");
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("心跳", "heart beat");
        LogUtil.logDebug(tag, "heart beat onStartCommand exetected " + intent);
        triggerTime = System.currentTimeMillis() + BEAT_FREQUENCY;
        receiver = new Intent(NetElectricsConst.ACTION_HEART_BEAT);
        pendReceiver = PendingIntent.getBroadcast(this, 0, receiver, 0);
        alarmManager.set(AlarmManager.RTC_WAKEUP, triggerTime, pendReceiver);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        alarmManager.cancel(pendReceiver);
        stopSelf();
        super.onDestroy();
    }
}
