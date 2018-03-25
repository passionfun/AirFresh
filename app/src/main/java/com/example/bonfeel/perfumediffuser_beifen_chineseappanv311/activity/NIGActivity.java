package com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.view.View.OnClickListener;

import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.R;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.model.BonfeelFrame;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.model.Device;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.model.NetElectricsConst;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.util.DeviceManager;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.util.NetCommunicationUtil;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.util.ReceiverTask;

/**
 *
 * Author:FunFun
 * Function:
 * Date:2016/4/12 11:45
 */
public class NIGActivity extends BaseActivity
{
    private TextView tvTitle;
    private Button btnNIG;

    private String title;
    private Device device;
    private IntentFilter nigFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        setContentView(R.layout.activity_nig);
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
        title = "负离子发生器";
        for (int i = 0; i < DeviceManager.getInstance().getCount(); i++)
        {
            device = (Device) DeviceManager.getInstance().get(i);
            if ( device.isSelect() )
            {
                break;
            }
        }
        //注册广播
        nigFilter = new IntentFilter();
        nigFilter.addAction(NetElectricsConst.ACTION_CONTROL_NIG);
        registerReceiver(broadcastReceiver, nigFilter);
        //查询NIG开关
        NetCommunicationUtil.queryDevice(BonfeelFrame.queryNIG, device.getMac(), NetElectricsConst.ControlNIG);
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
        btnNIG = (Button)findViewById(R.id.btn_nig);

        tvTitle.setText(title);
    }

    /* (非Javadoc)
     * 方法名称：	initialHandler
     * 方法描述：	TODO
     * 重写部分：	@see com.example.bonfeel.activity.BaseActivity#initialHandler()
     */
    @Override
    protected void initialHandler()
    {
        btnNIG.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if ( device.isNIG() )
                {
                    NetCommunicationUtil.controlDevice(BonfeelFrame.NIGOff, device.getMac(), NetElectricsConst.ControlNIG);
                    device.setNIG(false);
                }
                else
                {
                    NetCommunicationUtil.controlDevice(BonfeelFrame.NIGOn, device.getMac(), NetElectricsConst.ControlNIG);
                    device.setNIG(true);
                }
            }
        });
    }
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
                case 12:
                    //控制成功
                    if ( device.isNIG() )
                    {
                        btnNIG.setText("关闭");
                    }
                    else
                    {
                        btnNIG.setText("开启");
                    }
                    showInfo("控制成功");
                    break;
                case 11:
                    //查询结果，是开是关
                    if ( device.isNIG() )
                    {
                        btnNIG.setText("关闭");
                    }
                    else
                    {
                        btnNIG.setText("开启");
                    }
                default:
                    break;
            }
            super.handleMessage(msg);
        }

    };
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            if ( intent.getAction().equals(NetElectricsConst.ACTION_CONTROL_NIG) )
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
