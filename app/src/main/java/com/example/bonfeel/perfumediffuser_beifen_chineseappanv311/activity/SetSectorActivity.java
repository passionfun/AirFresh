package com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.View.OnClickListener;


import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.R;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.custom.CustomDialog;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.custom.NumberAdapter;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.custom.WeeklyBar;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.custom.WheelView;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.model.Device;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.model.TimeScale;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.util.DeviceManager;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.util.LogUtil;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.util.NetCommunicationUtil;

/**
 *
 * Author:FunFun
 * Function:各个时间段设置界面
 * Date:2016/4/12 11:57
 */
public class SetSectorActivity extends BaseActivity {
    private TextView tvTitle;
    private TextView tvStartTime;
    private TextView tvStopTime;
    private LinearLayout layoutWorkTime;
    private TextView tvWorkTime;
    private LinearLayout layoutRestTime;
    private TextView tvRestTime;

    private Button btnStart;
    private Button btnCancle;
    private WheelView wvWorkRest;
    private WheelView wvStartStopHour;
    private WheelView wvStartStopMin;
    private CustomDialog dlgWorkRest;
    private CustomDialog dlgStartStop;
    private View layoutWorkRest;
    private View layoutStartStop;
    private WeeklyBar weeklyBar;

    private String title;
    private Device device;
    private int index;
    private TimeScale timeScale;
    private int startHour, startMin, stopHour, stopMin;

    private Button btnBack;

    // 暂时不用的两个变量
    // private TextView tvAirSpeed;
    // private ImageView ivSetAirSpeed;
    // private Dialog diaSetAirSpeed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_set_sector);
        super.onCreate(savedInstanceState);
    }

    /*
     * 方法名称：	initialData
     * 方法描述：	TODO
     */
    @Override
    protected void initialData() {
        for (int i = 0; i < DeviceManager.getInstance().getCount(); i++) {
            device = (Device) DeviceManager.getInstance().get(i);
            if (device.isSelect()) {
                break;
            }
        }
        index = getIntent().getIntExtra("scaleId", 0);
        title = "时间段" + (index + 1);
        // 当前要设置的时间段
        timeScale = device.getTimeScale(index);
        LogUtil.logDebug(getTag(), "choose the timescale id is：" + index);
    }

    /*
     * 方法名称：	initialView
     * 方法描述：	TODO
     */
    @Override
    protected void initialView() {
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvStartTime = (TextView) findViewById(R.id.tv_start);
        tvStopTime = (TextView) findViewById(R.id.tv_stop);
        tvWorkTime = (TextView) findViewById(R.id.tv_set_work_time);
        layoutWorkTime = (LinearLayout) findViewById(R.id.layout_work_time);
        tvRestTime = (TextView) findViewById(R.id.tv_set_rest_time);
        layoutRestTime = (LinearLayout) findViewById(R.id.layout_rest_time);
        btnStart = (Button) findViewById(R.id.btn_start);
        btnCancle = (Button) findViewById(R.id.btn_close);
        weeklyBar = (WeeklyBar) findViewById(R.id.weeklyBar_circle);
        btnBack = (Button) findViewById(R.id.btn_left);

        // 暂时不用
        // tvAirSpeed = (TextView)findViewById(R.id.tv_airspeed);
        // ivSetAirSpeed = (ImageView)findViewById(R.id.iv_set_sector_airspeed);
        // tvAirSpeed.setText(getAirSpeed(timeScale.getAirSpeed()));

        tvTitle.setText(title);
        tvStartTime.setText(timeScale.getStartTime());
        tvStopTime.setText(timeScale.getEndTime());
        tvWorkTime.setText(timeScale.getWorkTime() + "min");
        tvRestTime.setText(timeScale.getRestTime() + "min");
        weeklyBar.setCircle(timeScale.getCircle());
    }

    /**
     * 方法名称：	getAirSpeed
     * 方法描述：	根据风速值，显示风速档位
     * 参数：			@param index
     * 参数：			@return
     * 返回值类型：	String
     * 创建时间：	2015-1-23上午9:03:12
     */
    private String getAirSpeed(int index) {
        String speed = "";
        switch (index) {
            case 1:
                speed = "低速";
                break;
            case 2:
                speed = "中速";
                break;
            case 3:
                speed = "中高速";
                break;
            case 4:
                speed = "高速";
                break;
        }
        return speed;
    }

    /**
     * 方法名称：	setWorkRestWheel
     * 方法描述：	加载运行时长，暂停时长的滚轮
     * 参数：
     * 返回值类型：	void
     * 创建时间：	2015-2-4下午1:18:32
     */
    private void setWorkRestWheel(boolean workrest) {
        layoutWorkRest = LayoutInflater.from(SetSectorActivity.this).inflate(
                R.layout.wheel_work_rest, null);
        wvWorkRest = (WheelView) layoutWorkRest
                .findViewById(R.id.wheelView_work_rest);
        if (workrest) {
            wvWorkRest.setAdapter(new NumberAdapter(1, 10));// 之前是10，现改为99现在又改为10
            wvWorkRest.setCurrentItem(timeScale.getWorkTime() - 1);
        } else {
            wvWorkRest.setAdapter(new NumberAdapter(1, 99));
            wvWorkRest.setCurrentItem(timeScale.getRestTime() - 1);
        }
        wvWorkRest.setCyclic(true);
        wvWorkRest.setLabel("min");
        wvWorkRest.TEXT_SIZE = 50;
    }

    /**
     * 方法名称：	setStartStopWheel
     * 方法描述：	加载起始时间，结束时间的滚轮
     * 参数：
     * 返回值类型：	void
     * 创建时间：	2015-2-4下午2:02:40
     */
    private void setStartStopWheel(boolean onoff) {
        layoutStartStop = LayoutInflater.from(SetSectorActivity.this).inflate(
                R.layout.wheel_start_stop, null);

        wvStartStopHour = (WheelView) layoutStartStop
                .findViewById(R.id.wheelView_hour);
        wvStartStopHour.setAdapter(new NumberAdapter(0, 23));
        wvStartStopHour.setCyclic(true);
        wvStartStopHour.setLabel("时");
        wvStartStopHour.TEXT_SIZE = 50;

        wvStartStopMin = (WheelView) layoutStartStop
                .findViewById(R.id.wheelView_minite);
        wvStartStopMin.setAdapter(new NumberAdapter(0, 59));
        wvStartStopMin.setCyclic(true);
        wvStartStopMin.setLabel("分");
        wvStartStopMin.TEXT_SIZE = 50;

        getSetTime();
        if (onoff) {
            wvStartStopHour.setCurrentItem(startHour);
            wvStartStopMin.setCurrentItem(startMin);
        } else {
            wvStartStopHour.setCurrentItem(stopHour);
            wvStartStopMin.setCurrentItem(stopMin);
        }
    }

    /*
     * 方法名称：	initialHandler
     * 方法描述：	TODO
     */
    @Override
    protected void initialHandler() {
        // 设置风速（暂时不用）
        // ivSetAirSpeed.setOnClickListener(new OnClickListener()
        // {
        //
        // @Override
        // public void onClick(View v)
        // {}
        // });
        btnBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtil.logDebug("back button", "back back back");
                NetCommunicationUtil.setStep(3);
                finish();
            }
        });
        // 起始时间
        tvStartTime.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setStartStopWheel(true);

                dlgStartStop = new CustomDialog(SetSectorActivity.this)
                        .builder().setTitle("开始时间").setView(layoutStartStop)
                        .setNegativeButton("取消", new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                            }
                        }).setPositiveButton("确定", new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                setStandTime(1);
                            }
                        });
                dlgStartStop.show();
            }
        });
        // 结束时间
        tvStopTime.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setStartStopWheel(false);

                dlgStartStop = new CustomDialog(SetSectorActivity.this)
                        .builder().setTitle("结束时间").setView(layoutStartStop)
                        .setNegativeButton("取消", new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                            }
                        }).setPositiveButton("确定", new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                setStandTime(0);
                            }
                        });
                dlgStartStop.show();
            }
        });
        // 运行时长
        layoutWorkTime.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // 先加载对话框内容部分的布局
                setWorkRestWheel(true);

                dlgWorkRest = new CustomDialog(SetSectorActivity.this)
                        .builder().setTitle("运行时长").setView(layoutWorkRest)
                        .setNegativeButton("取消", new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                            }
                        }).setPositiveButton("确定", new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                tvWorkTime.setText((wvWorkRest.getCurrentItem() + 1)
                                        + "min");
                            }
                        });
                dlgWorkRest.show();
            }
        });
        // 暂停时长
        layoutRestTime.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // 先加载对话框内容部分的布局
                setWorkRestWheel(false);

                dlgWorkRest = new CustomDialog(SetSectorActivity.this)
                        .builder().setTitle("暂停时长").setView(layoutWorkRest)
                        .setNegativeButton("取消", new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                            }
                        }).setPositiveButton("确定", new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                tvRestTime.setText((wvWorkRest.getCurrentItem() + 1)
                                        + "min");
                            }
                        });
                dlgWorkRest.show();
            }
        });
        // 开启响应
        btnStart.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkCorrect()) {
                    // 更新起始时间，结束时间，运行时长，暂停时长，周期
                    updateTimeScale();
                    if (NetCommunicationUtil.getStep() == 1) {
                        NetCommunicationUtil.setStep(2);
                        LogUtil.logDebug(getTag(), "back to FirstTagFragment");
                    } else {
                        LogUtil.logDebug(getTag(),
                                "back to SetTimeScaleActivity");
                        // 该操作用于更新时间段列表的数据
                        Intent intent = new Intent();
                        intent.putExtra("scaleId", index);
                        setResult(RESULT_OK, intent);
                    }
                    finish();
                }
            }
        });
        // 取消响应
        btnCancle.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                NetCommunicationUtil.setStep(3);
                finish();
            }
        });
    }

    /**
     * 方法名称：	checkCorrect
     * 方法描述：	检查时间设置的合理性
     * 参数：			@return
     * 返回值类型：	boolean
     * 创建时间：	2015-3-6上午8:53:19
     */
     private boolean checkCorrect() {
                    getSetTime();
                    if (startHour < stopHour) {
                        return true;
                    } else {
                        if (startHour == stopHour) {
                            if (startMin < stopMin) {
                                return true;
                            } else {
                    // 分钟上的大于等于
                    showInfo("时间段只能在同一天内");
                    return false;
                }
            } else {
                // 时钟上的大于
                showInfo("时间段只能在同一天内");
                return false;
            }
        }
    }

    /**
     * 方法名称：	getSetTime
     * 方法描述：	获取设置的时间
     * 参数：
     * 返回值类型：	void
     * 创建时间：	2015-3-6上午9:15:51
     */
    private void getSetTime() {
        String startTime = tvStartTime.getText().toString().trim();
        for (int i = 0; i < startTime.length(); i++) {
            if (startTime.charAt(i) == ':') {
                startHour = Integer.parseInt(startTime.substring(0, i));
                startMin = Integer.parseInt(startTime.substring(i + 1,
                        startTime.length()));
            }
        }
        String stopTime = tvStopTime.getText().toString().trim();
        for (int i = 0; i < stopTime.length(); i++) {
            if (stopTime.charAt(i) == ':') {
                stopHour = Integer.parseInt(stopTime.substring(0, i));
                stopMin = Integer.parseInt(stopTime.substring(i + 1,
                        stopTime.length()));
            }
        }
    }

    /**
     * 方法名称：	updateTimeScale
     * 方法描述：	时间段设置完成，更新该时间段数据
     * 参数：
     * 返回值类型：	void
     * 创建时间：	2015-2-10下午1:55:22
     */
    private void updateTimeScale() {
        timeScale.setStartTime(tvStartTime.getText().toString());
        timeScale.setEndTime(tvStopTime.getText().toString());
        timeScale.setWorkTime(Integer.parseInt(tvWorkTime.getText().toString()
                .substring(0, tvWorkTime.getText().toString().length() - 3)));
        timeScale.setRestTime(Integer.parseInt(tvRestTime.getText().toString()
                .substring(0, tvRestTime.getText().toString().length() - 3)));
        timeScale.setCircle(weeklyBar.getCircle());
    }

    /**
     * 方法名称：	setStandTime
     * 方法描述：	将起始时间和结束时间转换为标准的四位时间
     * 参数：
     * 返回值类型：	void
     * 创建时间：	2015-2-10下午1:37:04
     */
    private void setStandTime(int startorstop) {
        int hour = wvStartStopHour.getCurrentItem();
        String strHour;
        int min = wvStartStopMin.getCurrentItem();
        String strMin;
        if (hour < 10) {
            strHour = "0" + hour;
        } else {
            strHour = hour + "";
        }
        if (min < 10) {
            strMin = "0" + min;
        } else {
            strMin = min + "";
        }
        switch (startorstop) {
            // 起始时间
            case 1:
                tvStartTime.setText(strHour + ":" + strMin);
                break;
            // 结束时间
            case 0:
                tvStopTime.setText(strHour + ":" + strMin);
                break;
        }
    }

    /* (非Javadoc)
     * 方法名称：	dispatchKeyEvent
     * 方法描述：	TODO
     * 重写部分：	@see android.app.Activity#dispatchKeyEvent(android.view.KeyEvent)
     */
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            NetCommunicationUtil.setStep(3);
            finish();
            return true;
        }
        return super.dispatchKeyEvent(event);
    }

    /* (非Javadoc)
     * 方法名称：	onDestroy
     * 方法描述：	TODO
     * 重写部分：	@see android.app.Activity#onDestroy()
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
