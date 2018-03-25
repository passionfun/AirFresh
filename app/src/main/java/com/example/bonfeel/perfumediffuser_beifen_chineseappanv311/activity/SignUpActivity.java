package com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.R;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.model.NetElectricsConst;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.util.ApplicationUtil;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.util.NetCommunicationUtil;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.util.ToastUtil;

import org.ksoap2.serialization.PropertyInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * Author:FunFun
 * Function:注册新用户
 * Date:2016/4/12 12:01
 */
public class SignUpActivity extends BaseActivity {
    private TextView tvTitle;
    private EditText etUserName;
    private EditText etEmail;
    private EditText etUserCode;
    private EditText etCodeConfirm;
    private Button btnSignUp;

    private String userName;
    private String email;
    private String userCode;
    private String phoneMac;
    private String title;

    private final int SINGUP_FAIL = 0;
    private final int SINGUP_SUCCESS = 1;
    private final int SINGUP_ERROR = 2;

    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_sign_up);
        super.onCreate(savedInstanceState);
    }

    /*
     * 方法名称：	initialData
     * 方法描述：
     */
    @Override
    protected void initialData() {
        title = "用户注册";
    }

    /*
     * 方法名称：	initialView
     * 方法描述：
     */
    @Override
    protected void initialView() {
        tvTitle = (TextView) findViewById(R.id.tv_title);
        etUserName = (EditText) findViewById(R.id.et_count);
        etEmail = (EditText) findViewById(R.id.et_email);
        etUserCode = (EditText) findViewById(R.id.et_password);
        etCodeConfirm = (EditText) findViewById(R.id.et_psd_confirm);
        btnSignUp = (Button) findViewById(R.id.btn_confirm);
        tvTitle.setText(title);
    }

    /*
     * 方法名称：	initialHandler
     * 方法描述：
     */
    @Override
    protected void initialHandler() {
        btnSignUp.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkInput()) {
                    userCode = etUserCode.getText().toString().trim();
                    userName = etUserName.getText().toString().trim();
                    email = etEmail.getText().toString().trim();
                    dialog = ApplicationUtil.getLoadingDialog(SignUpActivity.this, "注册中...");
                    dialog.show();

                    // 检查网络连接
                    if (NetCommunicationUtil.getNetConnectState()) {
                        signUp();
                    } else {
                        dialog.dismiss();
                        //showInfo("请确保网络正确连接");
                        ToastUtil.showToast(SignUpActivity.this, "请确保网络正确连接");
                    }

                }
            }
        });
        //用户名输入框的监听事件
        etUserName.setOnFocusChangeListener(new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View arg0, boolean arg1) {
                if(!arg1){
                    if(etUserName.getText().toString().trim().length() != 0){
                        if(!checkUserCount(etUserName.getText().toString().trim())){
                            ToastUtil.showToast(SignUpActivity.this, "请输入正确的用户名!");
                            return;
                        }
                    }
                }

            }
        });
        //邮箱输入框的监听事件
        etEmail.setOnFocusChangeListener(new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View arg0, boolean arg1) {
                if(!arg1){
                    if(etEmail.getText().toString().trim().length() != 0){
                        if(!checkUserEmail(etEmail.getText().toString().trim())){
                            ToastUtil.showToast(SignUpActivity.this, "请输入正确的邮箱地址!");
                            return;
                        }
                    }
                }

            }
        });
        //输入密码框的监听事件
        etUserCode.setOnFocusChangeListener(new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View arg0, boolean arg1) {
                if(!arg1){
                    if(etUserCode.getText().toString().trim().length() != 0){
                        if(etUserCode.getText().toString().trim().length() < 6){
                            ToastUtil.showToast(SignUpActivity.this, "密码长度至少六位，请重试!");
                        }
                    }
                }

            }
        });


    }

    /************************************************
     * 方法名称：	signUp
     * 说明：		联网注册
     * 创建时间：	2015-3-29
     * 作者：		jkwen
     ***********************************************/
    private void signUp() {
        phoneMac = NetCommunicationUtil.getMobileMac();
        NetCommunicationUtil.httpConnect(getPropertyInfos(),
                NetElectricsConst.METHOD_REGIST, NetElectricsConst.STYLE_RES,
                new NetCommunicationUtil.HttpCallbackListener() {

                    @Override
                    public void onFinish(Bundle bundle) {
                        Message message = handler.obtainMessage();
                        if (bundle.getInt("resCode") == 1) {
                            message.what = SINGUP_SUCCESS;
                        } else {
                            message.what = SINGUP_FAIL;
                        }
                        handler.sendMessage(message);
                    }

                    @Override
                    public void onError(Bundle bundle) {
                        Message message = handler.obtainMessage();
                        message.what = SINGUP_ERROR;
                        handler.sendMessage(message);
                    }
                });
    }

    /************************************************
     * 方法名称：	checkInput
     * 说明：		判断是否输入完全
     * 创建时间：	2015-3-29
     * 作者：		jkwen
     ***********************************************/
    private boolean checkInput() {
        if (etUserName.getText().toString().length() != 0
                && etEmail.getText().length() != 0
                && etUserCode.getText().toString().length() != 0
                && etCodeConfirm.getText().toString().length() != 0) {
            return checkCorrect();
        } else {
            ToastUtil.showToast(SignUpActivity.this, "请将信息填写完整");
            //showInfo("请将信息填写完整");
            return false;
        }
    }

    /************************************************
     * 方法名称：	checkCorrect
     * 说明：		检查正确性
     * 创建时间：	2015-3-29
     * 作者：		jkwen
     ***********************************************/
    private boolean checkCorrect() {
//		if (checkUserCount(etUserName.getText().toString())
//				&& checkUserEmail(etEmail.getText().toString().trim())) {
//			// 除去腾讯qq邮箱
//			if (etEmail.getText().toString().endsWith("@qq.com")) {
//				showInfo("暂不支持腾讯邮箱!注册失败!");
//				return false;
//			}
//			return checkUserCode();
//		} else {
//			showInfo("请输入正确的手机号或邮箱地址!");
//			return false;
//		}

        if(checkUserCount(etUserName.getText().toString().trim())){
            if(checkUserEmail(etEmail.getText().toString().trim())){
//				if(etEmail.getText().toString().endsWith("@qq.com")){
//					showInfo("暂不支持腾讯邮箱!注册失败!");
//					return false;
//				}
                return checkUserCode();
            }else{
                showInfo("请输入正确的邮箱地址!");
                return false;
            }
        }else{
            showInfo("请输入正确的手机号!");
            return false;
        }
    }

    /**
     * 邮箱的合法性验证
     * @param email
     * @return
     */
    private boolean checkUserEmail(String email) {
        String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(email);
        return m.matches();
    }

    /**
     * 方法名称：	checkUserCode
     * 方法描述：	判断密码是否是数字或者大小写字母
     * 参数：			@return
     * 返回值类型：	boolean
     * 创建时间：	2015-4-2上午9:23:47
     */
    private boolean checkUserCode() {
        // 密码只限数字和大小写字母48-57, 65-90, 97-122
        if (etUserCode.getText().toString().length() == etCodeConfirm.getText()
                .toString().length()
                && etUserCode.getText().toString()
                .equals(etCodeConfirm.getText().toString())) {
            boolean flag = true;
            for (int i = 0; i < etUserCode.getText().toString().length(); i++) {
                char index = etUserCode.getText().toString().charAt(i);
                if (index > 96 && index < 123) {
                } else {
                    if (index > 64 && index < 91) {
                    } else {
                        if (index > 47 && index < 58) {
                        } else {
                            flag = false;
                            break;
                        }
                    }
                }
            }
            if (!flag) {
                showInfo("请不要输入特殊字符");
            } else {
                if (etUserCode.getText().toString().length() < 6) {
                    showInfo("密码长度至少六位，请重试");
                    flag = false;
                }
            }
            return flag;
        } else {
            showInfo("密码不一致，请确认");
            return false;
        }
    }

    /**
     * 处理用户存在和错误的handler
     */
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            dialog.dismiss();
            switch (msg.what) {
                case SINGUP_SUCCESS:
                    Intent intent = new Intent();
                    intent.putExtra("id", userName);
                    intent.putExtra("code", userCode);
                    setResult(RESULT_OK, intent);
                    finish();
                    break;
                case SINGUP_FAIL:
                    showInfo("用户已存在");
                    break;
                case SINGUP_ERROR:
                    //showInfo("网络异常，注册失败,请重试");
                    ToastUtil.showToast(SignUpActivity.this, "网络异常，注册失败,请重试!");
                    break;
            }
        }
    };

    /**
     * 方法名称：	checkUserCount
     * 方法描述：	判断是否是手机号
     * 参数：			@param count
     * 参数：			@return
     * 返回值类型：	boolean
     * 创建时间：	2015-1-23下午4:53:09
     */
    private boolean checkUserCount(String count) {
        Pattern pattern = Pattern.compile("1[358][0-9]{9}");
        Matcher matcher = pattern.matcher(count);
        return matcher.matches();
        // if (count.length() == 7 || count.length() == 10 || count.length() ==
        // 11) {
        // return true;
        // } else {
        // return false;
        // }
    }

    /**
     * 方法名称：	getPropertyInfos
     * 方法描述：	生成http通信的方法参数集
     * 参数：			@return
     * 返回值类型：	List<PropertyInfo>
     * 创建时间：	2015-1-23下午3:14:20
     */
    private List<PropertyInfo> getPropertyInfos() {
        List<PropertyInfo> infos = new ArrayList<PropertyInfo>();

        PropertyInfo nameInfo = new PropertyInfo();
        nameInfo.setName(NetElectricsConst.USER_NAME);
        nameInfo.setValue(userName);

        PropertyInfo codeInfo = new PropertyInfo();
        codeInfo.setName(NetElectricsConst.USER_CODE);
        codeInfo.setValue(userCode);

        PropertyInfo phoneMacInfo = new PropertyInfo();
        phoneMacInfo.setName(NetElectricsConst.PHONE_MAC);
        phoneMacInfo.setValue(phoneMac);

        PropertyInfo emailInfo = new PropertyInfo();
        emailInfo.setName(NetElectricsConst.EMAIL);
        emailInfo.setValue(email);

        PropertyInfo token = new PropertyInfo();
        token.setName(NetElectricsConst.TOKEN);
        token.setValue("");

        infos.add(nameInfo);
        infos.add(codeInfo);
        infos.add(emailInfo);
        infos.add(phoneMacInfo);
        infos.add(token);

        return infos;
    }
}
