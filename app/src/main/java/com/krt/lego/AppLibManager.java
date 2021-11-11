package com.krt.lego;

import android.content.Context;

import com.krt.base.util.SpUtil;
import com.krt.lego.oc.core.surface.Subgrade;

import java.util.HashMap;

/**
 * @author: MaGua
 * @create_on:2021/9/24 9:06
 * @description
 */
public class AppLibManager {
    /**
     * 存放所有contextImp,便于跨界面获取其内容
     */
    private static HashMap<String, Subgrade> subgradeHashMap = new HashMap<>();

    /**
     * 在界面生命周期创建注册进AppLibManager
     *
     * @param pageId
     * @param contextImp
     */
    public static void registerContext(String pageId, Subgrade contextImp) {
        subgradeHashMap.put(pageId, contextImp);
    }

    /**
     * 从sp中放入数据
     *
     * @param k
     * @param v
     * @param context
     */
    public static void putStorageVal(String k, String v, Context context) {
        SpUtil.putStorageVal(k, v, context);
    }

    /**
     * @param k
     * @param context
     * @return
     */
    public static String getStorageVal(String k, Context context) {
        return SpUtil.getStorageVal(k, context);
    }

    /**
     * 在界面生命周期注销时反注册
     *
     * @param pageId
     */
    public static void unRegisterContext(String pageId) {
        subgradeHashMap.remove(pageId);
    }

    /**
     * 根据cid获取contextImp
     *
     * @param cid
     * @return
     */
    public static Subgrade getPageContext(String cid) {
        return subgradeHashMap.get(cid);
    }

}
