package com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.util;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;

import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.model.Scene;

import java.util.List;

/**
 *
 * Author:FunFun
 * Function:在线加载场景头像
 * Date:2016/4/12 9:32
 */
public class GetSceneIconTask extends AsyncTask<List<Scene>, Integer, Integer>
{
    private List<Scene> scenes;
    private Handler handler;
    private int count;
    private static final int GET_SCENE_ICON_SUCCESS = 13;
    /**
     * 构造方法名称：	GetSceneIconTask
     * 创建时间：		2015-4-9下午12:18:10
     * 方法描述：		TODO
     */
    public GetSceneIconTask(Handler handler)
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
        message.what = GET_SCENE_ICON_SUCCESS;
        handler.sendMessage(message);
        super.onPostExecute(result);
    }

    /* (非Javadoc)
     * 方法名称：	doInBackground
     * 方法描述：	TODO
     * 重写部分：	@see android.os.AsyncTask#doInBackground(Params[])
     */
    @Override
    protected Integer doInBackground(List<Scene>... params)
    {
        scenes = params[0];
        count = scenes.size();
        do
        {
            for (int i = 0; i < scenes.size(); i++)
            {
                if ( scenes.get(i).getIcon() == null )
                {
                    System.out.println("loading scene icon");
                    scenes.get(i).setIcon(DownLoadUtil.getIconOnline(scenes.get(i).getIconUrl()));
                }
                else
                {
                    count--;
                }
            }
            if ( count != 0 )
            {
                count = scenes.size();
            }
        } while (count > 0);
        System.out.println("finish loading scene icon");
        return GET_SCENE_ICON_SUCCESS;
    }
}

