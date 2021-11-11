package com.krt.base.util;

/**
 * @author Marcus
 * @package krt.wid.util
 * @description
 * @time 2018/6/30
 */
public class MClickUtil {
    private static long lastClickTime;

    public MClickUtil() {
    }

    public static synchronized boolean isClick() {
        long time = System.currentTimeMillis();
        if (time - lastClickTime < 500L) {
            return true;
        } else {
            lastClickTime = time;
            return false;
        }
    }
}
