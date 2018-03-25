package com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.custom;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.R;

import java.util.List;

/**
 *
 * Author:FunFun
 * Function:wifi适配器
 * Date:2016/4/12 10:45
 */
public class WifiAdapter extends ArrayAdapter<ScanResult>
{
    private int resourceId;
    /**
     * 构造方法名称：	WifiAdapter
     * 创建时间：		2015-3-19下午3:53:34
     * 方法描述：		TODO
     */
    public WifiAdapter(Context context, int resource, List<ScanResult> objects)
    {
        super(context, resource, objects);
        resourceId = resource;
    }
    private class ViewHolder
    {
        TextView tvSsid;
        TextView tvDistance;
        ImageView ivWifi;
    }
    /* (非Javadoc)
     * 方法名称：	getView
     * 方法描述：	TODO
     * 重写部分：	@see android.widget.ArrayAdapter#getView(int, android.view.View, android.view.ViewGroup)
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ScanResult result = getItem(position);
        View view;
        ViewHolder viewHolder;
        if ( convertView == null )
        {
            view = LayoutInflater.from(getContext()).inflate(resourceId, null);
            viewHolder = new ViewHolder();
            viewHolder.ivWifi = (ImageView)view.findViewById(R.id.iv_wifi_icon);
            viewHolder.tvSsid = (TextView)view.findViewById(R.id.tv_ssid_name);
            viewHolder.tvDistance = (TextView)view.findViewById(R.id.tv_distance);
            view.setTag(viewHolder);
        }
        else
        {
            view = convertView;
            viewHolder = (ViewHolder)view.getTag();
        }
        viewHolder.tvSsid.setText(result.SSID);
//		viewHolder.tvDistance.setText(String.valueOf(Math.abs(result.level)));
        viewHolder.tvDistance.setText("");
        //根据level的不同显示不同的信号强度图片
        if ( Math.abs(result.level) < 50 )
        {
            viewHolder.ivWifi.setImageResource(R.drawable.wifi_level_1);
//			viewHolder.tvDistance.setText("强");
        }
        else
        {
            if ( Math.abs(result.level) < 70 )
            {
                viewHolder.ivWifi.setImageResource(R.drawable.wifi_level_2);
//				viewHolder.tvDistance.setText("中");
            }
            else
            {
                if ( Math.abs(result.level) < 90 )
                {
                    viewHolder.ivWifi.setImageResource(R.drawable.wifi_level_3);
//					viewHolder.tvDistance.setText("弱");
                }
                else
                {
                    viewHolder.ivWifi.setImageResource(R.drawable.wifi_level_4);
//					viewHolder.tvDistance.setText("差");
                }
            }
        }
        return view;
    }
}
