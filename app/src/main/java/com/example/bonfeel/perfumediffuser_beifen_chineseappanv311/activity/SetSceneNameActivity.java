package com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.activity;

import android.app.Dialog;
import android.content.Intent;
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
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.model.Scene;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.util.ApplicationUtil;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.util.NetCommunicationUtil;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.util.SceneManager;

import org.ksoap2.serialization.PropertyInfo;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Author:FunFun
 * Function:设置场景名称
 * Date:2016/4/12 11:55
 */
public class SetSceneNameActivity extends BaseActivity
{
    private TextView tvTitle;
    private EditText etNewName;
    private boolean sameName = false;//区别场景名字是否相同的标志位
    //private Button btnOk;
    private Button btnConfirm;
    private List<Scene> msceneList ;

    private String title;
    private String name = "";

    private Scene scene;

    private final int MODIFY_SUCCESS = 1;
    private final int MODIFY_FAIL = 2;
    private final int MODIFY_ERROR = 3;

    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        setContentView(R.layout.activity_set_scene_name);
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
        title = "更改名称";
        name = getIntent().getStringExtra("name");
        scene = (Scene) SceneManager.getInstance().get(getIntent().getStringExtra("id"));
        dialog = ApplicationUtil.getLoadingDialog(this, "正在修改...");
        dialog.setCancelable(true);
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
        etNewName = (EditText)findViewById(R.id.et_new_scene_name);
        //btnOk = (Button)findViewById(R.id.btn_right);
        btnConfirm = (Button) findViewById(R.id.btn_uscene);

        tvTitle.setText(title);
        etNewName.setText(name);
        etNewName.setSelection(name.length());
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
                if ( checkInput() )
                {
                    dialog.show();
                    String sceneName = etNewName.getText().toString().trim();
                    msceneList = SceneManager.getInstance().getScenes();
                    sameName = false;
                    if(sceneName.equals(scene.getName())){
                        name = etNewName.getText().toString().trim();
                        modifyName();
                    }else{
                        for (int i = 0; i < msceneList.size(); i++) {
                            if (sceneName.equals(msceneList.get(i).getName())) {
                                sameName = true;
                                break;
                            }
                        }
                        if (!sameName) {
                            name = etNewName.getText().toString().trim();
                            modifyName();
                        } else {
                            dialog.dismiss();
                            showInfo("与已有的场景名称相同，请重试！");
                        }
                    }
                    //modifyName();
                }
            }
        });
    }
    private boolean checkInput()
    {
        if ( !etNewName.getText().toString().trim().equals(name) &&
                etNewName.getText().toString().trim().length() != 0)
        {
            return true;
        }
        else
        {
            if ( etNewName.getText().toString().trim().length() == 0 )
            {
                showInfo("请填写场景名称");
                return false;
            }
            else
            {
                showInfo("场景名称未更改");
                return false;
            }
        }
    }
    /**
     * 方法名称：	modifyName
     * 方法描述：	进行名称的修改
     * 参数：
     * 返回值类型：	void
     * 创建时间：	2015-4-16下午7:22:09
     */
    private void modifyName()
    {
        NetCommunicationUtil.httpConnect(
                getNewSceneInfos(),
                NetElectricsConst.METHOD_SCENE_NAME,
                NetElectricsConst.STYLE_RES,
                new NetCommunicationUtil.HttpCallbackListener() {
                    @Override
                    public void onFinish(Bundle bundle) {
                        Message message = handler.obtainMessage();
                        if (bundle.getInt("resCode") == 1) {
                            message.what = MODIFY_SUCCESS;
                        } else {
                            message.what = MODIFY_FAIL;
                        }
                        handler.sendMessage(message);
                    }

                    @Override
                    public void onError(Bundle bundle) {
                        Message message = handler.obtainMessage();
                        message.what = MODIFY_ERROR;
                        handler.sendMessage(message);
                    }
                });
    }
    private List<PropertyInfo> getNewSceneInfos()
    {
        List<PropertyInfo> infos = new ArrayList<PropertyInfo>();

        PropertyInfo sceneId = new PropertyInfo();
        sceneId.setName(NetElectricsConst.SCENE_ID);
        sceneId.setValue(scene.getId());

        PropertyInfo value = new PropertyInfo();
        value.setName(NetElectricsConst.SCENE_NAME);
        value.setValue(name);

        PropertyInfo token = new PropertyInfo();
        token.setName(NetElectricsConst.TOKEN);
        token.setValue("");

        infos.add(sceneId);
        infos.add(value);
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
            dialog.dismiss();
            switch (msg.what)
            {
                case MODIFY_SUCCESS:
                    showInfo("修改成功");
                    Intent intent = new Intent();
                    intent.putExtra("newName", name);
                    setResult(RESULT_OK, intent);
                    finish();
                    break;
                case MODIFY_FAIL:
                    modifyName();
                    break;
                case MODIFY_ERROR:
                    break;
                default:
                    break;
            }
        }
    };
}
