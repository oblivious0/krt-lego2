package com.krt;

import com.krt.base.config.BaseModuleConfig;
import com.krt.lego.load.info.ApplicationVersionInfo;

/**
 * author: MaGua
 * create on:2021/2/23 10:06
 * description
 */
public final class Lego {

    public static final String TAG = "Lego";

    /**
     * 测试版
     */
    public static final String VERSION_ALPHA = "-1";
    /**
     * 开发版
     */
    public static final String VERSION_DEVELOP = "0";
    /**
     * 体验版
     */
    public static final String VERSION_BETA = "1";
    /**
     * 发布版
     */
    public static final String VERSION_OFFICIAL = "2";
    /**
     * 历史发布版
     */
    public static final String VERSION_HISTORY = "3";

    private static BaseModuleConfig sbaseConfig;
    private static LegoInfo legoInfo;
    private static ApplicationVersionInfo versionInfo;

    private Lego() {
    }

    /**
     * Base库初始化配置，详情见{@com.krt.base.config.BaseConfig}
     *
     * @param baseConfig
     */
    public static void initialize(BaseModuleConfig baseConfig) {
        sbaseConfig = baseConfig;
    }

    public static BaseModuleConfig getBaseConfig() {
        if (sbaseConfig == null) {
            return BaseModuleConfig.newBuilder().build();
        } else {
            return sbaseConfig;
        }
    }

    /**
     * 获取解析器信息
     *
     * @return
     */
    public static LegoInfo getLegoInfo() {
        if (legoInfo == null)
            legoInfo = new LegoInfo();
        return legoInfo;
    }

    /**
     * 获取当前项目信息
     *
     * @return
     */
    public static ApplicationVersionInfo getVersionInfo() {
        return versionInfo;
    }

    public static void setVersionInfo(ApplicationVersionInfo versionInfo) {
        Lego.versionInfo = versionInfo;
    }
}
