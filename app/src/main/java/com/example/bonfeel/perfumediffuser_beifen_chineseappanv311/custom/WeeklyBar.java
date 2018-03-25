package com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.custom;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.R;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.util.LogUtil;

/**
 *
 * Author:FunFun
 * Function:自定义周期设置控件
 * Date:2016/4/12 10:38
 */
public class WeeklyBar extends LinearLayout
{
    private Button btnSun, btnMon, btnTue, btnWed, btnThu, btnFri, btnSat;
    private Button[] btnWeek;
    private byte circle;
    private boolean[] isWeek;
    /**
     * 构造方法名称：	WeeklyBar
     * 创建时间：		2015-2-4下午3:13:47
     * 方法描述：		TODO
     */
    public WeeklyBar(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        //加载自定义控件的布局
        LayoutInflater.from(context).inflate(R.layout.layout_weeklybar, this);
        circle = 0x00;
        isWeek = new boolean[]{
                false, false, false, false, false, false, false
        };

        initialView();
        initialHandler();
    }
    /**
     * 方法名称：	initialView
     * 方法描述：	TODO
     * 参数：
     * 返回值类型：	void
     * 创建时间：	2015-2-4下午3:20:43
     */
    private void initialView()
    {
        btnSun = (Button)findViewById(R.id.btn_sun);
        btnMon = (Button)findViewById(R.id.btn_mon);
        btnTue = (Button)findViewById(R.id.btn_tue);
        btnWed = (Button)findViewById(R.id.btn_wed);
        btnThu = (Button)findViewById(R.id.btn_thu);
        btnFri = (Button)findViewById(R.id.btn_fri);
        btnSat = (Button)findViewById(R.id.btn_sat);

        btnWeek = new Button[7];
        btnWeek[0] = btnMon;
        btnWeek[1] = btnTue;
        btnWeek[2] = btnWed;
        btnWeek[3] = btnThu;
        btnWeek[4] = btnFri;
        btnWeek[5] = btnSat;
        btnWeek[6] = btnSun;
    }
    /**
     * 方法名称：	initialHandler
     * 方法描述：	TODO
     * 参数：
     * 返回值类型：	void
     * 创建时间：	2015-2-4下午3:20:39
     */
    private void initialHandler()
    {
        btnWeek[6].setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if ( !isWeek[6] )
                {
                    circle = (byte) (circle | 0x40);
                    isWeek[6] = true;
//					btnWeek[6].setBackgroundColor(Color.rgb(0, 102, 60));
                    btnWeek[6].setBackgroundResource(R.drawable.set_day);
                    btnWeek[6].setTextColor(Color.rgb(0, 0, 0));
                }
                else
                {
                    circle = (byte) (circle & 0x3F);
                    isWeek[6] = false;
//					btnWeek[6].setBackgroundColor(Color.rgb(11, 211, 138));
                    btnWeek[6].setBackgroundResource(R.drawable.off_day);
                    btnWeek[6].setTextColor(Color.rgb(255, 255, 255));
                }
            }
        });
        btnWeek[0].setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                if ( !isWeek[0] )
                {
                    circle = (byte) (circle | 0x01);
                    isWeek[0] = true;
//					btnWeek[0].setBackgroundColor(Color.rgb(0, 102, 60));
                    btnWeek[0].setBackgroundResource(R.drawable.set_day);
                    btnWeek[0].setTextColor(Color.rgb(0, 0, 0));
                }
                else
                {
                    circle = (byte) (circle & 0x7E);
                    isWeek[0] = false;
//					btnWeek[0].setBackgroundColor(Color.rgb(11, 211, 138));
                    btnWeek[0].setBackgroundResource(R.drawable.off_day);
                    btnWeek[0].setTextColor(Color.rgb(255, 255, 255));
                }
            }
        });
        btnWeek[1].setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                if ( !isWeek[1] )
                {
                    circle = (byte) (circle | 0x02);
                    isWeek[1] = true;
//					btnWeek[1].setBackgroundColor(Color.rgb(0, 102, 60));
                    btnWeek[1].setBackgroundResource(R.drawable.set_day);
                    btnWeek[1].setTextColor(Color.rgb(0, 0, 0));
                }
                else
                {
                    circle = (byte) (circle & 0x7D);
                    isWeek[1] = false;
//					btnWeek[1].setBackgroundColor(Color.rgb(11, 211, 138));
                    btnWeek[1].setBackgroundResource(R.drawable.off_day);
                    btnWeek[1].setTextColor(Color.rgb(255, 255, 255));
                }
            }
        });
        btnWeek[2].setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                if ( !isWeek[2] )
                {
                    circle = (byte) (circle | 0x04);
                    isWeek[2] = true;
//					btnWeek[2].setBackgroundColor(Color.rgb(0, 102, 60));
                    btnWeek[2].setBackgroundResource(R.drawable.set_day);
                    btnWeek[2].setTextColor(Color.rgb(0, 0, 0));
                }
                else
                {
                    circle = (byte) (circle & 0x7B);
                    isWeek[2] = false;
//					btnWeek[2].setBackgroundColor(Color.rgb(11, 211, 138));
                    btnWeek[2].setBackgroundResource(R.drawable.off_day);
                    btnWeek[2].setTextColor(Color.rgb(255, 255, 255));
                }
            }
        });
        btnWeek[3].setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                if ( !isWeek[3] )
                {
                    circle = (byte) (circle | 0x08);
                    isWeek[3] = true;
//					btnWeek[3].setBackgroundColor(Color.rgb(0, 102, 60));
                    btnWeek[3].setBackgroundResource(R.drawable.set_day);
                    btnWeek[3].setTextColor(Color.rgb(0, 0, 0));
                }
                else
                {
                    circle = (byte) (circle & 0x77);
                    isWeek[3] = false;
//					btnWeek[3].setBackgroundColor(Color.rgb(11, 211, 138));
                    btnWeek[3].setBackgroundResource(R.drawable.off_day);
                    btnWeek[3].setTextColor(Color.rgb(255, 255, 255));
                }
            }
        });
        btnWeek[4].setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                if ( !isWeek[4] )
                {
                    circle = (byte) (circle | 0x10);
                    isWeek[4] = true;
//					btnWeek[4].setBackgroundColor(Color.rgb(0, 102, 60));
                    btnWeek[4].setBackgroundResource(R.drawable.set_day);
                    btnWeek[4].setTextColor(Color.rgb(0, 0, 0));
                }
                else
                {
                    circle = (byte) (circle & 0x6F);
                    isWeek[4] = false;
//					btnWeek[4].setBackgroundColor(Color.rgb(11, 211, 138));
                    btnWeek[4].setBackgroundResource(R.drawable.off_day);
                    btnWeek[4].setTextColor(Color.rgb(255, 255, 255));
                }
            }
        });
        btnWeek[5].setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                if ( !isWeek[5] )
                {
                    circle = (byte) (circle | 0x20);
                    isWeek[5] = true;
//					btnWeek[5].setBackgroundColor(Color.rgb(0, 102, 60));
                    btnWeek[5].setBackgroundResource(R.drawable.set_day);
                    btnWeek[5].setTextColor(Color.rgb(0, 0, 0));
                }
                else
                {
                    circle = (byte) (circle & 0x5F);
                    isWeek[5] = false;
//					btnWeek[5].setBackgroundColor(Color.rgb(11, 211, 138));
                    btnWeek[5].setBackgroundResource(R.drawable.off_day);
                    btnWeek[5].setTextColor(Color.rgb(255, 255, 255));
                }
            }
        });
    }
    /**
     * 返回值： circle
     */
    public byte getCircle()
    {
        LogUtil.logDebug("WeeklyBar", "" + Integer.toHexString(circle));
        return circle;
    }
    /**
     * 参数设置： circle to set
     */
    public void setCircle(byte circle)
    {
        LogUtil.logDebug("WeeklyBar", ""+Integer.toHexString(circle));
        this.circle = circle;
        //将byte转boolean[]
        byteToBoolean();
        //根据boolean[]显示控件情况
        showData();
    }
    /**
     * 方法名称：	byteToBoolean
     * 方法描述：	将circle解析成boolean数组
     * 参数：
     * 返回值类型：	void
     * 创建时间：	2015-2-4下午4:10:08
     */
    private void byteToBoolean()
    {
        byte temp = 0x00;
        byte tool = 0x01;
        int index = 0;
        while (index < 7)
        {
            temp = circle;
            if ( (temp & tool) != 0x00 )
            {
                isWeek[index] = true;
            }
            else
            {
                isWeek[index] = false;
            }
            index++;
            tool = (byte) (tool << 1);
        }
    }
    /**
     * 方法名称：	showData
     * 方法描述：	将boolean[]的值用控件显示出来
     * 参数：
     * 返回值类型：	void
     * 创建时间：	2015-2-4下午4:39:58
     */
    private void showData()
    {
        for (int i = 0; i < 7; i++)
        {
            if ( isWeek[i] )
            {
                btnWeek[i].setBackgroundResource(R.drawable.set_day);
            }
            else
            {
                btnWeek[i].setBackgroundResource(R.drawable.off_day);
            }
        }
    }
}
