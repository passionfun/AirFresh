package com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.R;

/**
 *
 * Author:FunFun
 * Function:
 * Date:2016/4/12 11:43
 */
public class NewFuncActivity extends BaseActivity
{
    private TextView tvTitle;
    private TextView tvNewFunc;
    private TextView tvFunc1;

    private String title;
    private String version;
    private String function1;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        setContentView(R.layout.activity_new_func);
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
        title = "功能介绍";
        version = getIntent().getStringExtra("version");
//		function1 = "一键配置可以选择网络";
//		function1 = "提示设备故障及故障原因";
//		function1 = "设备列表中，显示红色的为故障设备";
        function1 = "一键配置新增手动模式，可以扫描二维码或者输入设备MAC";
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
        tvNewFunc = (TextView)findViewById(R.id.tv_new_func);
        tvFunc1 = (TextView)findViewById(R.id.tv_function1);

        tvTitle.setText(title);
        tvNewFunc.setText("百芬扩香仪"+version+"主要更新");
        tvFunc1.setText(function1);
    }

    /* (非Javadoc)
     * 方法名称：	initialHandler
     * 方法描述：	TODO
     * 重写部分：	@see com.example.bonfeel.activity.BaseActivity#initialHandler()
     */
    @Override
    protected void initialHandler()
    {
    }
}
