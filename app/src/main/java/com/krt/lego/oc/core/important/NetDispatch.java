package com.krt.lego.oc.core.important;

import com.krt.Lego;
import com.krt.lego.load.info.ApplicationVersionInfo.AppInfoBean.BasePathBean;
import com.krt.lego.oc.util.LegoUtil;

/**
 * author: MaGua
 * create on:2021/3/3 11:04
 * description
 */
public class NetDispatch {

    /**
     * 初始化Api 必须在Lego.setVersionInfo()之后执行
     */
    public static void initApi() {
        Net.defaultIndex = Lego.getVersionInfo().getAppInfo().getDefaultPath();
        for (BasePathBean bean : Lego.getVersionInfo().getAppInfo().getBasePath()) {
            Net.prod.add(bean.getProd());
            Net.dev.add(bean.getDev());
        }
    }

    /**
     * 获取Api(默认API)
     *
     * @return
     */
    public static String getApiUrl() {
        return getApiUrl(Net.defaultIndex);
    }

    /**
     * 获取Api
     *
     * @param index
     * @return
     */
    public static String getApiUrl(int index) {
        if (index == 99) index = Net.defaultIndex;
        if (LegoUtil.isDev())
            return Net.dev.get(index);
        else
            return Net.prod.get(index);
    }

}
