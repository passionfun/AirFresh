package com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.util;

import android.app.Activity;
import android.app.Application;
import android.app.Dialog;
import android.content.Context;
import android.os.Environment;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.R;

/**
 *
 * Author:FunFun
 * Function:用于获取全局context
 * Date:2016/4/12 9:03
 */
public class ApplicationUtil extends Application {
    /*
	 * Global application context.
	 */
    private static Context context;
    private static int screen_width;
    private static int screen_heigh;

    @Override
    public void onCreate()
    {
        super.onCreate();
        context = getApplicationContext();
    }
    /**
     *Get the global application context.
     *
     * @return Application context.
     * @throws
     *
     */
    public static Context getContext()
    {
        if ( context == null )
        {
            throw new GlobalException(GlobalException.APPLICATION_CONTEXT_IS_NULL);
        }
        return context;
    }

    /* (非Javadoc)
     * 方法名称：	onLowMemory
     * 方法描述：	TODO
     * 重写部分：	@see android.app.Application#onLowMemory()
     */
    @Override
    public void onLowMemory()
    {
        super.onLowMemory();
        context = getApplicationContext();
    }
    /**
     * 返回值： screen_width
     */
    public static int getScreen_width()
    {
        screen_width = context.getResources().getDisplayMetrics().widthPixels;
        LogUtil.logDebug("ApplicationUtil", "手机分辨率_宽"+screen_width);
        return screen_width;
    }
    /**
     * 返回值： screen_heigh
     */
    public static int getScreen_heigh()
    {
        screen_heigh = context.getResources().getDisplayMetrics().heightPixels;
        LogUtil.logDebug("ApplicationUtil", "手机分辨率_高"+screen_heigh);
        return screen_heigh;
    }
    public static boolean hasSdcard()
    {
        String state = Environment.getExternalStorageState();
        if ( state.equals(Environment.MEDIA_MOUNTED) )
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    /**
     * 方法名称：	getLoadingDialog
     * 方法描述：	自定义进度加载框
     * 参数：			@param context
     * 参数：			@param msg
     * 参数：			@return
     * 返回值类型：	Dialog
     * 创建时间：	2015-3-27下午12:41:23
     */
    public static Dialog getLoadingDialog(Context context, String msg)
    {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.loading_dialog	, null);
        LinearLayout layout = (LinearLayout)view.findViewById(R.id.layout_loading);
        ImageView ivLoading = (ImageView)view.findViewById(R.id.img);
        TextView tvTip = (TextView)view.findViewById(R.id.tipTextView);

        Animation animation = AnimationUtils.loadAnimation(context, R.anim.loading_animation);
        //匀速
        LinearInterpolator lir = new LinearInterpolator();
        animation.setInterpolator(lir);
        ivLoading.startAnimation(animation);
        tvTip.setText(msg);

        Dialog loadingDialog = new Dialog(context, R.style.loading_dialog);
        loadingDialog.setCancelable(true);
        loadingDialog.setCanceledOnTouchOutside(false);
        loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        return loadingDialog;
    }
    /**
     * 方法名称：	vibrate
     * 方法描述：	震动操作
     * 参数：			@param activity
     * 参数：			@param time
     * 返回值类型：	void
     * 创建时间：	2015-4-16下午8:12:23
     */
    public static void vibrate(Activity activity, long time)
    {
        Vibrator vibrator = (Vibrator) activity.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(time);
    }
}
