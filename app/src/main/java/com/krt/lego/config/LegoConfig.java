package com.krt.lego.config;

import com.krt.lego.oc.core.execute.EventHandler;
import com.krt.lego.oc.core.tools.FragmentFactory;
import com.krt.lego.oc.imp.surface.Buildings;
import com.krt.lego.oc.imp.surface.Lump;
import com.krt.lego.x5.WebFragment;

/**
 * author: MaGua
 * create on:2021/2/23 10:23
 * description
 */
public class LegoConfig {
    /**
     * 项目的FragmentClazz实例
     */
    private static Class<? extends Lump> fragmentClazz;
    private static Class<? extends Buildings> activityClazz;
    private static Class<? extends WebFragment> webFragmentClazz;
    private static EventHandler eventHander;
    private static Class<? extends FragmentFactory> fragmentFactory;
    static LegoAppWatcher legoAppWatcher;

    public static Class<? extends FragmentFactory> getFragmentFactory() {
        return fragmentFactory;
    }

    public static void setFragmentFactory(Class<? extends FragmentFactory> fragmentFactory) {
        LegoConfig.fragmentFactory = fragmentFactory;
    }

    public static Class<? extends WebFragment> getWebFragmentClazz() {
        return webFragmentClazz;
    }

    public static void setWebFragmentClazz(Class<? extends WebFragment> webFragmentClazz) {
        LegoConfig.webFragmentClazz = webFragmentClazz;
    }

    public static void setLegoAppWatcher(LegoAppWatcher legoAppWatcher) {
        LegoConfig.legoAppWatcher = legoAppWatcher;
    }

    public static Class<? extends Lump> getFragmentClazz() {
        return fragmentClazz;
    }

    public static void setFragmentClazz(Class<? extends Lump> fragmentClazz) {
        LegoConfig.fragmentClazz = fragmentClazz;
    }

    public static Class<? extends Buildings> getActivityClazz() {
        return activityClazz;
    }

    public static void setActivityClazz(Class<? extends Buildings> activityClazz) {
        LegoConfig.activityClazz = activityClazz;
    }

    public static EventHandler getEventHander() {
        return eventHander;
    }

    public static void setEventHander(EventHandler eventHander) {
        LegoConfig.eventHander = eventHander;
    }

    public interface LegoAppWatcher{
        /**
         * 实现是否登录
         * @return
         */
        boolean isLogin();

        /**
         * 获取Token
         * @return
         */
        String[] registerAuthorization();
    }


}
