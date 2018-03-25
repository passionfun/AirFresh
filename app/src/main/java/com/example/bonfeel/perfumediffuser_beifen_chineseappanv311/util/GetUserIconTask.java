package com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.util;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;

import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.model.User;

/**
 *
 * Author:FunFun
 * Function:获取用户头像
 * Date:2016/4/12 9:33
 */
public class GetUserIconTask extends AsyncTask<User, Integer, Integer>
{
    private User user;
    private Handler handler;
    private static final int GET_USER_ICON_SUCCESS = 6;

    /**
     * 构造方法名称：	GetUserIconTask
     * 创建时间：		2015-4-9下午8:21:34
     * 方法描述：		TODO
     */
    public GetUserIconTask(Handler handler)
    {
        super();
        this.handler = handler;
    }

    /* (非Javadoc)
     * 方法名称：	onPostExecute
     * 方法描述：	TODO
     * 重写部分：	@see android.os.AsyncTask#onPostExecute(java.lang.Object)
     */
    @Override
    protected void onPostExecute(Integer result)
    {
        Message message = handler.obtainMessage();
        message.what = GET_USER_ICON_SUCCESS;
        handler.sendMessage(message);
        super.onPostExecute(result);
    }

    /* (非Javadoc)
     * 方法名称：	doInBackground
     * 方法描述：	TODO
     * 重写部分：	@see android.os.AsyncTask#doInBackground(Params[])
     */
    @Override
    protected Integer doInBackground(User... params)
    {
        user = params[0];

        user.setIcon(DownLoadUtil.getIconOnline(user.getIconUrl()));

        return GET_USER_ICON_SUCCESS;
    }

}

