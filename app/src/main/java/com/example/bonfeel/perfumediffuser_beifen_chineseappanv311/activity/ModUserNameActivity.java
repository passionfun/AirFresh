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

/**
 *
 * Author:FunFun
 * Function:修改用户名称
 * Date:2016/4/12 11:38
 */
public class ModUserNameActivity extends BaseActivity
{
    private TextView tvTitle;
    private Button btnConfirm;
    private EditText etUserName;

    private String title;
    private Bundle bundle;
    private String newName;

    private final int MOD_NAME_SUCCESS = 1;
    private final int MOD_NAME_FAIL = 2;
    private final int MOD_NAME_ERROR = 3;

    private Dialog dialog;

    private int reModefy;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        setContentView(R.layout.activity_mod_user_name);
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
        title = "修改用户名";
        bundle = getIntent().getBundleExtra("userInfo");
        reModefy = 0;
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
        btnConfirm = (Button) findViewById(R.id.btn_uname);
        etUserName = (EditText)findViewById(R.id.et_new_username);


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
    protected void initialHandler()
    {
        btnConfirm.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if ( isNameInput() )
                {
                    dialog = ApplicationUtil.getLoadingDialog(ModUserNameActivity.this, "正在修改...");
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.show();
                    updateName();
                }
            }
        });
    }
    /**
     * 方法名称：	isNameInput
     * 方法描述：	判断输入框是否为空
     * 参数：			@return
     * 返回值类型：	boolean
     * 创建时间：	2015-3-18下午4:44:23
     */
    private boolean isNameInput()
    {
        if ( etUserName.getText().toString().trim().length() != 0 )
        {
            newName = etUserName.getText().toString().trim();
            return true;
        }
        else
        {
            showInfo("请输入新的用户名");
            return false;
        }
    }
    /**
     * 方法名称：	updateName
     * 方法描述：	更新用户名
     * 参数：
     * 返回值类型：	void
     * 创建时间：	2015-3-18下午4:46:15
     */
    private void updateName()
    {
        NetCommunicationUtil.httpConnect(
                getNewUserInfos(),
                NetElectricsConst.METHOD_SET_COUNT,
                NetElectricsConst.STYLE_RES,
                new NetCommunicationUtil.HttpCallbackListener() {
                    @Override
                    public void onFinish(Bundle bundle) {
                        Message message = handler.obtainMessage();
                        if (bundle.getInt("resCode") == 1) {
                            message.what = MOD_NAME_SUCCESS;
                        } else {
                            message.what = MOD_NAME_FAIL;
                        }
                        handler.sendMessage(message);
                    }

                    @Override
                    public void onError(Bundle bundle) {
                        Message message = handler.obtainMessage();
                        message.what = MOD_NAME_ERROR;
                        handler.sendMessage(message);
                    }
                });
    }
    /**
     * 方法名称：	getNewUserInfos
     * 方法描述：	获取http参数集
     * 参数：			@return
     * 返回值类型：	List<PropertyInfo>
     * 创建时间：	2015-3-18下午4:59:24
     */
    private List<PropertyInfo> getNewUserInfos()
    {
        List<PropertyInfo> infos = new ArrayList<PropertyInfo>();

        PropertyInfo phone = new PropertyInfo();
        phone.setName(NetElectricsConst.USER_NAME);
        phone.setValue(((User) UserManager.getInstance().get(0)).getPhone());

        PropertyInfo name = new PropertyInfo();
        name.setName(NetElectricsConst.NAME);
        name.setValue(newName);

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
        mail.setValue(bundle.get("mail"));

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
                case MOD_NAME_SUCCESS:
                    dialog.dismiss();
                    showInfo("修改成功");
                    setResult(RESULT_OK);
                    finish();
                    break;
                case MOD_NAME_FAIL:
                    if ( reModefy < 2)
                    {
                        updateName();
                        reModefy++;
                    }
                    else
                    {
                        showInfo("修改失败，请稍后重试");
                        reModefy = 0;
                    }
                    break;
                case MOD_NAME_ERROR:
                    dialog.dismiss();
                    showInfo("程序异常，请稍后重试");
                    break;
                default:
                    break;
            }
        }

    };
}
