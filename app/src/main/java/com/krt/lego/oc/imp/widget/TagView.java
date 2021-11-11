package com.krt.lego.oc.imp.widget;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.krt.base.util.MUtil;
import com.krt.lego.oc.core.bean.BaseLayoutBean;
import com.krt.lego.oc.core.surface.BaseWidget;
import com.krt.lego.oc.core.surface.Subgrade;

import org.apmem.tools.layouts.FlowLayout;

/**
 * @author: MaGua
 * @create_on:2021/11/10 14:42
 * @description
 */
public class TagView extends BaseWidget<FrameLayout> {

    private FlowLayout flowLayout;

    public TagView(Subgrade imp, BaseLayoutBean obj, boolean isListChild) {
        super(imp, obj, isListChild);
    }

    @Override
    protected void createView() {
        view = new FrameLayout(subgrade.getCarrier());
        flowLayout = new FlowLayout(subgrade.getCarrier());
        FlowLayout.LayoutParams lp = new FlowLayout.LayoutParams(FlowLayout.LayoutParams.WRAP_CONTENT, FlowLayout.LayoutParams.WRAP_CONTENT);
        flowLayout.setOrientation(FlowLayout.HORIZONTAL);
        flowLayout.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
    }

    @Override
    protected void initView() {
        String val = getStringVal("text");

        //无分隔符
        if (TextUtils.isEmpty(getStringVal("separator")) || !val.contains(getStringVal("separator"))) {
            TextView tv = new TextView(subgrade.getCarrier());
            tv.setText(val);
            setTextStyle(tv);
            flowLayout.addView(tv);
        } else {
            for (int i = 0; i < val.split(getStringVal("separator")).length; i++) {
                TextView tv = new TextView(subgrade.getCarrier());
                tv.setText(val.split(getStringVal("separator"))[i]);
                setTextStyle(tv);
                View view = new View(subgrade.getCarrier());
                FrameLayout.LayoutParams lp1 = new FrameLayout.LayoutParams
                        (getIntVal("gap"), getIntVal("tagHeight"));
                view.setLayoutParams(lp1);
                flowLayout.addView(tv);
                flowLayout.addView(view);
            }
        }
        view.addView(flowLayout);
    }

    /**
     * 设置TextView的样式
     *
     * @param tv
     */
    private void setTextStyle(TextView tv) {
        FrameLayout.LayoutParams lp1 = new FrameLayout.LayoutParams
                (getBooleanVal("autoWidth") ?
                        ViewGroup.LayoutParams.WRAP_CONTENT : (getIntVal("tagWidth") + 20),
                        getIntVal("tagHeight"));
        lp1.setMargins(getIntVal("gap"), 0, getIntVal("gap"), 0);
        tv.setPadding(10, 0, 10, 0);
        tv.setLayoutParams(lp1);
        tv.setGravity(Gravity.CENTER);
        tv.setTextColor(MUtil.getRealColor(getStringVal("fontColor")));

        tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, MUtil.getRealValue(getIntVal("fontSize")));
        GradientDrawable drawable = MUtil.getBgDrawable(getStringVal("bgColor"),
                GradientDrawable.RECTANGLE, getStringVal("shape").equals("square") ? 0 : 10, 2,
                getStringVal("borderColor"));
        tv.setBackgroundDrawable(drawable);
    }
}
