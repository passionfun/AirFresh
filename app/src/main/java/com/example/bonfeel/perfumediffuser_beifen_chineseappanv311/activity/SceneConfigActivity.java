package com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.LayoutParams;
import android.widget.TextView;

import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.R;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.custom.CustomDialog;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.custom.SwitchButton;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.model.Device;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.model.NetElectricsConst;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.model.Scene;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.util.DeviceManager;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.util.Helper;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.util.LogUtil;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.util.NetCommunicationUtil;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.util.SceneManager;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.util.ToastUtil;

import org.ksoap2.serialization.PropertyInfo;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * Author:FunFun
 * Function:
 * Date:2016/4/12 11:48
 */
public class SceneConfigActivity extends BaseActivity
{
    private TextView tvTitle;
    private ImageView ivSceneIcon;
    private TextView tvSceneName;
    private TextView tvDeviceName;
    private RadioGroup rgDeviceCheck, rgDeviceList;
    private LinearLayout layoutDeviceProperty;
    //	private Button btnPower;
    private SwitchButton sbPower;
    //	private Button btnFinish;
    private Button btnToControl;//lzw add
    //private Button btnSave;

    private String title;
    private Scene scene;
    private List<Device> allDevices;
    private List<Device> bindDevices;
    private List<Device> unbindDevices;
    private List<Device> rList;
    private Device device;

    private CustomDialog iconDialog;
    private View layoutIconView;
    private TextView tvGallary;
    private TextView tvCamera;

    private File file;
    private final int SET_NAME = 1;
    private final int FROM_CONTENT = 2;
    private final int TAKE_PHOTO = 3;
    private final int CROP_PHOTO = 4;
    private final int photo_size = 320;

    private Bitmap photo;

    private final int TAG_IMAGE = 1;
    private final int TAG_NAME = 2;
    private final int TAG_LIST = 3;

    private final int GET_DETAIL_SUCCESS = 1;
    private final int GET_DETAIL_FAIL = 2;
    private final int GET_DETAIL_ERROR = 3;
    private final int UPDATE_ICON_SUCCESS = 4;
    private final int UPDATE_ICON_FAIL = 5;
    private final int UPDATE_ICON_ERROR = 6;
    private final int UNBIND_SUCCESS = 7;
    private final int UNBIND_FAIL = 8;
    private final int UNBIND_ERROR = 9;
    private final int BIND_SUCCESS = 10;
    private final int BIND_FAIL = 11;
    private final int BIND_ERROR = 12;
    private final int PRO_SET_SUCCESS = 13;
    private final int PRO_SET_FAIL = 14;
    private final int PRO_SET_ERROR = 15;

    private Device changeDevice;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        setContentView(R.layout.activity_scene_config);
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
        title = "场景配置";
        scene = (Scene) SceneManager.getInstance().get(getIntent().getStringExtra("id"));
        Log.i("sid", "sid:" + getIntent().getStringExtra("id"));
        //获取设备列表，并显示
        allDevices = DeviceManager.getInstance().getDevices();

		/*
		 * 调用I_GetSceneDetail(sceneid)
		 * 返回该场景的具体信息
		 */
        sceneDetail();
    }
    /* (非Javadoc)
     * 方法名称：	initialView
     * 方法描述：	TODO
     * 重写部分：	@see com.example.bonfeel.activity.BaseActivity#initialView()
     */
    @Override
    protected void initialView()
    {
        btnToControl = (Button)findViewById(R.id.btn_sc_device_control);
        //btnSave = (Button) findViewById(R.id.btn_sc_device_save);
        tvTitle = (TextView)findViewById(R.id.tv_title);
        ivSceneIcon = (ImageView)findViewById(R.id.iv_scene_icon);
        tvSceneName = (TextView)findViewById(R.id.tv_scene_name);
        rgDeviceCheck = (RadioGroup)findViewById(R.id.rg_device_select_item);
        rgDeviceList = (RadioGroup)findViewById(R.id.rg_device_item);
        tvDeviceName = (TextView)findViewById(R.id.tv_sc_device_name);
//		btnPower = (Button)findViewById(R.id.btn_sc_device_power);
        sbPower = (SwitchButton)findViewById(R.id.sb_device_prop_power);
        layoutDeviceProperty = (LinearLayout)findViewById(R.id.layout_device_property);

        tvTitle.setText(title);
        ivSceneIcon.setImageBitmap(Helper.getRoundBitmap(scene.getIcon()));
        tvSceneName.setText(scene.getName());
    }
    /**
     * 方法名称：	initialDeviceList
     * 方法描述：	初始化设备列表
     * 参数：
     * 返回值类型：	void
     * 创建时间：	2015-3-26上午10:45:24
     */
    private void initialDeviceList()
    {
        rgDeviceCheck.removeAllViews();
        rgDeviceList.removeAllViews();
        bindDevices = scene.getDeviceList();
        unbindDevices = new ArrayList<Device>();

        //fun add
//		rList = new ArrayList<Device>();
//		for (int i = 0; i < allDevices.size(); i++) {
//			if(!rList.contains(allDevices.get(i))){
//				rList.add(allDevices.get(i));
//			}else{
//				rList.remove(allDevices.get(i));
//			}
//		}
//		allDevices = rList;


        for (int i = 0; i < allDevices.size(); i++){
            unbindDevices.add(allDevices.get(i));
        }
        for (int i = 0; i < bindDevices.size(); i++){
            Device device = bindDevices.get(i);
            createView(device, true);
        }

		/*lzw add*/
        if ( bindDevices.size() != 0 ){
            initDeviceProperty(bindDevices.get(0));
        }

        //从设备列表中除去已绑定的设备，剩下就是未绑定的设备
        for (int i = 0; i < bindDevices.size(); i++)
        {
            for (int j = 0; j < unbindDevices.size(); j++)
            {
                if ( unbindDevices.get(j).getMac().equals(bindDevices.get(i).getMac()) )
                {
                    unbindDevices.remove(j);
                }
            }
        }
        for (int i = 0; i < unbindDevices.size(); i++)
        {
            Device device = unbindDevices.get(i);
            createView(device, false);
        }
    }
    /**
     * 方法名称：	createView
     * 方法描述：	显示界面
     * 参数：			@param device
     * 参数：			@param isbind
     * 返回值类型：	void
     * 创建时间：	2015-3-26上午10:55:37
     */
    private void createView(Device device, boolean isbind)
    {
        //设备列表左侧的选择框控件，布局参数
        LayoutParams selectParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        selectParams.width = (int) (25*this.getResources().getDisplayMetrics().density+0.5f);
        selectParams.height = (int) (50*this.getResources().getDisplayMetrics().density+0.5f);
        selectParams.setMargins(0, 1, 0, 0);
        CheckBox selectButton = new CheckBox(this);
        selectButton.setLayoutParams(selectParams);

//		selectButton.setPadding(30, 0, 0, 0);
        selectButton.setGravity(Gravity.CENTER);
        selectButton.setButtonDrawable(android.R.color.transparent);
        selectButton.setBackgroundResource(R.drawable.checked_device);
        selectButton.setChecked(isbind);
        selectButton.setSingleLine(true);
        //设备列表右侧的设备按钮，布局参数
        LayoutParams buttonParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
//		buttonParams.width = 200;
        buttonParams.height =(int) (50*this.getResources().getDisplayMetrics().density+0.5f); //150;
        buttonParams.setMargins(0, 1, 0, 0);
        RadioButton deviceButton = new RadioButton(this);
        deviceButton.setLayoutParams(buttonParams);

        deviceButton.setText(device.getName());
//		deviceButton.setPadding(10, 0, 0, 0);
        deviceButton.setGravity(Gravity.CENTER);
        deviceButton.setTextSize(15);
        deviceButton.setButtonDrawable(android.R.color.transparent);
        deviceButton.setBackgroundResource(R.drawable.background_button_style4);
//		deviceButton.setSingleLine(true);

        //背景设置
        if ( isbind )
        {
            deviceButton.setClickable(true);
            deviceButton.setTextColor(Color.WHITE);
        }
        else
        {
            deviceButton.setClickable(false);
            deviceButton.setTextColor(Color.GRAY);
        }
        //关联选择框和设备按钮
        selectButton.setTag(deviceButton);

        rgDeviceCheck.addView(selectButton);
        rgDeviceList.addView(deviceButton);

        selectButton.setOnCheckedChangeListener(onDeviceSelectListener);
    }

    /**
     * 方法名称：	initDeviceProperty
     * 方法描述：	显示界面
     * 参数：			@param device
     * 返回值类型：	void
     * 创建时间：	2015-6-14上午9:22:37
     * lzw add
     */
    private void initDeviceProperty(Device device)
    {
        layoutDeviceProperty.setVisibility(View.VISIBLE);

        String name = device.getName();
        tvDeviceName.setText(name);
        sbPower.setChecked(device.isPower());
    }

    OnCheckedChangeListener onDeviceSelectListener = new OnCheckedChangeListener()
    {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
        {
            CheckBox checkBox = (CheckBox) buttonView;
            RadioButton radioButton = (RadioButton) checkBox.getTag();
            //通过选择某个设备按钮，来获取设备信息
            String name = radioButton.getText().toString();

            if ( isChecked )
            {
                changeDevice = null;
                for (int i = 0; i < allDevices.size(); i++)
                {
                    if ( allDevices.get(i).getName().equals(name) )
                    {
                        changeDevice = allDevices.get(i);
                        break;
                    }
                }
                unbindDevices.remove(changeDevice);
                bindDevices.add(changeDevice);
                radioButton.setTextColor(Color.WHITE);
                radioButton.setClickable(true);
                //告诉服务器要绑定该设备
                BindDevice();
            }
            else
            {
                changeDevice = null;
                for (int i = 0; i < bindDevices.size(); i++)
                {
                    if ( bindDevices.get(i).getName().equals(name) )
                    {
                        changeDevice = bindDevices.get(i);
                        break;
                    }
                }
                bindDevices.remove(changeDevice);
                unbindDevices.add(changeDevice);
                layoutDeviceProperty.setVisibility(View.INVISIBLE);
                radioButton.setTextColor(Color.GRAY);
                radioButton.setClickable(false);
                radioButton.setChecked(false);
                //告诉服务器要解绑该设备
                unBindDevice();
            }
        }
    };
    /**
     * 方法名称：	unBindDevice
     * 方法描述：	解绑设备
     * 参数：
     * 返回值类型：	void
     * 创建时间：	2015-4-16下午8:25:02
     */
    private void unBindDevice()
    {
        NetCommunicationUtil.httpConnect(
                getUnbindInfos(),
                NetElectricsConst.METHOD_SCENE_UNBIND,
                NetElectricsConst.STYLE_RES,
                new NetCommunicationUtil.HttpCallbackListener() {
                    @Override
                    public void onFinish(Bundle bundle) {
                        Message message = handler.obtainMessage();
                        if (bundle.getInt("resCode") == 1) {
                            message.what = UNBIND_SUCCESS;
                        } else {
                            message.what = UNBIND_FAIL;
                        }
                        handler.sendMessage(message);
                    }

                    @Override
                    public void onError(Bundle bundle) {
                        Message message = handler.obtainMessage();
                        message.what = UNBIND_ERROR;
                        handler.sendMessage(message);
                    }
                });
    }
    private List<PropertyInfo> getUnbindInfos()
    {
        List<PropertyInfo> infos = new ArrayList<PropertyInfo>();

        PropertyInfo itmeid = new PropertyInfo();
        itmeid.setName(NetElectricsConst.SCENE_DEVICE_ITME);
        itmeid.setValue(changeDevice.getSceneID());
        LogUtil.logDebug("getUnbindInfos", "ItmeID" + changeDevice.getSceneID());

        PropertyInfo token = new PropertyInfo();
        token.setName(NetElectricsConst.TOKEN);
        token.setValue("");

        infos.add(itmeid);
        infos.add(token);

        return infos;
    }
    private void BindDevice()
    {
        NetCommunicationUtil.httpConnect(
                getBindInfos(),
                NetElectricsConst.METHOD_SCENE_BIND,
                NetElectricsConst.STYLE_RES,
                new NetCommunicationUtil.HttpCallbackListener()
                {
                    @Override
                    public void onFinish(Bundle bundle)
                    {
                        Message message = handler.obtainMessage();
                        if ( bundle.getInt("resCode") == 1)
                        {
                            message.what = BIND_SUCCESS;
                        }
                        else
                        {
                            message.what = BIND_FAIL;
                        }
                        handler.sendMessage(message);
                    }

                    @Override
                    public void onError(Bundle bundle)
                    {
                        Message message = handler.obtainMessage();
                        message.what = BIND_ERROR;
                        handler.sendMessage(message);
                    }
                });
    }
    private List<PropertyInfo> getBindInfos()
    {
        List<PropertyInfo> infos = new ArrayList<PropertyInfo>();

        PropertyInfo sceneId = new PropertyInfo();
        sceneId.setName(NetElectricsConst.SCENE_ID);
        sceneId.setValue(scene.getId());
        LogUtil.logDebug("getBindInfos", "sceneID"+scene.getId());

        PropertyInfo devicelist = new PropertyInfo();
        devicelist.setName(NetElectricsConst.DEVICE_LIST);
        devicelist.setValue(""+changeDevice.getId());
        LogUtil.logDebug("getBindInfos", "devicelistID"+changeDevice.getId());

        PropertyInfo token = new PropertyInfo();
        token.setName(NetElectricsConst.TOKEN);
        token.setValue("");

        infos.add(sceneId);
        infos.add(devicelist);
        infos.add(token);

        return infos;
    }
    /* (非Javadoc)
     * 方法名称：	initialHandler
     * 方法描述：	TODO
     * 重写部分：	@see com.example.bonfeel.activity.BaseActivity#initialHandler()
     */
    @Override
    protected void initialHandler()
    {
        //修改场景头像
        ivSceneIcon.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //弹出选择对话框，是相机还是相册
                showDialog();
            }
        });
        //修改场景名称
        tvSceneName.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //弹出修改界面
                Intent intent = new Intent(SceneConfigActivity.this, SetSceneNameActivity.class);
                intent.putExtra("name", tvSceneName.getText().toString().trim());
                intent.putExtra("id", scene.getId());
                startActivityForResult(intent, SET_NAME);
            }
        });

        //跳转到控制界面按钮 lzw add
        btnToControl.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String deviceName = tvDeviceName.getText().toString();
                Intent sceneToMaIN = new Intent(NetElectricsConst.SCENE_BROADCAST);
                sceneToMaIN.putExtra("deviceName", deviceName);
                sendBroadcast(sceneToMaIN);
                finish();
            }
        });
//		btnSave.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View arg0) {
//				SceneConfigActivity.this.finish();
//			}
//		});

        //设备列表，选择设备
        rgDeviceList.setOnCheckedChangeListener(new android.widget.RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                layoutDeviceProperty.setVisibility(View.VISIBLE);

                RadioButton radioButton = (RadioButton) findViewById(checkedId);
                String name = radioButton.getText().toString();
                if ( radioButton.isClickable() )
                {
                    radioButton.setChecked(true);
                    for (int i = 0; i < bindDevices.size(); i++)
                    {
                        if ( bindDevices.get(i).getName().equals(name) )
                        {
                            device = bindDevices.get(i);
                            break;
                        }
                    }
                    tvDeviceName.setText(name);
//					if ( device.isPower() )
//					{
//						btnPower.setText("开");
//					}
//					else
//					{
//						btnPower.setText("关");
//					}
                    sbPower.setChecked(device.isPower());
                }
                else
                {
                    layoutDeviceProperty.setVisibility(View.INVISIBLE);
                }
            }
        });
        sbPower.setOnChangeListener(new SwitchButton.OnChangeListener()
        {
            @Override
            public void OnChanged(boolean state)
            {
                //fun add
                if(device == null){
                    return ;
                }
                device.setPower(state);
                setDevicePro();
            }
        });
        //保存设置
//		btnFinish.setOnClickListener(new OnClickListener()
//		{
//			@Override
//			public void onClick(View v)
//			{
//			}
//		});
    }
    /**
     * 方法名称：	setDevicePro
     * 方法描述：	设置设备的属性值
     * 参数：
     * 返回值类型：	void
     * 创建时间：	2015-4-16下午9:20:25
     */
    private void setDevicePro()
    {
        NetCommunicationUtil.httpConnect(
                getDevicePro(),
                NetElectricsConst.METHOD_PROP_SET,
                NetElectricsConst.STYLE_RES,
                new NetCommunicationUtil.HttpCallbackListener()
                {
                    @Override
                    public void onFinish(Bundle bundle)
                    {
                        Message message = handler.obtainMessage();
                        if ( bundle.getInt("resCode") == 1 )
                        {
                            message.what = PRO_SET_SUCCESS;
                            Log.i(" success propname", bundle.getString("result"));
                        }
                        else
                        {
                            message.what = PRO_SET_FAIL;
                            Log.i(" fail propname", bundle.getString("result"));
                        }
                        handler.sendMessage(message);
                    }

                    @Override
                    public void onError(Bundle bundle)
                    {
                        Message message = handler.obtainMessage();
                        message.what = PRO_SET_ERROR;
                        handler.sendMessage(message);
                        Log.i("error propname", bundle.getString("message"));
                    }
                });
    }
    private List<PropertyInfo> getDevicePro()
    {
        List<PropertyInfo> infos = new ArrayList<PropertyInfo>();

        PropertyInfo itmeid = new PropertyInfo();
        itmeid.setName(NetElectricsConst.SCENE_DEVICE_ITME);
        itmeid.setValue(device.getSceneID());

        PropertyInfo proName = new PropertyInfo();
        proName.setName(NetElectricsConst.PROPNAME);
        proName.setValue("1");

        PropertyInfo proValue = new PropertyInfo();
        proValue.setName(NetElectricsConst.PROPVALUE);

        proValue.setValue(device.isPower()?"01":"00");
        //fun add
        //proValue.setValue("01");

        PropertyInfo token = new PropertyInfo();
        token.setName(NetElectricsConst.TOKEN);
        token.setValue("");

        infos.add(itmeid);
        infos.add(proName);
        infos.add(proValue);
        infos.add(token);
        //fun add
        for (int i = 0; i < infos.size(); i++) {
            Log.i("value", infos.get(i)+"");
        }
        return infos;
    }
    /**
     * 方法名称：	sceneDetail
     * 方法描述：	获取场景属性
     * 参数：
     * 返回值类型：	void
     * 创建时间：	2015-3-25下午3:11:45
     */
    private void sceneDetail()
    {
        NetCommunicationUtil.httpConnect(
                getSceneInfos(),
                NetElectricsConst.METHOD_SCENE_DETAIL,
                NetElectricsConst.STYLE_OBJECT,
                new NetCommunicationUtil.HttpCallbackListener()
                {
                    @Override
                    public void onFinish(Bundle bundle)
                    {
                        Message message = handler.obtainMessage();
                        if ( bundle.getInt("resCode") == 1)
                        {
                            NetCommunicationUtil.getHttpResultInfo(NetElectricsConst.METHOD_TAG.SceneDetail, bundle.getString("result"));
                            //fun add
                            Log.i("sceneconfigactivity", bundle.getString("result"));

                            message.what = GET_DETAIL_SUCCESS;
                        }
                        else
                        {
                            message.what = GET_DETAIL_FAIL;
                        }
                        handler.sendMessage(message);
                    }

                    @Override
                    public void onError(Bundle bundle)
                    {
                        System.out.println(bundle.getString("message"));
                        Message message = handler.obtainMessage();
                        message.what = GET_DETAIL_ERROR;
                        handler.sendMessage(message);
                    }
                });
    }
    /**
     * 方法名称：	getSceneInfos
     * 方法描述：	获取场景ID
     * 参数：			@return
     * 返回值类型：	List<PropertyInfo>
     * 创建时间：	2015-3-25下午3:12:34
     */
    private List<PropertyInfo> getSceneInfos()
    {
        List<PropertyInfo> infos = new ArrayList<PropertyInfo>();

        PropertyInfo sceneId = new PropertyInfo();
        sceneId.setName(NetElectricsConst.SCENE_ID);
        sceneId.setValue(scene.getId());

        PropertyInfo token = new PropertyInfo();
        token.setName(NetElectricsConst.TOKEN);
        token.setValue("");

        infos.add(sceneId);
        infos.add(token);

        return infos;
    }
    /**
     * 方法名称：	showDialog
     * 方法描述：	头像修改提示框
     * 参数：
     * 返回值类型：	void
     * 创建时间：	2015-3-26下午4:11:29
     */
    private void showDialog()
    {
        layoutIconView = LayoutInflater.from(SceneConfigActivity.this).inflate(R.layout.dialog_change_icon, null);
        tvGallary = (TextView)layoutIconView.findViewById(R.id.tv_from_gallary);
        tvCamera = (TextView)layoutIconView.findViewById(R.id.tv_from_camera);

        iconDialog = new CustomDialog(SceneConfigActivity.this)
                .builder()
                .setTitle("设置头像")
                .setView(layoutIconView)
                .setNegativeButton("取消", new OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {}
                });
        iconDialog.show();
        //从相册中选择图片
        tvGallary.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, FROM_CONTENT);
                iconDialog.dismiss();
            }
        });
        //从相机拍摄照片
        tvCamera.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                file = new File(Environment.getExternalStorageDirectory(),getPhotoFileName());
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                startActivityForResult(intent, TAKE_PHOTO);
                iconDialog.dismiss();
            }
        });
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
            case SET_NAME:
                if ( resultCode == RESULT_OK )
                {
                    scene.setName(data.getStringExtra("newName"));
                    tvSceneName.setText(scene.getName());
                }
                break;
            case FROM_CONTENT:
                if ( resultCode == RESULT_OK )
                {
                    zoomPhoto(data.getData(), photo_size);
                }
                break;
            case TAKE_PHOTO:
                if ( resultCode == RESULT_OK )
                {
                    zoomPhoto(Uri.fromFile(file), photo_size);
                }
                break;
            case CROP_PHOTO:
                if ( resultCode == RESULT_OK )
                {
                    saveIcon(data);
                }
                break;
            default:
                break;
        }
    }
    /**
     * 方法名称：	zoomPhoto
     * 方法描述：	裁剪图片
     * 参数：			@param uri
     * 返回值类型：	void
     * 创建时间：	2015-3-26下午5:13:36
     */
    private void zoomPhoto(Uri uri, int size)
    {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
//		 aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
//		 outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", size);
        intent.putExtra("outputY", size);
        intent.putExtra("scale", true);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, CROP_PHOTO);
    }
    /**
     * 方法名称：	saveIcon
     * 方法描述：	保存图片
     * 参数：			@param data
     * 返回值类型：	void
     * 创建时间：	2015-3-26下午5:19:44
     */
    private void saveIcon(Intent data)
    {
        Bundle extras = data.getExtras();
        if ( extras != null )
        {
            photo = extras.getParcelable("data");
            //显示新图片
//			ivSceneIcon.setImageBitmap(Helper.getRoundBitmap(photo));
			/*
			 * 上传图片资源
			 */
            uploadIcon();
        }
    }
    /**
     * 方法名称：	uploadIcon
     * 方法描述：	上传头像至服务器
     * 参数：
     * 返回值类型：	void
     * 创建时间：	2015-4-16上午10:55:58
     */
    private void uploadIcon()
    {
        NetCommunicationUtil.httpConnect(
                getNewSceneInfos(TAG_IMAGE),
                NetElectricsConst.METHOD_SCENE_ICON,
                NetElectricsConst.STYLE_RES,
                new NetCommunicationUtil.HttpCallbackListener()
                {
                    @Override
                    public void onFinish(Bundle bundle)
                    {
                        Message message = handler.obtainMessage();
                        if ( bundle.getInt("resCode") == 1 )
                        {
                            message.what = UPDATE_ICON_SUCCESS;
                        }
                        else
                        {
                            message.what = UPDATE_ICON_FAIL;
                        }
                        handler.sendMessage(message);
                    }

                    @Override
                    public void onError(Bundle bundle)
                    {
                        System.out.println(bundle.getString("message"));
                        Message message = handler.obtainMessage();
                        message.what = UPDATE_ICON_ERROR;
                        handler.sendMessage(message);
                    }
                });
    }
    private List<PropertyInfo> getNewSceneInfos(int tag)
    {
        List<PropertyInfo> infos = new ArrayList<PropertyInfo>();

        PropertyInfo sceneId = new PropertyInfo();
        sceneId.setName(NetElectricsConst.SCENE_ID);
        sceneId.setValue(scene.getId());

        PropertyInfo value = new PropertyInfo();
        switch (tag)
        {
            case TAG_IMAGE:
                value.setName(NetElectricsConst.SCENE_IMAGE);
                value.setValue(Helper.bitmap2String(photo));
                break;
            case TAG_LIST:
//			value.setName(NetElectricsConst.SCENE_IMAGE);
//			value.setValue(Helper.bitmap2String(photo));
                break;
            default:
                break;
        }

        PropertyInfo token = new PropertyInfo();
        token.setName(NetElectricsConst.TOKEN);
        token.setValue("");

        infos.add(sceneId);
        infos.add(value);
        infos.add(token);

        return infos;
    }
    /**
     * 方法名称：	getPhotoFileName
     * 方法描述：	头像命名
     * 参数：			@return
     * 返回值类型：	String
     * 创建时间：	2015-3-27上午9:11:46
     */
    private String getPhotoFileName()
    {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("'IMG'_yyyyMMdd_HHmmss");
        //照片的后缀格式会影响裁剪的时候是否旋转
        return dateFormat.format(date)+".jpg";
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
                case GET_DETAIL_SUCCESS:
                    scene.setDeviceList(NetCommunicationUtil.getSceneDevices());
                    initialDeviceList();
                    break;
                case GET_DETAIL_FAIL:
                    break;
                case GET_DETAIL_ERROR:
                    //showInfo("无法连接服务器，请检查网络，获取场景下的设备列表失败！");
                    ToastUtil.showToast(SceneConfigActivity.this, "无法连接服务器，请检查网络，获取场景下的设备列表失败！");
                    break;
                case UPDATE_ICON_SUCCESS:
                    scene.setIcon(photo);
                    ivSceneIcon.setImageBitmap(Helper.getRoundBitmap(scene.getIcon()));
                    break;
                case UPDATE_ICON_FAIL:
                    break;
                case UPDATE_ICON_ERROR:
                    //showInfo("无法连接服务器，请检查网络，更新图片失败！");
                    ToastUtil.showToast(SceneConfigActivity.this, "无法连接服务器，请检查网络，更新图片失败！");
                    break;
                case BIND_SUCCESS:
                    sceneDetail();
                    break;
                case BIND_FAIL:
                    break;
                case BIND_ERROR:
                    //showInfo("无法连接服务器，请检查网络，绑定该场景下的设备失败！");
                    ToastUtil.showToast(SceneConfigActivity.this, "无法连接服务器，请检查网络，绑定该场景下的设备失败！");
                    break;
                case UNBIND_SUCCESS:
                    //sceneDetail();
                    break;
                case UNBIND_FAIL:
                    break;
                case UNBIND_ERROR:
                    ToastUtil.showToast(SceneConfigActivity.this,"无法连接服务器，请检查网络，解绑该场景下的设备失败！");
                    //showInfo("无法连接服务器，请检查网络，解绑该场景下的设备失败！");
                    break;
                case PRO_SET_SUCCESS:
                    break;
                case PRO_SET_FAIL:
                    break;
                case PRO_SET_ERROR:
                    //showInfo("无法连接服务器，请检查网络，设置设备的状态失败！");
                    ToastUtil.showToast(SceneConfigActivity.this,"无法连接服务器，请检查网络，设置设备的状态失败！");
                    break;
                default:
                    break;
            }
        }
    };
}
