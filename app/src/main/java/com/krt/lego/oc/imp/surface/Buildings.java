package com.krt.lego.oc.imp.surface;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.blankj.utilcode.util.LogUtils;
import com.krt.base.util.MUtil;
import com.krt.lego.AppLibManager;
import com.krt.lego.Constants;
import com.krt.lego.R;
import com.krt.lego.oc.core.surface.Blueprint;
import com.krt.lego.oc.core.surface.Subgrade;
import com.krt.lego.oc.core.tools.BroadCastMessageWarp;
import com.krt.lego.oc.lifecyler.MLifecycleObserver;
import com.lzy.okgo.OkGo;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.Optional;

/**
 * @author: MaGua
 * @create_on:2021/9/24 16:35
 * @description
 */
public abstract class Buildings extends AppCompatActivity implements Subgrade {

    private Blueprint designer;
    private MLifecycleObserver mObserver;
    private String jsonFile;

    @Override
    public Context getCarrier() {
        return this;
    }

    @Override
    public Blueprint getDesigner() {
        return designer;
    }

    @Override
    protected final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        jsonFile = Optional.ofNullable(getIntent().getStringExtra("name")).orElse("");
        File file = new File(Constants.jsonPath + jsonFile + ".json");
        if (file.exists()) {
            String json = MUtil.getJson(file);
            if (!TextUtils.isEmpty(json)) {
                designer = new Blueprint(json, this);
                setContentView(designer.build());
            }
            init();
        } else {
            finish();
            return;
        }
    }

    private void init() {
        EventBus.getDefault().register(this);
        mObserver = new MLifecycleObserver(this);
        this.getLifecycle().addObserver(mObserver);
        AppLibManager.registerContext(jsonFile, this);
        designer.draw();
    }

    @Override
    public <T extends View> T getSurfaceView(String type) {
        switch (type) {
            case "NAV":
                return findViewById(R.id.navbar);
            case "COMMON":
                return findViewById(R.id.parent);
            case "LIST":
                return findViewById(R.id.swipeRefresh);
            case "FLOAT":
                return findViewById(R.id.floatLayout);
            case "L":
                return findViewById(R.id.leftContent);
            case "R":
                return findViewById(R.id.rightContent);
            case "T":
                return findViewById(R.id.topContent);
            case "B":
                return findViewById(R.id.bottomContent);
            case "BG":
                return findViewById(R.id.bg);
            default:
                return null;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void receiveBroadCast(BroadCastMessageWarp bean) {

    }

    @Override
    public void onStart() {
        super.onStart();
        mObserver.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mObserver.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mObserver.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        mObserver.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        OkGo.getInstance().cancelTag(this);
        AppLibManager.unRegisterContext(jsonFile);
        EventBus.getDefault().unregister(this);
    }
}
