package com.krt.lego.oc.imp.widget;

import android.view.View;

import com.krt.base.util.MUtil;
import com.krt.lego.oc.core.bean.BaseLayoutBean;
import com.krt.lego.oc.core.surface.BaseWidget;
import com.krt.lego.oc.core.surface.Subgrade;
import com.krt.lego.oc.core.tools.ModuleViewFactory;
import com.lihang.ShadowLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author: MaGua
 * @create_on:2021/10/3 15:02
 * @description
 */
public class LayoutView extends BaseWidget<ShadowLayout> {

    public List<BaseWidget> childViews;

    public LayoutView(Subgrade imp, BaseLayoutBean obj, boolean isListChild) {
        super(imp, obj, isListChild);
    }

    @Override
    public ShadowLayout getRawView() {
        return view;
    }

    @Override
    protected void createView() {
        view = new ShadowLayout(subgrade.getCarrier());
        childViews = new ArrayList<>();
    }

    @Override
    protected void initView() {
        view.setStrokeColor(MUtil.getRealColor(getStringVal("borderColor")));
        view.setStrokeWidth(getIntVal("borderWidth"));
        view.setLayoutBackground(MUtil.getRealColor(getStringVal("bgColor")));
        view.setShadowColor(MUtil.getRealColor(getStringVal("shadowColor")));
        view.setShadowHidden(false);
        if (Optional.ofNullable(MUtil.getRealColor(getStringVal("shadowColor"))).orElse(0) != 0) {
            view.setShadowLimit(10);
        } else {
            view.setShadowLimit(0);
        }
        int corner = getIntVal("radius");
        view.setVisibility(corner);
        view.setClipChildren(false);
        //clickable为true时，如果处于列表内组件会抢占焦点导致无法触发列表点击事件
        view.setClickable(false);
        view.setVisibility(getBooleanVal("isHidden") ? View.GONE : View.VISIBLE);
    }

    @Override
    protected void bindInMainThread() {
        if (bean.getChildren() != null && !bean.getChildren().isEmpty()) {
            ModuleViewFactory.createViews(bean.getChildren(), subgrade, view, childViews, isListChild, data);
        } else {
            //ShadowLayout里不存在子组件时不会渲染
            view.addView(new View(subgrade.getCarrier()));
        }
    }

    @Override
    public void bindData(String cid, String key, Object val) {
        if (cid.equals(this.cid)) {
            style_1.put(key, val);
        }
        if (childViews.size() != 0) {
            for (BaseWidget baseV : childViews) {
                baseV.bindData(cid, key, val);
            }
        }
    }

    @Override
    public void bindEvent() {
        if (Optional.ofNullable(matchingEvent()).orElse(new ArrayList<>()).size() > 0) {
            view.setClickable(true);
            super.bindEvent();
        }
    }
}
