package com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.R;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.custom.CountyAdapter;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.model.Countys;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.model.Device;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.util.DeviceManager;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.util.XmlAnalyseUtil;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Author:FunFun
 * Function:选择县区
 * Date:2016/4/12 11:25
 */
public class LocCountyActivity extends BaseActivity
{
    private TextView tvTitle;
    private ListView lvCounty;

    private String title;
    private int cId;
    private List<Countys> listAllCounty;
    private List<Countys> listCounty;
    private Device device;

    private CountyAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        setContentView(R.layout.activity_loc_county);
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
        title = "选择县区";
        cId = 0;

        listAllCounty = new ArrayList<Countys>();
        listCounty = new ArrayList<Countys>();
        XmlAnalyseUtil.parseXMLWithSAX("districts.xml");
        listAllCounty = XmlAnalyseUtil.getListCountys();

        cId = getIntent().getIntExtra("cId", 1);

        for (int i = 0; i < listAllCounty.size(); i++)
        {
            if ( cId == listAllCounty.get(i).getcId() )
            {
                listCounty.add(listAllCounty.get(i));
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
        adapter = new CountyAdapter(this, android.R.layout.simple_list_item_1, listCounty);
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
        lvCounty = (ListView)findViewById(R.id.lv_county);

        tvTitle.setText(title);
        lvCounty.setAdapter(adapter);
    }

    /* (非Javadoc)
     * 方法名称：	initialHandler
     * 方法描述：	TODO
     * 重写部分：	@see com.example.bonfeel.activity.BaseActivity#initialHandler()
     */
    @Override
    protected void initialHandler()
    {
        lvCounty.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id)
            {
                Intent detail = new Intent(LocCountyActivity.this, LocDetailActivity.class);
                detail.putExtra("county", getIntent().getStringExtra("city")+"-"+listCounty.get(position).getName());
                startActivity(detail);
            }
        });
    }
}