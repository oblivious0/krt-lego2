package com.krt.base.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * author:Marcus
 * create on:2020/7/2 9:35
 * description
 */
public class SpUtil {
    private static final String FILE = "file";

    /**
     * 设置皮肤路径地址
     */
    public static void setIconSkinPath(Context context, String path) {
        SharedPreferences.Editor editor = context.getSharedPreferences(FILE, Context.MODE_PRIVATE).edit();
        editor.putString("path", path).apply();
    }

    /**
     * 获取皮肤路径地址
     */
    public static String getIconSkinPath(Context context) {
        SharedPreferences sp = context.getSharedPreferences(FILE, Context.MODE_PRIVATE);
        return sp.getString("path", "");
    }

    public static void putStorageVal(String k, String v, Context context) {
        SharedPreferences.Editor editor = context.getSharedPreferences(FILE, Context.MODE_PRIVATE).edit();
        editor.putString(k, v).apply();
    }

    public static String getStorageVal(String k, Context context) {
        SharedPreferences sp = context.getSharedPreferences(FILE, Context.MODE_PRIVATE);
        return sp.getString(k, "");
    }
}
