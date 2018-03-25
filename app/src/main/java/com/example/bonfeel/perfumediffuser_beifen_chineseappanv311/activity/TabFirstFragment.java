package com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.activity;

import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.R;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.custom.CustomDialog;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.custom.DevicePan;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.model.BonfeelFrame;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.model.Device;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.model.NetElectricsConst;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.model.User;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.service.AlarmService;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.util.ApplicationUtil;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.util.DeviceManager;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.util.DownLoadUtil;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.util.LogUtil;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.util.NetCommunicationUtil;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.util.ReceiverTask;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.util.ToastUtil;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.util.UserManager;

import org.ksoap2.serialization.PropertyInfo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * Author:FunFun
 * Function:设备界面
 * Date:2016/4/12 13:43
 */
public class TabFirstFragment extends Fragment{
    private String tag = "设备界面";
    private View tabLayout;
    //	private VelocityTracker mVelocityTracker;
    //private float downX;
    //private int menuPadding = 200;
    private LayoutParams contentParams;
    private int screenWidth;

    private ImageView ivAirSpeed;
    private ImageView ivItems;
    private TextView tvDeviceName;

    private LinearLayout layoutCustom;
    private DevicePan devicePan;
    private LinearLayout layoutVolume;
    private TextView tvVolume;
    private LinearLayout layoutWorkState;
    private TextView tvStartTime;
    private TextView tvStopTime;
    private TextView tvWorkTime;
    private TextView tvRestTime;
    private TextView tvTemprature;
    private TextView tvWeat;
    private LinearLayout layoutError;
    private TextView tvErrorCode;
    private TextView tvErrorContent;
    private TextView tvContact;
    private Button btnGetIt;
    private Button btnReLater;
    private TextView tvInfos;

    private int errorCode;
    private String errorContent;
    private String Contact;

    private Device device;
    private boolean hasDevice;

    private IntentFilter socketCast;
    private IntentFilter alarmCast;
    private IntentFilter beatCast;
    private Intent alarmService;
    private Intent heartBeatService;

    private AnimationDrawable refresh;

    private final int QUERY_SUCCESS = 11;
    private final int CONTROL_SUCCESS = 12;
    private final int CAST_ERROR = 13;
    private final int DEVICE_LEAVE = 14;

    private final int GET_VERSION_SUCCESS = 1;
    private final int GET_VERSION_FAIL = 2;
    private final int GET_VERSION_ERROR = 3;
    private final int UPDATE_APP = 4;
    private final int GET_APK_SIZE = 6;
    private final int GET_DOWNLOAD_PROGRESS = 5;

    private Dialog checkDialog;
    private Bundle resBundle;
    private String version;

    private CustomDialog confirmDialog;
    private View layoutContent;
    private TextView tvContent;
    private TextView tvUpdate;

    private NotificationManager notiManager;
    private Notification noti;
    private int fileSize;

    private int reCheckTime;

    /*
     * 方法名称：	onCreate
     * 方法描述：	初始化数据
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtil.logDebug(tag, "onCreate");
        // 初始化数据
        initialData();
    }

    /**
     * 方法名称：	initialData
     * 方法描述：	初始化数据
     * 参数：
     * 返回值类型：	void
     * 创建时间：	2015-1-16下午4:43:32
     */
    private void initialData() {
        hasDevice = false;
        reCheckTime = 0;
        // 获取屏幕的尺寸
        screenWidth = ApplicationUtil.getScreen_width();
        // 后台定时服务
        alarmService = new Intent();
        // 心跳服务
        heartBeatService = new Intent();
        // 注册广播接收器
        signSocketCast();
        // 注册定时广播接收器
        signAlarmCast();
        // 注册心跳广播接收器
        signHeartBeatCast();
        // initialise Notification Manager
        notiManager = (NotificationManager) getActivity().getSystemService(
                Context.NOTIFICATION_SERVICE);
        // 加载对话框
        checkDialog = ApplicationUtil.getLoadingDialog(getActivity(), "检查版本更新，请稍后……");
        // 初始化加载框
        checkDialog.show();
        // 获取到当前的设备
        getCurrentDevice();
        // 心跳任务启动
        heartBeatTask();
    }

    /**
     * 方法名称：	getCurrentDevice
     * 方法描述：	获取当前选择的设备
     * 参数：
     * 返回值类型：	void
     * 创建时间：	2015-4-30上午9:11:34
     */
    private void getCurrentDevice() {
        device = null;
        if(DeviceManager.getInstance().getCount() == 0){
            //Toast.makeText(getActivity(), "当前设备列表中的设备为空", Toast.LENGTH_LONG).show();
            return;
        }
        for (int i = 0; i < DeviceManager.getInstance().getCount(); i++) {
            // 如果设备列表中有数据，那就有一个是最近一次使用的
            if (((Device) DeviceManager.getInstance().get(i)).isSelect()) {
                hasDevice = true;
                device = (Device) DeviceManager.getInstance().get(i);
                LogUtil.logDebug(tag, "主界面中的设备信息:" + "设备MAC:" + device.getMac()
                        + "设备状态:" + device.isLeave());
                break;
            }
        }
    }

    /**
     * 方法名称：	signSocketCast
     * 方法描述：	注册socket通信广播
     * 参数：
     * 返回值类型：	void
     * 创建时间：	2015-4-23下午5:07:05
     */
    private void signSocketCast() {
        socketCast = new IntentFilter();
        socketCast.addAction(NetElectricsConst.ACTION_DATA_RECEIVE);
        socketCast.addAction(NetElectricsConst.ACTION_SET_SUCCEED);
        socketCast.addAction(NetElectricsConst.ACTION_SET_TIMESCALE_SUCCEED);
        socketCast.addAction(NetElectricsConst.ACTION_SET_CURTIMESCALE);
        socketCast.addAction(NetElectricsConst.ACTION_DATA_ERROR);
        ApplicationUtil.getContext().registerReceiver(broadcastReceiver,
                socketCast);
    }

    /**
     * 方法名称：	signAlarmCast
     * 方法描述：	注册alarm定时广播
     * 参数：
     * 返回值类型：	void
     * 创建时间：	2015-4-23下午5:08:24
     */
    private void signAlarmCast() {
        alarmCast = new IntentFilter();
        alarmCast.addAction(NetElectricsConst.ACTION_ALARM_CAST);
        ApplicationUtil.getContext().registerReceiver(alarmReceiver, alarmCast);
    }

    private void signHeartBeatCast() {
        beatCast = new IntentFilter();
        beatCast.addAction(NetElectricsConst.ACTION_HEART_BEAT);
        ApplicationUtil.getContext().registerReceiver(heartBeatReceiver,
                beatCast);
    }

    /*
     * 方法名称：	onCreateView
     * 方法描述：	加载界面布局
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        LogUtil.logDebug(tag, "onCreateView");
        if (UserManager.getInstance().get(0) == null) {
            return null;
        }
        // 加载布局
        tabLayout = inflater.inflate(R.layout.fragment_tab1, container, false);
        //tabLayout.setOnTouchListener(this);
        // 初始化控件
        initialView(tabLayout);
        initialHandler();

        return tabLayout;
    }

    /**
     * 方法名称：	initialView
     * 方法描述：
     * 参数：			@param v
     * 返回值类型：	void
     * 创建时间：	2015-1-20下午1:16:14
     */
    private void initialView(View v) {
        contentParams = (LayoutParams) v.getLayoutParams();
        contentParams.width = screenWidth;

        ivAirSpeed = (ImageView) v.findViewById(R.id.iv_device_airspeed);
        ivItems = (ImageView) v.findViewById(R.id.iv_device_items);
        tvDeviceName = (TextView) v.findViewById(R.id.tv_device_name);
        layoutCustom = (LinearLayout) v.findViewById(R.id.layout_custom);
        devicePan = (DevicePan) v.findViewById(R.id.device_pan);
        devicePan.setContext(getActivity());
        layoutVolume = (LinearLayout) v.findViewById(R.id.layout_volume);
        tvVolume = (TextView) v.findViewById(R.id.tv_volume);
        layoutWorkState = (LinearLayout) v.findViewById(R.id.layout_work_state);
        tvStartTime = (TextView) v.findViewById(R.id.tv_device_start_time);
        tvStopTime = (TextView) v.findViewById(R.id.tv_device_stop_time);
        tvWorkTime = (TextView) v.findViewById(R.id.tv_device_work_time);
        tvRestTime = (TextView) v.findViewById(R.id.tv_device_rest_time);
        tvTemprature = (TextView) v.findViewById(R.id.tv_device_temprature);
        tvWeat = (TextView) v.findViewById(R.id.tv_device_weat);
        layoutError = (LinearLayout) v.findViewById(R.id.layout_error);
        tvErrorCode = (TextView) v.findViewById(R.id.tv_error_code);
        tvErrorContent = (TextView) v.findViewById(R.id.tv_error_content);
        tvContact = (TextView) v.findViewById(R.id.tv_solution);
        btnGetIt = (Button) v.findViewById(R.id.btn_get_it);
        btnReLater = (Button) v.findViewById(R.id.btn_remind_later);
        tvInfos = (TextView) v.findViewById(R.id.tv_infos);
    }

    /**
     * 方法名称：	initialHandler
     * 方法描述：
     * 参数：
     * 返回值类型：	void
     * 创建时间：	2015-1-21上午8:42:48
     */
    private void initialHandler() {
		/*
		 * V0.4.8更改为刷新功能
		 */
        ivAirSpeed.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                checkState();
            }
        });
        // 设备列表item图标
        ivItems.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity mainActivity = (MainActivity) TabFirstFragment.this
                        .getActivity();
                mainActivity.getSlideMenuFragment().updateDeviceList();
                mainActivity.showSlideMenu();
                mainActivity.showMenu();
            }
        });
        // 开关键
        devicePan.setOnCallbackListener(new DevicePan.OnPowerSetListener() {
            @Override
            public void OnSetPower(boolean power) {
                // 开启单个设备控制开关
                NetCommunicationUtil.setSingle(true);
                device.setPower(power);
                if (power) {
                    // 开机
                    NetCommunicationUtil.controlDevice(BonfeelFrame.DeviceOn,
                            device.getMac(), NetElectricsConst.ControlDevice);
                } else {
                    // 关机
                    NetCommunicationUtil.controlDevice(BonfeelFrame.DeviceOff,
                            device.getMac(), NetElectricsConst.ControlDevice);
                }
            }
        });
        // 设备名称修改
        tvDeviceName.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent setName = new Intent(ApplicationUtil.getContext(),
                        SetDeviceNameActivity.class);
                startActivity(setName);
            }
        });
        // 设备工作状态修改
        layoutWorkState.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (device.getTimeScale(device.getCurIndex()) != null) {
                    Intent setState = new Intent(ApplicationUtil.getContext(),
                            SetSectorActivity.class);
                    // 将设备当前工作的时间段ID告诉设置时间段的界面
                    setState.putExtra("scaleId", device.getCurIndex());
                    startActivity(setState);
                    // 告诉时间段参数界面，现在设置的时间段是设备界面的
                    NetCommunicationUtil.setStep(1);
                } else {
                    System.out.println("invalidate timescale");
                    //CustomToast("无效的时间段，请重新设置");
                    ToastUtil.showToast(getActivity(), "无效的时间段，请重新设置");
                }
            }
        });
        // 滑动监听
//		devicePan.setOnSliderListener(new OnSliderListener() {
//			@Override
//			public void OnGetSliderVelocity(float distance) {
//				MainActivity mainActivity = (MainActivity) TabFirstFragment.this
//						.getActivity();
//				if (distance > 0) {
//					// 展开slidemenu界面
//					contentParams.leftMargin += distance;
//					if (contentParams.leftMargin > (screenWidth - menuPadding)) {
//						contentParams.leftMargin = screenWidth - menuPadding;
//					}
//					mainActivity.showSlideMenu();
//				} else {
//					contentParams.leftMargin += distance;
//					if (contentParams.leftMargin < 0) {
//						contentParams.leftMargin = 0;
//					}
//				}
//				tabLayout.setLayoutParams(contentParams);
//			}
//		});
//		// 滑动倾向监听
//		devicePan.setOnSliderTrackListener(new OnSliderTrackListener() {
//			@Override
//			public void OnGetSliderTracker(float distance, MotionEvent event) {
//				createVelocityTracker(event);
//				if (distance > 0) {
//					new ScrollTask().execute(30);
//				}
//				if (distance < 0) {
//					new ScrollTask().execute(-30);
//				}
//				recycleVelocity();
//			}
//		});
        // 故障界面按钮（停止上报）
        btnGetIt.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                toastDevice();
                // 告诉设备已知道故障
                tellDeviceNotSendAgain();
                stopRefreshAnim();
            }
        });
        // 故障界面按钮（稍后提醒）
        btnReLater.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                toastDevice();
                stopRefreshAnim();
            }
        });
    }

    /*
     * 方法名称：	onActivityCreated
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        LogUtil.logDebug(tag, "onActivityCreated");
        super.onActivityCreated(savedInstanceState);
        if (UserManager.getInstance().get(0) == null) {
            return;
        }
        if (device != null) {
            checkState();
        }
        showUIState();
        checkForUpdate();
    }

    /**
     * 方法名称：	checkForUpdate
     * 方法描述：	检查新版本
     * 参数：
     * 返回值类型：	void
     * 创建时间：	2015-4-14上午8:57:32
     */
    private void checkForUpdate() {
        LogUtil.logDebug(tag, "check_for_update");
        // 获取软件版本号
        version = getAppVersion();
        // 联网检查版本
        NetCommunicationUtil.httpConnect(getAppInfos(),
                NetElectricsConst.METHOD_APP_VERSION,
                NetElectricsConst.STYLE_OBJECT, new NetCommunicationUtil.HttpCallbackListener() {
                    @Override
                    public void onFinish(Bundle bundle) {
                        System.out.println("onFinish has exected");
                        Message message = checkHandler.obtainMessage();
                        if (bundle.getInt("resCode") == 1) {
                            resBundle = NetCommunicationUtil.getHttpResultInfo(
                                    bundle.getString("result"),
                                    NetElectricsConst.METHOD_TAG.GetAppVersion);
                            message.what = GET_VERSION_SUCCESS;
                        } else {
                            message.what = GET_VERSION_FAIL;
                        }
                        checkHandler.sendMessage(message);
                    }

                    @Override
                    public void onError(Bundle bundle) {
                        Message message = checkHandler.obtainMessage();
                        message.what = GET_VERSION_ERROR;
                        checkHandler.sendMessage(message);
                    }
                });
    }

    /**
     * 方法名称：	getAppInfos
     * 方法描述：	TODO
     * 参数：			@return
     * 返回值类型：	List<PropertyInfo>
     * 创建时间：	2015-4-14上午8:58:25
     */
    private List<PropertyInfo> getAppInfos() {
        List<PropertyInfo> infos = new ArrayList<PropertyInfo>();

        PropertyInfo token = new PropertyInfo();
        token.setName(NetElectricsConst.TOKEN);
        token.setValue("");

        infos.add(token);

        return infos;
    }

    /**
     * 方法名称：	getAppVersion
     * 方法描述：	获取软件版本号
     * 参数：			@return
     * 返回值类型：	String
     * 创建时间：	2015-4-14上午9:17:01
     */
    private String getAppVersion() {
        PackageManager packageManager = getActivity().getPackageManager();
        PackageInfo packageInfo = null;
        try {
            packageInfo = packageManager.getPackageInfo(getActivity()
                    .getPackageName(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        LogUtil.logDebug(tag, "the App version is " + packageInfo.versionName);
        return packageInfo.versionName;
    }

    /**
     * 方法名称：	getNewVersion
     * 方法描述：	比对版本号
     * 参数：
     * 返回值类型：	void
     * 创建时间：	2015-4-14上午9:12:26
     */
    private void getNewVersion() {
        System.out.println("getNewVersion has exected");
        //checkDialog.dismiss();
        Message message = checkHandler.obtainMessage();
        if (resBundle.getString("version").equals(version)) {
        } else {
            if (!resBundle.getString("version").equals("")) {
                message.what = UPDATE_APP;
            }
        }
        checkHandler.sendMessage(message);
    }

    /**
     * 方法名称：	showDownLoadDialog
     * 方法描述：	显示软件更新提示框
     * 参数：
     * 返回值类型：	void
     * 创建时间：	2015-4-14上午9:35:35
     */
    private void showDownLoadDialog() {
        layoutContent = LayoutInflater.from(getActivity()).inflate(
                R.layout.dialog_hint_control, null);
        tvContent = (TextView) layoutContent.findViewById(R.id.tv_hint_control);
        tvContent.setText("有新安装包，现在需要更新吗？");
        tvUpdate = (TextView) layoutContent
                .findViewById(R.id.tv_update_content);
        tvUpdate.setText("主要更新：" + resBundle.getString("content"));

        confirmDialog = new CustomDialog(getActivity()).builder()
                .setTitle("软件更新").setView(layoutContent)
                .setNegativeButton("下次再说", new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                }).setPositiveButton("现在更新", new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        initialNotificate();
                        downLoadApk();
                    }
                });
        confirmDialog.show();
    }

    /**
     * 方法名称：	initialNotificate
     * 方法描述：	初始化notification
     * 参数：
     * 返回值类型：	void
     * 创建时间：	2015-4-14上午9:52:54
     */
    private void initialNotificate() {
        noti = new Notification();
        noti.icon = R.drawable.bonfeel_icon;
        noti.tickerText = "正在下载 Bonfeel_V" + resBundle.getString("version");
        noti.when = System.currentTimeMillis();
        noti.contentView = new RemoteViews(getActivity().getPackageName(),
                R.layout.layout_notification);
    }

    /**
     * 方法名称：	downLoadApk
     * 方法描述：	下载apk
     * 参数：
     * 返回值类型：	void
     * 创建时间：	2015-4-14上午9:53:45
     */
    private void downLoadApk() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    File file = DownLoadUtil.getAPKFileFromServer(
                            resBundle.getString("url"), checkHandler);
                    Thread.sleep(2000);
                    notiManager.cancel(1);
                    installAPK(file);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 方法名称：	installAPK
     * 方法描述：	安装APK
     * 参数：			@param file
     * 返回值类型：	void
     * 创建时间：	2015-4-14上午10:06:23
     */
    private void installAPK(File file) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.fromFile(file),
                "application/vnd.android.package-archive");
        startActivity(intent);
    }

    /**
     * 方法名称：	showUIState
     * 方法描述：	更新界面
     * 参数：
     * 返回值类型：	void
     * 创建时间：	2015-1-21下午1:39:45
     */
    private void showUIState() {
        if (hasDevice) {
            toastDevice();
            // 显示当前选择设备的名称
            tvDeviceName.setText(device.getName());
            // 更新设备开关机
            devicePan.setPower(device.isPower());
            // 更新设备香水剩余量
            devicePan.setVolume(device.getVolume());
            LogUtil.logInfo(tag, "volume:" + device.getVolume());
            // 20150917
            if (device.getVolume() > 100 || device.getVolume() == 100) {
                tvVolume.setText("剩余100%");
            } else {
                tvVolume.setText("剩余" + (int) device.getVolume() + "%");
            }

            // 更新温度，湿度
            tvTemprature.setText("周围温度:" + device.getTemp() + "℃");
            tvWeat.setText("周围湿度:" + device.getWeat() + "%");
            // 更新当前设备工作的时间段
            upDateTimeScale();
        } else {
            hideDevice();
        }
    }

    /**
     * 方法名称：	upDateTimeScale
     * 方法描述：	更新当前时间，
     * 参数：
     * 返回值类型：	void
     * 创建时间：	2015-2-11上午10:15:15
     */
    private void upDateTimeScale() {
        if (device.getTimeScale(device.getCurIndex()) != null) {
            tvStartTime.setText("开始时间:"
                    + device.getTimeScale(device.getCurIndex()).getStartTime());
            tvStopTime.setText("结束时间:"
                    + device.getTimeScale(device.getCurIndex()).getEndTime());
            tvWorkTime.setText("运行时间:"
                    + device.getTimeScale(device.getCurIndex()).getWorkTime()
                    + "min");
            tvRestTime.setText("暂停时间:"
                    + device.getTimeScale(device.getCurIndex()).getRestTime()
                    + "min");
        }
    }

    /**
     * 方法名称：	setDevice
     * 方法描述：	当前选择的设备
     * 参数：			@param d
     * 返回值类型：	void
     * 创建时间：	2015-1-21下午1:56:17
     */
    public void setDevice(Device d) {
        device = d;
        if (d != null) {
            device.setSelect(true);
            for (int i = 0; i < DeviceManager.getInstance().getCount(); i++) {
                Device de = (Device) DeviceManager.getInstance().get(i);
                if (de.getId() != device.getId()) {
                    de.setSelect(false);
                }
            }
            hasDevice = true;
            checkState();
        } else {
            hasDevice = false;
        }
        //new ScrollTask().execute(-30);
        showUIState();
    }

    /**
     * 方法名称：	checkState
     * 方法描述：	全查设备状态
     * 参数：
     * 返回值类型：	void
     * 创建时间：	2015-4-15下午7:25:05
     */
    private void checkState() {
        if (reCheckTime < 3) {
            startRefreshAnim();
            if (device.getError() != 0) {
                System.out
                        .println("device has has problem, query device error");
                // 标记需要重查故障
                NetCommunicationUtil.setStepForQuery(1);
                // 查询故障是否还存在
                if (NetCommunicationUtil.isCompetable()) {
                    NetCommunicationUtil.queryDevice(BonfeelFrame.queryErrorV2,
                            device.getMac(), NetElectricsConst.QueryState);
                } else {
                    NetCommunicationUtil.queryDevice(BonfeelFrame.queryErrorV1,
                            device.getMac(), NetElectricsConst.QueryState);
                }
				/*
				 * 2s后检查状态是否已经查询成功
				 */
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (NetCommunicationUtil.getStepForQuery() == 1
                                && NetCommunicationUtil.getNetConnectState()) {
                            if (device != null) {
                                checkState();
                            }
                        }
                    }
                }, 2000);// 之前是6s，而不是2s
            } else {
                System.out.println("device has no error, full query state");
                // 标记需要重查状态
                NetCommunicationUtil.setStepForQuery(2);
                if (NetCommunicationUtil.isCompetable()) {
                    NetCommunicationUtil.queryDevice(BonfeelFrame.queryStateV2,
                            device.getMac(), NetElectricsConst.QueryState);
                } else {
                    NetCommunicationUtil.queryDevice(BonfeelFrame.queryStateV1,
                            device.getMac(), NetElectricsConst.QueryState);
                }
				/*
				 * 8s后检查状态是否已经查询成功
				 */
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (NetCommunicationUtil.getStepForQuery() == 2
                                && NetCommunicationUtil.getNetConnectState()) {
                            System.out.println("requery device's state");
                            if (device != null) {
                                checkState();
                            }
                        }
                    }
                }, 8000);
            }
            reCheckTime++;
        } else {
            reCheckTime = 0;
            stopRefreshAnim();
        }
    }

    private void startRefreshAnim() {
        // 播放更新状态的动画
        ivAirSpeed.setImageResource(R.drawable.animation_refresh);
        refresh = (AnimationDrawable) ivAirSpeed.getDrawable();
        refresh.stop();
        refresh.start();
    }

    private void stopRefreshAnim() {
        // 停止更新状态的动画
        if (refresh != null) {
            refresh.stop();
            ivAirSpeed.setImageResource(R.drawable.refresh);
        }
    }

    /**
     * @return the contentParams
     */
    public LayoutParams getContentParams() {
        return contentParams;
    }

    // 用于检查版本更新的handler
    private Handler checkHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case GET_VERSION_SUCCESS:
                    checkDialog.dismiss();
                    getNewVersion();
                    break;
                case GET_VERSION_FAIL:
                    checkForUpdate();
                    break;
                case GET_VERSION_ERROR:
                    checkDialog.dismiss();
                    break;
                case UPDATE_APP:
                    showDownLoadDialog();
                    break;
                case GET_DOWNLOAD_PROGRESS:
                    int persent = (msg.arg1 * 100) / fileSize;
                    noti.contentView.setTextViewText(R.id.tv_noti_title,
                            "Bonfeel_V" + resBundle.getString("version")
                                    + "        " + persent + "%");
                    noti.contentView.setProgressBar(R.id.pb_noti_progress,
                            fileSize, msg.arg1, false);
                    notiManager.notify(1, noti);
                    break;
                case GET_APK_SIZE:
                    fileSize = msg.arg1;
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };
    // 用于socket通信的handler
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // 保证有设备才更新
            if (device != null) {
                switch (msg.what) {
                    case QUERY_SUCCESS:
                        querySuccess(msg.arg1);
                        break;
                    case CONTROL_SUCCESS:
                        controlSuccess(msg.arg1, msg.obj);
                        break;
                    case CAST_ERROR:
                        gotDeviceError();
                        break;
                    case DEVICE_LEAVE:
                        deviceOffline();
                        break;
                    default:
                        break;
                }
                tellDevicePan();
            }
            super.handleMessage(msg);
        }
    };

    /**
     * 方法名称：	querySuccess
     * 方法描述：	查询成功的操作
     * 参数：
     * 返回值类型：	void
     * 创建时间：	2015-4-18下午3:04:23
     */
    private void querySuccess(int tag) {
        // 能查询成功，就表示设备在线的
        device.setLeave(false);
		/*
		 * 然后首先判断设备是否有致命故障,再更新设备信息
		 */
        if (device.getError() != 0) {
            // 提示致命故障
            toastError();
            // 发送致命故障就停止定时查询
            stopAlarmTask();
        } else {
			/*
			 * 更新设备信息或者提示温度传感器故障
			 */
            // 更新界面
            showUIState();
            // 判断是否全查成功
            switch (tag) {
                case 1:
                    // 全查成功，停止更新动画，启动定时服务
                    reCheckTime = 0;
                    stopRefreshAnim();
                    alarmTask();
                    ToastUtil.showToast(getActivity(), "设备状态已更新");
                    //CustomToast("设备状态已更新");

				/*
				 * 温度传感器故障，因为不影响设备使用，因此只做提示
				 */
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (device.getTempError() != 0) {
                                //CustomToast("温度传感器故障");
                                ToastUtil.showToast(getActivity(), "温度传感器故障");
                            }
                            if (device.getTimeError() != 0) {
                                //CustomToast("设备时钟故障");
                                ToastUtil.showToast(getActivity(), "设备时钟故障");
                            }
                        }
                    }, 2000);
                    break;
                case 2:
                    // 查询故障成功，且没有故障，通信正常；若查询故障失败，则会重查；
                    // 若查询故障成功，但有故障，则会提示故障
                    if (device != null) {
                        checkState();
                    }
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 方法名称：	controlSuccess
     * 方法描述：	控制成功后的操作（控制包括开关机及设置）
     * 参数：			标记是参数不识别还是正常帧
     * 返回值类型：	void
     * 创建时间：	2015-4-18下午3:42:01
     */
    private void controlSuccess(int tag, Object obj) {
        // 收到控制返回，表示设备是在线的
        device.setLeave(false);
        if (device.getError() != 0 || device.getTempError() != 0
                || device.getTimeError() != 0) {
            return;
            // toastError();
            // 发送致命故障就停止定时查询
            // stopAlarmTask();
        } else {
            if (NetCommunicationUtil.isSingle()) {
                if (tag == 1) {
                    //CustomToast("控制成功");
                    ToastUtil.showToast(getActivity(),"控制成功");
                }else{
                    //CustomToast("控制失败！");
                    ToastUtil.showToast(getActivity(),"控制失败");
                }
                Log.i("tabfirst", "single");
            } else {
                // NetCommunicationUtil.setControlSucSize();
                if (obj != null) {
                    NetCommunicationUtil.addControlSucDevices(obj.toString());
                    Log.i("tabfirst", "onecontrolobj");
                }
            }
        }
    }

    /**
     * 方法名称：	gotDeviceError
     * 方法描述：	收到设备的主动上报
     * 参数：
     * 返回值类型：	void
     * 创建时间：	2015-4-18下午3:45:11
     */
    private void gotDeviceError() {
        NetCommunicationUtil.setSingle(true);
        // 能主动上报，则表示设备在线
        device.setLeave(false);
        // 停止刷新动画
        stopRefreshAnim();
		/*
		 * 主动上报的原因有，设备发生致命故障，温度传感器出现故障两种情况
		 */
        if (device.getError() != 0) {
            toastError();
            // 发送致命故障就停止定时查询
            stopAlarmTask();
        } else {
            // 温度传感器故障上报
            if (device.getTempError() != 0) {
                CustomToast("温度传感器故障");
                //ToastUtil.showToast(getActivity(), "温度传感器故障");
            }
            if (device.getTimeError() != 0) {
                CustomToast("设备时钟故障");
                //ToastUtil.showToast(getActivity(), "设备时钟故障");
            }
            tellDeviceNotSendAgain();
        }
    }

    /**
     * 方法名称：	deviceOffline
     * 方法描述：	设备离线，是查询成功后的结果
     * 参数：
     * 返回值类型：	void
     * 创建时间：	2015-4-18下午3:57:45
     */
    private void deviceOffline() {
        // 由服务器告诉手机设备离线
        device.setLeave(true);
        stopRefreshAnim();
        // 若用户在查询设备状态，则由此告诉用户不用再重查了
        NetCommunicationUtil.setStepForQuery(3);
        // 停止定时查询
        stopAlarmTask();
        //CustomToast("设备离线");
        ToastUtil.showToast(getActivity(),"设备离线");
    }

    /**
     * 方法名称：	tellDevicePan
     * 方法描述：	告诉设备圆盘控件，当前设备在离线和故障状态
     * 参数：
     * 返回值类型：	void
     * 创建时间：	2015-4-18下午4:02:18
     */
    private void tellDevicePan() {
        // 设备在离线
        devicePan.setLeave(device.isLeave());
        // 设备是否故障
        if (device.getError() != 0) {
            devicePan.setError(true, device.getError());
        } else {
            devicePan.setError(false, 0);
        }
    }

    /**
     * 方法名称：	CustomToast
     * 方法描述：	自定义Toast
     * 参数：			@param info
     * 返回值类型：	void
     * 创建时间：	2015-3-9下午2:59:08
     */
    private void CustomToast(String info) {
        Toast toast = Toast.makeText(getActivity(), info, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    /**
     * 方法名称：	toastDevice
     * 方法描述：	显示设备信息界面
     * 参数：
     * 返回值类型：	void
     * 创建时间：	2015-3-20下午2:05:33
     */
    private void toastDevice() {
        tvDeviceName.setVisibility(View.VISIBLE);
        ivAirSpeed.setVisibility(View.VISIBLE);
        layoutCustom.setVisibility(View.VISIBLE);
        layoutVolume.setVisibility(View.VISIBLE);
        layoutWorkState.setVisibility(View.VISIBLE);

        layoutError.setVisibility(View.GONE);
        tvInfos.setVisibility(View.GONE);
    }

    /**
     * 方法名称：	tellDeviceNotSendAgain
     * 方法描述：	告诉设备不用再上报故障了
     * 参数：
     * 返回值类型：	void
     * 创建时间：	2015-4-18下午4:06:22
     */
    private void tellDeviceNotSendAgain() {
        NetCommunicationUtil.controlDevice(feedback(), device.getMac(),
                NetElectricsConst.ControlDevice);
    }

    /**
     * 方法名称：	feedback
     * 方法描述：	主动上报的反馈帧
     * 参数：			@return
     * 返回值类型：	byte[]
     * 创建时间：	2015-4-8上午9:27:58
     */
    private byte[] feedback() {
        byte[] content = new byte[3];
        // 温度传感器故障
        if (device.getTempError() != 0) {
            content[0] = 0x2F;
            content[1] = 0x01;
        }
        // 时钟故障
        if (device.getTimeError() != 0) {
            content[0] = 0x31;
            content[1] = 0x01;
        }
        // 致命故障
        if (device.getError() != 0) {
            content[0] = 0x24;
            switch (device.getError()) {
                case 1:
                    content[1] = 0x01;
                    break;
                case 2:
                    content[1] = 0x02;
                    break;
                case 4:
                    content[1] = 0x04;
                    break;
            }
        }
        content[2] = 0x00;
        return content;
    }

    /**
     * 方法名称：	hideDevice
     * 方法描述：	隐藏设备界面
     * 参数：
     * 返回值类型：	void
     * 创建时间：	2015-3-20下午2:11:59
     */
    private void hideDevice() {
        tvDeviceName.setVisibility(View.INVISIBLE);
        ivAirSpeed.setVisibility(View.INVISIBLE);
        layoutCustom.setVisibility(View.GONE);
        layoutVolume.setVisibility(View.GONE);
        layoutWorkState.setVisibility(View.GONE);

        layoutError.setVisibility(View.GONE);
        tvInfos.setVisibility(View.VISIBLE);
    }

    /**
     * 方法名称：	toastError
     * 方法描述：	提示故障信息
     * 参数：
     * 返回值类型：	void
     * 创建时间：	2015-3-20上午11:46:32
     */
    private void toastError() {
        ivAirSpeed.setVisibility(View.INVISIBLE);
        layoutCustom.setVisibility(View.GONE);
        layoutVolume.setVisibility(View.GONE);
        layoutWorkState.setVisibility(View.GONE);

        layoutError.setVisibility(View.VISIBLE);
        tvInfos.setVisibility(View.GONE);
        switch (device.getError()) {
            case 1:
                // 香水瓶不匹配 0x0001
                errorContent = "精油瓶不匹配";
                break;
            case 2:
                // 电机故障 0x0002
                errorContent = "电机故障";
                break;
            case 4:
                // 香水剩余量为零 0x0004
                errorContent = "精油用完";
                break;
        }
        errorCode = device.getError();
        tvErrorCode.setText("代码：00" + errorCode);
        tvErrorContent.setText(errorContent);
    }

    /**
     * 方法名称：	alarmTask
     * 方法描述：	后台定时任务
     * 参数：
     * 返回值类型：	void
     * 创建时间：	2015-2-5下午2:12:36
     */

    private void alarmTask() {
        LogUtil.logDebug(tag, "alarmTask's intent is " + alarmService);
        alarmService.setAction(NetElectricsConst.ACTION_ALARM);
        ApplicationUtil.getContext().startService(alarmService);
    }

    /**
     * 方法名称：	stopAlarmTask
     * 方法描述：	停止后台定时任务
     * 参数：
     * 返回值类型：	void
     * 创建时间：	2015-3-21下午4:06:01
     */
    private void stopAlarmTask() {
        alarmService = new Intent(NetElectricsConst.ACTION_ALARM);
        ApplicationUtil.getContext().stopService(alarmService);
    }

    /**
     * 方法名称：	heartBeatTask
     * 方法描述：	启动心跳服务
     * 参数：
     * 返回值类型：	void
     * 创建时间：	2015-4-23下午5:18:55
     */
    private void heartBeatTask() {
        heartBeatService.setAction(NetElectricsConst.ACTION_HEART_BEAT);
        ApplicationUtil.getContext().startService(heartBeatService);
    }

    /**
     * 方法名称：	stopBeatTask
     * 方法描述：	停止心跳服务
     * 参数：
     * 返回值类型：	void
     * 创建时间：	2015-4-23下午5:21:19
     */
    private void stopBeatTask() {
        heartBeatService.setAction(NetElectricsConst.ACTION_HEART_BEAT);
        ApplicationUtil.getContext().stopService(heartBeatService);
    }

    /**
     * 广播接收器，向服务器端写完数据之后，等待服务器返回数据，并通过此端口接收
     */
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            handleAction(intent, context);
        }
    };

    private void handleAction(Intent intent, Context context) {
        switch (getAction(intent.getAction())) {
            case 1:
                LogUtil.logDebug(tag, "action is: action_data_receive");
                byte[] receivedata = intent.getByteArrayExtra("data");
                // fun add
                // Log.i("datawind", Arrays.toString(receivedata));
                ReceiverTask receiverTask = new ReceiverTask(device, handler);
                receiverTask.execute(receivedata);
                break;
            case 2:
                LogUtil.logDebug(tag, "action is: action_set_curtimescale");
                byte[] scaledata = intent.getByteArrayExtra("data");
                ReceiverTask scaleTask = new ReceiverTask(device, handler);
                scaleTask.execute(scaledata);
                break;
            case 3:
                LogUtil.logDebug(tag, "action is: action_data_error");
                // 检查网络，然后重新建立socket连接，并重新进行心跳和定时查询
                if (NetCommunicationUtil.getNetConnectState()) {
                    NetCommunicationUtil.buildListener();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            User user = (User) UserManager.getInstance().get(0);
                            NetCommunicationUtil.phoneSignUp(user.getPhone(),
                                    NetElectricsConst.PhoneSignUp);
                        }
                    }, 1000);
                    alarmTask();
                    heartBeatTask();
                } else {
                    stopRefreshAnim();
                    stopAlarmTask();
                    stopBeatTask();
                    //CustomToast("请确保网络连接");
                    ToastUtil.showToast(getActivity(),"请确保网络连接");
                }
                break;
            default:
                break;
        }
    }

    private int getAction(String action) {
        if (action.equals(NetElectricsConst.ACTION_DATA_RECEIVE)) {
            return 1;
        }
        if (action.equals(NetElectricsConst.ACTION_SET_CURTIMESCALE)) {
            return 2;
        }
        if (action.equals(NetElectricsConst.ACTION_DATA_ERROR)) {
            return 3;
        }
        return 0;
    }

    // 用于接收定时服务的广播
    public BroadcastReceiver alarmReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // 其他广播action
            if (intent.getAction().equals(NetElectricsConst.ACTION_ALARM_CAST)) {
                if (device != null) {
                    if (NetCommunicationUtil.isCompetable()) {
                        NetCommunicationUtil.queryDevice(
                                BonfeelFrame.queryAlarmTaskV2, device.getMac(),
                                NetElectricsConst.QueryState);
                    } else {
                        NetCommunicationUtil.queryDevice(
                                BonfeelFrame.queryAlarmTaskV1, device.getMac(),
                                NetElectricsConst.QueryState);
                    }
                    // 启动服务
                    alarmTask();
                } else {
                    stopAlarmTask();
                }
            }
        }
    };
    public BroadcastReceiver heartBeatReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            LogUtil.logDebug(tag, "received heartbeat");
            if (UserManager.getInstance().get(0) != null) {
                NetCommunicationUtil.heartBeat();
                heartBeatTask();
            }
        }
    };

    /**
     * 方法名称：	slowDown
     * 方法描述：	当程序被沉睡时，定时查询的时候为120s一次改成60s
     * 参数：
     * 返回值类型：	void
     * 创建时间：	2015-4-27下午12:06:56
     */
    private void slowDown() {
        AlarmService.setRUNNING_TIME(60 * 1000);
    }

    /**
     * 方法名称：	accelerate
     * 方法描述：	当程序被唤醒时，定时查询的时钟为60s一次改成20s最终改为10s
     * 参数：
     * 返回值类型：	void
     * 创建时间：	2015-4-27下午12:06:16
     */
    private void accelerate() {
        AlarmService.setRUNNING_TIME(10 * 1000);
    }

    /* (非Javadoc)
     * 方法名称：	onPause
     * 方法描述：	TODO
     * 重写部分：	@see android.app.Fragment#onPause()
     */
    @Override
    public void onPause() {
        LogUtil.logDebug(tag, "onPause has been set succeed");
        slowDown();
        super.onPause();
    }

    /* (非Javadoc)
     * 方法名称：	onResume
     * 方法描述：	TODO
     * 重写部分：	@see android.app.Fragment#onResume()
     */
    @Override
    public void onResume() {
        LogUtil.logDebug(tag, "onResume");
        accelerate();
        if (NetCommunicationUtil.getStep() == 2) {
            // 发送时间段新参数给设备
            upDateTimeScale();
            byte[] content = BonfeelFrame.setTimeScale(device.getCurIndex(),
                    device.getTimeScale(device.getCurIndex()).getStartTime()
                            .substring(0, 2),
                    device.getTimeScale(device.getCurIndex()).getStartTime()
                            .substring(3, 5),
                    device.getTimeScale(device.getCurIndex()).getEndTime()
                            .substring(0, 2),
                    device.getTimeScale(device.getCurIndex()).getEndTime()
                            .substring(3, 5),
                    device.getTimeScale(device.getCurIndex()).getWorkTime(),
                    device.getTimeScale(device.getCurIndex()).getRestTime(),
                    device.getTimeScale(device.getCurIndex()).getCircle());
            NetCommunicationUtil.controlDevice(content, device.getMac(),
                    NetElectricsConst.SetCurTimeScale);
            NetCommunicationUtil.setStep(3);
        }
        if (device != null) {
            tvDeviceName.setText(device.getName());
        }
        super.onResume();
    }

	/*
	 * ********************************************************
	 * 以下为触摸响应
	 */
	/*
	 * 方法名称：	onTouch
	 * 方法描述：	触摸响应，用于侧滑
	 */
    //@Override
//	public boolean onTouch(View v, MotionEvent event) {
//		createVelocityTracker(event);
//		switch (event.getAction()) {
//		case MotionEvent.ACTION_DOWN:
//			downX = event.getX();
//			break;
//		case MotionEvent.ACTION_MOVE:
//			if (event.getX() > downX) {
//				// 右滑，显示设备列表
//				contentParams.leftMargin += (event.getX() - downX);
//				if (contentParams.leftMargin > (screenWidth - menuPadding)) {
//					contentParams.leftMargin = (screenWidth - menuPadding);
//				}
//			} else {
//				contentParams.leftMargin += (event.getX() - downX);
//				if (contentParams.leftMargin < 0) {
//					contentParams.leftMargin = 0;
//				}
//			}
//			tabLayout.setLayoutParams(contentParams);
//			// 显示或隐藏设备列表界面
//			MainActivity mainActivity = (MainActivity) TabFirstFragment.this
//					.getActivity();
//			if (contentParams.leftMargin == 0) {
//				mainActivity.hideSlideMenu();
//			} else {
//				mainActivity.showSlideMenu();
//			}
//			break;
//		case MotionEvent.ACTION_UP:
//
//			if (event.getX() - downX > 0) {
//				if (getScrollVelocity() > 10) {
//					// 异步线程，进行滑动
//					new ScrollTask().execute(30);
//				} else {
//					new ScrollTask().execute(-30);
//				}
//			}
//			if (event.getX() - downX < 0) {
//				if (getScrollVelocity() > 10) {
//					new ScrollTask().execute(-30);
//				} else {
//					new ScrollTask().execute(30);
//				}
//			}
//			recycleVelocity();
//			break;
//		}
//		return true;
//	}

    /**
     * 方法名称：	createVelocityTracker
     * 方法描述：	创建加速度追踪器，这样就能使界面自动按滑动方向展开或关闭
     * 参数：			@param event
     * 返回值类型：	void
     * 创建时间：	2015-1-8上午8:58:59
     */
//	private void createVelocityTracker(MotionEvent event) {
//		if (mVelocityTracker == null) {
//			mVelocityTracker = VelocityTracker.obtain();
//		}
//		mVelocityTracker.addMovement(event);
//	}

    /**
     * 方法名称：	getScrollVelocity
     * 方法描述：	获取滑动的加速度
     * 参数：			@return
     * 返回值类型：	int
     * 创建时间：	2015-1-8上午9:04:10
     */
//	private int getScrollVelocity() {
//		mVelocityTracker.computeCurrentVelocity(1000);
//		int velocity = (int) mVelocityTracker.getXVelocity();
//		return Math.abs(velocity);
//	}

    /**
     * 方法名称：	recycleVelocity
     * 方法描述：	回收加速度变量
     * 参数：
     * 返回值类型：	void
     * 创建时间：	2015-1-8上午9:05:17
     */
//	private void recycleVelocity() {
//		mVelocityTracker.recycle();
//		mVelocityTracker = null;
//	}

    // 异步线程，用于自动滑动
//	public class ScrollTask extends AsyncTask<Integer, Integer, Integer> {
//		@Override
//		protected Integer doInBackground(Integer... speed) {
//			int leftmargin = contentParams.leftMargin;
//			while (true) {
//				leftmargin += speed[0];
//				if (leftmargin > (screenWidth - menuPadding)) {
//					leftmargin = screenWidth - menuPadding;
//					break;
//				}
//				if (leftmargin < 0) {
//					leftmargin = 0;
//					break;
//				}
//				publishProgress(leftmargin);
//				sleep(20);
//			}
//			return leftmargin;
//		}
//
//		@Override
//		protected void onPostExecute(Integer result) {
//			super.onPostExecute(result);
//			contentParams.leftMargin = result;
//			tabLayout.setLayoutParams(contentParams);
//			if (result != 0) {
//				MainActivity mainActivity = (MainActivity) TabFirstFragment.this
//						.getActivity();
//				mainActivity.getSlideMenuFragment().updateDeviceList();
//			}
//		}
//
//		@Override
//		protected void onProgressUpdate(Integer... values) {
//			super.onProgressUpdate(values);
//			contentParams.leftMargin = values[0];
//			tabLayout.setLayoutParams(contentParams);
//			// 显示或隐藏设备列表界面
//			MainActivity mainActivity = (MainActivity) TabFirstFragment.this
//					.getActivity();
//			if (values[0] == 0) {
//				mainActivity.hideSlideMenu();
//			} else {
//				mainActivity.showSlideMenu();
//			}
//		}
//	}

    /**
     * 方法名称：	sleep
     * 方法描述：	睡眠操作
     * 参数：			@param millis
     * 返回值类型：	void
     * 创建时间：	2015-1-8上午9:06:54
     */
//	private void sleep(long millis) {
//		try {
//			Thread.sleep(millis);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}

	/*
	 * *******************************************************
	 */
	/*
	 * 方法名称：	onDestroy
	 * 方法描述：
	 */
    @Override
    public void onDestroy() {
        super.onDestroy();
        if(checkDialog.isShowing() && checkDialog != null){
            checkDialog.dismiss();
        }
        LogUtil.logDebug(tag, "TabFirstFragment onDestroy Exected");
        if (!NetCommunicationUtil.isReBoot()) {
            ApplicationUtil.getContext().unregisterReceiver(broadcastReceiver);
            ApplicationUtil.getContext().unregisterReceiver(alarmReceiver);
            ApplicationUtil.getContext().unregisterReceiver(heartBeatReceiver);
        }
        Intent serviceClose = new Intent(NetElectricsConst.ACTION_CLOSE);
        ApplicationUtil.getContext().startService(serviceClose);
    }
}