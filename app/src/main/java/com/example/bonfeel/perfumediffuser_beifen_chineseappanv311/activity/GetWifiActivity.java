package com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.activity;

import android.content.Intent;
import android.net.wifi.ScanResult;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;


import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.R;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.custom.WifiAdapter;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.util.EasyLinkWifiManager;

import java.util.List;

/**
 *
 * Author:FunFun
 * Function:
 * Date:2016/4/12 11:23
 */
public class GetWifiActivity extends BaseActivity
{
    private TextView tvTitle;
    private ListView lvWifi;

    private WifiAdapter adapter;
    private List<ScanResult> wifiResults;
    private EasyLinkWifiManager wifiManager;
    private String title;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        setContentView(R.layout.activity_get_wifi);
        super.onCreate(savedInstanceState);
    }

    /* (非Javadoc)
     * 方法名称：	initialData
     * 方法描述：	TODO
     * 重写部分：	@see com.example.easylinkclient.activity.BaseActivity#initialData()
     */
    @Override
    protected void initialData()
    {
        title = "网络选择";
        wifiManager = new EasyLinkWifiManager(this);
        wifiResults = wifiManager.getWifiList();
        adapter = new WifiAdapter(this, R.layout.wifi_item, wifiResults);
    }

    /* (非Javadoc)
     * 方法名称：	initialView
     * 方法描述：	TODO
     * 重写部分：	@see com.example.easylinkclient.activity.BaseActivity#initialView()
     */
    @Override
    protected void initialView()
    {
        tvTitle = (TextView)findViewById(R.id.tv_title);
        lvWifi = (ListView)findViewById(R.id.lv_wifi);

        tvTitle.setText(title);
        lvWifi.setAdapter(adapter);
    }

    /* (非Javadoc)
     * 方法名称：	initialHandler
     * 方法描述：	TODO
     * 重写部分：	@see com.example.easylinkclient.activity.BaseActivity#initialHandler()
     */
    @Override
    protected void initialHandler()
    {
        lvWifi.setOnItemClickListener(new OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id)
            {
                ScanResult wifi = wifiResults.get(position);
                Intent intent = new Intent();
                intent.putExtra("ssid", wifi.SSID);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }
}
