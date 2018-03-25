package com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.model;

/**
 *
 * Author:FunFun
 * Function:意见反馈的消息实体
 * Date:2016/4/12 8:46
 */
public class ChatMsg {
    private int who;
    private String msg;
    /**
     * 返回值： who
     */
    public int getWho()
    {
        return who;
    }
    /**
     * 参数设置： who to set
     */
    public void setWho(int who)
    {
        this.who = who;
    }
    /**
     * 返回值： msg
     */
    public String getMsg()
    {
        return msg;
    }
    /**
     * 参数设置： msg to set
     */
    public void setMsg(String msg)
    {
        this.msg = msg;
    }
}
