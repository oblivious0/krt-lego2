package com.krt.lego.oc.imp.widget;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;

import com.krt.lego.oc.core.bean.BaseLayoutBean;
import com.krt.lego.oc.core.surface.BaseWidget;
import com.krt.lego.oc.core.surface.Subgrade;
import com.krt.lego.oc.core.tools.ModuleViewFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: MaGua
 * @create_on:2021/11/9 9:56
 * @description 滚动容器
 */
public class ScrollLayoutView extends BaseWidget<HorizontalScrollView> {

    public List<BaseWidget> childViews;

    public ScrollLayoutView(Subgrade imp, BaseLayoutBean obj, boolean isListChild) {
        super(imp, obj, isListChild);
    }

    @Override
    protected void createView() {
        childViews = new ArrayList<>();
        view = new HorizontalScrollView(subgrade.getCarrier());
    }

    @Override
    protected void initView() {
        FrameLayout frameLayout = new FrameLayout(subgrade.getCarrier());
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams
                (FrameLayout.LayoutParams.MATCH_PARENT,FrameLayout.LayoutParams.MATCH_PARENT);
        frameLayout.setLayoutParams(lp);
        view.addView(frameLayout);
        if (bean.getChildren() != null && !bean.getChildren().isEmpty()) {
            ModuleViewFactory.createViews(bean.getChildren(), subgrade, frameLayout, childViews, isListChild,data);
        }
        view.setVisibility(getBooleanVal("isHidden") ? View.GONE : View.VISIBLE);
    }

}
