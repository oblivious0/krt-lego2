package com.krt.simple.module;

import androidx.fragment.app.Fragment;

import com.krt.base.ui.TestFragment;
import com.krt.lego.oc.core.tools.FragmentFactory;

/**
 * author: MaGua
 * create on:2021/6/8 9:29
 * description
 */
public class MFragmentFactory extends FragmentFactory {
    @Override
    public Fragment manufactureOrigin(String sgin) {
        return new TestFragment();
    }
}
