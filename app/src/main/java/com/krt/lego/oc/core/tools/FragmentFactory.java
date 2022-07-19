package com.krt.lego.oc.core.tools;

import androidx.fragment.app.Fragment;

import com.krt.base.ui.TestFragment;
import com.krt.lego.config.LegoConfig;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * author: MaGua
 * create on:2021/5/4 11:43
 * description Fragment工厂
 */
public abstract class FragmentFactory {
    public Fragment manufactureModule(String pageId) {
        try {
            return LegoConfig.getFragmentClazz().newInstance().setJsonFile(pageId);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        return new TestFragment();
    }

    public Fragment manufactureWeb(String url) {
        try {
            return (Fragment) LegoConfig.getWebFragmentClazz().newInstance().setUrl(url);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        return new TestFragment();
    }

    /**
     * 由项目方通过标识指定示例Fragment
     * @param sgin
     * @return
     */
    public abstract Fragment manufactureOrigin(String sgin);
}
