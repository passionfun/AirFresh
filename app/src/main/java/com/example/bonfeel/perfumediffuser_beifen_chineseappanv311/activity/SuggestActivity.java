package com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.view.View.OnClickListener;

import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.R;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.custom.SuggestAdapter;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.model.ChatMsg;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.model.NetElectricsConst;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.model.User;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.util.NetCommunicationUtil;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.util.UserManager;

import org.ksoap2.serialization.PropertyInfo;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Author:FunFun
 * Function:意见反馈
 * Date:2016/4/12 13:38
 */
public class SuggestActivity extends BaseActivity
{
    private TextView tvTitle;
    private ListView lvSuggest;
    private Button btnSend;
    private EditText etMessage;

    private String title;
    private List<ChatMsg> listChatMsgs;
    private SuggestAdapter adapter;
    String msg;

    private final int SEND_SUCCESS = 1;
    private final int SEND_FAIL = 2;
    private final int SEND_ERROR = 3;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        setContentView(R.layout.activity_suggest);
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
        title = "意见反馈";

        msg = "";

        listChatMsgs = new ArrayList<ChatMsg>();
        ChatMsg serverMsg = new ChatMsg();
        serverMsg.setWho(0);
        serverMsg.setMsg("欢迎给我们提出意见和建议，我们会根据您的反馈改善产品使用体验。感谢您对百芬的支持");
        listChatMsgs.add(serverMsg);
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
        lvSuggest = (ListView)findViewById(R.id.lv_suggest_talk);
        etMessage = (EditText)findViewById(R.id.et_message);
        btnSend = (Button)findViewById(R.id.btn_send);

        adapter = new SuggestAdapter(this, R.layout.suggest_item, listChatMsgs);
        lvSuggest.setAdapter(adapter);

        tvTitle.setText(title);
    }

    /* (非Javadoc)
     * 方法名称：	initialHandler
     * 方法描述：	TODO
     * 重写部分：	@see com.example.bonfeel.activity.BaseActivity#initialHandler()
     */
    @Override
    protected void initialHandler()
    {
        btnSend.setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                if ( checkInput() )
                {
                    sendMessage();
                }
            }
        });
    }
    /**
     * 方法名称：	checkInput
     * 方法描述：	判空
     * 参数：			@return
     * 返回值类型：	boolean
     * 创建时间：	2015-3-21下午5:37:10
     */
    private boolean checkInput()
    {
        if ( etMessage.getText().toString().trim().length() == 0 )
        {
            showInfo("请输入要反馈的建议");
            return false;
        }
        else
        {
            return true;
        }
    }
    /**
     * 方法名称：	sendMessage
     * 方法描述：	将意见反馈给服务器
     * 参数：
     * 返回值类型：	void
     * 创建时间：	2015-3-21下午5:39:24
     */
    private void sendMessage()
    {
        //先将意见显示在聊天框中
        msg = etMessage.getText().toString().trim();
        ChatMsg userMsg = new ChatMsg();
        userMsg.setWho(1);
        userMsg.setMsg(msg);
        listChatMsgs.add(userMsg);
        adapter.notifyDataSetChanged();
        etMessage.setText("");
        //再将意见上传到服务器
        NetCommunicationUtil.httpConnect(
                getSuggestInfos(),
                NetElectricsConst.METHOD_SUB_SERVICE,
                NetElectricsConst.STYLE_RES,
                new NetCommunicationUtil.HttpCallbackListener() {
                    @Override
                    public void onFinish(Bundle bundle) {
                        Message message = handler.obtainMessage();
                        if (bundle.getInt("resCode") == 1) {
                            message.what = SEND_SUCCESS;
                        } else {
                            message.what = SEND_FAIL;
                        }
                        handler.sendMessage(message);
                    }

                    @Override
                    public void onError(Bundle bundle) {
                        Message message = handler.obtainMessage();
                        message.what = SEND_ERROR;
                        handler.sendMessage(message);
                    }
                });
    }
    /**
     * 方法名称：	getSuggestInfos
     * 方法描述：	获取参数集
     * 参数：			@return
     * 返回值类型：	List<PropertyInfo>
     * 创建时间：	2015-3-21下午5:53:55
     */
    private List<PropertyInfo> getSuggestInfos()
    {
        List<PropertyInfo> infos = new ArrayList<PropertyInfo>();

        PropertyInfo userName = new PropertyInfo();
        userName.setName(NetElectricsConst.USER_NAME);
        userName.setValue(((User) UserManager.getInstance().get(0)).getPhone());

        PropertyInfo theme = new PropertyInfo();
        theme.setName(NetElectricsConst.TITLE);
        theme.setValue("");

        PropertyInfo content = new PropertyInfo();
        content.setName(NetElectricsConst.CONTENT);
        content.setValue(msg);

        PropertyInfo image = new PropertyInfo();
        image.setName(NetElectricsConst.IMAGE);
        image.setValue(0);

        PropertyInfo tel = new PropertyInfo();
        tel.setName(NetElectricsConst.TELEPHONE);
        tel.setValue("");

        PropertyInfo addr = new PropertyInfo();
        addr.setName(NetElectricsConst.ADDR);
        addr.setValue("");

        PropertyInfo type = new PropertyInfo();
        type.setName(NetElectricsConst.TYPE);
        type.setValue("1");

        infos.add(userName);
        infos.add(theme);
        infos.add(content);
        infos.add(image);
        infos.add(tel);
        infos.add(addr);
        infos.add(type);

        return infos;
    }
    /**
     * 方法名称：	getResponse
     * 方法描述：	服务器收到意见之后的反馈
     * 参数：
     * 返回值类型：	void
     * 创建时间：	2015-3-21下午6:09:14
     */
    private void getResponse()
    {
        ChatMsg serverMsg = new ChatMsg();
        serverMsg.setWho(0);
        serverMsg.setMsg("感谢您对百芬的支持，我们会依据您的反馈，尽快完善相关功能。");
        listChatMsgs.add(serverMsg);
        adapter.notifyDataSetChanged();
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
                case SEND_SUCCESS:
                    getResponse();
                    break;
                case SEND_FAIL:
                    showInfo("发送失败，请重试");
                    break;
                case SEND_ERROR:
                    showInfo("程序异常，请稍后重试");
                    break;
                default:
                    break;
            }
        }
    };
}
