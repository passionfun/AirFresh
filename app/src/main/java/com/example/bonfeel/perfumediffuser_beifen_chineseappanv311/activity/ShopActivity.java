package com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.R;

/**
 *
 * Author:FunFun
 * Function:积分商城
 * Date:2016/4/12 12:01
 */
public class ShopActivity extends BaseActivity
{
    private TextView tvTitle;
    private WebView wvShop;

    private String title;
    private String shopUrl;
    private ProgressDialog pd = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_shop);
        super.onCreate(savedInstanceState);
    }

    /*
     * (非Javadoc) 方法名称： initialData 方法描述： TODO 重写部分： @see
     * com.example.bonfeel.activity.BaseActivity#initialData()
     */
    @Override
    protected void initialData() {
        title = "百芬商城";
        shopUrl = "http://wsh.gaopeng.com/bonfeel001?from=singlemessage&isappinstalled=0";
    }
    /*
     * (非Javadoc) 方法名称： initialView 方法描述： TODO 重写部分： @see
     * com.example.bonfeel.activity.BaseActivity#initialView()
     */
    @SuppressLint("SetJavaScriptEnabled") @Override
    protected void initialView() {
        tvTitle = (TextView) findViewById(R.id.tv_title);
        wvShop = (WebView) findViewById(R.id.webview_shop);
        tvTitle.setText(title);
        // 支持javascript
        WebSettings setting = wvShop.getSettings();
        setting.setJavaScriptEnabled(true);
        setting.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);

        wvShop.loadUrl(shopUrl);

        wvShop.setWebViewClient(new WebViewClient() {
            /*
             * (非Javadoc) 方法名称： shouldOverrideUrlLoading 方法描述： TODO 重写部分： @see
             * android
             * .webkit.WebViewClient#shouldOverrideUrlLoading(android.webkit
             * .WebView, java.lang.String)
             */
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

        // 判断页面加载的过程
        wvShop.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (newProgress == 100) {
                    if (pd != null && pd.isShowing()) {
                        pd.dismiss();
                        pd = null;
                    }
                } else {
                    if (pd == null) {
                        pd = new ProgressDialog(ShopActivity.this);
                        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                        pd.setTitle("正在加载……");
                        pd.setProgress(newProgress);
                        pd.show();
                    } else {
                        pd.setProgress(newProgress);
                    }
                }
            }
        });
    }

    /*
     * (非Javadoc) 方法名称： initialHandler 方法描述： TODO 重写部分： @see
     * com.example.bonfeel.activity.BaseActivity#initialHandler()
     */
    @Override
    protected void initialHandler() {

    }

    /**
     * 设置回退
     */
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && wvShop.canGoBack()) {
            wvShop.goBack();
            return true;
        } else {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
