package com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.activity;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.R;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.custom.CustomDialog;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.model.NetElectricsConst;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.util.DownLoadUtil;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.util.NetCommunicationUtil;

import org.ksoap2.serialization.PropertyInfo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * Author:FunFun
 * Function:关于软件
 * Date:2016/4/12 11:06
 */
public class AboutUsActivity extends BaseActivity
{
    private TextView tvTitle;
    private TextView tvVersion;
    private LinearLayout layoutWelcome;
    private LinearLayout layoutFunc;
    private LinearLayout layoutNewVersion;
    private LinearLayout layoutDevelopers;
    //private LinearLayout layoutExitSystem;

    private String title;
    private Bundle resBundle;
    private String version;

    private CustomDialog confirmDialog;
    private View layoutContent;
    private TextView tvContent;
    private TextView tvUpdate;

    private CustomDialog exitDialog;
    private View layoutExit;
    private TextView tvExitToast;

    private static final int GET_VERSION_SUCCESS = 1;
    private static final int GET_VERSION_FAIL = 2;
    private static final int GET_VERSION_ERROR = 3;
    private static final int UPDATE_SOFT = 4;
    private static final int UPDATE_PROGRESS = 5;
    private static final int GET_FILE_SIZE = 6;

    private Notification noti;
    private NotificationManager notiManager;
    private int fileSize;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        setContentView(R.layout.activity_about_us);
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
        title = "关于软件";
        version = getVersion();

        notiManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    }
    /**
     * 方法名称：	getVersion
     * 方法描述：	获取软件当前版本
     * 参数：
     * 返回值类型：	void
     * 创建时间：	2015-3-17下午8:12:58
     */
    private String getVersion()
    {
        PackageManager packageManager = getPackageManager();
        PackageInfo packageInfo = null;
        try
        {
            packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
        }
        catch ( NameNotFoundException e )
        {
            e.printStackTrace();
        }
        return packageInfo.versionName;
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
        tvVersion = (TextView)findViewById(R.id.tv_soft_version);
        layoutWelcome = (LinearLayout)findViewById(R.id.layout_welcome);
        layoutFunc = (LinearLayout)findViewById(R.id.layout_func);
        layoutNewVersion = (LinearLayout)findViewById(R.id.layout_check_version);
        layoutDevelopers = (LinearLayout)findViewById(R.id.layout_develope_group);
        //layoutExitSystem = (LinearLayout)findViewById(R.id.layout_exit_system);

        tvTitle.setText(title);
        tvVersion.setText("V"+version);
    }

    /* (非Javadoc)
     * 方法名称：	initialHandler
     * 方法描述：	TODO
     * 重写部分：	@see com.example.bonfeel.activity.BaseActivity#initialHandler()
     */
    @Override
    protected void initialHandler()
    {
        //欢迎页
        layoutWelcome.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent (AboutUsActivity.this,WelcomePageActivity.class);
                startActivity(intent);
            }
        });
        //功能介绍
        layoutFunc.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent (AboutUsActivity.this,NewFuncActivity.class);
                intent.putExtra("version", version);
                startActivity(intent);
            }
        });
        //开发团队
        layoutDevelopers.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent (AboutUsActivity.this,DevelopeGroupActivity.class);
                startActivity(intent);
            }
        });
        //检查新版本
        layoutNewVersion.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //检查软件版本
                getNewVersion();
            }
        });
        //退出系统
//		layoutExitSystem.setOnClickListener(new OnClickListener()
//		{
//			@Override
//			public void onClick(View v)
//			{
//				//弹出提示对话框
//				showExitDialog();
//			}
//		});
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
        layoutExit = LayoutInflater.from(AboutUsActivity.this).inflate(R.layout.dialog_hint_control, null);
        tvExitToast = (TextView)layoutExit.findViewById(R.id.tv_hint_control);
        tvExitToast.setText("确认退出软件吗？");

        exitDialog = new CustomDialog(AboutUsActivity.this)
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
                        NetCommunicationUtil.setExitTag(1);
                        AboutUsActivity.this.finish();
                    }
                });
        exitDialog.show();
    }
    /**
     * 方法名称：	getVersion
     * 方法描述：	联网获取软件最新版本
     * 参数：
     * 返回值类型：	void
     * 创建时间：	2015-3-17下午3:19:48
     */
    private void getNewVersion()
    {
        NetCommunicationUtil.httpConnect(
                getAboutUsInfos(),
                NetElectricsConst.METHOD_APP_VERSION,
                NetElectricsConst.STYLE_OBJECT,
                new NetCommunicationUtil.HttpCallbackListener()
                {
                    @Override
                    public void onFinish(Bundle bundle)
                    {
                        Message message = handler.obtainMessage();
                        if ( bundle.getInt("resCode") == 1 )
                        {
                            resBundle = NetCommunicationUtil.getHttpResultInfo(bundle.getString("result"), NetElectricsConst.METHOD_TAG.GetAppVersion);
                            message.what = GET_VERSION_SUCCESS;
                        }
                        else
                        {
                            message.what = GET_VERSION_FAIL;
                        }
                        handler.sendMessage(message);
                    }
                    @Override
                    public void onError(Bundle bundle)
                    {
                        Message message = handler.obtainMessage();
                        message.what = GET_VERSION_ERROR;
                        handler.sendMessage(message);
                    }
                });
    }
    /**
     * 方法名称：	getAboutUsInfos
     * 方法描述：	获取参数集
     * 参数：			@return
     * 返回值类型：	List<PropertyInfo>
     * 创建时间：	2015-1-31上午8:43:45
     */
    private List<PropertyInfo> getAboutUsInfos()
    {
        List<PropertyInfo> infos = new ArrayList<PropertyInfo>();

        PropertyInfo token = new PropertyInfo();
        token.setName(NetElectricsConst.TOKEN);
        token.setValue("");

        infos.add(token);

        return infos;
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
                case GET_VERSION_SUCCESS:
                    checkForUpdate();
                    break;
                case GET_VERSION_FAIL:
                    showInfo("检查更新失败");
                    break;
                case GET_VERSION_ERROR:
                    showInfo("检查更新异常");
                    break;
                case UPDATE_SOFT:
                    showDownLoadDialog();
                    break;
                case UPDATE_PROGRESS:
//				System.out.println("handler update message");
                    int persent = (msg.arg1 * 100) / fileSize;
                    noti.contentView.setTextViewText(R.id.tv_noti_title, "Bonfeel_V"+resBundle.getString("version") +"        "+ persent + "%");
                    noti.contentView.setProgressBar(R.id.pb_noti_progress, fileSize, msg.arg1, false);
                    notiManager.notify(1, noti);
                    break;
                case GET_FILE_SIZE:
                    fileSize = msg.arg1;
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };
    private void checkForUpdate()
    {
        Message message = handler.obtainMessage();
        //比对版本是否需要升级
        if ( resBundle.getString("version").equals(version) )
        {
            showInfo("当前已是最新版本");
        }
        else
        {
            if ( !resBundle.getString("version").equals("") )
            {
                message.what = UPDATE_SOFT;
            }
        }
        handler.sendMessage(message);
    }
    /**
     * 方法名称：	showDownLoadDialog
     * 方法描述：	新版本下载提示框
     * 参数：
     * 返回值类型：	void
     * 创建时间：	2015-3-17下午8:50:35
     */
    private void showDownLoadDialog()
    {
        layoutContent = LayoutInflater.from(AboutUsActivity.this).inflate(R.layout.dialog_hint_control, null);
        tvContent = (TextView)layoutContent.findViewById(R.id.tv_hint_control);
        tvContent.setText("有新安装包，现在需要更新吗？");
        tvUpdate = (TextView)layoutContent.findViewById(R.id.tv_update_content);
        tvUpdate.setText("主要更新："+resBundle.getString("content"));

        confirmDialog = new CustomDialog(AboutUsActivity.this)
                .builder()
                .setTitle("软件更新")
                .setView(layoutContent)
                .setNegativeButton("下次再说", new OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {}
                })
                .setPositiveButton("现在更新", new OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
//				downLoadApp();
				/*
				 * 改为隐藏到通知栏下载更新
				 */
                        initialNotificate();
                        downLoadApk();
                    }
                });
        confirmDialog.show();
    }
    /**
     * 方法名称：	initialNotificate
     * 方法描述：	初始化通知提示
     * 参数：
     * 返回值类型：	void
     * 创建时间：	2015-4-13下午12:41:22
     */
    private void initialNotificate()
    {
        noti = new Notification();
        noti.icon = R.drawable.bonfeel_icon;
        noti.tickerText = "正在下载 Bonfeel_V"+resBundle.getString("version");
        noti.when = System.currentTimeMillis();
        noti.contentView = new RemoteViews(getPackageName(), R.layout.layout_notification);
    }
    /**
     * 方法名称：	downLoadApk
     * 方法描述：	TODO
     * 参数：
     * 返回值类型：	void
     * 创建时间：	2015-4-13下午2:07:40
     */
    private void downLoadApk()
    {
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    File file = DownLoadUtil.getAPKFileFromServer(resBundle.getString("url"), handler);
                    Thread.sleep(2000);
                    //下载完成，开始安装
                    notiManager.cancel(1);
                    installAPK(file);
                }
                catch ( InterruptedException e )
                {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    /**
     * 方法名称：	downLoadApp
     * 方法描述：	下载安装包
     * 参数：
     * 返回值类型：	void
     * 创建时间：	2015-3-18下午3:16:35
     */
    private void downLoadApp()
    {
        final ProgressDialog pDialog;
        pDialog = new ProgressDialog(AboutUsActivity.this);
        pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pDialog.setMessage("正在下载更新");
        pDialog.show();
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    File file = DownLoadUtil.getAPKFileFromServer(resBundle.getString("url"), pDialog);
                    Thread.sleep(3000);
                    installAPK(file);
                    pDialog.dismiss();
                }
                catch ( InterruptedException e )
                {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    /**
     * 方法名称：	installAPK
     * 方法描述：	安装apk
     * 参数：			@param file
     * 返回值类型：	void
     * 创建时间：	2015-3-18下午3:53:55
     */
    private void installAPK(File file)
    {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        startActivity(intent);
    }
}
