package com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.custom;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.R;

/**
 *
 * Author:FunFun
 * Function:自定义标题控件
 * Date:2016/4/12 10:38
 */
public class TitleBar extends LinearLayout
{
    Button btnLeft;
    /**
     * 构造方法名称：	TitleBar
     * 创建时间：		2015-1-19上午10:35:18
     */
    public TitleBar(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        //加载自定义控件的布局
        LayoutInflater.from(context).inflate(R.layout.layout_titlebar, this);

        btnLeft = (Button)findViewById(R.id.btn_left);
        btnLeft.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Activity activity = (Activity)getContext();
                activity.finish();
            }
        });
    }

}
