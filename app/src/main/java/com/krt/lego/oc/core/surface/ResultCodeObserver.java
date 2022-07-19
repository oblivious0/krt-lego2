package com.krt.lego.oc.core.surface;

import android.content.Intent;

import androidx.annotation.Nullable;

/**
 * @author: MaGua
 * @create_on:2021/12/12 9:37
 * @description
 */
public interface ResultCodeObserver {
    /**
     * Activity onActivityResult 监听回调
     * @param requestCode
     * @param resultCode
     * @param data
     */
    void onActivityResult(int requestCode, int resultCode, @Nullable Intent data);
}
