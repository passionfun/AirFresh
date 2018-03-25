package com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.R;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.model.NetElectricsConst;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.util.ApplicationUtil;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.util.NetCommunicationUtil;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.util.ToastUtil;

import org.ksoap2.serialization.PropertyInfo;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Author:FunFun
 * Function:忘记密码时的找回密码操作
 * Date:2016/4/12 11:18
 */
public class FindKeyActivity extends BaseActivity {
    private TextView tvTitle;
    private EditText et_sendCode;
    private Button btn_sendCode;
    private Dialog dialog;
    String title;
    private String tel;
    private final int FINDPWD_FAIL = 0;
    private final int FINDPWD_SUCCESS = 1;
    private final int FINDPWD_ERROR = 2;
    /**
     * 处理用户存在和错误的handler
     */
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            dialog.dismiss();
            switch (msg.what) {
                case FINDPWD_SUCCESS:
                    Toast.makeText(ApplicationUtil.getContext(),
                            "修改密码链接已发送到你的邮箱，请登录邮箱及时更改密码!", Toast.LENGTH_LONG)
                            .show();
                    finish();
                    break;
                case FINDPWD_FAIL:
                    showInfo("用户不存在或者未设置E-mail!");
                    break;
                case FINDPWD_ERROR:
                    showInfo("发送异常,请重试");
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_find_key);
        super.onCreate(savedInstanceState);

    }

    /* (非Javadoc)
     * 方法名称：	initialView
     * 方法描述：	TODO
     * 重写部分：	@see com.example.netelectricsui.activity.BaseActivity#initialView()
     */
    @Override
    protected void initialView() {
        tvTitle = (TextView) findViewById(R.id.tv_title);
        et_sendCode = (EditText) findViewById(R.id.et_phone_number);
        btn_sendCode = (Button) findViewById(R.id.btn_send_code);
        tvTitle.setText(title);
    }

    /* (非Javadoc)
     * 方法名称：	initialData
     * 方法描述：	TODO
     * 重写部分：	@see com.example.netelectricsui.activity.BaseActivity#initialData()
     */
    @Override
    protected void initialData() {
        title = "找回密码";
    }

    /* (非Javadoc)
     * 方法名称：	initialHandler
     * 方法描述：	TODO
     * 重写部分：	@see com.example.netelectricsui.activity.BaseActivity#initialHandler()
     */
    @Override
    protected void initialHandler() {
        btn_sendCode.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                tel = et_sendCode.getText().toString().trim();
                dialog = ApplicationUtil.getLoadingDialog(FindKeyActivity.this,
                        "正在发送验证码到你邮箱，请稍后...");
                dialog.show();
                if (TextUtils.isEmpty(tel)) {
                    Toast.makeText(FindKeyActivity.this, "手机号码不能为空!",
                            Toast.LENGTH_LONG).show();
                    return;
                } else {
                    // 检查网络连接
                    if (NetCommunicationUtil.getNetConnectState()) {
                        sendMessageToMail();
                    } else {
                        dialog.dismiss();
                        //showInfo("请确保网络正确连接");
                        ToastUtil.showToast(FindKeyActivity.this, "请确保网络正确连接");
                    }

                }

            }
        });

    }

    protected void sendMessageToMail() {
        NetCommunicationUtil.httpConnect(getPropertyInfos(),
                NetElectricsConst.METHOD_FINDPASSWORD,
                NetElectricsConst.STYLE_RES, new NetCommunicationUtil.HttpCallbackListener() {

                    @Override
                    public void onFinish(Bundle bundle) {
                        Message message = handler.obtainMessage();
                        if (bundle.getInt("resCode") == 1) {
                            message.what = FINDPWD_SUCCESS;
                        } else {
                            message.what = FINDPWD_FAIL;
                        }
                        handler.sendMessage(message);
                    }

                    @Override
                    public void onError(Bundle bundle) {
                        Message message = handler.obtainMessage();
                        message.what = FINDPWD_ERROR;
                        handler.sendMessage(message);
                    }
                });
    }

    private List<PropertyInfo> getPropertyInfos() {
        List<PropertyInfo> infos = new ArrayList<PropertyInfo>();

        PropertyInfo nameInfo = new PropertyInfo();
        nameInfo.setName(NetElectricsConst.USER_NAME);
        nameInfo.setValue(tel);

        PropertyInfo token = new PropertyInfo();
        token.setName(NetElectricsConst.TOKEN);
        token.setValue("");

        infos.add(nameInfo);
        infos.add(token);

        return infos;
    }
}
