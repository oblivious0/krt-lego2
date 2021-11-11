package com.krt.base.config;

/**
 * Created by marcus on 18/3/2.
 */

public class BaseModule {
    private static BaseModuleConfig sbaseModuleConfig;

    private BaseModule(){

    }

    public static void initialize(BaseModuleConfig baseModuleConfig) {
        sbaseModuleConfig = baseModuleConfig;
    }

    public static BaseModuleConfig getBaseModuleConfig() {
        if (sbaseModuleConfig == null) {
            return BaseModuleConfig.newBuilder().build();
        } else {
            return sbaseModuleConfig;
        }
    }
}
