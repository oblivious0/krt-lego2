package com.krt.simple.module;

import androidx.fragment.app.Fragment;

import com.krt.lego.oc.imp.surface.Main;
import com.krt.simple.base.TestFragment;


/**
 * author: MaGua
 * create on:2021/3/25 14:51
 * description
 */
public class ModuleMainActivity extends Main {
    @Override
    protected void beforeBindLayout() {

    }

    @Override
    protected void init() {

    }

    @Override
    public Fragment instanceFragment(int idx, String url) {
        return new TestFragment();
    }


}
