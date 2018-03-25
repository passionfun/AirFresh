package com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.View.OnClickListener;

import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.R;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.model.Device;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.util.ApplicationUtil;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.util.DeviceManager;

/**
 *
 * Author:FunFun
 * Function:设备设置界面
 * Date:2016/4/12 13:53
 */
public class TabThirdFragment extends Fragment {
    private String tag = "设置界面";
    private View tabLayout;

    private LinearLayout layout_setting;
    private TextView tvInfo;

    private Button btnSetBeep;
    private TextView tvSetName;
    private TextView tvTime;
    private TextView tvTimeScale;

    // 测试风速
    private Button btnWind;
    private EditText et_inputWind;

    private LinearLayout layoutName;
    private LinearLayout layoutTime;
    private LinearLayout layoutTimeScale;

    private LinearLayout layoutTemp;
    private LinearLayout layoutWeat;
    private LinearLayout layoutNIG;

    private boolean hasDevice;
    private Device device;

    /*
     * 方法名称： onCreate 方法描述： TODO
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initialData();
    }

    /**
     * 方法名称： initialData 方法描述： TODO 参数： 返回值类型： void 创建时间： 2015-1-21下午7:30:53
     */
    private void initialData() {
        hasDevice = false;
        for (int i = 0; i < DeviceManager.getInstance().getCount(); i++) {
            device = (Device) DeviceManager.getInstance().get(i);
            if (device.isSelect()) {
                hasDevice = true;
                break;
            } else {
                device = null;
            }
        }
    }

    /*
     * 方法名称： onCreateView 方法描述：
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        tabLayout = inflater.inflate(R.layout.fragment_tab3, container, false);
        initialView(tabLayout);
        initialHandler();
        return tabLayout;
    }

    /**
     * 方法名称： initialView 方法描述： TODO 参数： @param v 返回值类型： void 创建时间：
     * 2015-1-21下午7:31:08
     */
    private void initialView(View v) {
        layout_setting = (LinearLayout) v.findViewById(R.id.layout_setting);
        tvInfo = (TextView) v.findViewById(R.id.tv_infos_nodevice);

        // 蜂鸣器控件（暂时不用）
        btnSetBeep = (Button) v.findViewById(R.id.btn_set_beep);
        tvSetName = (TextView) v.findViewById(R.id.tv_set_device_name);
        tvTime = (TextView) v.findViewById(R.id.tv_set_time);
        tvTimeScale = (TextView) v.findViewById(R.id.tv_set_timescale);

        // 风速测试
        // btnWind=(Button) v.findViewById(R.id.btn_wind);
        // et_inputWind=(EditText) v.findViewById(R.id.et_inputWind);

        layoutName = (LinearLayout) v.findViewById(R.id.layout_name);
        layoutTime = (LinearLayout) v.findViewById(R.id.layout_time);
        layoutTimeScale = (LinearLayout) v.findViewById(R.id.layout_timescale);

        layoutTemp = (LinearLayout) v.findViewById(R.id.layout_temprature);
        layoutWeat = (LinearLayout) v.findViewById(R.id.layout_weat);
        layoutNIG = (LinearLayout) v.findViewById(R.id.layout_nig);

        showUIState();
    }

    /**
     * 方法名称： showUIState 方法描述： 显示界面 参数： 返回值类型： void 创建时间： 2015-1-22上午10:44:24
     */
    private void showUIState() {
        if (hasDevice) {
            layout_setting.setVisibility(View.VISIBLE);
            tvInfo.setVisibility(View.GONE);
            tvSetName.setText(device.getName());
            // 蜂鸣器设置（暂时不要）
            // btnSetBeep.setText((device.isBeep() == true)?"开":"关");
        } else {
            layout_setting.setVisibility(View.INVISIBLE);
            tvInfo.setVisibility(View.VISIBLE);
        }
    }

    public void setDevice(Device d) {
        device = d;
        if (d != null) {
            device.setSelect(true);
            for (int i = 0; i < DeviceManager.getInstance().getCount(); i++) {
                Device de = (Device) DeviceManager.getInstance().get(i);
                ;
                if (de.getId() != device.getId()) {
                    de.setSelect(false);
                }
            }
            hasDevice = true;
        } else {
            hasDevice = false;
        }
        showUIState();
    }

    /**
     * 方法名称： initialHandler 方法描述： TODO 参数： 返回值类型： void 创建时间： 2015-1-21下午7:31:22
     */
    private void initialHandler() {
        // 设置设备名称
        layoutName.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // 对话框
                Intent setName = new Intent(getActivity(),
                        SetDeviceNameActivity.class);
                startActivity(setName);
            }
        });
        // 蜂鸣器设置（暂时不要）
        btnSetBeep.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        // 时间校准
        layoutTime.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // 时间校准界面
                Intent checkTime = new Intent(ApplicationUtil.getContext(),
                        CheckTimeActivity.class);
                startActivity(checkTime);
            }
        });
        // 时间段设置
        layoutTimeScale.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // 时间段设置界面
                Intent setTimeScale = new Intent(ApplicationUtil.getContext(),
                        SetTimeScaleActivity.class);
                startActivity(setTimeScale);
            }
        });
        // 查看温度界面
        layoutTemp.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent checkTemp = new Intent(ApplicationUtil.getContext(),
                        SurroundTempActivity.class);
                startActivity(checkTemp);
            }
        });
        // 查看湿度界面
        layoutWeat.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent checkWeat = new Intent(ApplicationUtil.getContext(),
                        SurroundWeatActivity.class);
                startActivity(checkWeat);
            }
        });
        // 负离子发生器开关(暂不需要)
        layoutNIG.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent NIG = new Intent(ApplicationUtil.getContext(),
                        NIGActivity.class);
                startActivity(NIG);
            }
        });

        // 风速测试
		/*
		 * btnWind.setOnClickListener(new OnClickListener() {
		 *
		 * @Override public void onClick(View arg0) { String windNum =
		 * et_inputWind.getText().toString().trim(); if
		 * (TextUtils.isEmpty(windNum)) { et_inputWind.setText("");
		 * Toast.makeText(getActivity(), "输入不能为空，请重新输入……",
		 * Toast.LENGTH_LONG).show(); return; } int windRank =
		 * Integer.parseInt(windNum); if (windRank > -1 && windRank < 11) { //
		 * 处理不同档位的风速 NetCommunicationUtil.controlDevice(
		 * BonfeelFrame.setWindRank(windRank), device.getMac(),
		 * NetElectricsConst.ControlDevice); et_inputWind.setText(""); } else {
		 * et_inputWind.setText(""); Toast.makeText(getActivity(),
		 * "请输入风力等级数字（0-10）：0高，10低", Toast.LENGTH_LONG).show(); return; } } });
		 */

    }

    /*
     * (非Javadoc) 方法名称： onResume 方法描述： TODO 重写部分： @see
     * android.app.Fragment#onResume()
     */
    @Override
    public void onResume() {
        if (hasDevice) {
            tvSetName.setText(device.getName());
        }
        super.onResume();
    }
}