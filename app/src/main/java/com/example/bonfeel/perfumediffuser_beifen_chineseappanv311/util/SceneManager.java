package com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.util;

import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.model.Scene;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Author:FunFun
 * Function:场景管理器，单例模式，管理场景的增，删，该，查
 * Date:2016/4/12 9:50
 */
public class SceneManager extends Manager
{
    //创建单例模式
    private static class SceneHolder
    {
        static final SceneManager SCENE_MANAGER = new SceneManager();
    }
    public static SceneManager getInstance()
    {
        return SceneHolder.SCENE_MANAGER;
    }
    private Scene scene;
    //场景列表
    private List<Scene> scenes;

    private SceneManager()
    {
        scenes = new ArrayList<Scene>();
    }

    @Override
    public Object create()
    {
        scene = new Scene();
        return scene;
    }

    @Override
    public void delete(Object item)
    {}

    @Override
    public void update(Object item)
    {}

    public Object get(String id)
    {
        int index = 0;
        if ( scenes == null )
        {
            return null;
        }
        for (int i = 0; i < scenes.size(); i++)
        {
            if ( scenes.get(i).getId().equals(id) )
            {
                index = i;
                break;
            }
        }
        return scenes.get(index);
    }
    public List<Scene> getScenes()
    {
        return scenes;
    }
    @Override
    public void add(Object item)
    {
        for (int i = 0; i < scenes.size(); i++)
        {
            if ( ((Scene) item).getId().equals(scenes.get(i).getId()) )
            {
                return;
            }
        }
        scenes.add((Scene)item);
    }

    /* (非Javadoc)
     * 方法名称：	get
     * 方法描述：	TODO
     * 重写部分：	@see com.example.bonfeel.util.Manager#get(int)
     */
    @Override
    public Object get(int index)
    {

        return null;
    }

    /* (non-Javadoc)
     * @see com.example.bonfeel.util.Manager#clean()
     */
    @Override
    public void clean()
    {
        scene = null;
        scenes.clear();
    }
}
