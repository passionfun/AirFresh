package com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.activity;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.R;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.model.BonfeelFrame;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.model.Device;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.model.NetElectricsConst;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.util.ApplicationUtil;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.util.DeviceManager;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.util.NetCommunicationUtil;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.util.ReceiverTask;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.util.ToastUtil;

/**
 *
 * Author:FunFun
 * Function:
 * Date:2016/4/12 13:40
 */
public class SurroundTempActivity extends BaseActivity
{
    private TextView tvTitle;
    private TextView tvTemp;
    private TextView tvTag;

    private String title;
    private Device device;
    private IntentFilter tempCast;

    private boolean reCheck;
    private int reCheckTime;
    private final int QUERY_OUT_TIME = 1;
    private final int QUERY_SUCCESS = 11;
    private final int CAST_ERROR = 13;
    private final int DEVICE_LEAVE = 14;
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        setContentView(R.layout.activity_surround_temp);
        super.onCreate(savedInstanceState);
    }

    /* (非Javadoc)
     * 方法名称：	initialData
     * 方法描述：	TODO
     * 重写部分：	@see com.example.bonfeel.activity.BaseActivity#initialData()
     */
    @Override
    protected void initialData()
    {
        title = "周围温度";
        reCheck = true;
        reCheckTime = 0;
        dialog = ApplicationUtil.getLoadingDialog(this, "正在查询...");
        dialog.show();
        for (int i = 0; i < DeviceManager.getInstance().getCount(); i++)
        {
            device = (Device) DeviceManager.getInstance().get(i);
            if ( device.isSelect() )
            {
                break;
            }
        }
        //注册广播
        tempCast = new IntentFilter();
        tempCast.addAction(NetElectricsConst.ACTION_CHECK_TEMPWEAT);
        registerReceiver(broadcastReceiver, tempCast);
        //查询数据
        if (NetCommunicationUtil.getNetConnectState()) {
            queryTempreture();
            // 开始重查
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    reCheckTemp();
                }
            }, 3000);

        } else {
            dialog.dismiss();
            ToastUtil.showToast(SurroundTempActivity.this, "请确保网络正确连接!");
        }
    }
    private void queryTempreture()
    {
        NetCommunicationUtil.queryDevice(
                BonfeelFrame.queryTemp,
                device.getMac(),
                NetElectricsConst.CheckTempWeat);
    }
    /**
     * 方法名称：	reCheckTemp
     * 方法描述：	重查温度
     * 参数：
     * 返回值类型：	void
     * 创建时间：	2015-3-31下午8:23:42
     */
    private void reCheckTemp()
    {
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                while (reCheck && reCheckTime < 5)
                {
                    try
                    {
                        Thread.sleep(1000);
                        queryTempreture();
                        reCheckTime++;
                    }
                    catch ( InterruptedException e )
                    {
                        e.printStackTrace();
                    }
                }
                if ( reCheckTime >= 5 )
                {
                    Message message = handler.obtainMessage();
                    message.what = QUERY_OUT_TIME;
                    handler.sendMessage(message);
                }
            }
        }).start();
    }
    /* (非Javadoc)
     * 方法名称：	initialView
     * 方法描述：	TODO
     * 重写部分：	@see com.example.bonfeel.activity.BaseActivity#initialView()
     */
    @Override
    protected void initialView()
    {
        tvTitle = (TextView)findViewById(R.id.tv_title);
        tvTemp = (TextView)findViewById(R.id.tv_show_temp);
        tvTag = (TextView)findViewById(R.id.tv_temp_tag);

        tvTitle.setText(title);
    }

    /* (非Javadoc)
     * 方法名称：	initialHandler
     * 方法描述：	TODO
     * 重写部分：	@see com.example.bonfeel.activity.BaseActivity#initialHandler()
     */
    @Override
    protected void initialHandler()
    {}
    private Handler handler = new Handler()
    {
        /* (非Javadoc)
         * 方法名称：	handleMessage
         * 方法描述：	TODO
         * 重写部分：	@see android.os.Handler#handleMessage(android.os.Message)
         */
        @Override
        public void handleMessage(Message msg)
        {
            switch (msg.what)
            {
                case QUERY_SUCCESS:
                    dialog.dismiss();
                    reCheck = false;
                    reCheckTime = 0;
                    tvTemp.setText(device.getTemp()+"");
                    tvTag.setVisibility(View.VISIBLE);
                    if ( device.getError() == 0 )
                    {
                        new Handler().postDelayed(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                queryTempreture();
                            }
                        }, 5000);
                    }
                    break;
                case CAST_ERROR:
                    dialog.dismiss();
                    checkError();
                    break;
                case DEVICE_LEAVE:
                    dialog.dismiss();
                    reCheck = false;
                    reCheckTime = 0;
                    showInfo("设备离线,无法查询");
                    break;
                case QUERY_OUT_TIME:
                    dialog.dismiss();
                    showInfo("查询超时，请重试");
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };
    /**
     * 方法名称：	checkError
     * 方法描述：	根据设备主动上报检查故障
     * 参数：
     * 返回值类型：	void
     * 创建时间：	2015-4-10下午12:44:30
     */
    private void checkError()
    {
        if ( device.getError() !=0  )
        {
            switch (device.getError())
            {
                case 1:
                    showInfo("设备故障:香水瓶不匹配");
                    break;
                case 2:
                    showInfo("设备故障:电机故障");
                    break;
                case 4:
                    showInfo("设备故障:香水用完");
                    break;
            }
            stopAlarmTask();
        }
        else
        {
            if ( device.getTempError() != 0 )
            {
                showInfo("温度传感器发生故障");
            }
        }
    }
    /**
     * 方法名称：	stopAlarmTask
     * 方法描述：	停止定时查询服务
     * 参数：
     * 返回值类型：	void
     * 创建时间：	2015-4-10下午5:21:14
     */
    private void stopAlarmTask()
    {
        Intent alarm = new Intent(NetElectricsConst.ACTION_ALARM);
        stopService(alarm);
    }
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            if ( intent.getAction().equals(NetElectricsConst.ACTION_CHECK_TEMPWEAT) )
            {
                ReceiverTask receiverTask = new ReceiverTask(device, handler);
                receiverTask.execute(intent.getByteArrayExtra("data"));
            }
        }
    };

    /* (非Javadoc)
     * 方法名称：	onDestroy
     * 方法描述：	TODO
     * 重写部分：	@see android.app.Activity#onDestroy()
     */
    @Override
    protected void onDestroy()
    {
        unregisterReceiver(broadcastReceiver);
        super.onDestroy();
    }

}

