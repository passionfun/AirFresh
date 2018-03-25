package com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.model;

/**
 *
 * Author:FunFun
 * Function:问题
 * Date:2016/4/12 8:57
 */
public class Problem {
    private String name;
    private String content;

    /**
     * 构造方法名称：	Problem
     * 创建时间：		2015-3-5下午4:01:51
     * 方法描述：		TODO
     */
    public Problem( )
    {
        super();
        this.name = "";
        this.content = "";
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
     * 返回值： content
     */
    public String getContent()
    {
        return content;
    }
    /**
     * 参数设置： content to set
     */
    public void setContent(String content)
    {
        this.content = content;
    }
}
