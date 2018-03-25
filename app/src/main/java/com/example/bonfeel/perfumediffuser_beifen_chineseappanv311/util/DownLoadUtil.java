package com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.util;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 *
 * Author:FunFun
 * Function:下载工具
 * Date:2016/4/12 9:25
 */
public class DownLoadUtil {
    /**
     * 方法名称：	getAPKFileFromServer
     * 方法描述：	采用后台下载
     * 参数：			@param urlPath
     * 参数：			@param handler
     * 参数：			@return
     * 返回值类型：	File
     * 创建时间：	2015-4-13下午7:46:29
     */
    public static File getAPKFileFromServer(String urlPath, Handler handler)
    {
        File file = null;
        if ( Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) )
        {
            try
            {
                URL url = new URL(urlPath);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setConnectTimeout(5000);
                InputStream inStream = connection.getInputStream();

                if ( connection.getContentLength() <= 0 )
                {
                    return null;
                }
                if ( inStream == null )
                {
                    return null;
                }
                Message message = handler.obtainMessage();
                message.what = 6;
                message.arg1 = connection.getContentLength();
                handler.handleMessage(message);

                file = new File(Environment.getExternalStorageDirectory(), "bonfeel.apk");
                FileOutputStream foutStream = new FileOutputStream(file);
                BufferedInputStream binStream = new BufferedInputStream(inStream);
                byte[] buffer = new byte[5120];
                int len;
                int total = 0;
                message = handler.obtainMessage();
                while ((len = binStream.read(buffer)) != -1)
                {
                    foutStream.write(buffer, 0, len);
                    total += len;
                    try
                    {
                        Thread.sleep(400);
                    }
                    catch ( InterruptedException e )
                    {
                        e.printStackTrace();
                    }
                    message.what = 5;
                    message.arg1 = total;
                    handler.handleMessage(message);
                }
                foutStream.close();
                binStream.close();
                inStream.close();
            }
            catch ( MalformedURLException e )
            {
                e.printStackTrace();
            }
            catch ( IOException e )
            {
                e.printStackTrace();
            }
            return file;
        }
        else
        {
            return null;
        }
    }
    /**
     * 方法名称：	getAPKFileFromServer
     * 方法描述：	从服务器下载apk(对话框下载)
     * 参数：			@param urlPath
     * 参数：			@param pd
     * 参数：			@return
     * 返回值类型：	File
     * 创建时间：	2015-3-18下午3:38:17
     */
    public static File getAPKFileFromServer(String urlPath, ProgressDialog pd)
    {
        File file = null;
        if ( Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) )
        {
            try
            {
                URL url = new URL(urlPath);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setConnectTimeout(5000);
                //获取文件大小
                pd.setMax(connection.getContentLength());
                InputStream inStream = connection.getInputStream();
                file = new File(Environment.getExternalStorageDirectory(), "bonfeel.apk");
                FileOutputStream foutStream = new FileOutputStream(file);
                BufferedInputStream binStream = new BufferedInputStream(inStream);
                byte[] buffer = new byte[1024];
                int len;
                int total = 0;
                while ((len = binStream.read(buffer)) != -1)
                {
                    foutStream.write(buffer, 0, len);
                    total += len;
                    pd.setProgress(total);
                }
                foutStream.close();
                binStream.close();
                inStream.close();
            }
            catch ( MalformedURLException e )
            {
                e.printStackTrace();
            }
            catch ( IOException e )
            {
                e.printStackTrace();
            }
            return file;
        }
        else
        {
            return null;
        }
    }
    /**
     * 方法名称：	getSceneIcon
     * 方法描述：	联网在线获取头像图片资源
     * 参数：			@param path
     * 参数：			@return
     * 返回值类型：	Bitmap
     * 创建时间：	2015-3-31下午2:21:48
     */
    public static Bitmap getIconOnline(final String path)
    {
        Bitmap bitmap = null;
        URL url = null;
        InputStream inStream;
        try
        {
            url = new URL(path);
            inStream = url.openStream();
            bitmap = BitmapFactory.decodeStream(inStream);
            inStream.close();
        }
        catch ( MalformedURLException e )
        {
            e.printStackTrace();
        }
        catch ( IOException e )
        {
            e.printStackTrace();
        }
        return bitmap;
    }
}
