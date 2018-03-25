package com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.custom;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.R;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.util.ApplicationUtil;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.util.LogUtil;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.util.NetCommunicationUtil;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.util.ToastUtil;

/**
 *
 * Author:FunFun
 * Function:百芬扩香仪设备的控制盘，在这控制设备的开关，查看香水的剩余量
 * Date:2016/4/12 10:24
 */
public class DevicePan extends View {
    private String tag = "设备盘";
    private double volume;
    private boolean power;
    private boolean operate;
    private boolean isLeave;
    private boolean isError;
    private int errorTag;

    private boolean getScale;
    private float scale;

    private Paint paint;
    private RectF rectF;
    private Bitmap pan;
    private Bitmap powerOn;
    private Bitmap powerOff;
    private Bitmap rulerValue;
    private Bitmap bitmapLoad ;

    private CustomDialog hintDialog;
    private View layoutHint;
    private TextView tvHintContent;
    private Context context;

    private float radiusParm;
    private float centerX, centerY;
    private float radius;
    private float startAngle, angle;

    private float panX, panY;
    private float powerX, powerY;
    private float rulerX, rulerY;
    private float downX;

    private OnPowerSetListener listener;
    private OnSliderListener sliderListener;
    private OnSliderTrackListener sliderTrackListener;

    private static double ANGLE_OFFSET = 2;
    private static double VOLUME_OFFSET = 0.84;

    // 手机自适应的分辨率参数
    private static int PARM_FOR_1440 = (int) (110 * 0.9);
    private static int PARM_FOR_1152_1920 = (int) (113 * 0.9);
    private static int PARM_FOR_1080_1920 = (int) (122 * 0.9);
    private static int PARM_FOR_1080_default = (int) (112 * 0.9);
    private static int PARM_FOR_1080_1812 = (int) (112 * 0.9);
    private static int PARM_FOR_1080_1800 = (int) (112 * 0.9);
    private static int PARM_FOR_720_1280 = (int) (82 * 0.9);
    private static int PARM_FOR_720_default = (int) (72 * 0.9);
    private static int PARM_FOR_540_960 = (int) (60 * 0.9);
    private static int PARM_FOR_480_800 = (int) (57 * 0.8);
    private static int PARM_FOR_480_854 = (int) (57 * 0.8);
    private static int PARM_FOR_480_default = (int) (53 * 0.8);

    /**
     * 构造方法名称：	DevicePan
     * 创建时间：		2015-1-20下午3:29:57
     * 方法描述：
     */
    public DevicePan(Context context) {
        // super(context);
        this(context, null);
        initialData();
        bitmapLoad = BitmapFactory.decodeResource(context.getResources(), R.drawable.ruler_value);
    }

    public DevicePan(Context context, AttributeSet attrs) {
        // super(context, attrs);
        this(context, attrs, 0);
        initialData();
        bitmapLoad = BitmapFactory.decodeResource(context.getResources(), R.drawable.ruler_value);
    }

    public DevicePan(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        LogUtil.logDebug(tag, "devicepan's construct");
        initialData();
        bitmapLoad = BitmapFactory.decodeResource(context.getResources(), R.drawable.ruler_value);
    }

    /**
     * 方法名称：	initialData
     * 方法描述：	初始化数据
     * 参数：
     * 返回值类型：	void
     * 创建时间：	2015-4-20下午7:58:16
     */
    private void initialData() {
        LogUtil.logDebug(tag, "devicepan's initialData");
        angle = 0;
        // 因为刻度的底脚是个圆弧，所以修正了2度，即百分之0.84的香水剩余量
        startAngle = (float) (150 - ANGLE_OFFSET);
        paint = new Paint();
        getScale = true;
        power = false;
        operate = false;
        isLeave = false;
        isError = false;
        errorTag = 0;
    }

    /**
     * 参数设置： context to set
     */
    public void setContext(Context context) {
        this.context = context;
    }

    /**
     * 参数设置： power to set
     */
    public void setPower(boolean power) {
        this.power = power;
        invalidate();
    }

    public void setLeave(boolean leave) {
        this.isLeave = leave;
    }

    public void setError(boolean error, int tag) {
        this.isError = error;
        this.errorTag = tag;
    }

    /**
     * 参数设置： volume to set
     */
    public void setVolume(double volume) {
        this.volume = volume + VOLUME_OFFSET;
        // 当香水剩余量为0时，修正百分之2的人为偏差
        if (volume < 0.005) {
            this.volume = 0;
        }
        // 当香水满瓶时，补充百分之2的偏差
        if (volume > 99.999) {
            this.volume = 100 + VOLUME_OFFSET * 2 + 1.6;
        }
        Log.i("devicepan", "volume:" + volume);
        // volume TO angle
        volumeToAngle();
        invalidate();
    }

    /**
     * 方法名称：	volumeToAngle
     * 方法描述：	将香水剩余量转化为angle
     * 参数：
     * 返回值类型：	void
     * 创建时间：	2015-1-26上午9:52:38
     */
    private void volumeToAngle() {
        angle = (float) (volume * 2.370);
    }

    /**
     * 方法名称：	getBitmapResource
     * 方法描述：	对图片资源做适应处理，使得适应屏幕大小
     * 参数：			@param bitmap
     * 参数：			@return
     * 返回值类型：	Bitmap
     * 创建时间：	2015-1-20下午2:24:25
     */
    private Bitmap getBitmapResource(int id) {
//		Bitmap bitmap = ((BitmapDrawable) getResources().getDrawable(id))
//				.getBitmap();
        Bitmap bitmap = BitmapFactory.decodeResource(ApplicationUtil.getContext().getResources(), id);
        Bitmap dest;
        Matrix matrix = new Matrix();
        matrix.reset();
        matrix.postScale(scale, scale);
        dest = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                bitmap.getHeight(), matrix, true);
        return dest;
    }

    /**
     * 方法名称：	setValue
     * 方法描述：	设置各个坐标点的值
     * 参数：
     * 返回值类型：	void
     * 创建时间：	2015-1-20下午2:58:36
     */
    private void setValue() {
        LogUtil.logWarn(tag, "devicepan's setValue");
        rulerX = getWidth() / 2 - rulerValue.getWidth() / 2;
        rulerY = getHeight() / 2 - rulerValue.getHeight() / 2;

        panX = getWidth() / 2 - pan.getWidth() / 2;
        panY = getHeight() / 2 - pan.getHeight() / 2;

        powerX = getWidth() / 2 - powerOff.getWidth() / 2;
        powerY = getHeight() / 2 - powerOff.getHeight() / 2;

        centerX = getWidth() / 2;
        centerY = getHeight() / 2;

        radius = powerOff.getWidth() / 2 + radiusParm;
    }

    /**
     * 方法名称：	drawVolumeArc
     * 方法描述：	绘制圆环
     * 参数：			@param canvas
     * 返回值类型：	void
     * 创建时间：	2015-1-20下午2:54:02
     */
    private void drawVolumeArc(Canvas canvas) {
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(radiusParm * 3);
        paint.setAntiAlias(true);

        rectF = new RectF(centerX - radius, centerY - radius, centerX + radius,
                centerY + radius);
        // 画圆环
        if (power) {
            // e0c37d
            paint.setColor(Color.rgb(224, 195, 125));
        } else {
            paint.setColor(Color.rgb(164, 162, 162));
        }
        canvas.drawArc(rectF, startAngle, angle, false, paint);
    }

    /**
     * 方法名称：	drawBackground
     * 方法描述：	绘制背景图片
     * 参数：			@param canvas
     * 返回值类型：	void
     * 创建时间：	2015-1-20下午2:22:22
     */
    private void drawBackground(Canvas canvas) {
        canvas.drawBitmap(rulerValue, rulerX, rulerY, null);
        canvas.drawBitmap(pan, panX, panY, null);
    }

    /**
     * 方法名称：	drawState
     * 方法描述：	根据设备的开关机状态更新界面
     * 参数：			@param canvas
     * 返回值类型：	void
     * 创建时间：	2015-3-6上午11:47:01
     */
    private void drawState(Canvas canvas) {
        if (power) {
            canvas.drawBitmap(powerOn, powerX, powerY, null);
        } else {
            canvas.drawBitmap(powerOff, powerX, powerY, null);
        }
    }

    /*
     * 方法名称：	onDraw
     * 方法描述：	绘图
     */
    @Override
    protected void onDraw(Canvas canvas) {
        LogUtil.logWarn(tag, "devicepan's onDraw");
        if (getScale) {
            initialLoad();
        }
        // 显示圆环
        drawVolumeArc(canvas);
        // 显示圆盘背景
        drawBackground(canvas);
        // 显示开关键
        drawState(canvas);
    }

    /**
     * 方法名称：	initialLoad
     * 方法描述：	首次加载资源
     * 参数：
     * 返回值类型：	void
     * 创建时间：	2015-4-21上午9:36:58
     */
    private void initialLoad() {
//		BitmapDrawable bitmapDrawable = ((BitmapDrawable) getResources()
//				.getDrawable(R.drawable.ruler_value));
//		Bitmap bitmap = bitmapDrawable.getBitmap();


//		//fun add
//		Bitmap bitmap = BitmapFactory.decodeResource(ApplicationUtil.getContext().getResources(), R.drawable.ruler_value);

        // 若view的尺寸小，则scale<1, 若view的尺寸大，则scale>1
        scale = (float) this.getWidth() / (float) bitmapLoad.getWidth();
        getPropableSize(ApplicationUtil.getScreen_width(),
                ApplicationUtil.getScreen_heigh());
        rulerValue = getBitmapResource(R.drawable.ruler_value);
        pan = getBitmapResource(R.drawable.device_pan);
        powerOn = getBitmapResource(R.drawable.power_on);
        powerOff = getBitmapResource(R.drawable.power_off);
        getScale = false;
        // 设置坐标
        setValue();
    }

    /**
     * 方法名称：	getPropableSize
     * 方法描述：	根据不同尺寸的手机做的自适应调整
     * 参数：			@param phoneSize
     * 返回值类型：	void
     * 创建时间：	2015-4-22上午10:49:07
     */
    private void getPropableSize(int phoneWidth, int phoneHeigh) {
        switch (phoneWidth) {
            case 1440:
                LogUtil.logDebug(tag, "the phone's definision is 1440*default");
                scale = (float) (scale * 0.9);
                radiusParm = PARM_FOR_1080_1920;
                break;
            case 1152:
                LogUtil.logDebug(tag, "the phone's definision is 1152*1920");
                scale = (float) (scale * 0.75);
                radiusParm = PARM_FOR_1152_1920;
                break;
            case 1080:
                switch (phoneHeigh) {
                    case 1800:
                        scale = (float) (scale * 0.78);
                        LogUtil.logDebug(tag, "the phone's definision is 1080*1800");
                        radiusParm = PARM_FOR_1080_1800;
                        break;
                    case 1812:
                        scale = (float) (scale * 0.8);
                        LogUtil.logDebug(tag, "the phone's definision is 1080*1812");
                        radiusParm = PARM_FOR_1080_1812;
                        break;
                    case 1920:
                        scale = (float) (scale * 0.9);
                        LogUtil.logDebug(tag, "the phone's definision is 1080*1920");
                        radiusParm = PARM_FOR_1080_1920;
                        break;
                    default:
                        scale = (float) (scale * 0.8);
                        LogUtil.logDebug(tag, "the phone's definision is 1080*default");
                        radiusParm = PARM_FOR_1080_default;
                        break;
                }
                break;
            case 720:
                switch (phoneHeigh) {
                    case 1280:
                        scale = (float) (scale * 0.9);
                        LogUtil.logDebug(tag, "the phone's definision is 720*1280");
                        radiusParm = PARM_FOR_720_1280;
                        break;
                    default:
                        scale = (float) (scale * 0.8);
                        LogUtil.logDebug(tag, "the phone's definision is 720*default");
                        radiusParm = PARM_FOR_720_default;
                        break;
                }
                break;
            case 480:
                switch (phoneHeigh) {
                    case 800:
                        scale = (float) (scale * 0.8);
                        LogUtil.logDebug(tag, "the phone's definision is 480*800");
                        radiusParm = PARM_FOR_480_800;
                        break;
                    case 854:
                        scale = (float) (scale * 0.8);
                        LogUtil.logDebug(tag, "the phone's definision is 480*854");
                        radiusParm = PARM_FOR_480_854;
                        break;
                    default:
                        scale = (float) (scale * 0.75);
                        LogUtil.logDebug(tag, "the phone's definision is 480*default");
                        radiusParm = PARM_FOR_480_default;
                        break;
                }
                break;
            case 540:
                switch (phoneHeigh) {
                    case 960:
                        scale = (float) (scale * 0.85);
                        LogUtil.logDebug(tag, "the phone's definision is 540*960");
                        radiusParm = PARM_FOR_540_960;
                        break;
                    default:
                        break;
                }
                break;
            default:
                scale = (float) (scale * 0.8);
                LogUtil.logDebug(tag, "the phone's definision is default");
                // 该参数需要参考到dpi
                radiusParm = PARM_FOR_480_800;
                break;
        }
    }

    /*
     * 方法名称：	onTouchEvent
     * 方法描述：
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // 监测按下的点是否在开关机范围内
                downX = event.getX();
                operate = isOperate(event.getX(), event.getY());
                break;
            case MotionEvent.ACTION_MOVE:
                if (!operate) {
                    if (sliderListener != null) {
                        sliderListener.OnGetSliderVelocity(event.getX() - downX);
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                // 若在开关机范围内，则执行开关机操作
                if (operate) {
                    if (isLeave || isError) {
                        if (isLeave) {
                            // 提示用户设备离线不能控制
//						Toast toast = Toast.makeText(context, "设备已离线，请检查网络",
//								Toast.LENGTH_SHORT);
//						toast.setGravity(Gravity.CENTER, 0, 0);
//						toast.show();
                            ToastUtil.showToast(context,"设备已离线，请检查网络");
                        } else {
                            // 提示用户设备故障不能控制
                            Toast toast = null;
                            switch (errorTag) {
                                case 1:
                                    toast = Toast.makeText(context, "设备故障:香水瓶不匹配",
                                            Toast.LENGTH_SHORT);
                                    break;
                                case 2:
                                    toast = Toast.makeText(context, "设备故障:电机故障",
                                            Toast.LENGTH_SHORT);
                                    break;
                                case 4:
                                    toast = Toast.makeText(context, "设备故障:香水用完",
                                            Toast.LENGTH_SHORT);
                                    break;
                            }
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                        }
                    } else {
                        if (NetCommunicationUtil.getNetConnectState()) {
                            tipDialog();
                        } else {
                            ToastUtil.showToast(context, "请确保网络连接");
                        }
                    }
                } else {
                    if (sliderTrackListener != null) {
                        sliderTrackListener.OnGetSliderTracker(
                                event.getX() - downX, event);
                    }
                }
                break;
        }
        return true;
    }

    /**
     * 方法名称：	setHintControlLayout
     * 方法描述：	设置开关键弹出提示框
     * 参数：			@param power
     * 返回值类型：	void
     * 创建时间：	2015-3-6下午1:46:47
     */
    private void setDialogLayout(boolean power) {
        layoutHint = LayoutInflater.from(ApplicationUtil.getContext()).inflate(
                R.layout.dialog_hint_control, null);
        tvHintContent = (TextView) layoutHint
                .findViewById(R.id.tv_hint_control);
        if (!power) {
            tvHintContent.setText("确认开启设备？");
        } else {
            tvHintContent.setText("确认关闭设备?");
        }
    }

    /**
     * 方法名称：	tipDialog
     * 方法描述：	显示提示对话框
     * 参数：
     * 返回值类型：	void
     * 创建时间：	2015-4-6下午12:33:59
     */
    private void tipDialog() {
        setDialogLayout(power);
        hintDialog = new CustomDialog(context).builder().setTitle("提示")
                .setView(layoutHint)
                .setNegativeButton("取消", new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                }).setPositiveButton("确定", new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!power) {
                            power = true;
                        } else {
                            power = false;
                        }
                        if (listener != null) {
                            listener.OnSetPower(power);
                        }
                        // 只有在点击确定之后才更新
                        invalidate();
                    }
                });
        hintDialog.show();
    }

    /**
     * 方法名称：	isOperate
     * 方法描述：	判断触摸点是否在可执行开关操作的圆形范围之内
     * 参数：			@param x
     * 参数：			@param y
     * 参数：			@return
     * 返回值类型：	boolean
     * 创建时间：	2015-1-20下午4:13:52
     */
    private boolean isOperate(float x, float y) {
        if ((Math.abs(x - centerX) * Math.abs(x - centerX) + Math.abs(y
                - centerY)* Math.abs(y - centerY)) < (powerOff.getWidth() / 2)
                * (powerOff.getWidth() / 2)) {
            return true;
        }
        return false;
    }

    /**
     * 方法名称：	setOnCallbackListener
     * 方法描述：	供外部使用
     * 参数：			@param listener
     * 返回值类型：	void
     * 创建时间：	2015-1-21下午3:07:59
     */
    public void setOnCallbackListener(OnPowerSetListener listener) {
        this.listener = listener;
    }

    public void setOnSliderListener(OnSliderListener listener) {
        this.sliderListener = listener;
    }

    public void setOnSliderTrackListener(OnSliderTrackListener listener) {
        this.sliderTrackListener = listener;
    }

    // 开关键值接口
    public interface OnPowerSetListener {
        public void OnSetPower(boolean power);
    }

    // 滑动接口
    public interface OnSliderListener {
        public void OnGetSliderVelocity(float distance);
    }

    // 滑动跟踪接口
    public interface OnSliderTrackListener {
        public void OnGetSliderTracker(float distance, MotionEvent event);
    }
}
