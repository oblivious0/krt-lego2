package com.krt.lego.oc.imp.surface;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;

import com.blankj.utilcode.util.LogUtils;
import com.krt.base.util.MUtil;
import com.krt.base.util.ParseJsonUtil;
import com.krt.lego.AppLibManager;
import com.krt.lego.Constants;
import com.krt.lego.R;
import com.krt.lego.config.LegoConfig;
import com.krt.lego.oc.core.bean.BroadCastBean;
import com.krt.lego.oc.core.surface.Blueprint;
import com.krt.lego.oc.core.surface.Subgrade;
import com.krt.lego.oc.core.tools.BroadCastMessageWarp;
import com.krt.lego.oc.lifecyler.MLifecycleObserver;
import com.lzy.okgo.OkGo;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;

/**
 * @author: MaGua
 * @create_on:2021/9/24 9:13
 * @description
 */
public abstract class Lump extends Fragment implements Subgrade {

    private Blueprint designer;
    private MLifecycleObserver mObserver;
    public String jsonFile;
    protected Context mContext;
    protected boolean isPrepared = false;
    public View surfaceView;

    public Lump setJsonFile(String name) {
        this.jsonFile = name;
        return this;
    }

    @Override
    public Context getCarrier() {
        return getContext();
    }

    @Override
    public Blueprint getDesigner() {
        return designer;
    }

    @Override
    public <T extends View> T findViewById(int rid) {
        return this.surfaceView.findViewById(rid);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        File file = new File(Constants.jsonPath + jsonFile + ".json");
        if (file.exists()) {
            String json = MUtil.getJson(file);
            if (!TextUtils.isEmpty(json)) {
                isPrepared = true;
                designer = new Blueprint(json, this);
                surfaceView = inflater.inflate(designer.build(), container, false);
                return surfaceView;
            }
        }
        return new View(getContext());
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mObserver = new MLifecycleObserver(this);
        this.getLifecycle().addObserver(mObserver);
        AppLibManager.registerContext(jsonFile, this);
        designer.draw();
    }

    private void lazyLoad() {
        boolean visable = getUserVisibleHint();
        if (visable && isPrepared) {
//            loadData();
        }
    }

    @Override
    public <T extends View> T getSurfaceView(String type) {
        if (surfaceView != null) {
            switch (type) {
                case "NAV":
                    return surfaceView.findViewById(R.id.navbar);
                case "COMMON":
                    return surfaceView.findViewById(R.id.parent);
                case "LIST":
                    return surfaceView.findViewById(R.id.swipeRefresh);
                case "FLOAT":
                    return surfaceView.findViewById(R.id.floatLayout);
                case "L":
                    return surfaceView.findViewById(R.id.leftContent);
                case "R":
                    return surfaceView.findViewById(R.id.rightContent);
                case "T":
                    return surfaceView.findViewById(R.id.topContent);
                case "B":
                    return surfaceView.findViewById(R.id.bottomContent);
                case "BG":
                    return surfaceView.findViewById(R.id.bg);
                default:
            }
        }
        return null;
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
    public void onDestroyView() {
        super.onDestroyView();
        OkGo.getInstance().cancelTag(this);
        AppLibManager.unRegisterContext(jsonFile);
        EventBus.getDefault().unregister(this);
    }
}
