package com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.R;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.custom.CityAdapter;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.model.Citys;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.model.Device;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.util.DeviceManager;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.util.XmlAnalyseUtil;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Author:FunFun
 * Function:选择城市
 * Date:2016/4/12 11:24
 */
public class LocCityActivity extends BaseActivity
{
    private TextView tvTitle;
    private ListView lvCity;

    private String title;
    private int pId;
    private List<Citys> listAllCity;
    private List<Citys> listCity;
    private Device device;

    private CityAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        setContentView(R.layout.activity_loc_city);
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
        title = "选择城市";
        pId = 0;

        listAllCity = new ArrayList<Citys>();
        listCity = new ArrayList<Citys>();
        XmlAnalyseUtil.parseXMLWithSAX("cities.xml");
        listAllCity = XmlAnalyseUtil.getListCitys();

        pId = getIntent().getIntExtra("pId", 1);

        for (int i = 0; i < listAllCity.size(); i++)
        {
            //将当前选择的省份的城市筛选出来
            if ( pId == listAllCity.get(i).getpId() )
            {
                listCity.add(listAllCity.get(i));
            }
        }

        for (int i = 0; i < DeviceManager.getInstance().getCount(); i++)
        {
            device = (Device) DeviceManager.getInstance().get(i);
            if ( device.isSelect() )
            {
                break;
            }
        }

        adapter = new CityAdapter(this, android.R.layout.simple_list_item_1, listCity);
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
        lvCity = (ListView)findViewById(R.id.lv_city);

        tvTitle.setText(title);
        lvCity.setAdapter(adapter);
    }

    /* (非Javadoc)
     * 方法名称：	initialHandler
     * 方法描述：	TODO
     * 重写部分：	@see com.example.bonfeel.activity.BaseActivity#initialHandler()
     */
    @Override
    protected void initialHandler()
    {
        lvCity.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id)
            {
                Intent countyIntent = new Intent(LocCityActivity.this, LocCountyActivity.class);
                countyIntent.putExtra("cId", listCity.get(position).getId());
                countyIntent.putExtra("city", getIntent().getStringExtra("province")+"-"+listCity.get(position).getName());
                startActivity(countyIntent);
            }
        });
    }
}
