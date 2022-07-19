package com.krt.lego.x5;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;


/**
 * @author Marcus
 * @package com.krt.zhdn.view
 * @description
 * @time 2018/11/3
 */
public class X5WebView extends FrameLayout {

    private Context mContext;
    public WebView mWebView;

    public X5WebView(Context context) {
        this(context, null);
    }

    public X5WebView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public X5WebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        init();
    }

    private void init() {
        mWebView = new WebView(mContext);
        this.addView(mWebView, LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);
        mWebView.setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                if (keyCode == KeyEvent.KEYCODE_BACK
                        && mWebView.canGoBack()) { // 表示按返回键时的操作
                    mWebView.goBack(); // 后退
                    return true; // 已处理
                }
            }
            return false;
        });
    }

    @Override
    public void setClickable(boolean value) {
        mWebView.setClickable(value);
    }

    public WebSettings getSetting() {
        return mWebView.getSettings();
    }

    public void setWebViewClient(WebViewClient value) {
        mWebView.setWebViewClient(value);
    }

    public void loadUrl(String url) {
        mWebView.loadUrl(url);
    }

    public void reload() {
        mWebView.reload();
    }

    public void postUrl(String url, byte[] post) {
        mWebView.postUrl(url, post);
    }

    public String getUrl() {
        return mWebView.getUrl();
    }

    public void onPause() {
        mWebView.onPause();
    }

    public void destroy() {
        if (mWebView != null)
            mWebView.destroy();
    }

    /**
     * 使用GoBack方法
     */
    public void GoBack() {
        mWebView.goBack();
    }

    /**
     * 使用GoBack方法
     */
    public boolean CanGoBack() {
        return mWebView.canGoBack();
    }

    public WebView getWebView() {
        return mWebView;
    }
}
