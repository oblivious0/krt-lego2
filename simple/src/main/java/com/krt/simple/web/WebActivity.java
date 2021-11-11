package com.krt.simple.web;

import android.content.Intent;
import android.view.KeyEvent;

import androidx.core.content.ContextCompat;

import com.krt.base.base.MBaseActivity;
import com.krt.base.util.MTitle;
import com.krt.simple.R;

/**
 * @author xzy
 * @package krt.com.zhdn.activity
 * @description
 * @time 2018/10/31
 */
public class WebActivity extends MBaseActivity {
    MTitle mTitle;

    private String url;

    private X5WebFragment x5WebFragment;

    private boolean showTitle;


    @Override
    public void bindButterKnife() {

    }

    @Override
    public void unbindButterknife() {

    }

    @Override
    public void beforeBindLayout() {

    }

    @Override
    public void init() {
        mTitle = findViewById(R.id.title);
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_web;
    }

    public MTitle getmTitle() {
        return mTitle;
    }

    @Override
    public void initView() {
        String title = "仅展示不进行js交互";
        url = getIntent().getStringExtra("url");
        mTitle.setCenterText(title, 16, ContextCompat.getColor(this, R.color.color333));
        x5WebFragment = new X5WebFragment().setUrl(url).setShowTitle(showTitle);
        getSupportFragmentManager().beginTransaction().add(R.id.content, x5WebFragment).commit();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            x5WebFragment.onKeyDown(keyCode, event);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        x5WebFragment.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void loadData() {

    }

}
