package com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.util;

import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.model.Problem;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Author:FunFun
 * Function:常见问题的管理器
 * Date:2016/4/12 9:47
 */
public class ProblemManager extends Manager
{
    private static class ProblemHolder
    {
        static final ProblemManager PROBLEM_MANAGER = new ProblemManager();
    }
    public static ProblemManager getInstance()
    {
        return ProblemHolder.PROBLEM_MANAGER;
    }
    private Problem problem;
    private List<Problem> problems;
    private ProblemManager()
    {
        problems = new ArrayList<Problem>();
    }
    /* (非Javadoc)
     * 方法名称：	create
     * 方法描述：	TODO
     * 重写部分：	@see com.example.bonfeel.util.Manager#create()
     */
    @Override
    public Object create()
    {
        problem = new Problem( );
        return problem;
    }

    /* (非Javadoc)
     * 方法名称：	delete
     * 方法描述：	TODO
     * 重写部分：	@see com.example.bonfeel.util.Manager#delete(java.lang.Object)
     */
    @Override
    public void delete(Object item)
    {
    }

    /* (非Javadoc)
     * 方法名称：	add
     * 方法描述：	TODO
     * 重写部分：	@see com.example.bonfeel.util.Manager#add(java.lang.Object)
     */
    @Override
    public void add(Object item)
    {
        for (int i = 0; i < problems.size(); i++)
        {
            if ( ((Problem)item).getName().equals(problems.get(i).getName()) )
            {
                return;
            }
        }
        problems.add((Problem) item);
    }

    /* (非Javadoc)
     * 方法名称：	update
     * 方法描述：	TODO
     * 重写部分：	@see com.example.bonfeel.util.Manager#update(java.lang.Object)
     */
    @Override
    public void update(Object item)
    {
    }

    /* (非Javadoc)
     * 方法名称：	get
     * 方法描述：	TODO
     * 重写部分：	@see com.example.bonfeel.util.Manager#get(int)
     */
    @Override
    public Object get(int index)
    {
        return problems.get(index);
    }
    public Object get(String name)
    {
        int j = 0;
        for (int i = 0; i < problems.size(); i++)
        {
            if ( problems.get(i).getName().equals(name) )
            {
                j = i;
                break;
            }
        }
        return problems.get(j);
    }
    public List<Problem> getList()
    {
        return problems;
    }
    /* (non-Javadoc)
     * @see com.example.bonfeel.util.Manager#clean()
     */
    @Override
    public void clean()
    {
        problem = null;
        problems.clear();
    }

}
