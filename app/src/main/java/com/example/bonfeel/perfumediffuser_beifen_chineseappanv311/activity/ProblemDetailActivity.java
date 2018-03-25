package com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.R;

/**
 *
 * Author:FunFun
 * Function:
 * Date:2016/4/12 11:46
 */
public class ProblemDetailActivity extends BaseActivity
{
    private String title;
    private String name;
    private String content;

    private TextView tvTitle;
    private TextView tvName;
    private TextView tvContent;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        setContentView(R.layout.activity_problem_detail);
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
        title = "问题解答";
        name = getIntent().getBundleExtra("proBundle").getString("name");
        content = getIntent().getBundleExtra("proBundle").getString("content");
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
        tvName = (TextView)findViewById(R.id.tv_problem_name);
        tvContent = (TextView)findViewById(R.id.tv_problem_detail);

        tvTitle.setText(title);
        tvName.setText(name);
        tvContent.setText(content);
    }

    /* (非Javadoc)
     * 方法名称：	initialHandler
     * 方法描述：	TODO
     * 重写部分：	@see com.example.bonfeel.activity.BaseActivity#initialHandler()
     */
    @Override
    protected void initialHandler()
    {}
}