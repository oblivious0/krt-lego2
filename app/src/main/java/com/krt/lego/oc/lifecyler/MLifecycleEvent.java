package com.krt.lego.oc.lifecyler;

/**
 * author: MaGua
 * create on:2021/2/3 14:56
 * description
 */
public interface MLifecycleEvent {

    /**
     *  界面的onStart生命周期
     *  界面首次进入时未开始进行模块化界面解析，不推荐组件在此初始化
     */
    void onStart();

    /**
     *
     */
    void onResume();

    /**
     *
     */
    void onPause();

    /**
     *
     */
    void onStop();

    /**
     *
     */
    void onDestroy();

    /**
     * 当模块化组件加载完成后的事件
     */
    void onPrepare();
}
