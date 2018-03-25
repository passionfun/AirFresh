package com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.R;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.custom.ProvinceAdapter;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.model.Device;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.model.Provinces;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.util.DeviceManager;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.util.XmlAnalyseUtil;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Author:FunFun
 * Function:选择省份
 * Date:2016/4/12 11:27
 */
public class LocProvinceActivity extends BaseActivity
{
    private TextView tvTitle;
    private ListView lvProvince;

    private String title;
    private List<Provinces> listProvince;
    private ProvinceAdapter adapter;

    private Device device;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        setContentView(R.layout.activity_loc_province);
        super.onCreate(savedInstanceState);
    }

    /* (非Javadoc)
     * 方法名称：	initialData
     * 方法描述：	TODO
     * 重写部分：	@see com.example.bonfeel.activity.BaseActivity#initialData()
     */
    @Override
    protected void initialData()
    {
        title = "选择省份";
        listProvince = new ArrayList<Provinces>();
        XmlAnalyseUtil.parseXMLWithSAX("provinces.xml");
        listProvince = XmlAnalyseUtil.getListProvinces();

        adapter = new ProvinceAdapter(this, android.R.layout.simple_list_item_1, listProvince);

        for (int i = 0; i < DeviceManager.getInstance().getCount(); i++)
        {
            device = (Device) DeviceManager.getInstance().get(i);
            if ( device.isSelect() )
            {
                break;
            }
        }
    }

    /* (非Javadoc)
     * 方法名称：	initialView
     * 方法描述：	TODO
     * 重写部分：	@see com.example.bonfeel.activity.BaseActivity#initialView()
     */
    @Override
    protected void initialView()
    {
        tvTitle = (TextView)findViewById(R.id.tv_title);
        lvProvince = (ListView)findViewById(R.id.lv_province);

        tvTitle.setText(title);
        lvProvince.setAdapter(adapter);
    }

    /* (非Javadoc)
     * 方法名称：	initialHandler
     * 方法描述：	TODO
     * 重写部分：	@see com.example.bonfeel.activity.BaseActivity#initialHandler()
     */
    @Override
    protected void initialHandler()
    {
        lvProvince.setOnItemClickListener(new OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id)
            {
                Intent cityIntent = new Intent(LocProvinceActivity.this, LocCityActivity.class);
                cityIntent.putExtra("pId", position+1);
                cityIntent.putExtra("province", listProvince.get(position).getName());
                startActivity(cityIntent);
            }
        });
    }
}
