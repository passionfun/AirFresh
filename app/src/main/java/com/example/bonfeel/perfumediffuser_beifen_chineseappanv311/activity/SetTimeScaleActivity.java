package com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.R;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.custom.CustomDialog;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.custom.TimeScaleAdapter;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.model.BonfeelFrame;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.model.Device;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.model.NetElectricsConst;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.model.TimeScale;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.util.ApplicationUtil;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.util.DeviceManager;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.util.LogUtil;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.util.NetCommunicationUtil;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.util.ReceiverTask;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Author:FunFun
 * Function:时间段选择界面
 * Date:2016/4/12 11:59
 */
public class SetTimeScaleActivity extends BaseActivity
{
    private TextView tvTitle;
    private ListView lvTimeScale;
    private View layoutAdd;
    private Button btnAdd;

    private String title;
    private Device device;
    private List<TimeScale> listTimeScale;
    private TimeScaleAdapter adapter;
    private int selectIndex;

    private IntentFilter timeScaleCast;

    private final int CONTROL_SUCCESS = 12;
    private final int CONFIG_TIMESCALE = 1;

    private CustomDialog itemDialog;
    private View layoutItem;
    private TextView tvConfig;
    private TextView tvDelete;

    private CustomDialog confirmDialog;
    private View layoutConfirm;
    private TextView tvConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        setContentView(R.layout.activity_set_time_scale);
        super.onCreate(savedInstanceState);
    }

    /*
     * 方法名称：	initialData
     * 方法描述：	TODO
     */
    @Override
    protected void initialData()
    {
        title = "时间段设置";
        listTimeScale = new ArrayList<TimeScale>();
        //找到当前选择设备
        for (int i = 0; i < DeviceManager.getInstance().getCount(); i++)
        {
            device = (Device) DeviceManager.getInstance().get(i);
            if ( device.isSelect() )
            {
                break;
            }
        }
        listTimeScale = device.getTimeScales();
        adapter = new TimeScaleAdapter(this, R.layout.timescale_item, listTimeScale);

        timeScaleCast = new IntentFilter();
        timeScaleCast.addAction(NetElectricsConst.ACTION_SET_TIMESCALE);
        registerReceiver(broadcastReceiver, timeScaleCast);
    }

    /*
     * 方法名称：	initialView
     * 方法描述：	TODO
     */
    @Override
    protected void initialView()
    {
        tvTitle = (TextView)findViewById(R.id.tv_title);
        lvTimeScale = (ListView)findViewById(R.id.lv_timescale);
        //添加在时间段列表后面的新增按键的布局
        layoutAdd = LayoutInflater.from(this).inflate(R.layout.layout_add_timescale_button, null);
        btnAdd = (Button)layoutAdd.findViewById(R.id.btn_add_timescale);

        tvTitle.setText(title);
        //在listview的底部添加控件的时候一定要在绑定适配器之前添加
        lvTimeScale.addFooterView(layoutAdd);
        lvTimeScale.setAdapter(adapter);

        if ( listTimeScale.size() == 5 )
        {
            layoutAdd.setVisibility(View.GONE);
        }
    }
    /*
     * 方法名称：	initialHandler
     * 方法描述：	TODO
     */
    @Override
    protected void initialHandler()
    {
        //时间段列表的单击响应，用来开启或关闭时间段
        lvTimeScale.setOnItemClickListener(new OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id)
            {
                if ( !isAddTimeScale(position) )
                {
                    //开关时间段
                    operateTimeScale(position);
                }
                adapter.notifyDataSetChanged();
            }
        });
        //时间段列表的长按响应，用来选择设置时间段还是删除时间段
        lvTimeScale.setOnItemLongClickListener(new OnItemLongClickListener()
        {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           int position, long id)
            {
                selectIndex = position;
                //避免长按新增时间段按钮
                if ( !isAddTimeScale(position) )
                {
                    ApplicationUtil.vibrate(SetTimeScaleActivity.this, 30);
                    showItemDialog();
                }
                return true;
            }
        });
        //新增时间段响应
        btnAdd.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                addTimeScale();
            }
        });
    }
    /**
     * 方法名称：	isAddTimeScale
     * 方法描述：	判断点击的位置是要新增时间段还是开关时间段
     * 参数：			@param index
     * 参数：			@return
     * 返回值类型：	boolean
     * 创建时间：	2015-4-17下午2:26:47
     */
    private boolean isAddTimeScale(int index)
    {
        if ( index == listTimeScale.size() )
        {
            return true;
        }
        return false;
    }
    /**
     * 方法名称：	operateTimeScale
     * 方法描述：	开关时间段
     * 参数：			@param index
     * 返回值类型：	void
     * 创建时间：	2015-4-17下午2:31:17
     */
    private void operateTimeScale(int index)
    {
        String start = listTimeScale.get(index).getStartTime();
        String end = listTimeScale.get(index).getEndTime();

        if ( !start.equals(end))
        {
            if ( listTimeScale.get(index).isWork())
            {
                //发送关闭时间段的操作
                turnOff(listTimeScale.get(index).getId());
                listTimeScale.get(index).setWork(false);
            }
            else
            {
                //发送关闭时间段的操作
                turnOn(listTimeScale.get(index).getId());
                listTimeScale.get(index).setWork(true);
            }
        }
        else
        {
            showInfo("请先设置时间段");
        }
    }
    /**
     * 方法名称：	showItemDialog
     * 方法描述：	弹出选项对话框，提供设置时间段和删除时间段
     * 参数：
     * 返回值类型：	void
     * 创建时间：	2015-4-13上午9:59:15
     */
    private void showItemDialog()
    {
        layoutItem = LayoutInflater.from(SetTimeScaleActivity.this).inflate(R.layout.dialog_change_icon, null);
        tvConfig = (TextView)layoutItem.findViewById(R.id.tv_from_gallary);
        tvDelete = (TextView)layoutItem.findViewById(R.id.tv_from_camera);
        tvConfig.setText("设置参数");
        tvDelete.setText("删除时间段");

        itemDialog = new CustomDialog(SetTimeScaleActivity.this)
                .builder()
                .setTitle("提示")
                .setView(layoutItem)
                .setNegativeButton("取消", new OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {}
                });
        itemDialog.show();
        //设置时间段参数选项
        tvConfig.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                configTimeScale();
                itemDialog.dismiss();
            }
        });
        //删除时间段参数
        tvDelete.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //弹出删除确认框，避免误删除
                showConfirmDialog();
                itemDialog.dismiss();
            }
        });
    }
    /**
     * 方法名称：	showConfirmDialog
     * 方法描述：	删除提示框
     * 参数：
     * 返回值类型：	void
     * 创建时间：	2015-4-18上午9:58:43
     */
    private void showConfirmDialog()
    {
        layoutConfirm = LayoutInflater.from(SetTimeScaleActivity.this).inflate(R.layout.dialog_hint_control, null);
        tvConfirm = (TextView)layoutConfirm.findViewById(R.id.tv_hint_control);

        tvConfirm.setText("确定删除吗？");
        confirmDialog = new CustomDialog(SetTimeScaleActivity.this)
                .builder()
                .setTitle("提示")
                .setView(layoutConfirm)
                .setNegativeButton("取消", new OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {}
                })
                .setPositiveButton("确定", new OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        delTimeScale();
                        adapter.notifyDataSetChanged();
                    }
                });
        confirmDialog.show();
    }
    /**
     * 方法名称：	configTimeScale
     * 方法描述：	设置时间段的参数
     * 参数：
     * 返回值类型：	void
     * 创建时间：	2015-4-17下午2:47:30
     */
    private void configTimeScale()
    {
        Intent setTimeScale = new Intent(SetTimeScaleActivity.this, SetSectorActivity.class);
        setTimeScale.putExtra("scaleId", listTimeScale.get(selectIndex).getId());
        startActivityForResult(setTimeScale, CONFIG_TIMESCALE);
    }
    /**
     * 方法名称：	delTimeScale
     * 方法描述：	删除时间段
     * 参数：
     * 返回值类型：	void
     * 创建时间：	2015-4-17下午2:48:16
     */
    private void delTimeScale()
    {
        turnOff(listTimeScale.get(selectIndex).getId());
        device.delTimeScale(listTimeScale.get(selectIndex).getId());
        if ( listTimeScale.size() < 5 )
        {
            layoutAdd.setVisibility(View.VISIBLE);
        }
    }
    /**
     * 方法名称：	addTimeScale
     * 方法描述：	新增时间段
     * 参数：
     * 返回值类型：	void
     * 创建时间：	2015-4-17下午2:54:45
     */
    private void addTimeScale()
    {
        int j = 0;
        boolean isUsed = false;
        //确认要新增的时间段ID
        do
        {
            for (int i = 0; i < listTimeScale.size(); i++)
            {
                if ( listTimeScale.get(i).getId() == j )
                {
                    isUsed = true;
                    break;
                }
            }
            if ( !isUsed )
            {
                break;
            }
            isUsed = false;
            j++;
        } while (j < 5);

        TimeScale timeScale = new TimeScale(j, "00:00", "00:00");
        device.addNewTimeScale(timeScale);

        if ( listTimeScale.size() == 5 )
        {
            layoutAdd.setVisibility(View.GONE);
        }
        adapter.notifyDataSetChanged();

        Intent newTimeScale = new Intent(SetTimeScaleActivity.this, SetSectorActivity.class);
        newTimeScale.putExtra("scaleId", timeScale.getId());
        LogUtil.logDebug(getTag(), "新增时间段的ID" + timeScale.getId());
        startActivityForResult(newTimeScale, CONFIG_TIMESCALE);
    }
    /* (非Javadoc)
     * 方法名称：	onActivityResult
     * 方法描述：	TODO
     * 重写部分：	@see android.app.Activity#onActivityResult(int, int, android.content.Intent)
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        switch (requestCode)
        {
            case CONFIG_TIMESCALE:
                if ( resultCode == RESULT_OK )
                {
                    adapter.notifyDataSetChanged();
                    int id = data.getIntExtra("scaleId", 0);
                    //判断当前设置的时间段是开启还是关闭，若开启，则将数据同步到设备，若未开启则等待用户开启再同步
                    if ( device.getTimeScale(id).isWork() )
                    {
                        TimeScale timeScale = device.getTimeScale(id);
                        //同步数据
                        byte[] timeFrame = BonfeelFrame.setTimeScale(
                                timeScale.getId(),
                                timeScale.getStartTime().substring(0, 2),
                                timeScale.getStartTime().substring(3, 5),
                                timeScale.getEndTime().substring(0, 2),
                                timeScale.getEndTime().substring(3, 5),
                                timeScale.getWorkTime(),
                                timeScale.getRestTime(),
                                timeScale.getCircle());
                        NetCommunicationUtil.controlDevice(timeFrame,device.getMac(), NetElectricsConst.SetTimeScale);
                        LogUtil.logDebug(getTag(), "该时间段设置已发送给设备");
                    }
                }
                break;
            default:
                break;
        }
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
                case CONTROL_SUCCESS:
                    showInfo("设置成功");
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };
    /**
     * 方法名称：	turnOff
     * 方法描述：	关闭时间段
     * 参数：
     * 返回值类型：	void
     * 创建时间：	2015-2-10下午8:21:13
     */
    private void turnOff(int index)
    {
        byte[] content = BonfeelFrame.setTimeScale(index, "00", "00", "00", "00");
        NetCommunicationUtil.controlDevice(content, device.getMac(), NetElectricsConst.SetTimeScale);
    }
    /**
     * 方法名称：	turnOn
     * 方法描述：	打开时间段
     * 参数：			@param index
     * 返回值类型：	void
     * 创建时间：	2015-2-10下午8:26:44
     */
    private void turnOn(int index)
    {
        byte[] content = BonfeelFrame.setTimeScale(
                index,
                device.getTimeScale(index).getStartTime().substring(0, 2),
                device.getTimeScale(index).getStartTime().substring(3, 5),
                device.getTimeScale(index).getEndTime().substring(0, 2),
                device.getTimeScale(index).getEndTime().substring(3, 5),
                device.getTimeScale(index).getWorkTime(),
                device.getTimeScale(index).getRestTime(),
                device.getTimeScale(index).getCircle());
        NetCommunicationUtil.controlDevice(content, device.getMac(), NetElectricsConst.SetTimeScale);
    }
    //接收时间段设置是否成功的广播接收器
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            if ( intent.getAction().equals(NetElectricsConst.ACTION_SET_TIMESCALE) )
            {
                //用来接收时间段是否开启或关闭成功
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
