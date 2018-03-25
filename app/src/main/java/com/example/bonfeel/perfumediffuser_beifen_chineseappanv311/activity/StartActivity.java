package com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.R;

/**
 *
 * Author:FunFun
 * Function:软件的启动项，即软件从该活动开始执行
 * Date:2016/4/12 13:37
 */
public class StartActivity extends BaseActivity
{
    private ImageView ivStart;
    private Animation animStart;

    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        setContentView(R.layout.activity_start);
        super.onCreate(savedInstanceState);
//		TestinAgent.init(this, "bonfeel_key");
//		TestinAgent.setLocalDebug(true);
    }
    /*
     * 方法名称：	initialData
     * 方法描述：
     */
    @Override
    protected void initialData()
    {
        preferences = getSharedPreferences("phone", Context.MODE_PRIVATE);
    }

    /*
     * 方法名称：	initialView
     * 方法描述：
     */
    @Override
    protected void initialView()
    {
        ivStart = (ImageView)findViewById(R.id.iv_start);
        animStart = AnimationUtils.loadAnimation(this, R.anim.animation_start);
        animStart.setFillEnabled(true);
        animStart.setFillAfter(true);
        ivStart.setAnimation(animStart);
    }

    /*
     * 方法名称：	initialHandler
     * 方法描述：
     */
    @Override
    protected void initialHandler()
    {
        animStart.setAnimationListener(new AnimationListener()
        {
            @Override
            public void onAnimationStart(Animation animation)
            {
            }

            @Override
            public void onAnimationRepeat(Animation animation)
            {
            }

            @Override
            public void onAnimationEnd(Animation animation)
            {
                //采用SharedPreference来显示首次使用的引导页
                if ( preferences.getBoolean("first", true) )
                {
                    Intent intent = new Intent (StartActivity.this,WelcomePageActivity.class);
                    startActivity(intent);
                    finish();
                }
                else
                {
                    Intent login = new Intent(StartActivity.this, LoginActivity.class);
                    startActivity(login);
                    finish();
                }
            }
        });
    }
}
