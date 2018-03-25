package com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.View.OnClickListener;

import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.R;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.custom.CustomDialog;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.util.ApplicationUtil;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.util.NetCommunicationUtil;

/**
 *
 * Author:FunFun
 * Function:服务界面
 * Date:2016/4/12 13:47
 */
public class TabFourthFragment extends Fragment
{
    private LinearLayout layoutUserCenter;
    //private LinearLayout layoutMessagePush;
    private LinearLayout layoutShop;
    private LinearLayout layoutUsualProblems;
    private LinearLayout layoutSuggest;
    private LinearLayout layoutAboutUs;
    private LinearLayout layoutExitSystem;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private View tabLayout;
    private CustomDialog exitDialog;
    private View layoutExit;
    private TextView tvExitToast;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        preferences = getActivity().getSharedPreferences("user_info.txt", Context.MODE_PRIVATE);
        editor = preferences.edit();
    }
    /*
     * 方法名称：	onCreateView
     * 方法描述：
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        tabLayout = inflater.inflate(R.layout.fragment_tab4, container, false);
        initialView(tabLayout);
        initialHandler();
        return tabLayout;
    }
    /**
     * 方法名称：	initialView
     * 方法描述：	初始化控件
     * 参数：			@param v
     * 返回值类型：	void
     * 创建时间：	2015-1-30下午8:37:29
     */
    private void initialView(View v)
    {
        layoutUserCenter = (LinearLayout)v.findViewById(R.id.layout_user_center);
        //layoutMessagePush = (LinearLayout)v.findViewById(R.id.layout_message_push);
        layoutShop = (LinearLayout)v.findViewById(R.id.layout_shop);
        layoutUsualProblems = (LinearLayout)v.findViewById(R.id.layout_usual_problem);
        layoutExitSystem = (LinearLayout)v.findViewById(R.id.layout_exit_system);


        layoutSuggest = (LinearLayout)v.findViewById(R.id.layout_suggest);
        layoutAboutUs = (LinearLayout)v.findViewById(R.id.layout_about_us);

        //隐藏信息推送（功能未实现）
        //layoutMessagePush.setVisibility(View.GONE);
    }
    /**
     * 方法名称：	initialHandler
     * 方法描述：	初始化事件响应
     * 参数：
     * 返回值类型：	void
     * 创建时间：	2015-1-30下午8:40:28
     */
    private void initialHandler()
    {
        //个人中心（新增扫一扫功能）
        layoutUserCenter.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent myCount = new Intent(ApplicationUtil.getContext(), MyCountActivity.class);
                startActivity(myCount);
            }
        });
        //信息推送(咱未实现)
//		layoutMessagePush.setOnClickListener(new OnClickListener()
//		{
//			@Override
//			public void onClick(View v)
//			{
//				Intent msgPush = new Intent(ApplicationUtil.getContext(), MsgPushActivity.class);
//				startActivity(msgPush);
//			}
//		});
        //积分商城
        layoutShop.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent shop = new Intent(ApplicationUtil.getContext(), ShopActivity.class);
                startActivity(shop);
            }
        });
        //常见问题
        layoutUsualProblems.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent usualProblem = new Intent(ApplicationUtil.getContext(), UsualProblemActivity.class);
                startActivity(usualProblem);
            }
        });
        //意见反馈
        layoutSuggest.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent suggest = new Intent(ApplicationUtil.getContext(), SuggestActivity.class);
                startActivity(suggest);
            }
        });
        //关于软件
        layoutAboutUs.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent aboutUs = new Intent(ApplicationUtil.getContext(), AboutUsActivity.class);
                startActivity(aboutUs);
            }
        });
        layoutExitSystem.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v)
            {
                //弹出提示对话框
                showExitDialog();
            }
        });
    }
    /**
     * 方法名称：	exitDialog
     * 方法描述：	退出提示框
     * 参数：
     * 返回值类型：	void
     * 创建时间：	2015-4-23下午12:04:25
     */
    private void showExitDialog()
    {
        layoutExit = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_hint_control, null);
        tvExitToast = (TextView)layoutExit.findViewById(R.id.tv_hint_control);
        tvExitToast.setText("确认退出软件吗？");

        exitDialog = new CustomDialog(getActivity())
                .builder()
                .setTitle("提示")
                .setView(layoutExit)
                .setNegativeButton("取消", new OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {}
                })
                .setPositiveButton("确定", new OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        //	NetCommunicationUtil.setExitTag(1);
                        editor.putString("code", "");
                        editor.putBoolean("remember", false);
                        editor.putBoolean("auto", false);
                        editor.commit();
                        Intent toLogin = new Intent(getActivity(), LoginActivity.class);
                        startActivity(toLogin);
                        MainActivity.mMainActivity.finish();
                    }
                });
        exitDialog.show();
    }
    /* (非Javadoc)
     * 方法名称：	onResume
     * 方法描述：	TODO
     * 重写部分：	@see android.app.Fragment#onResume()
     */
    @Override
    public void onResume()
    {
        if ( NetCommunicationUtil.getExitTag() == 1 )
        {
            MainActivity mainActivity = (MainActivity) TabFourthFragment.this.getActivity();
            mainActivity.exitSys();
        }
        super.onResume();
    }

}
