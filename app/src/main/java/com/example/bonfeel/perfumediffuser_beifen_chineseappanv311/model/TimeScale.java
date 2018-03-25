package com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.model;

/**
 *
 * Author:FunFun
 * Function:时间段
 * Date:2016/4/12 9:00
 */
public class TimeScale {
    private int id;
    private String startTime;//起始时间
    private String endTime;//终止时间
    private int airSpeed;//风速(暂不需要)
    private int workTime;//运行时长
    private int restTime;//暂停时长
    private byte circle;//定时周期
    private boolean isWork;//开启/关闭标志

    /**
     * 构造方法名称：	TimeScale
     * 创建时间：		2015-1-23上午8:55:13
     * 方法描述：
     */
    public TimeScale(int id, String startTime, String endTime)
    {
        this.id = id;
        this.startTime = startTime;
        this.endTime = endTime;
        this.airSpeed = 1;
        this.workTime = 3;
        this.restTime = 1;
        this.circle = 0x7f;
        this.isWork = false;
    }

    /**
     * 构造方法名称：	TimeScale
     * 创建时间：		2015-2-6下午2:28:09
     * 方法描述：		TODO
     */
    public TimeScale()
    {
        super();
        this.startTime = "00:00";
        this.endTime = "00:00";
        this.airSpeed = 0;
        this.workTime = 0;
        this.restTime = 0;
        this.circle = 0x00;
        this.isWork = false;
    }

    /**
     * 返回值： id
     */
    public int getId()
    {
        return id;
    }
    /**
     * 参数设置： id to set
     */
    public void setId(int id)
    {
        this.id = id;
    }
    /**
     * 返回值： startTime
     */
    public String getStartTime()
    {
        return startTime;
    }
    /**
     * 参数设置： startTime to set
     */
    public void setStartTime(String startTime)
    {
        this.startTime = startTime;
    }
    /**
     * 返回值： endTime
     */
    public String getEndTime()
    {
        return endTime;
    }
    /**
     * 参数设置： endTime to set
     */
    public void setEndTime(String endTime)
    {
        this.endTime = endTime;
    }
    /**
     * 返回值： airSpeed
     */
    public int getAirSpeed()
    {
        return airSpeed;
    }
    /**
     * 参数设置： airSpeed to set
     */
    public void setAirSpeed(int airSpeed)
    {
        this.airSpeed = airSpeed;
    }
    /**
     * 返回值： workTime
     */
    public int getWorkTime()
    {
        return workTime;
    }
    /**
     * 参数设置： workTime to set
     */
    public void setWorkTime(int workTime)
    {
        this.workTime = workTime;
    }
    /**
     * 返回值： restTime
     */
    public int getRestTime()
    {
        return restTime;
    }
    /**
     * 参数设置： restTime to set
     */
    public void setRestTime(int restTime)
    {
        this.restTime = restTime;
    }

    /**
     * 返回值： circle
     */
    public byte getCircle()
    {
        return circle;
    }

    /**
     * 参数设置： circle to set
     */
    public void setCircle(byte circle)
    {
        this.circle = circle;
    }

    /**
     * 返回值： isWork
     */
    public boolean isWork()
    {
        return isWork;
    }

    /**
     * 参数设置： isWork to set
     */
    public void setWork(boolean isWork)
    {
        this.isWork = isWork;
    }
}
