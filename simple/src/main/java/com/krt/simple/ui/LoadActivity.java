package com.krt.simple.ui;

import android.Manifest;
import android.content.Intent;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.daimajia.numberprogressbar.NumberProgressBar;
import com.krt.base.base.MBaseActivity;
import com.krt.base.util.MPermissions;
import com.krt.base.util.MToast;
import com.krt.lego.load.ZLoader;
import com.krt.lego.load.info.ApplicationVersionInfo;
import com.krt.simple.R;
import com.krt.simple.module.ModuleActivity;
import com.krt.simple.module.ModuleMainActivity;

import net.frakbot.jumpingbeans.JumpingBeans;

/**
 * author: MaGua
 * create on:2021/3/25 14:53
 * description
 */
public class LoadActivity extends MBaseActivity implements ZLoader.LoaderListener {

    LottieAnimationView animationView;
    TextView loadText;
    NumberProgressBar progressBar;

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
        return R.layout.activity_load;
    }

    @Override
    public void initView() {
        animationView = findViewById(R.id.lottieAnimationView);
        animationView.setAnimation(R.raw.load);
        animationView.loop(true);
        animationView.playAnimation();
        loadText = findViewById(R.id.loadText);
        JumpingBeans.with(loadText)
                .appendJumpingDots()
                .build();
        progressBar = findViewById(R.id.progressBar);
    }

    @Override
    public void loadData() {
        MPermissions.getInstance().request(this,
                new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE}, value -> {
                    if (value) {
                        ZLoader.newBuilder()
                                .setCode(getIntent().getStringExtra("krtCode"))
                                .setVersion(getIntent().getStringExtra("krtVer"))
                                .setLoaderListener(this)
                                .build()
                                .checkVer();
                    } else {
                        finish();
                    }
                });

    }

    @Override
    public void coerceUpdate() {

    }

    @Override
    public void update() {

    }

    @Override
    public void assembleApi() {

    }

    @Override
    public void progressBack(int max, int current) {
        progressBar.setProgress((int) (((1.0f * max) / current) * 100));
    }

    @Override
    public void finish(String type, String cid) {
        progressBar.setProgress(100);
        switch (type) {
            case "tabbar":
                startActivity(new Intent(this, ModuleMainActivity.class)
                        .putExtra("name", cid));
                break;
            case "singlePage":
                startActivity(new Intent(this, ModuleActivity.class)
                        .putExtra("name", cid));
                break;
            default:
        }
        finish();
    }

    @Override
    public ApplicationVersionInfo fail() {
        MToast.showToast(this, "加载失败");
        return null;
    }
}
