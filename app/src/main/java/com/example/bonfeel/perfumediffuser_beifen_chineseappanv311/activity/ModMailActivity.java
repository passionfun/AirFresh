package com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.view.View.OnClickListener;

import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.R;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.model.NetElectricsConst;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.model.User;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.util.ApplicationUtil;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.util.NetCommunicationUtil;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.util.UserManager;

import org.ksoap2.serialization.PropertyInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * Author:FunFun
 * Function:
 * Date:2016/4/12 11:35
 */
public class ModMailActivity extends BaseActivity {
    private TextView tvTitle;
    private Button btnConfirm;
    private EditText etEmail;

    private String title;
    private Bundle bundle;
    private String newEmail;

    private Dialog dialog;

    private final int MOD_MAIL_SUCCESS = 1;
    private final int MOD_MAIL_FAIL = 2;
    private final int MOD_MAIL_ERROR = 3;

    private int reModefy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_mod_mail);
        super.onCreate(savedInstanceState);
    }

    /* (非Javadoc)
     * 方法名称：	initialData
     * 方法描述：	TODO
     * 重写部分：	@see com.example.bonfeel.activity.BaseActivity#initialData()
     */
    @Override
    protected void initialData() {
        title = "修改邮箱";
        bundle = getIntent().getBundleExtra("userInfo");
        reModefy = 0;
    }

    /* (非Javadoc)
     * 方法名称：	initialView
     * 方法描述：	TODO
     * 重写部分：	@see com.example.bonfeel.activity.BaseActivity#initialView()
     */
    @Override
    protected void initialView() {
        tvTitle = (TextView) findViewById(R.id.tv_title);
        btnConfirm = (Button) findViewById(R.id.btn_uemail);
        etEmail = (EditText) findViewById(R.id.et_new_mail);

        tvTitle.setText(title);
//		btnOk.setVisibility(View.VISIBLE);
//		btnOk.setText("");
//		btnOk.setBackgroundResource(R.drawable.button_finish);
    }

    /* (非Javadoc)
     * 方法名称：	initialHandler
     * 方法描述：	TODO
     * 重写部分：	@see com.example.bonfeel.activity.BaseActivity#initialHandler()
     */
    @Override
    protected void initialHandler() {
        btnConfirm.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (isEmailInput()) {
                    dialog = ApplicationUtil.getLoadingDialog(
                            ModMailActivity.this, "正在修改...");
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.show();
                    newEmail = etEmail.getText().toString();
                    updateEmail();
                }
            }
        });
    }

    /**
     * 方法名称：	isEmailInput
     * 方法描述：	判空操作
     * 参数：			@return
     * 返回值类型：	boolean
     * 创建时间：	2015-3-18下午7:09:45
     */
    private boolean isEmailInput() {
        if (etEmail.getText().toString().length() != 0) {
            return isEmailFormat();
        } else {
            showInfo("请输入新的邮箱");
            return false;
        }
    }

    /**
     * 方法名称：	isEmailFormat
     * 方法描述：	判断是否符合电子邮箱格式
     * 参数：			@return
     * 返回值类型：	boolean
     * 创建时间：	2015-3-31下午8:00:49
     */
    private boolean isEmailFormat() {
        String email = etEmail.getText().toString();
        String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(email);
        if (!m.matches()) {
            showInfo("请确保邮箱格式正确");
        }
        // 除去腾讯邮箱
        if (email.endsWith("@qq.com")) {
            showInfo("暂不支持腾讯邮箱,修改邮箱失败!");
            return false;
        }
        return m.matches();
    }

    /**
     * 方法名称：	updateEmail
     * 方法描述：	更新邮箱
     * 参数：
     * 返回值类型：	void
     * 创建时间：	2015-3-18下午7:12:37
     */
    private void updateEmail() {
        NetCommunicationUtil.httpConnect(getNewEmailInfos(),
                NetElectricsConst.METHOD_SET_COUNT,
                NetElectricsConst.STYLE_RES, new NetCommunicationUtil.HttpCallbackListener() {
                    @Override
                    public void onFinish(Bundle bundle) {
                        Message message = handler.obtainMessage();
                        if (bundle.getInt("resCode") == 1) {
                            message.what = MOD_MAIL_SUCCESS;
                        } else {
                            message.what = MOD_MAIL_FAIL;
                        }
                        handler.sendMessage(message);
                    }

                    @Override
                    public void onError(Bundle bundle) {
                        Message message = handler.obtainMessage();
                        message.what = MOD_MAIL_ERROR;
                        message.obj = bundle.getString("message");
                        handler.sendMessage(message);
                    }
                });
    }

    /**
     * 方法名称：	getNewEmailInfos
     * 方法描述：	获取http参数集
     * 参数：			@return
     * 返回值类型：	List<PropertyInfo>
     * 创建时间：	2015-3-18下午7:18:04
     */
    private List<PropertyInfo> getNewEmailInfos() {
        List<PropertyInfo> infos = new ArrayList<PropertyInfo>();

        PropertyInfo phone = new PropertyInfo();
        phone.setName(NetElectricsConst.USER_NAME);
        phone.setValue(((User) UserManager.getInstance().get(0)).getPhone());

        PropertyInfo name = new PropertyInfo();
        name.setName(NetElectricsConst.NAME);
        name.setValue(bundle.get("name"));

        PropertyInfo areaid = new PropertyInfo();
        areaid.setName(NetElectricsConst.AREA_ID);
        areaid.setValue(bundle.get("areaid"));

        PropertyInfo areaname = new PropertyInfo();
        areaname.setName(NetElectricsConst.AREA_NAME);
        areaname.setValue(bundle.get("areaname"));

        PropertyInfo address = new PropertyInfo();
        address.setName(NetElectricsConst.ADDRESS);
        address.setValue(bundle.get("commit_addr"));

        PropertyInfo mail = new PropertyInfo();
        mail.setName(NetElectricsConst.MAIL_BOX);
        mail.setValue(newEmail);

        PropertyInfo mobilephone = new PropertyInfo();
        mobilephone.setName(NetElectricsConst.MOBILE);
        mobilephone.setValue(bundle.get("mobilephone"));

        PropertyInfo qq = new PropertyInfo();
        qq.setName(NetElectricsConst.QQ);
        qq.setValue(bundle.get("qq"));

        PropertyInfo weixin = new PropertyInfo();
        weixin.setName(NetElectricsConst.WEIXIN);
        weixin.setValue(bundle.get("weixin"));

        PropertyInfo image = new PropertyInfo();
        image.setName(NetElectricsConst.IMAGE);
        image.setValue("");

        PropertyInfo token = new PropertyInfo();
        token.setName(NetElectricsConst.TOKEN);
        token.setValue("");

        infos.add(phone);
        infos.add(name);
        infos.add(areaid);
        infos.add(areaname);
        infos.add(address);
        infos.add(mail);
        infos.add(mobilephone);
        infos.add(qq);
        infos.add(weixin);
        infos.add(image);
        infos.add(token);

        return infos;
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
                case MOD_MAIL_SUCCESS:
                    dialog.dismiss();
                    showInfo("修改成功");
                    setResult(RESULT_OK);
                    finish();
                    break;
                case MOD_MAIL_FAIL:
                    if (reModefy < 2) {
                        updateEmail();
                        reModefy++;
                    } else {
                        showInfo("修改失败，请稍后重试");
                        reModefy = 0;
                    }
                    break;
                case MOD_MAIL_ERROR:
                    dialog.dismiss();
                    showInfo("程序异常，请稍后重试" + msg.obj);
                    // LogUtil.logDebug("mod_mail", msg.obj+"");
                    break;
                default:
                    break;
            }
        }

    };
}
