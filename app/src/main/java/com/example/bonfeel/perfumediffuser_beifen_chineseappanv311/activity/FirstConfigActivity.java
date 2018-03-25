package com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.R;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.custom.SwitchButton;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.model.NetElectricsConst;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.model.Problem;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.model.User;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.util.ApplicationUtil;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.util.EasyLinkConfig;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.util.EasyLinkUtil;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.util.Helper;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.util.LogUtil;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.util.NetCommunicationUtil;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.util.ProblemManager;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.util.UserManager;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.ksoap2.serialization.PropertyInfo;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Author:FunFun
 * Function:一键配置操作，整个配置过程需要30S
 * Date:2016/4/12 11:20
 */
public class FirstConfigActivity extends BaseActivity {
    private static final String tag = "FirstConfigActivity";
    private TextView tvTitle;
    private TextView tvSSID;
    private EditText etKey;
    private TextView tvConfigHelp;
    private Button btnEasyLink;
    private Button btnScanMac;
    private SwitchButton sbSwitchModel;
    private LinearLayout layoutSSID;
    private LinearLayout layoutMac;
    private EditText etMac;

    private String title;

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private String ssid;
    private String key;

    private final int START_EASY_LINK = 1;
    private final int START_MATCH_USER = 2;
    private final int NO_DEVICE = 3;
    private final int MATCH_SUCCESS = 4;
    private final int MATCH_FAIL = 5;
    private final int MATCH_ERROR = 6;
    private final int GET_LIST_SUCCESS = 7;
    private final int GET_LIST_FAIL = 8;
    private final int GET_LIST_ERROR = 9;
    private final int GET_PROBLEM_SUCCESS = 10;
    private final int GET_PROBLEM_FAIL = 11;
    private final int GET_PROBLEM_ERROR = 12;

    private final int GET_WIFI_SSID = 1;
    private final int ACTIVITY_FOR_QR = 2;

    private Dialog dialog;
    private int reMatch;

    private IntentFilter signIntent;

    private boolean isReSign;
    private int reSignTime;

    private boolean isManual;
    private String mac;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_easy_link);
        super.onCreate(savedInstanceState);
    }

    /* (非Javadoc)
     * 方法名称：	initialData
     * 方法描述：	TODO
     * 重写部分：	@see com.example.bonfeel.activity.BaseActivity#initialData()
     */
    @Override
    protected void initialData() {
        title = "添加设备";
        preferences = getSharedPreferences("easylink", MODE_PRIVATE);
        editor = preferences.edit();
        key = preferences.getString("key", "");
        mac = preferences.getString("mac", "");
        ssid = preferences.getString("ssid", "");
        // 显示ssid
        if (ssid.equals("")) {
            ssid = EasyLinkUtil.getNetSsid(this);
        }
        reMatch = 0;
        isReSign = true;
        reSignTime = 0;
        isManual = false;
        // 广播接收器
        signIntent = new IntentFilter();
        signIntent.addAction(NetElectricsConst.ACTION_PHONE_SIGNUP);
        signIntent.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(broadcastReceiver, signIntent);
    }

    /* (非Javadoc)
     * 方法名称：	initialView
     * 方法描述：	TODO
     * 重写部分：	@see com.example.bonfeel.activity.BaseActivity#initialView()
     */
    @Override
    protected void initialView() {
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvSSID = (TextView) findViewById(R.id.tv_ssid);
        etKey = (EditText) findViewById(R.id.et_key);
        tvConfigHelp = (TextView) findViewById(R.id.tv_config_help);
        btnEasyLink = (Button) findViewById(R.id.btn_easylink);
        layoutSSID = (LinearLayout) findViewById(R.id.layout_ssid);
        btnScanMac = (Button) findViewById(R.id.btn_right);
        sbSwitchModel = (SwitchButton) findViewById(R.id.sb_switch_model);
        sbSwitchModel.setImageResource(R.drawable.switch_model_bg);
        layoutMac = (LinearLayout) findViewById(R.id.layout_device_mac);
        etMac = (EditText) findViewById(R.id.et_mac);

        tvTitle.setText(title);
        tvSSID.setText(ssid);
        etKey.setText(key);
        if (!mac.equals("")) {
            etMac.setText(mac.substring(0, 8));
        }
        btnScanMac.setText("");
        btnScanMac.setVisibility(View.VISIBLE);
        btnScanMac.setBackgroundResource(R.drawable.button_scan);
    }

    /* (非Javadoc)
     * 方法名称：	initialHandler
     * 方法描述：	TODO
     * 重写部分：	@see com.example.bonfeel.activity.BaseActivity#initialHandler()
     */
    @Override
    protected void initialHandler() {
        btnEasyLink.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isWifiAviliable()) {
                    dialog = ApplicationUtil.getLoadingDialog(
                            FirstConfigActivity.this, "正在配置");
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.setCancelable(false);
                    dialog.show();
                    btnEasyLink.setText("配置中...");
                    btnEasyLink.setEnabled(false);

                    startConfig();
                }
            }
        });
        // 配置帮助
        tvConfigHelp.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isHasProblems()) {
                    getHelp();
                }
            }
        });
        sbSwitchModel.setOnChangeListener(new SwitchButton.OnChangeListener() {
            @Override
            public void OnChanged(boolean state) {
                if (state) {
                    if (!mac.equals("")) {
                        etMac.setText(mac.substring(0, 8));
                    }
                    layoutMac.setVisibility(View.VISIBLE);
                } else {
                    etMac.setText("");
                    layoutMac.setVisibility(View.GONE);
                }
                isManual = state;
            }
        });
        btnScanMac.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isManual) {
                    Intent qrIntent = new Intent(FirstConfigActivity.this,
                            CaptureActivity.class);
                    startActivityForResult(qrIntent, ACTIVITY_FOR_QR);
                } else {
                    showInfo("请先切换到手动模式");
                }
            }
        });
        // layoutSSID.setOnClickListener(new OnClickListener()
        // {
        // @Override
        // public void onClick(View v)
        // {
        // Intent intent = new Intent(EasyLinkActivity.this,
        // GetWifiActivity.class);
        // startActivityForResult(intent, GET_WIFI_SSID);
        // }
        // });
    }

    // private boolean isKeyInput()
    // {
    // if ( tvSSID.getText().toString().length() != 0 )
    // {
    // /*
    // * 首先保证ssid已选择，然后记录密码和路由器
    // */
    // key = etKey.getText().toString().trim();
    // ssid = tvSSID.getText().toString();
    // editor.putString("key", key);
    // editor.putString("ssid", ssid);
    // editor.commit();
    // return true;
    // }
    // else
    // {
    // showInfo("请务必输入无线网络密码");
    // return false;
    // }
    // }
    private boolean isWifiAviliable() {
        if (NetCommunicationUtil.isWifiConnected()) {
            key = etKey.getText().toString().trim();
            ssid = tvSSID.getText().toString();
            editor.putString("key", key);
            editor.putString("ssid", ssid);
            if (isManual) {
                mac = etMac.getText().toString().trim();
                if (mac.length() != 0) {
                    if (!isRightMac(mac)) {
                        return false;
                    }
                } else {
                    showInfo("请输入设备MAC");
                    return false;
                }
                editor.putString("mac", mac);
            }
            editor.commit();
            return true;
        } else {
            showInfo("请确保手机使用无线网络");
            return false;
        }
    }

    /**
     * 方法名称：	isRightMac
     * 方法描述：	检查mac是否正确（不包含特殊字符，长度为12位）
     * 参数：			@param mac
     * 参数：			@return
     * 返回值类型：	boolean
     * 创建时间：	2015-5-12下午3:08:21
     */
    private boolean isRightMac(String mac) {
        for (int i = 0; i < mac.length(); i++) {
            char unit = mac.charAt(i);
            if (unit < 48 || unit > 122) {
                showInfo("设备MAC不正确");
                return false;
            } else {
                if (unit > 57 && unit < 65) {
                    showInfo("设备MAC不正确");
                    return false;
                } else {
                    if (unit > 90 && unit < 97) {
                        showInfo("设备MAC不正确");
                        return false;
                    }
                }
            }
        }
        if (mac.length() != 12) {
            showInfo("设备MAC不正确");
            return false;
        }
        return true;
    }

    /**
     * 方法名称：	startConfig
     * 方法描述：	根据手动还是自动，进行不同配置操作
     * 参数：
     * 返回值类型：	void
     * 创建时间：	2015-5-12上午11:28:00
     */
    private void startConfig() {
        EasyLinkUtil.clean();
        EasyLinkUtil.setNetInfo(key, "", 0, FirstConfigActivity.this);
        if (isManual) {
            // 配置路由器
            EasyLinkConfig.transminNetInfo(FirstConfigActivity.this);
            // 15s later, start to match device, using the already know mac
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    EasyLinkConfig.stopTransminNetInfo();

                    Message message = handler.obtainMessage();
                    message.what = START_MATCH_USER;
                    handler.sendMessage(message);
                }
            }, 15000);
        } else {
            // 检查已入网设备
            EasyLinkConfig.checkDevice(handler);
        }
    }

    private boolean isHasProblems() {
        if (ProblemManager.getInstance().getList().size() == 0) {
            // 联网查询问题
            getProblems();
            return false;
        } else {
            return true;
        }
    }

    private void getProblems() {
        NetCommunicationUtil.httpConnect(getProblemInfos(),
                NetElectricsConst.METHOD_GET_USUALPROBLEM,
                NetElectricsConst.STYLE_OBJECT, new NetCommunicationUtil.HttpCallbackListener() {
                    @Override
                    public void onFinish(Bundle bundle) {
                        Message message = handler.obtainMessage();
                        if (bundle.getInt("resCode") == 1) {
                            NetCommunicationUtil.getHttpResultInfo(
                                    NetElectricsConst.METHOD_TAG.GetProblems,
                                    bundle.getString("result"));
                            message.what = GET_PROBLEM_SUCCESS;
                        } else {
                            message.what = GET_PROBLEM_FAIL;
                        }
                        handler.sendMessage(message);
                    }

                    @Override
                    public void onError(Bundle bundle) {
                        Message message = handler.obtainMessage();
                        message.what = GET_PROBLEM_ERROR;
                        handler.sendMessage(message);
                    }
                });
    }

    /**
     * 方法名称：	getProblemInfos
     * 方法描述：	获取参数集
     * 参数：			@return
     * 返回值类型：	List<PropertyInfo>
     * 创建时间：	2015-3-21下午4:12:51
     */
    private List<PropertyInfo> getProblemInfos() {
        List<PropertyInfo> infos = new ArrayList<PropertyInfo>();

        PropertyInfo token = new PropertyInfo();
        token.setName(NetElectricsConst.TOKEN);
        token.setValue("");

        infos.add(token);

        return infos;
    }

    private void getHelp() {
        // 判断问题列表是否为空
        Problem problem = (Problem) ProblemManager.getInstance().get("如何添加设备？");
        Intent configIntent = new Intent(FirstConfigActivity.this,
                ProblemDetailActivity.class);
        Bundle problemBundle = new Bundle();
        problemBundle.putString("name", problem.getName());
        problemBundle.putString("content", problem.getContent());
        configIntent.putExtra("proBundle", problemBundle);
        startActivity(configIntent);
    }

    private Handler handler = new Handler() {
        /* (非Javadoc)
         * 方法名称：	handleMessage
         * 方法描述：	TODO
         * 重写部分：	@see android.os.Handler#handleMessage(android.os.Message)
         */
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case START_EASY_LINK:
				/*
				 * finish check network , if there are devices already in network, they are in existQueue
				 * start to config ssid and key to new devices
				 */
                    configDevice();
                    break;
                case START_MATCH_USER:
				/*
				 * finish config ssid and key to new devices (if they are exist)
				 * start to make new devices to match user
				 */
                    matchDevice();
                    break;
                case NO_DEVICE:
				/*
				 * queue has no device, so has no new device in network
				 */
                    dialog.dismiss();
                    btnEasyLink.setText("配置");
                    btnEasyLink.setEnabled(true);
                    showInfo("没有新设备，请确认");
                    break;
                case MATCH_SUCCESS:
				/*
				 * finish match user, update the device list, so user can see the new device in the list
				 */
                    queryDeviceList();
                    break;
                case MATCH_FAIL:
				/*
				 * failed in matching user,then should try again,
				 * in 3 times, if match success, the go to MATCH_SUCCESS
				 * else toast do not rematch device
				 */
                    if (reMatch < 3) {
                        reMatch++;
                        matchDevice();
                    } else {
                        dialog.dismiss();
                        btnEasyLink.setText("配置");
                        btnEasyLink.setEnabled(true);

                        Toast toast = null;
                        if (msg.obj.toString().equals("")) {
                            toast = Toast.makeText(FirstConfigActivity.this,
                                    "请确保设备成功连接服务器", Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                        } else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(
                                    FirstConfigActivity.this);
                            builder.setMessage("该设备已被用户：" + msg.obj + " 绑定");
                            builder.create().show();

                            // toast = Toast.makeText(
                            // EasyLinkActivity.this,
                            // "该设备已被用户："+msg.obj+" 绑定", 20000);
                            // Toast.LENGTH_LONG);
                        }

                        reMatch = 0;
                    }
                    break;
                case MATCH_ERROR:
				/*
				 * someException has happened in matching user
				 * (for example, the network is useless)
				 * just toast user
				 */
                    if (reMatch < 3) {
                        reMatch++;
                        matchDevice();
                    } else {
                        dialog.dismiss();
                        btnEasyLink.setText("配置");
                        btnEasyLink.setEnabled(true);
                        showInfo("配置异常，请重试");
                        reMatch = 0;
                    }
                    break;
                case GET_LIST_SUCCESS:
				/*
				 * ball shit
				 */
                    signAgain();
                    break;
                case GET_LIST_FAIL:
				/*
				 * failed in updating device list , so try again
				 */
                    queryDeviceList();
                    break;
                case GET_LIST_ERROR:
				/*
				 * no handler
				 */
                    dialog.setCancelable(true);
                    dialog.dismiss();
                    btnEasyLink.setText("配置");
                    btnEasyLink.setEnabled(true);
                    showInfo("请退出软件重试");
                    break;
                case GET_PROBLEM_SUCCESS:
                    getHelp();
                    break;
                case GET_PROBLEM_FAIL:
                    break;
                case GET_PROBLEM_ERROR:
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 方法名称：	signAgain
     * 方法描述：	重新调用手机注册帧，因为服务器的奇葩设计，导致了这个步骤的产生
     * 参数：
     * 返回值类型：	void
     * 创建时间：	2015-5-5下午5:49:34
     */
    private void signAgain() {
        NetCommunicationUtil.phoneSignUp(
                ((User) UserManager.getInstance().get(0)).getPhone(),
                NetElectricsConst.PhoneSignUp);
        // 2s later check if need reSign
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // 重发5次，若不成功则退出
                if (reSignTime < 5) {
                    if (isReSign) {
                        signAgain();
                    }
                    reSignTime++;
                } else {
                    dialog.dismiss();
                    reSignTime = 0;
                    isReSign = false;
                    finish();
                }
            }
        }, 2000);
    }

    /**
     * 方法名称：	matchDevice
     * 方法描述：	http接口调用
     * 参数：
     * 返回值类型：	void
     * 创建时间：	2015-3-24下午3:32:39
     */
    private void matchDevice() {
        byte[] mac = null;
        if (isManual) {
            // mac from string to byte[]
            mac = Helper.string2ByteArray(etMac.getText().toString().trim());
            NetCommunicationUtil.httpConnect(getEasyLinkInfos(mac),
                    NetElectricsConst.METHOD_EASY_LINKV3,
                    NetElectricsConst.STYLE_RES, new NetCommunicationUtil.HttpCallbackListener() {
                        @Override
                        public void onFinish(Bundle bundle) {
                            Message message = handler.obtainMessage();
                            if (bundle.getInt("resCode") == 1) {
                                message.what = MATCH_SUCCESS;
                            } else {
                                message.what = MATCH_FAIL;
                                Log.i("调用webservice1", "调用webservice返回的数据："
                                        + bundle.getString("result"));
                                message.obj = getBelonger(bundle);
                            }
                            handler.sendMessage(message);
                        }

                        @Override
                        public void onError(Bundle bundle) {
                            Message message = handler.obtainMessage();
                            message.what = MATCH_ERROR;
                            handler.sendMessage(message);
                        }
                    });
        } else {
            // 判断是否有新设备入网
            if (EasyLinkUtil.getConfigQueues() != null) {
                // 一配一
                mac = EasyLinkUtil.getConfigQueues()[0].getMac();
                NetCommunicationUtil.httpConnect(getEasyLinkInfos(mac),
                        NetElectricsConst.METHOD_EASY_LINKV3,
                        NetElectricsConst.STYLE_RES,
                        new NetCommunicationUtil.HttpCallbackListener() {
                            @Override
                            public void onFinish(Bundle bundle) {
                                Message message = handler.obtainMessage();
                                if (bundle.getInt("resCode") == 1) {
                                    message.what = MATCH_SUCCESS;
                                } else {
                                    message.what = MATCH_FAIL;
                                    Log.i("调用webservice",
                                            "调用webservice接口返回的数据："
                                                    + bundle.getString("result"));
                                    message.obj = getBelonger(bundle);
                                    Log.i("绑定到哪个账号：", "绑定到哪个账号:"
                                            + getBelonger(bundle));
                                }
                                handler.sendMessage(message);
                            }

                            @Override
                            public void onError(Bundle bundle) {
                                Message message = handler.obtainMessage();
                                message.what = MATCH_ERROR;
                                handler.sendMessage(message);
                            }
                        });
            } else {
                Message message = handler.obtainMessage();
                message.what = NO_DEVICE;
                handler.handleMessage(message);
            }
        }
    }

    /**
     * 方法名称：	getBelonger
     * 方法描述：	获取到已绑定的设备归属于哪个账号
     * 参数：			@param bundle
     * 参数：			@return
     * 返回值类型：	String
     * 创建时间：	2015-5-7下午2:18:29
     */
    private String getBelonger(Bundle bundle) {
        String str = "";
        try {
            JSONTokener jsonTokener = new JSONTokener(
                    bundle.getString("result"));
            JSONObject jsonObject = (JSONObject) jsonTokener.nextValue();
            str = jsonObject.getString("Mobileid");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return str;
    }

    /**
     * 方法名称：	getEasyLinkInfos
     * 方法描述：	http接口调用的参数
     * 参数：			@return
     * 返回值类型：	List<PropertyInfo>
     * 创建时间：	2015-3-16下午1:38:33
     */
    private List<PropertyInfo> getEasyLinkInfos(byte[] mac) {
        List<PropertyInfo> infos = new ArrayList<PropertyInfo>();

        PropertyInfo userInfo = new PropertyInfo();
        userInfo.setName(NetElectricsConst.USER_NAME);
        userInfo.setValue(((User) UserManager.getInstance().get(0)).getPhone());

        PropertyInfo deviceMac = new PropertyInfo();
        deviceMac.setName(NetElectricsConst.DEVICE_MAC);
        String strMac = "";
        for (int i = 0; i < 6; i++) {
            String t = Integer.toHexString(mac[i] & 0xff);
            if (t.length() == 1) {
                t = "0" + t;
            }
            strMac += t;
            if (i < 5) {
                strMac += "-";
            }
        }
        deviceMac.setValue(strMac);
        System.out.println(strMac);
        Log.i("配上路由器:", "配上路由器设备的MAC:" + strMac);
        PropertyInfo token = new PropertyInfo();
        token.setName(NetElectricsConst.TOKEN);
        token.setValue("");

        infos.add(userInfo);
        infos.add(deviceMac);
        infos.add(token);

        return infos;
    }

    /**
     * 方法名称：	queryDeviceList
     * 方法描述：	http接口，更新一键配置后的设备列表
     * 参数：
     * 返回值类型：	void
     * 创建时间：	2015-3-16下午4:35:23
     */
    private void queryDeviceList() {
        NetCommunicationUtil.httpConnect(getDeviceListInfos(),
                NetElectricsConst.METHOD_GET_DEVICE,
                NetElectricsConst.STYLE_OBJECT, new NetCommunicationUtil.HttpCallbackListener() {
                    @Override
                    public void onFinish(Bundle bundle) {
                        Message message = handler.obtainMessage();
                        if (bundle.getInt("resCode") == 1) {
                            // 设备列表
                            NetCommunicationUtil.getHttpResultInfo(
                                    NetElectricsConst.METHOD_TAG.GetDevice,
                                    bundle.getString("result"));
                            message.what = GET_LIST_SUCCESS;
                        } else {
                            message.what = GET_LIST_FAIL;
                        }
                        handler.sendMessage(message);
                    }

                    @Override
                    public void onError(Bundle bundle) {
                        Message message = handler.obtainMessage();
                        message.what = GET_LIST_ERROR;
                        handler.sendMessage(message);
                    }
                });
    }

    /**
     * 方法名称：	getDeviceListInfos
     * 方法描述：	获取设备列表的参数集
     * 参数：			@return
     * 返回值类型：	List<PropertyInfo>
     * 创建时间：	2015-3-16下午4:41:23
     */
    private List<PropertyInfo> getDeviceListInfos() {
        List<PropertyInfo> infos = new ArrayList<PropertyInfo>();

        PropertyInfo nameInfo = new PropertyInfo();
        nameInfo.setName(NetElectricsConst.USER_NAME);
        nameInfo.setValue(((User) UserManager.getInstance().get(0)).getPhone());

        PropertyInfo token = new PropertyInfo();
        token.setName(NetElectricsConst.TOKEN);
        token.setValue("");

        infos.add(nameInfo);
        infos.add(token);

        return infos;
    }

    /* (非Javadoc)
     * 方法名称：	onActivityResult
     * 方法描述：	TODO
     * 重写部分：	@see android.app.Activity#onActivityResult(int, int, android.content.Intent)
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case GET_WIFI_SSID:
                if (resultCode == RESULT_OK) {
                    ssid = data.getStringExtra("ssid");
                    tvSSID.setText(ssid);
                }
                break;
            case ACTIVITY_FOR_QR:
                if (resultCode == RESULT_OK) {
                    // 二维码扫描结果
                    Bundle bundle = data.getExtras();
                    String scanResult = bundle.getString("result");
                    etMac.setText(scanResult);
                }
            default:
                break;
        }
    }

    /**
     * 方法名称：	configDevice
     * 方法描述：	配置设备的ssid和key
     * 参数：
     * 返回值类型：	void
     * 创建时间：	2015-4-15下午4:06:51
     */
    private void configDevice() {
        // 配置路由器
        EasyLinkConfig.transminNetInfo(FirstConfigActivity.this);
        // 确认路由器是否配置成功
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                EasyLinkConfig.emspControl();
            }
        }, 3000);
    }

    @Override
    protected void onDestroy() {
        EasyLinkConfig.stopTransminNetInfo();
        unregisterReceiver(broadcastReceiver);
        super.onDestroy();
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
			/*
			 * update device list success, then user can see all matched devices and control them
			 */
            if (intent.getAction()
                    .equals(NetElectricsConst.ACTION_PHONE_SIGNUP)) {
                dialog.dismiss();
                isReSign = false;
                finish();
            }
            if (intent.getAction().equals(
                    ConnectivityManager.CONNECTIVITY_ACTION)) {
                if (NetCommunicationUtil.isWifiConnected()) {
                    if (!ssid.equals(EasyLinkUtil.getNetSsid(context))) {
                        LogUtil.logDebug("connectivity_action",
                                "wifi is changed");
                        tvSSID.setText(EasyLinkUtil.getNetSsid(context));
                        etKey.setText("");
                    } else {
                        LogUtil.logDebug("connectivity_action",
                                "wifi is not changed");
                        tvSSID.setText(ssid);

                        etKey.setText(key);
                    }
                }
            }
        }
    };
}
