package com.krt.lego.oc.imp.widget;

import android.graphics.drawable.GradientDrawable;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.krt.base.util.MUtil;
import com.krt.lego.oc.core.bean.BaseLayoutBean;
import com.krt.lego.oc.core.surface.BaseWidget;
import com.krt.lego.oc.core.surface.Subgrade;

import java.util.Optional;

/**
 * @author: MaGua
 * @create_on:2021/10/20 9:30
 * @description
 */
public class ButtonView extends BaseWidget<FrameLayout> {

    TextView tv;

    public ButtonView(Subgrade imp, BaseLayoutBean obj, boolean isListChild) {
        super(imp, obj, isListChild);
    }

    @Override
    protected void createView() {
        view = new FrameLayout(subgrade.getCarrier());
        tv = new TextView(subgrade.getCarrier());
        FrameLayout.LayoutParams tvlp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        tvlp.gravity = Gravity.CENTER;
        tv.setLayoutParams(tvlp);
        view.addView(tv);
    }

    @Override
    protected void initView() {
        GradientDrawable drawable = MUtil.getBgDrawable(getStringVal("bgColor"),
                GradientDrawable.RECTANGLE, getIntVal("borderRadius"), getIntVal("borderWidth"),
                getStringVal("borderColor"));
        view.setBackground(drawable);
        view.setVisibility(getBooleanVal("isHidden") ? View.GONE : View.VISIBLE);

        tv.setText(Optional.ofNullable(getStringVal("text")).orElse(""));
        tv.setTextColor(MUtil.getRealColor(getStringVal("color")));
        tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, MUtil.getRealValue(getIntVal("fontSize")));
    }

    @Override
    public void bindData(String cid, String key, Object val) {

    }
}
