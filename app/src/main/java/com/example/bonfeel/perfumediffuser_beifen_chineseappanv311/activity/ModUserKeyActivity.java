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
 * Function:
 * Date:2016/4/12 11:37
 */
public class ModUserKeyActivity extends BaseActivity
{
    private TextView tvTitle;
    private Button btnConfirm;
    private EditText etOldKey;
    private EditText etNewKey;
    private EditText etConfirmKey;

    private String title;
    private String oldKey;
    private String newKey;

    private Dialog dialog;

    private final int CHANGE_SUCCESS = 1;
    private final int CHANGE_FAIL = 2;
    private final int CHANGE_ERROR = 3;

    private int reModefy;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        setContentView(R.layout.activity_mod_user_key);
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
        title = "修改密码";
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
        btnConfirm = (Button)findViewById(R.id.btn_upassword);
        etOldKey = (EditText)findViewById(R.id.et_old_key);
        etNewKey = (EditText)findViewById(R.id.et_new_key);
        etConfirmKey = (EditText)findViewById(R.id.et_confirm_key);

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
                if ( isKeyInput() )
                {
                    dialog = ApplicationUtil.getLoadingDialog(ModUserKeyActivity.this, "正在修改...");
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.show();
                    newKey = etNewKey.getText().toString().trim();
                    updateKey();

                }
            }
        });
    }
    /**
     * 方法名称：	isKeyInput
     * 方法描述：	判空
     * 参数：			@return
     * 返回值类型：	boolean
     * 创建时间：	2015-3-18下午7:56:24
     */
    private boolean isKeyInput()
    {
        if ( etOldKey.getText().toString().length() != 0
                && etNewKey.getText().toString().length() != 0
                && etConfirmKey.getText().toString().length() != 0)
        {
            return checkCorrect();
        }
        else
        {
            showInfo("请将信息填写完整");
            return false;
        }
    }
    private boolean checkCorrect()
    {
        //先判断旧密码是否正确
        if ( etOldKey.getText().toString().equals(((User) UserManager.getInstance().get(0)).getPassword()) )
        {
            return checkNewCode();
        }
        else
        {
            showInfo("旧密码输入有误");
            return false;
        }
    }
    /**
     * 方法名称：	checkNewCode
     * 方法描述：	验证新密码
     * 参数：			@return
     * 返回值类型：	boolean
     * 创建时间：	2015-3-31下午9:30:51
     */
    private boolean checkNewCode()
    {
        //密码只限数字和大小写字母48-57, 65-90, 97-122
        if (etNewKey.getText().toString().length() == etConfirmKey.getText().toString().length()
                && etNewKey.getText().toString().equals(etConfirmKey.getText().toString()))
        {
            boolean flag = true;
            for (int i = 0; i < etNewKey.getText().toString().length(); i++)
            {
                char index = etNewKey.getText().toString().charAt(i);
                if (index > 96 && index < 123)
                {}
                else
                {
                    if (index > 64 && index < 91)
                    {}
                    else
                    {
                        if (index > 47 && index < 58)
                        {}
                        else
                        {
                            flag = false;
                            break;
                        }
                    }
                }
            }
            if (!flag)
            {
                showInfo("请不要输入特殊字符");
            }
            else
            {
                if ( etNewKey.getText().toString().length() < 6 )
                {
                    showInfo("密码长度至少六位，请重试");
                    flag = false;
                }
            }
            return flag;
        }
        else
        {
            showInfo("密码不一致，请确认");
            return false;
        }
    }
    /**
     * 方法名称：	updateKey
     * 方法描述：	修改密码
     * 参数：
     * 返回值类型：	void
     * 创建时间：	2015-3-18下午7:57:15
     */
    private void updateKey()
    {
        NetCommunicationUtil.httpConnect(
                getNewKeyInfos(),
                NetElectricsConst.METHOD_CHANGE_PASS,
                NetElectricsConst.STYLE_RES,
                new NetCommunicationUtil.HttpCallbackListener() {
                    @Override
                    public void onFinish(Bundle bundle) {
                        Message message = handler.obtainMessage();
                        if (bundle.getInt("resCode") == 1) {
                            message.what = CHANGE_SUCCESS;
                        } else {
                            message.what = CHANGE_FAIL;
                        }
                        handler.sendMessage(message);
                    }

                    @Override
                    public void onError(Bundle bundle) {
                        Message message = handler.obtainMessage();
                        message.what = CHANGE_ERROR;
                        handler.sendMessage(message);
                    }
                });
    }
    /**
     * 方法名称：	getNewKeyInfos
     * 方法描述：	参数集
     * 参数：			@return
     * 返回值类型：	List<PropertyInfo>
     * 创建时间：	2015-3-18下午7:58:08
     */
    private List<PropertyInfo> getNewKeyInfos()
    {
        List<PropertyInfo> infos = new ArrayList<PropertyInfo>();

        PropertyInfo phone = new PropertyInfo();
        phone.setName(NetElectricsConst.USER_NAME);
        phone.setValue(((User) UserManager.getInstance().get(0)).getPhone());

        PropertyInfo key = new PropertyInfo();
        key.setName(NetElectricsConst.OLD_CODE);
        key.setValue(((User) UserManager.getInstance().get(0)).getPassword());

        PropertyInfo setKey = new PropertyInfo();
        setKey.setName(NetElectricsConst.NEW_CODE);
        setKey.setValue(newKey);

        PropertyInfo token = new PropertyInfo();
        token.setName(NetElectricsConst.TOKEN);
        token.setValue("");

        infos.add(phone);
        infos.add(key);
        infos.add(setKey);
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
                case CHANGE_SUCCESS:
                    dialog.dismiss();
                    showInfo("修改成功");
                    //fun add
                    ((User) UserManager.getInstance().get(0)).setPassword(etNewKey.getText().toString());
                    setResult(RESULT_OK);
                    finish();
                    break;
                case CHANGE_FAIL:
                    if ( reModefy < 2)
                    {
                        updateKey();
                        reModefy++;
                    }
                    else
                    {
                        showInfo("修改失败，请稍后重试");
                        reModefy = 0;
                    }
                    break;
                case CHANGE_ERROR:
                    dialog.dismiss();
                    showInfo("程序异常，请稍后重试");
                    break;
            }
        }
    };
}
