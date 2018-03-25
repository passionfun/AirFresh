package com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.custom;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.R;

/**
 *
 * Author:FunFun
 * Function:滑动开关
 * Date:2016/4/12 10:35
 */
public class SwitchButton extends View implements View.OnTouchListener {
    private Bitmap bgOn;
    private Bitmap bgOff;
    private Bitmap switchBtn;

    private float downX, nowX;
    private float locX, locY;
    private boolean isSlip;
    private boolean state;
    private OnChangeListener mListener;

    public interface OnChangeListener {
        public void OnChanged(boolean state);
    }

    /**
     * 构造方法名称：	SwitchButton
     * 创建时间：		2015-4-20下午3:46:33
     * 方法描述：		TODO
     */
    public SwitchButton(Context context) {
        this(context, null);
    }

    public SwitchButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SwitchButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialData();
    }

    private void initialData() {
        bgOn = BitmapFactory.decodeResource(getResources(),
                R.drawable.switch_buttom);
        bgOff = bgOn;
        switchBtn = BitmapFactory.decodeResource(getResources(),
                R.drawable.switch_btn);

        isSlip = false;
        state = false;

        setOnTouchListener(this);
    }

    /**
     * 方法名称：	setImageResource
     * 方法描述：	自定义开关背景
     * 参数：			@param background
     * 返回值类型：	void
     * 创建时间：	2015-5-12上午10:25:15
     */
    public void setImageResource(int background) {
        bgOn = BitmapFactory.decodeResource(getResources(), background);
        bgOff = bgOn;
    }

    /* (非Javadoc)
     * 方法名称：	onDraw
     * 方法描述：	TODO
     * 重写部分：	@see android.view.View#onDraw(android.graphics.Canvas)
     */
    @Override
    protected void onDraw(Canvas canvas) {
        locX = getWidth() / 2 - bgOn.getWidth() / 2;
        locY = getHeight() / 2 - bgOn.getHeight() / 2;
        Paint paint = new Paint();
        float x = locX;

        if (nowX < (bgOn.getWidth() / 2)) {
            canvas.drawBitmap(bgOff, locX, locY, paint);
        } else {
            canvas.drawBitmap(bgOn, locX, locY, paint);
        }
        if (isSlip) {
            // 向右滑动过界
            if (nowX >= bgOn.getWidth() + locX) {
                x = bgOn.getWidth() - switchBtn.getWidth() + locX;
            } else {
                x = nowX - switchBtn.getWidth() + locX;
            }
        } else {
            if (state) {
                x = bgOn.getWidth() - switchBtn.getWidth() + locX;
            } else {
                x = locX;
            }
        }
        if (x < locX) {
            x = locX;
        } else {
            if (x > bgOn.getWidth() - switchBtn.getWidth() + locX) {
                x = bgOn.getWidth() - switchBtn.getWidth() + locX;
            }
        }
        canvas.drawBitmap(switchBtn, x, locY, paint);
    }

    /* (非Javadoc)
     * 方法名称：	onTouch
     * 方法描述：	TODO
     * 重写部分：	@see android.view.View.OnTouchListener#onTouch(android.view.View, android.view.MotionEvent)
     */
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (event.getX() > (bgOff.getWidth() + locX) || event.getX() < locX) {
                    return false;
                } else {
                    isSlip = true;
                    downX = event.getX();
                    nowX = downX;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                nowX = event.getX();
                break;
            case MotionEvent.ACTION_UP:
                isSlip = false;
                if (event.getX() >= (getWidth() / 2)) {
                    state = true;
                    nowX = bgOn.getWidth() - switchBtn.getWidth();
                } else {
                    state = false;
                    nowX = 0;
                }
                if (mListener != null) {
                    mListener.OnChanged(state);
                }
                break;
            default:
                break;
        }
        invalidate();
        return true;
    }

    public void setChecked(boolean checked) {
        if (checked) {
            nowX = bgOff.getWidth() + locX;
        } else {
            nowX = locX;
        }
        state = checked;
        invalidate();
    }

    public void setOnChangeListener(OnChangeListener listener) {
        mListener = listener;
    }
}
