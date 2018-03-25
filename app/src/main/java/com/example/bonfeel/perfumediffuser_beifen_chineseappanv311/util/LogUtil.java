package com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.util;

import android.util.Log;

/**
 *
 * Author:FunFun
 * Function:调式工具（打印日志）
 * Date:2016/4/12 9:41
 */
public class LogUtil {
    public static final int VERBOSE = 1;
    public static final int DEBUG = 2;
    public static final int INFO = 3;
    public static final int WARN = 4;

    public static final int LEVEL = DEBUG;

    @SuppressWarnings("unused")
    public static void logVerbose(String tag, String msg)
    {
        if ( LEVEL <= VERBOSE )
        {
            Log.v(tag, msg);
        }
    }
    public static void logDebug(String tag, String msg)
    {
        if ( LEVEL <= DEBUG )
        {
            Log.d(tag, msg);
        }
    }
    public static void logInfo(String tag, String msg)
    {
        if ( LEVEL <= INFO )
        {
            Log.i(tag, msg);
        }
    }
    public static void logWarn(String tag, String msg)
    {
        if ( LEVEL <= WARN )
        {
            Log.w(tag, msg);
        }
    }
}
