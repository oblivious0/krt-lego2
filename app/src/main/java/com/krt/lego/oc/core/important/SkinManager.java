package com.krt.lego.oc.core.important;

import com.krt.lego.load.info.ApplicationVersionInfo;
import com.krt.lego.oc.core.bean.SkinIconBean;

import java.util.HashMap;

/**
 * author: MaGua
 * create on:2021/3/3 14:20
 * description 皮肤裁剪管理类
 */
public class SkinManager {

    public static String defaultFile = "customSkin.png";

    /**
     * 皮肤裁剪坐标信息
     */
    private static HashMap<String, SkinIconBean> skinPositions;

    /**
     * 加载皮肤裁剪坐标信息
     *
     * @param skinBean
     */
    public static void load(ApplicationVersionInfo.SkinBean skinBean) {
        skinPositions = new HashMap<>(skinBean.getSkinIcon().size());
        for (SkinIconBean ico : skinBean.getSkinIcon()) {
            skinPositions.put(ico.getCode(), ico);
        }
    }

    /**
     * 获取裁剪坐标信息
     *
     * @param code
     * @return
     */
    public static SkinIconBean getPosition(String code) {
        if (skinPositions != null)
            return skinPositions.get(code);
        else
            return null;
    }

}
