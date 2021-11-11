package com.krt.simple.ui;

import android.Manifest;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.util.FileUtils;
import com.krt.Lego;
import com.krt.base.base.MBaseActivity;
import com.krt.base.util.MPermissions;
import com.krt.lego.Constants;
import com.krt.simple.R;

import java.io.File;
import java.io.IOException;

/**
 * author: MaGua
 * create on:2021/3/25 14:08
 * description
 */
public class InterProActivity extends MBaseActivity {

    private String tag;
    private String version;

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
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_inter_pro;
    }

    @Override
    public void initView() {
        ((TextView) findViewById(R.id.txt_version)).setText("解析器版本 V" + Lego.getLegoInfo().compiler_version + "." + Lego.getLegoInfo().terminal_version);
        tag = getIntent().getStringExtra("krtCode");
        version = getIntent().getStringExtra("krtVer");
        ((TextView) findViewById(R.id.pro_tag)).setText(tag);
        ((TextView) findViewById(R.id.pro_version)).setText(version);
    }

    @Override
    public void loadData() {

    }

    public void start(View view) {
        MPermissions.getInstance().request(this,
                new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE}, value -> {
                    if (value) {
                        //每次加载之前清除上次加载的记录，防止资源堆积占用大量物理空间
                        FileUtils.deleteAllInDir(Constants.defPath);
                        File nomedia = new File(Constants.imgPath, ".nomedia");
                        FileUtils.createOrExistsFile(nomedia);
                        startActivity(new Intent(this, LoadActivity.class)
                                .putExtra("krtCode", tag)
                                .putExtra("krtVer", version));
                    }
                });
    }

    public void back(View view) {
        finish();
    }

}
