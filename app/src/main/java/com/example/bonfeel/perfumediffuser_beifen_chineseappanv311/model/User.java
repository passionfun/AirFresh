package com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.model;

import android.graphics.Bitmap;

/**
 *
 * Author:FunFun
 * Function:用户类
 * Date:2016/4/12 9:01
 */
public class User {
    private String phone;
    private String password;
    private String phoneMac;
    private String email;
    private String address;
    private String name;
    private Bitmap icon;
    private String iconUrl;
    /**
     * 构造方法名称：	User
     * 创建时间：		2015-1-15上午9:21:57
     */
    public User()
    {
        this.phone = "";
        this.password = "";
        this.phoneMac = "";
        this.email = "";
        this.address = "";
        this.name = "";
        this.icon = null;
        this.iconUrl = "";
    }
    /**
     * 返回值： phone
     */
    public String getPhone()
    {
        return phone;
    }
    /**
     * 参数设置： phone to set
     */
    public void setPhone(String phone)
    {
        this.phone = phone;
    }
    /**
     * 返回值： password
     */
    public String getPassword()
    {
        return password;
    }
    /**
     * 参数设置： password to set
     */
    public void setPassword(String password)
    {
        this.password = password;
    }
    /**
     * 返回值： phoneMac
     */
    public String getPhoneMac()
    {
        return phoneMac;
    }
    /**
     * 参数设置： phoneMac to set
     */
    public void setPhoneMac(String phoneMac)
    {
        this.phoneMac = phoneMac;
    }
    /**
     * 返回值： email
     */
    public String getEmail()
    {
        return email;
    }
    /**
     * 参数设置： email to set
     */
    public void setEmail(String email)
    {
        this.email = email;
    }
    /**
     * 返回值： address
     */
    public String getAddress()
    {
        return address;
    }
    /**
     * 参数设置： address to set
     */
    public void setAddress(String address)
    {
        this.address = address;
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
}
