package com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Toast;

/**
 *
 * Author:FunFun
 * Function:活动基类, 自定义了一些方法，便于代码编写的逻辑划分
 * Date:2016/4/12 11:05
 */
public abstract class BaseActivity extends Activity
{
    private String name;
    /*
     * 方法名称：	onCreate
     * 方法描述：	活动初始化
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        name = getClass().getSimpleName();
        initialData();
        initialView();
        initialHandler();
    }
    /**
     * 返回值： name
     */
    public String getTag()
    {
        return name;
    }
    /**
     * 方法名称：	showInfo
     * 方法描述：	子类中用于显示相关信息
     * 参数：			@param info
     * 返回值类型：	void
     * 创建时间：	2015-1-19上午10:22:37
     */
    public void showInfo(String info)
    {
        Toast toast = Toast.makeText(this, info, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
    //初始化数据
    abstract protected void initialData();
    //初始化视图
    abstract protected void initialView();
    //初始化事件处理
    abstract protected void initialHandler();

}
