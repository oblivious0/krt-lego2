package com.krt.base.util;

import android.annotation.SuppressLint;

import androidx.fragment.app.FragmentActivity;

import com.blankj.utilcode.util.ThreadUtils;
import com.tbruyelle.rxpermissions2.RxPermissions;

import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * author:Marcus
 * create on:2019/3/19 10:56
 * description
 */
public class MPermissions {

    private static class SingletionHolder {
        private static final MPermissions instance = new MPermissions();

    }

    public static MPermissions getInstance() {
        return SingletionHolder.instance;
    }

    public interface PermissionListener {
        void callBack(boolean value);
    }


    private MPermissions() {

    }

    /**
     * 设置多个权限
     *
     * @param permissions
     * @param listener
     */
    @SuppressLint("checkResult")
    public void request(FragmentActivity activity, String[] permissions, final PermissionListener listener) {
        if (ThreadUtils.isMainThread()) {
            requestMethod(activity, permissions, listener);
        } else {
            ThreadUtils.runOnUiThread(() -> {
                requestMethod(activity, permissions, listener);
            });
        }
    }

    /**
     * 设置单个权限
     */
    @SuppressLint("checkResult")
    public void request(FragmentActivity activity, String permissions, final PermissionListener listener) {
        if (ThreadUtils.isMainThread()) {
            requestMethod(activity, permissions, listener);
        } else {
            ThreadUtils.runOnUiThread(() -> {
                requestMethod(activity, permissions, listener);
            });
        }
    }

    private void requestMethod(FragmentActivity activity, String permissions, final PermissionListener listener) {
        RxPermissions rxPermissions = new RxPermissions(activity);
        rxPermissions.request(permissions)
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(aBoolean -> {
                    if (listener != null) listener.callBack(aBoolean);
                });
    }

    private void requestMethod(FragmentActivity activity, String[] permissions, final PermissionListener listener) {
        RxPermissions rxPermissions = new RxPermissions(activity);
        rxPermissions.request(permissions)
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(aBoolean -> {
                    if (listener != null) listener.callBack(aBoolean);
                });
    }


}
