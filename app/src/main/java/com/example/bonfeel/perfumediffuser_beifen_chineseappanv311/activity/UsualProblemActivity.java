package com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.R;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.custom.ProblemAdapter;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.model.NetElectricsConst;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.model.Problem;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.util.ApplicationUtil;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.util.NetCommunicationUtil;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.util.ProblemManager;

import org.ksoap2.serialization.PropertyInfo;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Author:FunFun
 * Function:常见问题
 * Date:2016/4/12 13:55
 */
public class UsualProblemActivity extends BaseActivity
{
    private TextView tvTitle;
    private ListView lvProblem;

    private String title;
    private List<Problem> listProblem;
    private ProblemAdapter adapter;

    private Dialog dialog;

    private final int GET_INFO_FAIL = 0;
    private final int GET_INFO_SUCCESS = 1;
    private final int GET_INFO_ERROR = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        setContentView(R.layout.activity_usual_problem);
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
        title = "常见问题";
        dialog = ApplicationUtil.getLoadingDialog(this, "正在加载...");
        dialog.show();
        getUsualProblems();
    }
    /**
     * 方法名称：	getUsualProblems
     * 方法描述：	从服务器中获取常见问题
     * 参数：
     * 返回值类型：	void
     * 创建时间：	2015-3-21下午4:11:01
     */
    private void getUsualProblems()
    {
        NetCommunicationUtil.httpConnect(
                getProblemInfos(),
                NetElectricsConst.METHOD_GET_USUALPROBLEM,
                NetElectricsConst.STYLE_OBJECT,
                new NetCommunicationUtil.HttpCallbackListener() {

                    @Override
                    public void onFinish(Bundle bundle) {
                        Message message = handler.obtainMessage();
                        if (bundle.getInt("resCode") == 1) {
                            NetCommunicationUtil.getHttpResultInfo(NetElectricsConst.METHOD_TAG.GetProblems, bundle.getString("result"));
                            message.what = GET_INFO_SUCCESS;
                        } else {
                            message.what = GET_INFO_FAIL;
                        }
                        handler.sendMessage(message);
                    }

                    @Override
                    public void onError(Bundle bundle) {
                        Message message = handler.obtainMessage();
                        message.what = GET_INFO_ERROR;
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
    private List<PropertyInfo> getProblemInfos()
    {
        List<PropertyInfo> infos = new ArrayList<PropertyInfo>();

        PropertyInfo token = new PropertyInfo();
        token.setName(NetElectricsConst.TOKEN);
        token.setValue("");

        infos.add(token);

        return infos;
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
        lvProblem = (ListView)findViewById(R.id.lv_usual_problems);

        tvTitle.setText(title);
    }
    /**
     * 方法名称：	collectProblems
     * 方法描述：	收集常见问题
     * 参数：
     * 返回值类型：	void
     * 创建时间：	2015-3-2下午3:14:37
     */
    private void collectProblems()
    {
        listProblem = new ArrayList<Problem>();
        listProblem = ProblemManager.getInstance().getList();
        adapter = new ProblemAdapter(this, R.layout.problem_item, listProblem);
        lvProblem.setAdapter(adapter);
    }
    /* (非Javadoc)
     * 方法名称：	initialHandler
     * 方法描述：	TODO
     * 重写部分：	@see com.example.bonfeel.activity.BaseActivity#initialHandler()
     */
    @Override
    protected void initialHandler()
    {
        lvProblem.setOnItemClickListener(new OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id)
            {
                Intent detail = new Intent(UsualProblemActivity.this, ProblemDetailActivity.class);
                Bundle problemBundle = new Bundle();
                problemBundle.putString("name", listProblem.get(position).getName());
                problemBundle.putString("content", listProblem.get(position).getContent());
                detail.putExtra("proBundle", problemBundle);
                startActivity(detail);
            }
        });
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
                case GET_INFO_SUCCESS:
                    //将问题列表显示出来
                    collectProblems();
                    dialog.dismiss();
                    break;
                case GET_INFO_FAIL:
                    getUsualProblems();
                    break;
                case GET_INFO_ERROR:
                    dialog.dismiss();
                    showInfo("程序异常，请稍后重试");
                    break;
                default:
                    break;
            }
        }
    };
}
