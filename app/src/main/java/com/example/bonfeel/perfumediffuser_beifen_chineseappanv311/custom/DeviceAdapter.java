package com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.custom;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.R;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.model.Device;

import java.util.List;

/**
 *
 * Author:FunFun
 * Function:设备列表的item容器
 * Date:2016/4/12 10:23
 */
public class DeviceAdapter extends ArrayAdapter<Device> {
    private int resourceID;

    /**
     * 构造方法名称：	DeviceAdapter
     * 创建时间：		2015-1-21上午10:16:30
     * 方法描述：		TODO
     */
    public DeviceAdapter(Context context, int textViewResourceId,
                         List<Device> objects) {
        super(context, textViewResourceId, objects);
        resourceID = textViewResourceId;
    }

    private class ViewHolder {
        TextView tvDeviceName;
        TextView tvIsOnline;
    }

    /*
     * 方法名称：	getView
     * 方法描述：	TODO
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Device device = getItem(position);
        View view;
        ViewHolder viewHolder;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceID, null);
            viewHolder = new ViewHolder();
            viewHolder.tvDeviceName = (TextView) view
                    .findViewById(R.id.tv_dname);
            viewHolder.tvIsOnline = (TextView) view
                    .findViewById(R.id.tv_device_isonline);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.tvDeviceName.setText(device.getName());
        // 设备在离线的显示
        if (device.isLeave()) {
            viewHolder.tvIsOnline.setText("离线");
            viewHolder.tvIsOnline.setTextColor(Color.rgb(102, 102, 102));
        } else {
            viewHolder.tvIsOnline.setText("在线");
            viewHolder.tvIsOnline.setTextColor(Color.rgb(43, 153, 31));
        }
        // 设备是否故障的显示
        if (device.getError() != 0) {
            viewHolder.tvDeviceName.setTextColor(Color.rgb(255, 0, 0));
        } else {
            viewHolder.tvDeviceName.setTextColor(Color.rgb(102, 102, 102));
        }
        return view;
    }
}
