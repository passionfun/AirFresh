package com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.util;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

/**
 *
 * Author:FunFun
 * Function:
 * Date:2016/4/12 9:51
 */

public class ToastUtil {
    private static String oldMsg;
    private static long oneTime = 0;
    private static long twoTime = 0;
    private static Toast mToast = null;

    public static void showToast(Context context, String s) {
        if (mToast == null) {
            mToast = Toast.makeText(context, s, Toast.LENGTH_SHORT);
            mToast.setGravity(Gravity.CENTER, 0, 0);
            mToast.show();
            oneTime = System.currentTimeMillis();
        } else {
            twoTime = System.currentTimeMillis();
            if (s.equals(oldMsg)) {
                if (twoTime - oneTime > Toast.LENGTH_SHORT) {
                    mToast.show();
                }
            } else {
                oldMsg = s;
                mToast.setText(s);
                mToast.show();
            }
        }
        oneTime = twoTime;
    }

    public static void showToast(Context context, int resId) {
        showToast(context, context.getString(resId));
    }
}
