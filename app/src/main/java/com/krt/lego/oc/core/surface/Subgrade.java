package com.krt.lego.oc.core.surface;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.lifecycle.LifecycleOwner;

/**
 * @author: MaGua
 * @create_on:2021/9/23 17:11
 * @description
 */
public interface Subgrade {

    /**
     * 获取地基全局
     *
     * @return Actvity Context
     */
    Context getCarrier();

    /**
     * 获得模块化设计
     * @return
     */
    Blueprint getDesigner();

    /**
     * 根据res_id获取原生组件
     * @param rid
     * @param <T>
     * @return
     */
    <T extends View>T findViewById(int rid);

    /**
     * 获取区块ViewGroup
     * @param nav
     * @param <T>
     * @return
     */
    <T extends View> T getSurfaceView(String nav);
}
