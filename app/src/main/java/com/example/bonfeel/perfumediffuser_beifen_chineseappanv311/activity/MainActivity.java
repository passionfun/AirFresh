package com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioButton;
import android.widget.Toast;


import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.R;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.model.Device;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.model.NetElectricsConst;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.util.DeviceManager;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.util.LogUtil;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.util.NetCommunicationUtil;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.util.ProblemManager;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.util.SceneManager;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.util.UserManager;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

import java.util.List;

/**
 *
 * Author:FunFun
 * Function:界面主框架
 * Date:2016/4/12 11:31
 */
public class MainActivity extends SlidingFragmentActivity
{
    public static MainActivity mMainActivity;
    private BroadcastReceiver mBroadcastReceiver;
    private List<Device> mallDevices;
    private Device mdevice;
    private String tag = "MainActivity";

    private long exitTime = 0;
    //contents
    private TabFirstFragment firstFragment;
    private TabSecondFragment secondFragment;
    private TabThirdFragment thirdFragment;
    private TabFourthFragment fourthFragment;
    //slidemenu
    private SlideMenuFragment slideMenuFragment;
    //tabs
    private RadioButton rbTab1;
    private RadioButton rbTab2;
    private RadioButton rbTab3;
    private RadioButton rbTab4;

    private FragmentManager fragmentManager;
    private FragmentTransaction transaction;
    /*
     * 方法名称：	onCreate
     * 方法描述：	初始化创建
     */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mMainActivity = this;
		/*lzw add*/
        mBroadcastReceiver = new MyBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(NetElectricsConst.SCENE_BROADCAST);
        registerReceiver(mBroadcastReceiver, intentFilter);


        //检查软件是否需要重启
        if ( UserManager.getInstance().get(0) == null )
        {
            NetCommunicationUtil.setReBoot(true);
            exitSys();
            reBootSys();
        }
        else
        {
            initialView();
            initialData();
            initialHandler();
        }
    }
    /**
     * 方法名称：	reBootSys
     * 方法描述：	当应用切换到后台，由于系统内存资源有限，程序中的静态数据会被系统回收
     * 					若此时唤醒应用，会造成ANR，通过重启程序，使之重新获取到足够运行应用的资源。
     * 参数：
     * 返回值类型：	void
     * 创建时间：	2015-4-30下午3:11:26
     */
    private void reBootSys()
    {
        Intent intent = getPackageManager().getLaunchIntentForPackage(getPackageName());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
    /**
     * 方法名称：	initialView
     * 方法描述：	初始化控件
     * 参数：
     * 返回值类型：	void
     * 创建时间：	2015-1-16下午1:56:23
     */
    private void initialView()
    {
        LogUtil.logDebug(tag, "initialView");
        rbTab1 = (RadioButton)findViewById(R.id.tab1);
        rbTab2 = (RadioButton)findViewById(R.id.tab2);
        rbTab3 = (RadioButton)findViewById(R.id.tab3);
        rbTab4 = (RadioButton)findViewById(R.id.tab4);
    }
    /**
     * 方法名称：	initialData
     * 方法描述：	初始化数据
     * 参数：
     * 返回值类型：	void
     * 创建时间：	2015-1-16下午2:12:36
     */
    private void initialData()
    {
        setBehindContentView(R.layout.menu_frame_left);
        fragmentManager = getSupportFragmentManager();
        transaction = fragmentManager.beginTransaction();
        slideMenuFragment = new SlideMenuFragment();
        transaction.replace(R.id.menu_frame,slideMenuFragment).commit();
        SlidingMenu sm = getSlidingMenu();
        // 设置可以左右滑动的菜单
        sm.setMode(SlidingMenu.LEFT);
        // 设置滑动阴影的宽度
        sm.setShadowWidthRes(R.dimen.shadow_width);
        // 设置滑动菜单阴影的图像资源
        sm.setShadowDrawable(null);
        // 设置滑动菜单视图的宽度
        sm.setBehindOffsetRes(R.dimen.slide_menu_width);
        // 设置渐入渐出效果的值
        sm.setFadeDegree(0.35f);
        // 设置触摸屏幕的模式,这里设置为全屏
        sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
        // 设置下方视图的在滚动时的缩放比例
        sm.setBehindScrollScale(0.0f);

        sm.setOnOpenListener(new SlidingMenu.OnOpenListener() {
            @Override
            public void onOpen() {
                getSlideMenuFragment().updateDeviceList();
                showSlideMenu();
            }
        });
        setTabSelection(0);

    }

    /**
     * 显示侧边栏
     */
    public void showMenu(View view){
        getSlidingMenu().showMenu();
    }
    /**
     * 隐藏侧边栏
     */
    public void hideMemu(){
        getSlidingMenu().toggle();
    }
    /**
     * 方法名称：	setTabSelection
     * 方法描述：	选择第index个tab
     * 参数：			@param index
     * 返回值类型：	void
     * 创建时间：	2015-1-16下午1:58:09
     */
    public void setTabSelection(int index)
    {
        LogUtil.logDebug(tag, "setTabSelection");
        clearTabs();
        transaction = fragmentManager.beginTransaction();
        hideFragmentTab(transaction);
        switch (index)
        {
            case 0:
                if ( firstFragment == null )
                {
                    LogUtil.logDebug(tag, "firstFragment");
                    firstFragment = new TabFirstFragment();
                    transaction.add(R.id.content, firstFragment);
                }
                else
                {
                    transaction.show(firstFragment);
                }
                if ( firstFragment.getContentParams() != null )
                {
                    if (firstFragment.getContentParams().leftMargin != 0)
                    {
                        transaction.show(slideMenuFragment);
                    }
                }
                rbTab1.setTextColor(Color.rgb(224, 195, 125));
                break;
            case 1:
                if ( secondFragment == null )
                {
                    secondFragment = new TabSecondFragment();
                    transaction.add(R.id.content, secondFragment);
                }
                else
                {
                    transaction.show(secondFragment);
                }
                rbTab2.setTextColor(Color.rgb(224, 195, 125));
                break;
            case 2:
                if ( thirdFragment == null )
                {
                    thirdFragment = new TabThirdFragment();
                    transaction.add(R.id.content, thirdFragment);
                }
                else
                {
                    transaction.show(thirdFragment);
                }
                rbTab3.setTextColor(Color.rgb(224, 195, 125));
                break;
            case 3:
                if ( fourthFragment == null )
                {
                    fourthFragment = new TabFourthFragment();
                    transaction.add(R.id.content, fourthFragment);
                }
                else
                {
                    transaction.show(fourthFragment);
                }
                rbTab4.setTextColor(Color.rgb(224, 195, 125));
                break;
            default:
                break;
        }
        //transaction.commit();
        transaction.commitAllowingStateLoss();
    }
    /**
     * 方法名称：	clearTabs
     * 方法描述：	每次选择tab时，都要重置一次状态
     * 参数：
     * 返回值类型：	void
     * 创建时间：	2015-1-16下午2:02:32
     */
    private void clearTabs()
    {
        rbTab1.setTextColor(Color.rgb(136, 136, 136));
        rbTab2.setTextColor(Color.rgb(136, 136, 136));
        rbTab3.setTextColor(Color.rgb(136, 136, 136));
        rbTab4.setTextColor(Color.rgb(136, 136, 136));
    }
    /**
     * 方法名称：	hideFragmentTab
     * 方法描述：	当切换界面时，将隐藏未选择但已经创建的界面
     * 参数：			@param transaction
     * 返回值类型：	void
     * 创建时间：	2015-1-16下午2:06:06
     */
    private void hideFragmentTab(FragmentTransaction transaction)
    {
        if ( firstFragment != null )
        {
            transaction.hide(firstFragment);
        }
        if ( secondFragment != null )
        {
            transaction.hide(secondFragment);
        }
        if ( thirdFragment != null )
        {
            transaction.hide(thirdFragment);
        }
        if ( fourthFragment != null )
        {
            transaction.hide(fourthFragment);
        }
        if ( slideMenuFragment != null )
        {
            transaction.hide(slideMenuFragment);
        }
    }
    /**
     * 方法名称：	showSlideMenu
     * 方法描述：	显示设备列表界面
     * 参数：
     * 返回值类型：	void
     * 创建时间：	2015-4-6下午5:31:45
     */
    public void showSlideMenu()
    {
        transaction = fragmentManager.beginTransaction();
        transaction.show(slideMenuFragment);
        //transaction.commit();

        transaction.commitAllowingStateLoss();
    }
    /**
     * 方法名称：	hideSlideMenu
     * 方法描述：	隐藏设备列表界面
     * 参数：
     * 返回值类型：	void
     * 创建时间：	2015-4-7上午8:43:35
     */
    public void hideSlideMenu()
    {
        transaction = fragmentManager.beginTransaction();
        transaction.hide(slideMenuFragment);
        //transaction.commit();

        transaction.commitAllowingStateLoss();
    }
    /**
     * 方法名称：	initialHandler
     * 方法描述：	初始化事件处理
     * 参数：
     * 返回值类型：	void
     * 创建时间：	2015-1-16下午2:13:38
     */
    private void initialHandler()
    {
        rbTab1.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                setTabSelection(0);
            }
        });
        rbTab2.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                setTabSelection(1);
            }
        });
        rbTab3.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                setTabSelection(2);
            }
        });
        rbTab4.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                setTabSelection(3);
            }
        });
    }

    /**
     * 返回值： firstFragment
     */
    public TabFirstFragment getFirstFragment()
    {
        return firstFragment;
    }
    /**
     * 返回值： slideMenuFragment
     */
    public SlideMenuFragment getSlideMenuFragment()
    {
        return slideMenuFragment;
    }
    /**
     * 返回值： thirdFragment
     */
    public TabThirdFragment getThirdFragment()
    {
        return thirdFragment;
    }
    /*
     * 方法名称：	dispatchKeyEvent
     * 方法描述：	退出系统提示
     */
    @Override
    public boolean dispatchKeyEvent(KeyEvent event)
    {
        if ( event.getKeyCode() == KeyEvent.KEYCODE_BACK )
        {
            if ( event.getAction() == KeyEvent.ACTION_DOWN && event.getRepeatCount() == 0 )
            {
                if ( System.currentTimeMillis()-exitTime > 2000 )
                {
                    Toast.makeText(this, "再按一次返回键退出程序", Toast.LENGTH_SHORT).show();
                    exitTime = System.currentTimeMillis();
                    return false;
                }
                else
                {
                    exitSys();
                }
            }
            return true;
        }
        return super.dispatchKeyEvent(event);
    }
    /**
     * 方法名称：	exitSys
     * 方法描述：	退出系统的响应
     * 参数：
     * 返回值类型：	void
     * 创建时间：	2015-4-23下午12:35:54
     */
    public  void exitSys()
    {
        LogUtil.logDebug("MainActivity", "MainActivity onDestroy executed");
        UserManager.getInstance().clean();
        DeviceManager.getInstance().clean();
        SceneManager.getInstance().clean();
        ProblemManager.getInstance().clean();
        NetCommunicationUtil.clean();
        Intent alarmClose = new Intent(NetElectricsConst.ACTION_ALARM);
        stopService(alarmClose);
        Intent beatClose = new Intent(NetElectricsConst.ACTION_HEART_BEAT);
        stopService(beatClose);
        finish();
    }
    /* (非Javadoc)
     * 方法名称：	onDestroy
     * 方法描述：	TODO
     * 重写部分：	@see android.support.v4.app.FragmentActivity#onDestroy()
     */
    @Override
    protected void onDestroy()
    {
//		System.out.println("mainActivity onDestroy has exceted");
        super.onDestroy();

        unregisterReceiver(mBroadcastReceiver); //lzw add
    }
    /* (非Javadoc)
     * 方法名称：	onPause
     * 方法描述：	TODO
     * 重写部分：	@see android.support.v4.app.FragmentActivity#onPause()
     */
    @Override
    protected void onPause()
    {
//		System.out.println("mainActivity onPause has exceted");
        super.onPause();
    }
    /* (非Javadoc)
     * 方法名称：	onResume
     * 方法描述：	TODO
     * 重写部分：	@see android.support.v4.app.FragmentActivity#onResume()
     */
    @Override
    protected void onResume()
    {
//		System.out.println("mainActivity onResume has exceted");
        super.onResume();
    }
    /* (非Javadoc)
     * 方法名称：	onStart
     * 方法描述：	TODO
     * 重写部分：	@see android.support.v4.app.FragmentActivity#onStart()
     */
    @Override
    protected void onStart()
    {
//		System.out.println("mainActivity onStart has exceted");
        super.onStart();
    }
    /* (非Javadoc)
     * 方法名称：	onStop
     * 方法描述：	TODO
     * 重写部分：	@see android.support.v4.app.FragmentActivity#onStop()
     */
    @Override
    protected void onStop()
    {
//		System.out.println("mainActivity onStop has exceted");
        super.onStop();
    }


    public class MyBroadcastReceiver extends BroadcastReceiver
    {

        /*(非Javadoc)
        * 方法名称：	onReceive
        * 方法描述：	TODO
        * 重写部分：	@see android.content.BroadcastReceiver#onReceive(android.content.Context, android.content.Intent)
        */
        @Override
        public void onReceive(Context context, Intent intent)
        {
            String name = intent.getStringExtra("deviceName");

            mallDevices = DeviceManager.getInstance().getDevices();
            for (int i = 0; i < mallDevices.size(); i++)
            {
                if ( name.equals(mallDevices.get(i).getName()))
                {
                    mdevice = mallDevices.get(i);
                    break;
                }
            }
            getFirstFragment().setDevice(mdevice);
            if (getThirdFragment() != null) {
                getThirdFragment().setDevice(mdevice);
            }
            //getThirdFragment().setDevice(mdevice);//fun add
            rbTab1.setChecked(true);
            setTabSelection(0);
        }

    }
}
