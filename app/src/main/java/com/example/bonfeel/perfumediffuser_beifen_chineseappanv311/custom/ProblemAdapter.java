package com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.custom;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.R;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.model.Problem;

import java.util.List;

/**
 *
 * Author:FunFun
 * Function:常见问题适配器
 * Date:2016/4/12 10:30
 */
public class ProblemAdapter extends ArrayAdapter<Problem>
{
    private int resourceID;
    /**
     * 构造方法名称：	ProblemAdapter
     * 创建时间：		2015-3-5下午4:02:22
     * 方法描述：		TODO
     */
    public ProblemAdapter(Context context, int textViewResourceId,
                          List<Problem> objects)
    {
        super(context, textViewResourceId, objects);
        resourceID = textViewResourceId;
    }
    private class ViewHolder
    {
        TextView tvProblemName;
    }
    /* (非Javadoc)
     * 方法名称：	getView
     * 方法描述：	TODO
     * 重写部分：	@see android.widget.ArrayAdapter#getView(int, android.view.View, android.view.ViewGroup)
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        Problem problem = getItem(position);
        View view;
        ViewHolder viewHolder;
        if ( convertView == null )
        {
            view = LayoutInflater.from(getContext()).inflate(resourceID, null);
            viewHolder = new ViewHolder();
            viewHolder.tvProblemName = (TextView)view.findViewById(R.id.tv_problem_name);
            view.setTag(viewHolder);
        }
        else
        {
            view = convertView;
            viewHolder = (ViewHolder)view.getTag();
        }
        viewHolder.tvProblemName.setText(problem.getName());

        return view;
    }

}
