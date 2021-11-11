package com.krt.lego.oc.imp.widget;

import android.graphics.Color;
import android.widget.TextView;

import com.krt.lego.oc.core.bean.BaseLayoutBean;
import com.krt.lego.oc.core.surface.BaseWidget;
import com.krt.lego.oc.core.surface.Subgrade;

/**
 * @author: MaGua
 * @create_on:2021/9/28 15:32
 * @description
 */
public class DefaultView extends BaseWidget<TextView> {

    public DefaultView(Subgrade imp, BaseLayoutBean obj, boolean isListChild) {
        super(imp, obj, isListChild);
    }

    @Override
    public TextView getRawView() {
        return view;
    }

    @Override
    protected void createView() {
        view = new TextView(subgrade.getCarrier());
    }

    @Override
    protected void initView() {
        view.setText(bean.getType());
        view.setTextColor(Color.BLACK);
    }

}
