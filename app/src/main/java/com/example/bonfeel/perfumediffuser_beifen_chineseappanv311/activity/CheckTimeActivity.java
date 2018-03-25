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
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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

import org.ksoap2.serialization.PropertyInfo;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Author:FunFun
 * Function:时间校准界面
 * Date:2016/4/12 11:15
 */
public class CheckTimeActivity extends BaseActivity
{
    private Button btnCheck;
    private TextView tvTitle;
    private TextView tvDeviceTime;
    private TextView tvStandTime;
    private LinearLayout layoutCheckTime;

    private String title = "校准时间";
    private Device device;

    private IntentFilter socketCast;
    private Bundle resBundle;

    private final int GET_TIME_SUCCESS = 1;
    private final int GET_TIME_FAIL = 2;
    private final int GET_TIME_ERROR = 3;

    private final int QUERY_SUCCESS = 11;
    private final int CONTROL_SUCCESS = 12;
    private final int CAST_ERROR = 13;
    private final int DEVICE_LEAVE = 14;

    private Dialog dialog;

    private boolean isLeave;
    private int reCheck;

    //测试用控件
    private EditText etTime;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        setContentView(R.layout.activity_check_time);
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
        dialog = ApplicationUtil.getLoadingDialog(this, "正在查询...");
        dialog.show();
        isLeave = false;
        reCheck = 0;
        for (int i = 0; i < DeviceManager.getInstance().getCount(); i++)
        {
            device = (Device) DeviceManager.getInstance().get(i);
            if ( device.isSelect())
            {
                break;
            }
        }
        //注册广播接收器
        socketCast = new IntentFilter();
        socketCast.addAction(NetElectricsConst.ACTION_CURRENT_TIME);
        registerReceiver(broadcastReceiver, socketCast);

        if (NetCommunicationUtil.getNetConnectState()) {
            checkDeviceTime();
            new Handler().postDelayed(new Runnable()
            {
                @Override
                public void run()
                {
                    checkStandTime();
                }
            }, 2000);
        } else {
            dialog.dismiss();
            ToastUtil.showToast(CheckTimeActivity.this, "请确保网络正确连接!");
        }
    }
    /**
     * 方法名称：	checkDeviceTime
     * 方法描述：	查设备当前时间
     * 参数：
     * 返回值类型：	void
     * 创建时间：	2015-3-2上午10:42:49
     */
    private void checkDeviceTime()
    {
        NetCommunicationUtil.queryDevice(BonfeelFrame.queryCurDate, device.getMac(), NetElectricsConst.CurTime);
    }
    /**
     * 方法名称：	checkStandTime
     * 方法描述：	查服务器当前时间
     * 参数：
     * 返回值类型：	void
     * 创建时间：	2015-3-2上午10:41:56
     */
    private void checkStandTime()
    {
        NetCommunicationUtil.httpConnect(
                getNullPropertyInfos(),
                NetElectricsConst.METHOD_CURRENT_DATE,
                NetElectricsConst.STYLE_OBJECT,
                new NetCommunicationUtil.HttpCallbackListener()
                {
                    @Override
                    public void onFinish(Bundle bundle)
                    {
                        Message message = handler.obtainMessage();
                        if ( bundle.getInt("resCode") == 1 )
                        {
                            resBundle = NetCommunicationUtil.getHttpResultInfo(bundle.getString("result"), NetElectricsConst.METHOD_TAG.GetDate);
                            message.what = GET_TIME_SUCCESS;
                        }
                        else
                        {
                            message.what = GET_TIME_FAIL;
                        }
                        handler.sendMessage(message);
                    }

                    @Override
                    public void onError(Bundle bundle)
                    {
                        Message message = handler.obtainMessage();
                        message.what = GET_TIME_ERROR;
                        handler.sendMessage(message);
                    }
                });
    }
    /* (非Javadoc)
     * 方法名称：	initialView
     * 方法描述：	TODO
     * 重写部分：	@see com.example.bonfeel.activity.BaseActivity#initialView()
     */
    @Override
    protected void initialView()
    {
        btnCheck = (Button)findViewById(R.id.btn_check_time);
        tvTitle = (TextView)findViewById(R.id.tv_title);
        tvDeviceTime = (TextView)findViewById(R.id.tv_device_time_value);
        tvStandTime = (TextView)findViewById(R.id.tv_stand_time_value);
        layoutCheckTime = (LinearLayout)findViewById(R.id.layout_check_time);
        //测试用
        etTime = (EditText)findViewById(R.id.et_feedom_time);

        tvTitle.setText(title);
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
                case GET_TIME_SUCCESS:
                    if ( !isLeave )
                    {
                        if (tvDeviceTime.getText().toString().trim().length() == 0 && reCheck < 10)
                        {
                            checkDeviceTime();
                            checkStandTime();
                            reCheck++;
                        }
                        else
                        {
                            //查询标准时间成功，将标准时间显示
                            dialog.dismiss();
                            if ( device.getError() == 0 )
                            {
                                if ( reCheck < 10 )
                                {
                                    layoutCheckTime.setVisibility(View.VISIBLE);
                                    showStandTime();
                                    new Handler().postDelayed(new Runnable()
                                    {
                                        @Override
                                        public void run()
                                        {
                                            checkStandTime();
                                            checkDeviceTime();
                                        }
                                    }, (60 - Integer.parseInt(resBundle.getString("sec")))*1000);
                                }
                                else
                                {
                                    showInfo("查询超时，请重试");
                                    reCheck = 0;
                                }
                            }
                        }
                    }
                    break;
                case GET_TIME_FAIL:
                    checkStandTime();
                    break;
                case GET_TIME_ERROR:
                    dialog.dismiss();
                    showInfo("查询异常，请重试");
                    break;
                case QUERY_SUCCESS:
                    //查询设备时间成功，将设备时间显示
                    showDeviceTime();
                    break;
                case CONTROL_SUCCESS:
                    //同步设备时间成功，将新的设备时间显示
                    updateTime();
                    showInfo("校准成功");
                    break;
                case CAST_ERROR:
                    dialog.dismiss();
                    //主动上报故障
                    checkError();
                    break;
                case DEVICE_LEAVE:
                    isLeave = true;
                    //设备离线
                    dialog.dismiss();
                    showInfo("设备离线，无法查询");
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
    /* (非Javadoc)
     * 方法名称：	initialHandler
     * 方法描述：	TODO
     * 重写部分：	@see com.example.bonfeel.activity.BaseActivity#initialHandler()
     */
    @Override
    protected void initialHandler()
    {
        btnCheck.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
//				if ( etTime.getText().toString().trim().length() == 4 )
//				{
//					NetCommunicationUtil.controlDevice(
//							feakTime(),
//							device.getMac(),
//							NetElectricsConst.CurTime);
//				}
//				else
//				{
                //设置设备当前时间为服务器的当前时间
                NetCommunicationUtil.controlDevice(
                        syncroTime(),
                        device.getMac(),
                        NetElectricsConst.CurTime);
//				}
            }
        });
    }
    /**
     * 方法名称：	feakTime
     * 方法描述：	测试用时间
     * 参数：			@return
     * 返回值类型：	byte[]
     * 创建时间：	2015-4-8上午9:59:20
     */
    private byte[] feakTime()
    {
        String str = etTime.getText().toString();
        String hour = str.substring(0, 2);
        String min = str.substring(2, 4);

        byte[] content = new byte[12];
        byte[] year = BonfeelFrame.SetCurrentYear(Integer.parseInt("2015"));
        content[0] = year[0];
        content[1] = year[1];
        content[2] = year[2];

        byte[] date = BonfeelFrame.SetCurrentDate(Integer.parseInt("04"),Integer.parseInt("08"));
        content[3] = date[0];
        content[4] = date[1];
        content[5] = date[2];

        byte[] time = BonfeelFrame.SetCurrentTime(Integer.parseInt(hour), Integer.parseInt(min));
        content[6] = time[0];
        content[7] = time[1];
        content[8] = time[2];

        byte[] sec = BonfeelFrame.SetCurrentSec(Integer.parseInt("00"));
        content[9] = sec[0];
        content[10] = sec[1];
        content[11] = sec[2];
        return content;
    }
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            if (intent.getAction().equals(NetElectricsConst.ACTION_CURRENT_TIME))
            {
                //异步解析数据,获取设备的当前时间
                ReceiverTask receiverTask = new ReceiverTask(device, handler);
                receiverTask.execute(intent.getByteArrayExtra("data"));
            }
        }
    };
    /**
     * 方法名称：	getNullPropertyInfos
     * 方法描述：	对于webservice不需要参数的接口，提供空参数集
     * 参数：			@return
     * 返回值类型：	List<PropertyInfo>
     * 创建时间：	2015-2-6上午10:02:12
     */
    private List<PropertyInfo> getNullPropertyInfos()
    {
        List<PropertyInfo> infos = new ArrayList<PropertyInfo>();

        PropertyInfo token = new PropertyInfo();
        token.setName(NetElectricsConst.TOKEN);
        token.setValue("");

        infos.add(token);

        return infos;
    }
    /**
     * 方法名称：	syncroTime
     * 方法描述：	校准设备时间的控制帧数据域
     * 参数：			@return
     * 返回值类型：	byte[]
     * 创建时间：	2015-2-10上午9:29:46
     */
    private byte[] syncroTime()
    {
        byte[] content = new byte[12];

        byte[] year = BonfeelFrame.SetCurrentYear(
                Integer.parseInt(resBundle.getString("year")));
        content[0] = year[0];
        content[1] = year[1];
        content[2] = year[2];

        byte[] date = BonfeelFrame.SetCurrentDate(
                Integer.parseInt(resBundle.getString("month")),
                Integer.parseInt(resBundle.getString("day")));
        content[3] = date[0];
        content[4] = date[1];
        content[5] = date[2];

        byte[] time = BonfeelFrame.SetCurrentTime(
                Integer.parseInt(resBundle.getString("hour")),
                Integer.parseInt(resBundle.getString("min")));
        content[6] = time[0];
        content[7] = time[1];
        content[8] = time[2];

        byte[] sec = BonfeelFrame.SetCurrentSec(
                Integer.parseInt(resBundle.getString("sec")));
        content[9] = sec[0];
        content[10] = sec[1];
        content[11] = sec[2];
        return content;
    }
    /**
     * 方法名称：	showStandTime
     * 方法描述：	显示标准时间
     * 参数：
     * 返回值类型：	void
     * 创建时间：	2015-2-10上午9:11:55
     */
    private void showStandTime()
    {
        tvStandTime.setText(
                resBundle.getString("year")+"-"+
                        resBundle.getString("month")+"-"+
                        resBundle.getString("day")+" "+
                        resBundle.getString("hour")+":"+
                        resBundle.getString("min"));
    }
    /**
     * 方法名称：	showDeviceTime
     * 方法描述：	显示设备时间
     * 参数：
     * 返回值类型：	void
     * 创建时间：	2015-2-10上午9:34:07
     */
    private void showDeviceTime()
    {
        int year = device.getCurTime().getYear();
        int month = device.getCurTime().getMonth();
        String strMonth;
        int day = device.getCurTime().getDate();
        String strDay;
        String time = device.getCurTime().getTime();
        if ( month < 10 )
        {
            strMonth = "0"+month;
        }
        else
        {
            strMonth = month+"";
        }
        if ( day < 10 )
        {
            strDay = "0"+day;
        }
        else
        {
            strDay = day+"";
        }
        if ( time.length() == 3 )
        {
            time = "0"+time.charAt(0)+time.charAt(1)+"0"+time.charAt(2);
        }
        if ( time.length() == 4 )
        {
            if ( time.charAt(1) == ':' )
            {
                time = "0"+time.charAt(0)+time.charAt(1)+time.charAt(2)+time.charAt(3);
            }
            else
            {
                time = ""+time.charAt(0)+time.charAt(1)+time.charAt(2)+"0"+time.charAt(3);
            }
        }
        tvDeviceTime.setText(
                year+"-"+
                        strMonth+"-"+
                        strDay+" "+
                        time);
    }
    /**
     * 方法名称：	updateTime
     * 方法描述：	更新显示同步后的设备时间
     * 参数：
     * 返回值类型：	void
     * 创建时间：	2015-2-10上午9:34:32
     */
    private void updateTime()
    {
        device.getCurTime().setYear(Integer.parseInt(resBundle.getString("year")));
        device.getCurTime().setMonth(Integer.parseInt(resBundle.getString("month")));
        device.getCurTime().setDate(Integer.parseInt(resBundle.getString("day")));
        device.getCurTime().setTime(resBundle.getString("hour")+":"+resBundle.getString("min"));
        showDeviceTime();
    }
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
