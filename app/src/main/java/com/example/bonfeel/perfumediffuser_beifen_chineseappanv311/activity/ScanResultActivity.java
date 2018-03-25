package com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.R;

/**
 *
 * Author:FunFun
 * Function:二维码扫描结果
 * Date:2016/4/12 11:48
 */
public class ScanResultActivity extends BaseActivity
{
    private TextView tvTitle;
    private TextView tvResult;

    private String title;
    private String result;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        setContentView(R.layout.activity_scan_result);
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
        title = "扫描结果";
        result = getIntent().getStringExtra("result");
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
        tvResult = (TextView)findViewById(R.id.tv_scan_result);

        tvTitle.setText(title);
        tvResult.setText(result);
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
