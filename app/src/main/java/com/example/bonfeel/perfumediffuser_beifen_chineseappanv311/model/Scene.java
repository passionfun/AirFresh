package com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.model;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Author:FunFun
 * Function:情景类
 * Date:2016/4/12 8:59
 */
public class Scene {
    private String id;//场景编号
    private String name;//场景名称
    private Bitmap icon;//场景头像
    private String iconUrl;//头像路径
    private List<Device> deviceList;//设备列表

    private boolean isSelect;//是否被开启
    private boolean isDelete;//标识场景是否删除
    /**
     * 构造方法名称：	Scene
     * 创建时间：		2015-1-15下午4:24:47
     */
    public Scene()
    {
        this.id = "";
        this.name = "";
        this.icon = null;
        this.iconUrl = "";
        this.deviceList = new ArrayList<Device>();
        this.isSelect = false;
        this.isDelete = false;
    }

    /**
     * 返回值： id
     */
    public String getId()
    {
        return id;
    }

    /**
     * 参数设置： id to set
     */
    public void setId(String id)
    {
        this.id = id;
    }

    /**
     * 返回值： isDelete
     */
    public boolean isDelete()
    {
        return isDelete;
    }

    /**
     * 参数设置： isDelete to set
     */
    public void setDelete(boolean isDelete)
    {
        this.isDelete = isDelete;
    }

    /**
     * 返回值： name
     */
    public String getName()
    {
        return name;
    }
    /**
     * 参数设置： name to set
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * 返回值： iconUrl
     */
    public String getIconUrl()
    {
        return iconUrl;
    }

    /**
     * 参数设置： iconUrl to set
     */
    public void setIconUrl(String iconUrl)
    {
        this.iconUrl = iconUrl;
    }

    /**
     * 返回值： icon
     */
    public Bitmap getIcon()
    {
        return icon;
    }
    /**
     * 参数设置： icon to set
     */
    public void setIcon(Bitmap icon)
    {
        this.icon = icon;
    }
    /**
     * 返回值： deviceList
     */
    public List<Device> getDeviceList()
    {
        if ( deviceList == null )
        {
            deviceList = new ArrayList<Device>();
        }
        return deviceList;
    }
    /**
     * 参数设置： deviceList to set
     */
    public void setDeviceList(List<Device> deviceList)
    {
        this.deviceList = deviceList;
    }

    /**
     * 返回值： isSelect
     */
    public boolean isSelect()
    {
        return isSelect;
    }

    /**
     * 参数设置： isSelect to set
     */
    public void setSelect(boolean isSelect)
    {
        this.isSelect = isSelect;
    }
}
