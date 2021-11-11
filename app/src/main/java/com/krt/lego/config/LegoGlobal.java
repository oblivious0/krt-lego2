package com.krt.lego.config;

/**
 * author: MaGua
 * create on:2021/3/6 9:20
 * description 内部全局引用
 */
public class LegoGlobal {

    /**
     * App当前是否用户已登录
     *
     * @return
     */
    public static boolean isUserLogin() {
        if (LegoConfig.legoAppWatcher != null)
            return LegoConfig.legoAppWatcher.isLogin();

        return false;
    }

    /**
     * 获取App用户的TOKEN
     *
     * @return
     */
    public static String[] registerAuthorization() {
        if (LegoConfig.legoAppWatcher != null)
            return LegoConfig.legoAppWatcher.registerAuthorization();

        return null;
    }


}
