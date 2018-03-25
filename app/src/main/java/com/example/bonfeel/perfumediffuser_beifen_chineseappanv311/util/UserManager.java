package com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.util;

import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.model.User;

/**
 *
 * Author:FunFun
 * Function:用户管理器，单例模式，管理用户的增，删，改，查
 * Date:2016/4/12 9:51
 */
public class UserManager extends Manager
{
    //创建单例模式
    private static class UserHolder
    {
        static final UserManager USER_MANAGER = new UserManager();
    }
    public static UserManager getInstance()
    {
        return UserHolder.USER_MANAGER;
    }
    private UserManager()
    {}

    private User user;

    @Override
    public Object create()
    {
        user = new User();
        return user;
    }
    @Override
    public void delete(Object item)
    {}
    @Override
    public void update(Object item)
    {
        user = (User) item;
    }
    @Override
    public Object get(int index)
    {
        return user;
    }

    @Override
    public void add(Object item)
    {}
    /* (non-Javadoc)
     * @see com.example.bonfeel.util.Manager#clean()
     */
    @Override
    public void clean()
    {
        user = null;
    }
}
