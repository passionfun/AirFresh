package com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.R;

import java.util.ArrayList;

/**
 *
 * Author:FunFun
 * Function:
 * Date:2016/4/12 13:59
 */
public class WelcomePageActivity extends Activity
{
    private ViewPager mViewPager;
    private ImageView mPage0;
    private ImageView mPage1;
    private ImageView mPage2;

    private int currIndex = 0;

    private SharedPreferences preferences;
    private Editor editor;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_page_frame);

        mViewPager = (ViewPager)findViewById(R.id.whatsnew_viewpager);
        mViewPager.setOnPageChangeListener(new MyOnPageChangeListener());

        mPage0 = (ImageView)findViewById(R.id.page0);
        mPage1 = (ImageView)findViewById(R.id.page1);
        mPage2 = (ImageView)findViewById(R.id.page2);

        //将要分页显示的View装入数组中
        LayoutInflater mLi = LayoutInflater.from(this);
        View view1 = mLi.inflate(R.layout.welcome_page_1, null);
        View view2 = mLi.inflate(R.layout.welcome_page_2, null);
        View view3 = mLi.inflate(R.layout.welcome_page_3, null);

        //每个页面的view数据
        final ArrayList<View> views = new ArrayList<View>();
        views.add(view1);
        views.add(view2);
        views.add(view3);

        //填充ViewPager的数据适配器
        PagerAdapter mPagerAdapter = new PagerAdapter()
        {
            @Override
            public boolean isViewFromObject(View arg0, Object arg1)
            {
                return arg0 == arg1;
            }
            @Override
            public int getCount()
            {
                return views.size();
            }
            @Override
            public void destroyItem(View container, int position, Object object)
            {
                ((ViewPager)container).removeView(views.get(position));
            }
            @Override
            public Object instantiateItem(View container, int position)
            {
                ((ViewPager)container).addView(views.get(position));
                return views.get(position);
            }
        };
        mViewPager.setAdapter(mPagerAdapter);

        preferences = getSharedPreferences("phone", Context.MODE_PRIVATE);
        editor = preferences.edit();
    }


    public class MyOnPageChangeListener implements OnPageChangeListener
    {
        public void onPageSelected(int arg0)
        {
            switch (arg0)
            {
                case 0:
                    mPage0.setImageDrawable(getResources().getDrawable(R.drawable.page_now));
                    mPage1.setImageDrawable(getResources().getDrawable(R.drawable.page));
                    break;
                case 1:
                    mPage1.setImageDrawable(getResources().getDrawable(R.drawable.page_now));
                    mPage0.setImageDrawable(getResources().getDrawable(R.drawable.page));
                    mPage2.setImageDrawable(getResources().getDrawable(R.drawable.page));
                    break;
                case 2:
                    mPage2.setImageDrawable(getResources().getDrawable(R.drawable.page_now));
                    mPage1.setImageDrawable(getResources().getDrawable(R.drawable.page));
                    break;
            }
            currIndex = arg0;
        }
        public void onPageScrolled(int arg0, float arg1, int arg2) {}
        public void onPageScrollStateChanged(int arg0) {}
    }
    public void startbutton(View v)
    {
        if ( preferences.getBoolean("first", true) )
        {
            editor.putBoolean("first", false);
            editor.commit();
            Intent login = new Intent(this, LoginActivity.class);
            startActivity(login);
            finish();
        }else{
            this.finish();
        }
    }
}
