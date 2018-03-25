package com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.custom;

import android.app.Dialog;
import android.content.Context;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.R;

/**
 *
 * Author:FunFun
 * Function:自定义控件
 * Date:2016/4/12 10:21
 */
public class CustomDialog {
    private Context context;
    private Display display;

    private Dialog dialog;
    private LinearLayout layoutBackground;
    private TextView tvTitle;
    private TextView tvMsg;
    private EditText etResult;
    private LinearLayout layoutContent;
    private ImageView ivBottom;
    private Button btnCancel;
    private Button btnOk;
    private ImageView ivSpiner;

    private boolean showTitle = false;
    private boolean showMsg = false;
    private boolean showEditText = false;
    private boolean showLayout = false;
    private boolean showPosBtn = false;
    private boolean showNegBtn = false;

    /**
     * 构造方法名称：	MyAlertDialog
     * 创建时间：		2015-2-4上午11:04:17
     * 方法描述：		TODO
     */
    public CustomDialog(Context context) {
        this.context = context;
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        display = windowManager.getDefaultDisplay();
    }

    /**
     * 方法名称：	builder
     * 方法描述：	自定义对话框上面的控件初始化
     * 参数：			@return
     * 返回值类型：	MyAlertDialog
     * 创建时间：	2015-2-4上午11:04:52
     */
    public CustomDialog builder() {
        // 获取对话框的布局
        View view = LayoutInflater.from(context).inflate(
                R.layout.layou_custom_dialog, null);

        layoutBackground = (LinearLayout) view
                .findViewById(R.id.layout_background);

        tvTitle = (TextView) view.findViewById(R.id.tv_title);
        tvTitle.setVisibility(View.GONE);

        tvMsg = (TextView) view.findViewById(R.id.tv_msg);
        tvMsg.setVisibility(View.GONE);

        etResult = (EditText) view.findViewById(R.id.et_result);
        etResult.setVisibility(View.GONE);

        layoutContent = (LinearLayout) view.findViewById(R.id.layout_content);
        layoutContent.setVisibility(View.GONE);

        ivBottom = (ImageView) view.findViewById(R.id.iv_bottom);

        btnCancel = (Button) view.findViewById(R.id.btn_cancel);
        btnCancel.setVisibility(View.GONE);

        btnOk = (Button) view.findViewById(R.id.btn_ok);
        btnOk.setVisibility(View.GONE);

        ivSpiner = (ImageView) view.findViewById(R.id.iv_line);
        ivSpiner.setVisibility(View.GONE);

        // 定义对话框的风格和布局
        dialog = new Dialog(context, R.style.CustomDialog);
        dialog.setContentView(view);

        // 设置对话框的宽度始终是屏幕宽的0.85，高度适配
        layoutBackground.setLayoutParams(new FrameLayout.LayoutParams(
                (int) (display.getWidth() * 0.80), LayoutParams.WRAP_CONTENT));
        return this;
    }

    /**
     * 方法名称：	setTitle
     * 方法描述：	设置标题
     * 参数：			@param title
     * 参数：			@return
     * 返回值类型：	MyAlertDialog
     * 创建时间：	2015-2-4上午11:07:53
     */
    public CustomDialog setTitle(String title) {
        // 需要显示title
        showTitle = true;
        if ("".equals(title)) {
            tvTitle.setText("Title");
        } else {
            tvTitle.setText(title);
        }
        return this;
    }

    /**
     * 方法名称：	setEditText
     * 方法描述：	设置编辑框
     * 参数：			@param msg
     * 参数：			@return
     * 返回值类型：	MyAlertDialog
     * 创建时间：	2015-2-4上午11:09:06
     */
    public CustomDialog setEditText(String msg) {
        // 需要显示编辑框
        showEditText = true;
        if ("".equals(msg)) {
            etResult.setHint("Content");
        } else {
            etResult.setText(msg);
        }
        return this;
    }

    /**
     * 方法名称：	getResult
     * 方法描述：	获取编辑框的结果
     * 参数：			@return
     * 返回值类型：	String
     * 创建时间：	2015-2-4上午11:09:30
     */
    public String getResult() {
        return etResult.getText().toString();
    }

    /**
     * 方法名称：	setMsg
     * 方法描述：	设置消息
     * 参数：			@param msg
     * 参数：			@return
     * 返回值类型：	MyAlertDialog
     * 创建时间：	2015-2-4上午11:09:48
     */
    public CustomDialog setMsg(String msg) {
        // 需要显示消息
        showMsg = true;
        if ("".equals(msg)) {
            tvMsg.setText("Content");
        } else {
            tvMsg.setText(msg);
        }
        return this;
    }

    /**
     * 方法名称：	setView
     * 方法描述：	设置对话框内容部分
     * 参数：			@param view
     * 参数：			@return
     * 返回值类型：	MyAlertDialog
     * 创建时间：	2015-2-4上午11:10:10
     */
    public CustomDialog setView(View view) {
        // 需要显示对话框内容
        showLayout = true;
        if (view == null) {
            showLayout = false;
        } else
            layoutContent.addView(view,
                    android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                    android.view.ViewGroup.LayoutParams.MATCH_PARENT);
        return this;
    }

    /**
     * 方法名称：	setCancelable
     * 方法描述：	设置是否允许返回键可以取消对话框
     * 参数：			@param cancel
     * 参数：			@return
     * 返回值类型：	MyAlertDialog
     * 创建时间：	2015-2-4上午11:11:15
     */
    public CustomDialog setCancelable(boolean cancel) {
        dialog.setCancelable(cancel);
        return this;
    }

    // 设置对话框上的确定按钮响应
    public CustomDialog setPositiveButton(String text,
                                          final OnClickListener listener) {
        showPosBtn = true;
        if ("".equals(text)) {
            btnOk.setText("Ok");
        } else {
            btnOk.setText(text);
        }
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(v);
                dialog.dismiss();
            }
        });
        return this;
    }

    // 设置对话框取消按钮响应
    public CustomDialog setNegativeButton(String text,
                                          final View.OnClickListener listener) {
        showNegBtn = true;
        if ("".equals(text)) {
            btnCancel.setText("Cancel");
        } else {
            btnCancel.setText(text);
        }
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(v);
                dialog.dismiss();
            }
        });
        return this;
    }

    /**
     * 方法名称：	setLayout
     * 方法描述：	用于显示对话框的控件
     * 参数：
     * 返回值类型：	void
     * 创建时间：	2015-2-4上午11:13:03
     */
    private void setLayout() {
        if (!showTitle && !showMsg) {
            tvTitle.setText("Hint");
            tvTitle.setVisibility(View.VISIBLE);
        }
        if (showTitle) {
            tvTitle.setVisibility(View.VISIBLE);
        }
        if (showEditText) {
            etResult.setVisibility(View.VISIBLE);
        }
        if (showMsg) {
            tvMsg.setVisibility(View.VISIBLE);
        }
        if (showLayout) {
            layoutContent.setVisibility(View.VISIBLE);
            ivBottom.setVisibility(View.GONE);
        }
        if (!showPosBtn && !showNegBtn) {
            btnOk.setText("Ok");
            btnOk.setVisibility(View.VISIBLE);
            btnOk.setBackgroundResource(R.drawable.custom_dialog_single_button);
            btnOk.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
        }
        if (showPosBtn && showNegBtn) {
            btnOk.setVisibility(View.VISIBLE);
            btnOk.setBackgroundResource(R.drawable.custom_dialog_right_button);
            btnCancel.setVisibility(View.VISIBLE);
            btnCancel
                    .setBackgroundResource(R.drawable.custom_dialog_left_button);
            ivSpiner.setVisibility(View.VISIBLE);
        }
        if (showPosBtn && !showNegBtn) {
            btnOk.setVisibility(View.VISIBLE);
            btnOk.setBackgroundResource(R.drawable.custom_dialog_single_button);
        }
        if (!showPosBtn && showNegBtn) {
            btnCancel.setVisibility(View.VISIBLE);
            btnCancel
                    .setBackgroundResource(R.drawable.custom_dialog_single_button);
        }
    }

    /**
     * 方法名称：	show
     * 方法描述：	显示对话框
     * 参数：
     * 返回值类型：	void
     * 创建时间：	2015-2-4上午11:15:14
     */
    public void show() {
        setLayout();
        dialog.show();
    }

    /**
     * 方法名称：	dismiss
     * 方法描述：	隐藏对话框
     * 参数：
     * 返回值类型：	void
     * 创建时间：	2015-3-26下午5:38:24
     */
    public void dismiss() {
        dialog.dismiss();
    }
}
