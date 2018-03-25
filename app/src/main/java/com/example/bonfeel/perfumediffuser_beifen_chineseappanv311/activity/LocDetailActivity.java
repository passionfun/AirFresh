package com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.View.OnClickListener;

import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.R;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.model.Device;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.util.DeviceManager;

/**
 *
 * Author:FunFun
 * Function:详细地址填写
 * Date:2016/4/12 11:26
 */
public class LocDetailActivity extends BaseActivity
{
    private TextView tvTitle;
    private LinearLayout layoutProvince;
    private TextView tvLocProvince;
    private LinearLayout layoutStreet;//暂时不用
    private TextView tvLocStree;//暂时不用
    private EditText etDetail;
    private Button btnSave;
    private Button btnBack;

    private String title;
    private Device device;
    private String loc;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        setContentView(R.layout.activity_loc_detail);
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
        title = "地址管理";

        for (int i = 0; i < DeviceManager.getInstance().getCount(); i++)
        {
            device = (Device) DeviceManager.getInstance().get(i);
            if ( device.isSelect() )
            {
                break;
            }
        }
        if ( getIntent().getStringExtra("county") != null )
        {
            loc = "";
            for (int i = 0; i < getIntent().getStringExtra("county").length(); i++)
            {
                if ( getIntent().getStringExtra("county").charAt(i) != '-' )
                {
                    loc += getIntent().getStringExtra("county").charAt(i);
                }
            }
        }
        else
        {
            loc = device.getLocation().getProvince()+device.getLocation().getCity()+device.getLocation().getCounty();
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
        layoutProvince = (LinearLayout)findViewById(R.id.layout_province);
        tvLocProvince = (TextView)findViewById(R.id.tv_location_province);
        layoutStreet = (LinearLayout)findViewById(R.id.layout_street);
        tvLocStree = (TextView)findViewById(R.id.tv_location_street);
        etDetail = (EditText)findViewById(R.id.et_location_detail);
        btnSave = (Button)findViewById(R.id.btn_save_location);
        btnBack = (Button)findViewById(R.id.btn_left);

        tvTitle.setText(title);
        tvLocProvince.setText(loc);
        etDetail.setText(device.getLocation().getDetail());
        etDetail.setSelection(device.getLocation().getDetail().length());
    }

    /* (非Javadoc)
     * 方法名称：	initialHandler
     * 方法描述：	TODO
     * 重写部分：	@see com.example.bonfeel.activity.BaseActivity#initialHandler()
     */
    @Override
    protected void initialHandler()
    {
        //修改省份，城市，区县
        layoutProvince.setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                Intent setLocation = new Intent(LocDetailActivity.this, LocProvinceActivity.class);
                startActivity(setLocation);
            }
        });
        //保存
        btnSave.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                int index = 0;
                String addr = "";
                if ( getIntent().getStringExtra("county") != null )
                {
                    for (int j = 0; j < getIntent().getStringExtra("county").length(); j++)
                    {
                        if ( getIntent().getStringExtra("county").charAt(j) == '-' )
                        {
                            index++;
                        }
                        else
                        {
                            addr += getIntent().getStringExtra("county").charAt(j);
                            if ( j == getIntent().getStringExtra("county").length() - 1 )
                            {
                                index++;
                            }
                        }
                        switch (index)
                        {
                            case 1:
                                device.getLocation().setProvince(addr);
                                addr = "";
                                index = 2;
                                break;
                            case 3:
                                device.getLocation().setCity(addr);
                                addr = "";
                                index = 4;
                                break;
                            case 5:
                                device.getLocation().setCounty(addr);
                                addr = "";
                                index = 6;
                                break;
                            default:
                                break;
                        }
                    }
                }
                device.getLocation().setDetail(etDetail.getText().toString());
                Intent back = new Intent(LocDetailActivity.this, SetDeviceNameActivity.class);
                startActivity(back);
                finish();
            }
        });
        //返回到更改名称界面
        btnBack.setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                Intent back = new Intent(LocDetailActivity.this, SetDeviceNameActivity.class);
                startActivity(back);
                finish();
            }
        });
    }
    /* (非Javadoc)
     * 方法名称：	onBackPressed
     * 方法描述：	TODO
     * 重写部分：	@see android.app.Activity#onBackPressed()
     */
    @Override
    public void onBackPressed()
    {
        Intent back = new Intent(LocDetailActivity.this, SetDeviceNameActivity.class);
        startActivity(back);
        finish();
        super.onBackPressed();
    }
}
