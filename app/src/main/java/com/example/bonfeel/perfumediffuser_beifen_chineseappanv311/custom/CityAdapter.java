package com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.custom;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import android.R;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.model.Citys;

import java.util.List;

/**
 *
 * Author:FunFun
 * Function:城市列表适配器
 * Date:2016/4/12 10:19
 */
public class CityAdapter extends ArrayAdapter<Citys>
{
    private int resourceID;
    /**
     * 构造方法名称：	CityAdapter
     * 创建时间：		2015-2-2下午1:42:53
     * 方法描述：		TODO
     */
    public CityAdapter(Context context, int textViewResourceId,
                       List<Citys> objects)
    {
        super(context, textViewResourceId, objects);
        resourceID = textViewResourceId;
    }
    private class ViewHodler
    {
        TextView tvCitysName;
    }
    /* (非Javadoc)
     * 方法名称：	getView
     * 方法描述：	TODO
     * 重写部分：	@see android.widget.ArrayAdapter#getView(int, android.view.View, android.view.ViewGroup)
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        Citys city = getItem(position);
        View view;
        ViewHodler viewHodler;
        if ( convertView == null )
        {
            view = LayoutInflater.from(getContext()).inflate(resourceID, null);
            viewHodler = new ViewHodler();
            viewHodler.tvCitysName = (TextView)view.findViewById(R.id.text1);
            view.setTag(viewHodler);
        }
        else
        {
            view = convertView;
            viewHodler = (ViewHodler)view.getTag();
        }
        viewHodler.tvCitysName.setText(city.getName());

        return view;
    }

}
