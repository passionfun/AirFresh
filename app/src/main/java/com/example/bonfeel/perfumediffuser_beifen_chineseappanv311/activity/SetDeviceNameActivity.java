package com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.view.View.OnClickListener;

import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.R;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.model.Device;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.model.NetElectricsConst;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.util.ApplicationUtil;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.util.DeviceManager;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.util.NetCommunicationUtil;

import org.ksoap2.serialization.PropertyInfo;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Author:FunFun
 * Function:修改设备名称界面
 * Date:2016/4/12 11:54
 */
public class SetDeviceNameActivity extends BaseActivity
{
    private List<Device> mallDevices; //lzw add
    private boolean sameName = false; //lzw add
    private TextView tvTitle;
    private EditText etNewName;
    private Button btnConfirm;
    private TextView tvSetLocation;

    private Device device;
    private Message message;
    private String newName;

    private final int SET_NAME_SUCCESS = 1;
    private final int SET_NAME_FAIL = 2;
    private final int SET_NAME_ERROR = 3;

    private Dialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        setContentView(R.layout.activity_set_device_name);
        super.onCreate(savedInstanceState);
    }

    /* (非Javadoc)
     * 方法名称：	onResume
     * 方法描述：	TODO
     * 重写部分：	@see android.app.Activity#onResume()
     */
    @Override
    protected void onResume()
    {
        tvSetLocation.setText(
                device.getLocation().getProvince()+
                        device.getLocation().getCity()+
                        device.getLocation().getCounty()+
                        device.getLocation().getDetail());
        super.onResume();
    }

    /*
     * 方法名称：	initialData
     * 方法描述：	TODO
     */
    @Override
    protected void initialData()
    {
        for (int i = 0; i < DeviceManager.getInstance().getCount(); i++)
        {
            device = (Device) DeviceManager.getInstance().get(i);
            if ( device.isSelect() )
            {
                break;
            }
        }
    }
    /*
     * 方法名称：	initialView
     * 方法描述：	TODO
     */
    @Override
    protected void initialView()
    {
        tvTitle = (TextView)findViewById(R.id.tv_title);
        etNewName = (EditText)findViewById(R.id.et_new_device_name);
        btnConfirm = (Button)findViewById(R.id.btn_confirms);
        tvSetLocation = (TextView)findViewById(R.id.tv_set_location);

        tvTitle.setText("更改名称");
//		btnOk.setVisibility(View.VISIBLE);
//		btnOk.setText("");
//		btnOk.setBackgroundResource(R.drawable.button_finish);
        etNewName.setText(device.getName());
        etNewName.setSelection(device.getName().length());

        tvSetLocation.setText(
                device.getLocation().getProvince()+
                        device.getLocation().getCity()+
                        device.getLocation().getCounty()+
                        device.getLocation().getDetail());
    }
    /*
     * 方法名称：	initialHandler
     * 方法描述：	TODO
     */
    @Override
    protected void initialHandler()
    {
        //保存按钮
        btnConfirm.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if ( isInput() )
                {
                    dialog = ApplicationUtil.getLoadingDialog(SetDeviceNameActivity.this, "正在修改");
                    dialog.show();

					/*lzw add*/
                    String deviceName = etNewName.getText().toString();
                    mallDevices = DeviceManager.getInstance().getDevices();
                    //fun add
                    sameName = false;
                    for (int i = 0; i < mallDevices.size(); i++)
                    {
                        if ( deviceName.equals(mallDevices.get(i).getName()) )
                        {
                            sameName = true;
                            break;
                        }
                    }
                    if ( !sameName )
                    {
                        newName = etNewName.getText().toString().trim();
                        upDateName();
                    }
                    else {
                        dialog.dismiss();
                        showInfo("与已有设备名称重复，请重新设置！");
                    }

                }
            }
        });
        //设备位置设置
        tvSetLocation.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if ( device.getLocation().getProvince().equals("") )
                {
                    //引导设置地址
                    Intent setIntent = new Intent(SetDeviceNameActivity.this, LocProvinceActivity.class);
                    startActivity(setIntent);
                }
                else
                {
                    //修改地址
                    Intent detailIntent = new Intent(SetDeviceNameActivity.this, LocDetailActivity.class);
                    startActivity(detailIntent);
                }
            }
        });
    }
    /**
     * 方法名称：	upDateName
     * 方法描述：	更新名称
     * 参数：
     * 返回值类型：	void
     * 创建时间：	2015-4-6上午10:35:28
     */
    private void upDateName()
    {
        NetCommunicationUtil.httpConnect(
                getDeviceInfos(),
                NetElectricsConst.METHOD_CHANGE_DEVICE,
                NetElectricsConst.STYLE_RES,
                new NetCommunicationUtil.HttpCallbackListener() {
                    @Override
                    public void onFinish(Bundle bundle) {
                        message = handler.obtainMessage();
                        if (bundle.getInt("resCode") == 1) {
                            message.what = SET_NAME_SUCCESS;
                        } else {
                            message.what = SET_NAME_FAIL;
                        }
                        handler.sendMessage(message);
                    }

                    @Override
                    public void onError(Bundle bundle) {
                        message = handler.obtainMessage();
                        message.what = SET_NAME_ERROR;
                        handler.sendMessage(message);
                    }
                });
    }
    /**
     * 方法名称：	getDeviceInfos
     * 方法描述：	更新设备名称和安装地址的参数集
     * 参数：			@return
     * 返回值类型：	List<PropertyInfo>
     * 创建时间：	2015-2-6上午11:55:17
     */
    private List<PropertyInfo> getDeviceInfos()
    {
        List<PropertyInfo> infos = new ArrayList<PropertyInfo>();

        PropertyInfo deviceMac = new PropertyInfo();
        deviceMac.setName(NetElectricsConst.DEVICE_MAC_ID);
        deviceMac.setValue(device.getMac());

        PropertyInfo deviceName = new PropertyInfo();
        deviceName.setName(NetElectricsConst.DEVICE_NAME_ID);
        deviceName.setValue(newName);

        PropertyInfo deviceAddr = new PropertyInfo();
        deviceAddr.setName(NetElectricsConst.INSTALL_ADDR);
        deviceAddr.setValue(
                device.getLocation().getProvince()+"-"+
                        device.getLocation().getCity()+"-"+
                        device.getLocation().getCounty()+"-"+
                        device.getLocation().getDetail());

        infos.add(deviceMac);
        infos.add(deviceName);
        infos.add(deviceAddr);

        return infos;
    }
    /**
     * 方法名称：	isInput
     * 方法描述：	判断是否是新名字
     * 参数：			@return
     * 返回值类型：	boolean
     * 创建时间：	2015-4-6上午10:30:02
     */
    private boolean isInput()
    {
        if ( etNewName.getText().toString().trim().length() != 0)
        {
            return true;
        }
        else
        {
            showInfo("请输入新名称");
            return false;
        }
    }
    private Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            switch (msg.what)
            {
                case SET_NAME_SUCCESS:
                    dialog.dismiss();
                    showInfo("修改成功");
                    device.setName(newName);
                    finish();
                    break;
                case SET_NAME_FAIL:
                    dialog.dismiss();
                    break;
                case SET_NAME_ERROR:
                    dialog.dismiss();
                    showInfo("修改异常，请重试");
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };
}