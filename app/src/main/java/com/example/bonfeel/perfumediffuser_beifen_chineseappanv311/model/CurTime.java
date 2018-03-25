package com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.model;

/**
 *
 * Author:FunFun
 * Function:当前时间（包括年，月，日，时间）
 * Date:2016/4/12 8:50
 */
public class CurTime {
    private int year;
    private int month;
    private int date;
    private String time;
    /**
     * 构造方法名称：	CurTime
     * 创建时间：		2015-1-29下午2:10:15
     */
    public CurTime()
    {
        super();
        this.year = 2015;
        this.month = 01;
        this.date = 01;
        this.time = "00:00";
    }
    /**
     * 返回值： year
     */
    public int getYear()
    {
        return year;
    }
    /**
     * 参数设置： year to set
     */
    public void setYear(int year)
    {
        this.year = year;
    }
    /**
     * 返回值： month
     */
    public int getMonth()
    {
        return month;
    }
    /**
     * 参数设置： month to set
     */
    public void setMonth(int month)
    {
        this.month = month;
    }
    /**
     * 返回值： date
     */
    public int getDate()
    {
        return date;
    }
    /**
     * 参数设置： date to set
     */
    public void setDate(int date)
    {
        this.date = date;
    }
    /**
     * 返回值： time
     */
    public String getTime()
    {
        return time;
    }
    /**
     * 参数设置： time to set
     */
    public void setTime(String time)
    {
        this.time = time;
    }
}
