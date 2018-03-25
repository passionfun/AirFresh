package com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.custom;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.R;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.model.TimeScale;

import java.util.List;

/**
 *
 * Author:FunFun
 * Function:时间段列表适配器
 * Date:2016/4/12 10:37
 */
public class TimeScaleAdapter extends ArrayAdapter<TimeScale> {
    private int resourceID;
    /**
     * 构造方法名称：	TimeScaleAdapter
     * 创建时间：		2015-2-3下午1:59:57
     * 方法描述：		TODO
     */
    public TimeScaleAdapter(Context context, int textViewResourceId,
                            List<TimeScale> objects)
    {
        super(context, textViewResourceId, objects);
        resourceID = textViewResourceId;
    }
    private class ViewHolder
    {
        ImageView ivTimeScaleIcon;
        TextView tvTimeScaleName;
        TextView tvTimeScaleStartStop;
        TextView tvTimeScaleWork;
        TextView tvTimeScaleRest;
    }
    /*
     * 方法名称：	getView
     * 方法描述：	TODOw.ViewGroup)
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        TimeScale timeScale = getItem(position);
        View view;
        ViewHolder viewHolder;
        if ( convertView == null )
        {
            view = LayoutInflater.from(getContext()).inflate(resourceID, null);
            viewHolder = new ViewHolder();
            viewHolder.tvTimeScaleName = (TextView)view.findViewById(R.id.tv_id_timescale);
            viewHolder.ivTimeScaleIcon = (ImageView)view.findViewById(R.id.iv_icon_timescale);
            viewHolder.tvTimeScaleStartStop = (TextView)view.findViewById(R.id.tv_start_stop_timescale);
            viewHolder.tvTimeScaleWork = (TextView)view.findViewById(R.id.tv_work_timescale);
            viewHolder.tvTimeScaleRest = (TextView)view.findViewById(R.id.tv_rest_timescale);
            view.setTag(viewHolder);
        }
        else
        {
            view = convertView;
            viewHolder = (ViewHolder)view.getTag();
        }
        viewHolder.tvTimeScaleName.setText("时间段"+(timeScale.getId()+1));
        if ( timeScale.isWork() )
        {
            viewHolder.ivTimeScaleIcon.setImageResource(R.drawable.timescale_item_on);
        }
        else
        {
            viewHolder.ivTimeScaleIcon.setImageResource(R.drawable.timescale_item_off);
        }
        viewHolder.tvTimeScaleStartStop.setText(timeScale.getStartTime()+"~"+timeScale.getEndTime());
        viewHolder.tvTimeScaleWork.setText("运行"+timeScale.getWorkTime()+"min");
        viewHolder.tvTimeScaleRest.setText("暂停"+timeScale.getRestTime()+"min");
        return view;
    }
}