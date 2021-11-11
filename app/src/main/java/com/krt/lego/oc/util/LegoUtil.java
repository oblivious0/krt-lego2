package com.krt.lego.oc.util;

import com.krt.Lego;

import static com.krt.Lego.VERSION_BETA;
import static com.krt.Lego.VERSION_HISTORY;
import static com.krt.Lego.VERSION_OFFICIAL;

/**
 * author: MaGua
 * create on:2021/3/3 14:11
 * description
 */
public class LegoUtil {

    /**
     * 检测运行环境
     *
     * @return true是测试环境
     */
    public static boolean isDev() {
        switch (Lego.getVersionInfo().getStatus()) {
            case VERSION_OFFICIAL:
            case VERSION_HISTORY:
            case VERSION_BETA:
                return false;
            default:
                if ("prod".equals(Lego.getVersionInfo().getAppInfo().getDevelopment()))
                    return false;
                else
                    return true;
        }
    }

    public static boolean isDevStatic() {
        switch (Lego.getVersionInfo().getStatus()) {
            case VERSION_OFFICIAL:
            case VERSION_HISTORY:
            case VERSION_BETA:
                return false;
            default:
                return true;
        }
    }

}
