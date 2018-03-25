package com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.activity;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.view.View.OnClickListener;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.R;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.model.NetElectricsConst;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.model.User;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.util.ApplicationUtil;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.util.NetCommunicationUtil;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.util.ToastUtil;
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
 * Function:用户登录界面
 * Date:2016/4/12 11:29
 */
public class LoginActivity extends BaseActivity {
    private Button btnLogin;
    private TextView tvFindKey, tvSignUp;
    private EditText etName, etCode;
    private CheckBox cb_rememberPwd,cb_autoLogin;

    private UserManager userManager;
    private User user;

    private IntentFilter socketCast;

    private Dialog dialog;
    private String fail;

    // 新增用于自动记录登录信息功能
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private String name, code;

    private static final int SIGN_UP = 0;

    private final int LOGIN_SUCCESS = 1;
    private final int LOGIN_FAIL = 2;
    private final int LOGIN_ERROR = 3;

    private final int GET_DEVICES_SUCCESS = 4;
    private final int GET_DEVICES_FAIL = 5;
    private final int GET_DEVICES_ERROR = 6;

    private final int RE_LOGIN = 7;
    private final int LOGIN_IN = 8;
    private final int SEND_REGFRAME = 9;
    private final int RE_REG_FRAME = 10;

    private boolean isSignSuccess;
    private boolean isNetSuccess;

    private boolean isRemember = false,isAuto = false;

    private int reDoTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_login);
        super.onCreate(savedInstanceState);
    }

    /*
     * 方法名称：	initialData
     * 方法描述：	TODO
     */
    @Override
    protected void initialData() {
        //preferences = PreferenceManager.getDefaultSharedPreferences(this);
        preferences = getSharedPreferences("user_info.txt",MODE_PRIVATE);
        editor = preferences.edit();
        name = preferences.getString("account", "");
        code = preferences.getString("code", "");
        isRemember = preferences.getBoolean("remember", false);
        isAuto = preferences.getBoolean("auto", false);

        userManager = UserManager.getInstance();
        user = (User) userManager.create();
        // 广播接收器
        socketCast = new IntentFilter();
        socketCast.addAction(NetElectricsConst.ACTION_PHONE_SIGNUP);
        socketCast.addAction(NetElectricsConst.ACTION_DATA_FAILED);
        socketCast.addAction(NetElectricsConst.ACTION_DATA_ERROR);
        registerReceiver(broadcastReceiver, socketCast);

        isSignSuccess = false;
        isNetSuccess = true;
        reDoTime = 0;
    }
    /**
     * 自动登录和点击登录按钮进行登录
     */
    private void autoLogin() {
        if (getInputContext()) {
            name = etName.getText().toString();
            code = etCode.getText().toString();

            user.setPhone(etName.getText().toString());
            user.setPassword(etCode.getText().toString());

            dialog = ApplicationUtil.getLoadingDialog(
                    LoginActivity.this, "登录中...");
            dialog.show();

            // 检查网络连接
            if (NetCommunicationUtil.getNetConnectState()) {
                checkUserId();
            } else {
                dialog.dismiss();
                //showInfo("请确保网络正确连接");
                ToastUtil.showToast(LoginActivity.this,"请确保网络正确连接");
            }
        }

    }

    /*
     * 方法名称：	initialView
     * 方法描述：	TODO
     */
    @Override
    protected void initialView() {
        btnLogin = (Button) findViewById(R.id.btn_login);
        tvFindKey = (TextView) findViewById(R.id.tv_forget_psd);
        tvSignUp = (TextView) findViewById(R.id.tv_sign_up);
        etName = (EditText) findViewById(R.id.et_name);
        etCode = (EditText) findViewById(R.id.et_code);
        cb_rememberPwd = (CheckBox) findViewById(R.id.cb_rememberPwd);
        cb_autoLogin = (CheckBox) findViewById(R.id.cb_autoLogin);
        etName.setText(name);

        if (isRemember){
            cb_rememberPwd.setChecked(true);
            etCode.setText(code);
        }else{
            etCode.setText("");
        }

        if ( isAuto ){
            cb_autoLogin.setChecked(true);
            autoLogin();
        }
    }

    /*
     * 方法名称：	initialHandler
     * 方法描述：	TODO
     */
    @Override
    protected void initialHandler() {
        // 登录
        btnLogin.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // 检查用户名和密码是否输入
                autoLogin();
            }
        });
        cb_rememberPwd.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
                if (arg1){
                    editor.putBoolean("remember", true).commit();
                }else{
                    editor.putBoolean("remember", false).commit();
                }
            }
        });

        cb_autoLogin.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
                if (arg1){
                    cb_rememberPwd.setChecked(true);
                    editor.putBoolean("auto", true).commit();
                }else{
                    editor.putBoolean("auto", false).commit();
                }
            }
        });




        // 找回密码
        tvFindKey.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent findKey = new Intent(LoginActivity.this,
                        FindKeyActivity.class);
                startActivity(findKey);
            }
        });
        // 注册
        tvSignUp.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signUp = new Intent(LoginActivity.this,
                        SignUpActivity.class);
                startActivityForResult(signUp, SIGN_UP);
            }
        });
    }

    /**
     * 方法名称：	checkUserId
     * 方法描述：	验证用户身份
     * 参数：
     * 返回值类型：	void
     * 创建时间：	2015-3-19上午8:29:18
     */
    private void checkUserId() {
		/*
		 * 调用I_UserLogin接口
		 * 传入mobileid, password, token
		 * 返回0/手机号@在线状态（1/0）
		 */
        NetCommunicationUtil.httpConnect(getPropertyInfos("Login"),
                NetElectricsConst.METHOD_LOGINV2, NetElectricsConst.STYLE_RES,
                new NetCommunicationUtil.HttpCallbackListener() {
                    @Override
                    public void onFinish(Bundle bundle) {
                        Message message = handler.obtainMessage();
                        // 检查账号和密码是否正确
                        if (isCounterRight(bundle)) {
                            // 检查该账号是否已登录
                            if (!isReLogin(bundle)) {
                                // 启动后台socket通信服务
                                NetCommunicationUtil.buildListener();
                                // 发送消息准备去查询设备列表
                                message.what = LOGIN_SUCCESS;
                            } else {
                                fail = "账号已登录，请确认";
                                message.what = RE_LOGIN;
                            }
                        } else {
                            // 取出失败的原因
                            fail = NetCommunicationUtil.getHttpResultInfo(
                                    bundle.getString("result"),
                                    NetElectricsConst.METHOD_TAG.UserLogin).getString("message");
                            message.what = LOGIN_FAIL;
                        }
                        handler.sendMessage(message);
                    }

                    @Override
                    public void onError(Bundle bundle) {
                        fail = bundle.getString("message");
                        // 登录异常
                        Message message = handler.obtainMessage();
                        message.what = LOGIN_ERROR;
                        handler.sendMessage(message);
                    }
                });
    }

    /**
     * 方法名称：	isCounterRight
     * 方法描述：	检查账号和密码是否正确
     * 参数：			@return
     * 返回值类型：	boolean, @1则返回true; @0则返回false
     * 创建时间：	2015-4-29上午10:40:59
     */
    private boolean isCounterRight(Bundle bundle) {
        String identity = "";
        try {
            JSONTokener jsonTokener = new JSONTokener(
                    bundle.getString("result"));
            JSONObject jsonObject = (JSONObject) jsonTokener.nextValue();
            String str = jsonObject.getString("ResCode");
            for (int i = 0; i < str.length(); i++) {
                if (str.charAt(i) != '@') {
                    identity += str.charAt(i);
                } else {
                    break;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (identity.equals("0")) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 方法名称：	isReLogin
     * 方法描述：	检查是否已登录
     * 参数：			@return
     * 返回值类型：	boolean
     * 创建时间：	2015-4-9下午4:42:49
     */
    private boolean isReLogin(Bundle bundle) {
        int isOnline = 0;
        try {
            JSONTokener jsonTokener = new JSONTokener(
                    bundle.getString("result"));
            JSONObject jsonObject = (JSONObject) jsonTokener.nextValue();
            String str = jsonObject.getString("ResCode");
            for (int i = 0; i < str.length(); i++) {
                if (str.charAt(i) == '@') {
                    isOnline = Integer.parseInt(str.charAt(i + 1) + "");
                    break;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (isOnline == 0) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 方法名称：	getDeviceList
     * 方法描述：	获取当前用户的设备列表
     * 参数：
     * 返回值类型：	void
     * 创建时间：	2015-3-19上午8:32:35
     */
    private void getDeviceList() {
		/*
		 * 调用I_GetDevice接口
		 * 传入mobileid, token
		 * 返回0/device list
		 */
        NetCommunicationUtil.httpConnect(getPropertyInfos("devices"),
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
                            // Log.i("logindevicelist",
                            // bundle.getString("result"));
                            // 手机注册帧，告知服务器手机用户在线了
                            NetCommunicationUtil.phoneSignUp(user.getPhone(),
                                    NetElectricsConst.PhoneSignUp);
                            message.what = GET_DEVICES_SUCCESS;
                        } else {
                            message.what = GET_DEVICES_FAIL;
                        }
                        handler.sendMessage(message);
                    }

                    @Override
                    public void onError(Bundle bundle) {
                        fail = bundle.getString("message");
                        Message message = handler.obtainMessage();
                        message.what = GET_DEVICES_ERROR;
                        handler.sendMessage(message);
                    }
                });
    }

    // 处理网络通信结果
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case LOGIN_SUCCESS:
                    reDoTime = 0;
                    if ( isRemember ){
                        editor.putString("account", name);
                        editor.putString("code", code);
                    }else{
                        editor.putString("account", name);
                        editor.putString("code", "");
                    }
                    editor.commit();
                    // 用户身份验证成功，开始查询设备列表
                    getDeviceList();
                    break;
                case LOGIN_FAIL:
                    dialog.dismiss();
                    // login failed, cause error count or password
                    showInfo(fail);
                    break;
                case LOGIN_ERROR:
                    if (reDoTime < 3) {
                        checkUserId();
                        reDoTime++;
                    } else {
                        reDoTime = 0;
                        dialog.dismiss();
                        // 登录异常
                        showInfo(fail);
                    }
                    break;
                case GET_DEVICES_SUCCESS:
                    reDoTime = 0;
                    // 检查是否收到手机注册帧回应
                    reSign();
                    break;
                case GET_DEVICES_FAIL:
                    // 重查设备列表
                    getDeviceList();
                    break;
                case GET_DEVICES_ERROR:
                    if (reDoTime < 3) {
                        getDeviceList();
                        reDoTime++;
                    } else {
                        reDoTime = 0;
                        dialog.dismiss();
                        // 查询设备列表异常
                        //showInfo(fail);
                        ToastUtil.showToast(LoginActivity.this,fail);
                    }
                    break;
                case RE_LOGIN:
                    dialog.dismiss();
                    showInfo(fail);
                    break;
                case LOGIN_IN:
                    dialog.dismiss();
                    editor.putString("account", name);
                    editor.putString("code", code);
                    editor.commit();
                    // 登录验证成功
                    Intent main = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(main);
                    finish();
                    break;
                case SEND_REGFRAME:
                    dialog.dismiss();
                    //showInfo("无法连接服务器，请重试");
                    Log.i("SEND_REGFRAME", "SEND_REGFRAME");
                    ToastUtil.showToast(LoginActivity.this,"无法连接服务器，请重试!");
                    reDoTime = 0;
                    break;
                case RE_REG_FRAME:
                    dialog.dismiss();
                    //showInfo("无法连接服务器，请重试");
                    Log.i("RE_REG_FRAME", "RE_REG_FRAME");
                    ToastUtil.showToast(LoginActivity.this,"无法连接服务器，请重试!");
                    reDoTime = 0;
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };

    /**
     * 方法名称：	getInputContext
     * 方法描述：	判断输入框是否为空
     * 参数：			@return
     * 返回值类型：	boolean
     * 创建时间：	2015-1-20上午11:31:49
     */
    private boolean getInputContext() {
//		if (!etName.getText().toString().isEmpty()
//				&& !etCode.getText().toString().isEmpty()) {
//			return true;
//		}
//		showInfo("用户名和密码不能为空!");
//		return false;
        if(etName.getText().toString().isEmpty()){
            if(etCode.getText().toString().isEmpty()){
                //showInfo("用户名和密码不能为空!");
                ToastUtil.showToast(LoginActivity.this,"用户名和密码不能为空!");
                return false;
            }
        }
        if (etCode.getText().toString().isEmpty()) {
            //showInfo("请输入密码!");
            ToastUtil.showToast(LoginActivity.this,"请输入密码!");
            return false;
        } else {
            return true;
        }


    }

    /**
     * 方法名称：	getPropertyInfos
     * 方法描述：	生成http通信的方法参数集
     * 参数：			@return
     * 返回值类型：	List<PropertyInfo>
     * 创建时间：	2015-1-20上午11:45:35
     */
    private List<PropertyInfo> getPropertyInfos(String tag) {
        List<PropertyInfo> loginInfos = new ArrayList<PropertyInfo>();

        if (tag.equals("Login")) {
            PropertyInfo nameInfo = new PropertyInfo();
            nameInfo.setName(NetElectricsConst.USER_NAME);
            nameInfo.setValue(user.getPhone());

            PropertyInfo codeInfo = new PropertyInfo();
            codeInfo.setName(NetElectricsConst.USER_CODE);
            codeInfo.setValue(user.getPassword());

            PropertyInfo token = new PropertyInfo();
            token.setName(NetElectricsConst.TOKEN);
            token.setValue("");

            loginInfos.add(nameInfo);
            loginInfos.add(codeInfo);
            loginInfos.add(token);
        }
        if (tag.equals("devices")) {
            PropertyInfo nameInfo = new PropertyInfo();
            nameInfo.setName(NetElectricsConst.USER_NAME);
            nameInfo.setValue(user.getPhone());

            PropertyInfo token = new PropertyInfo();
            token.setName(NetElectricsConst.TOKEN);
            token.setValue("");

            loginInfos.add(nameInfo);
            loginInfos.add(token);
        }
        return loginInfos;
    }

    /*
     * 方法名称：	onActivityResult
     * 方法描述：	TODO
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case SIGN_UP:
                if (resultCode == RESULT_OK) {
                    etName.setText(data.getStringExtra("id"));
                    etCode.setText(data.getStringExtra("code"));
                    showInfo("注册成功，请登录");
                }
                break;
            default:
                break;
        }
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction()
                    .equals(NetElectricsConst.ACTION_PHONE_SIGNUP)) {
                // 收到手机注册帧的返回帧
                if (intent.getByteArrayExtra("data") != null) {
                    isSignSuccess = true;
                    Message message = handler.obtainMessage();
                    message.what = LOGIN_IN;
                    handler.sendMessage(message);
                }
            } else {
                if (intent.getAction().equals(
                        NetElectricsConst.ACTION_DATA_FAILED)) {
                    isNetSuccess = false;
                    // dialog.dismiss();
                    // showInfo("无法连接服务器，请重试");
                    reConnectServer();
                } else {
                    if (intent.getAction().equals(
                            NetElectricsConst.ACTION_DATA_ERROR)) {
                        isNetSuccess = false;
                        reConnectServer();
                    }
                }
            }
        }
    };

    /**
     * 方法名称：	reSign
     * 方法描述：	在网路通信正常的情况下，重新注册
     * 参数：
     * 返回值类型：	void
     * 创建时间：	2015-5-18下午3:12:40
     */
    private void reSign() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!isSignSuccess && isNetSuccess) {
                    if (reDoTime < 3) {
                        NetCommunicationUtil.phoneSignUp(user.getPhone(),
                                NetElectricsConst.PhoneSignUp);
                        reDoTime++;
                        reSign();
                    } else {
                        Message msg = new Message();
                        msg.what = RE_REG_FRAME;
                        handler.sendMessage(msg);
                    }
                } else {
                    reDoTime = 0;
                }
            }
        }, 2000);
    }

    /**
     * 方法名称：	reSignPhone
     * 方法描述：	重新向服务器注册账号,表示在线
     * 参数：
     * 返回值类型：	void
     * 创建时间：	2015-5-18下午3:12:40
     */
    private void reSignPhone() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!isSignSuccess) {
                    if (reDoTime < 3) {
                        NetCommunicationUtil.phoneSignUp(user.getPhone(),
                                NetElectricsConst.PhoneSignUp);
                        reDoTime++;
                        reSignPhone();
                        Log.i("resignphone","reSignPhone()");
                    } else {
                        Message msg = new Message();
                        msg.what = SEND_REGFRAME;
                        handler.sendMessage(msg);
                    }
                } else {
                    reDoTime = 0;
                }
            }
        }, 2000);
    }

    /**
     * 方法名称：	reConnectServer
     * 方法描述：	重新和服务器建立通信连接
     * 参数：
     * 返回值类型：	void
     * 创建时间：	2015-5-18下午3:09:58
     */
    private void reConnectServer() {
        if (NetCommunicationUtil.getNetConnectState()) {
            NetCommunicationUtil.buildListener();
            reSignPhone();
        } else {
            dialog.dismiss();
            //showInfo("请确保网络连接");
            ToastUtil.showToast(LoginActivity.this, "请确保网络连接");
        }
    }
    @Override
    protected void onDestroy() {
        unregisterReceiver(broadcastReceiver);
        if(dialog != null && dialog.isShowing()){
            dialog.dismiss();
        }
        super.onDestroy();
    }
}
