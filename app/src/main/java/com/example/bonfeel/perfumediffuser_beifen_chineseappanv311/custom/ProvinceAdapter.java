package com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.custom;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import android.R;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.model.Provinces;

import java.util.List;

/**
 *
 * Author:FunFun
 * Function:省份列表适配器
 * Date:2016/4/12 10:31
 */
public class ProvinceAdapter extends ArrayAdapter<Provinces>
{
    private int resourceID;
    /**
     * 构造方法名称：	ProvinceAdapter
     * 创建时间：		2015-2-2下午1:26:47
     * 方法描述：		TODO
     */
    public ProvinceAdapter(Context context, int textViewResourceId,
                           List<Provinces> objects)
    {
        super(context, textViewResourceId, objects);
        resourceID = textViewResourceId;
    }
    private class ViewHodler
    {
        TextView tvProvicesName;
    }
    /* (非Javadoc)
     * 方法名称：	getView
     * 方法描述：	TODO
     * 重写部分：	@see android.widget.ArrayAdapter#getView(int, android.view.View, android.view.ViewGroup)
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        Provinces province = getItem(position);
        View view;
        ViewHodler viewHodler;
        if ( convertView == null )
        {
            view = LayoutInflater.from(getContext()).inflate(resourceID, null);
            viewHodler = new ViewHodler();
            viewHodler.tvProvicesName = (TextView)view.findViewById(R.id.text1);
            view.setTag(viewHodler);
        }
        else
        {
            view = convertView;
            viewHodler = (ViewHodler)view.getTag();
        }
        viewHodler.tvProvicesName.setText(province.getName());

        return view;
    }
}