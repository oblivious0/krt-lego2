package com.krt.lego.oc.imp.widget;

import com.krt.lego.oc.core.bean.BaseLayoutBean;
import com.krt.lego.oc.core.surface.BaseWidget;
import com.krt.lego.oc.core.surface.Subgrade;
import com.krt.lego.oc.imp.custom.MapView;

/**
 * @author: MaGua
 * @create_on:2021/11/10 11:19
 * @description
 */
public class DrawMapView extends BaseWidget<MapView> {

    public DrawMapView(Subgrade imp, BaseLayoutBean obj, boolean isListChild) {
        super(imp, obj, isListChild);
    }

    @Override
    protected void createView() {
        view = new MapView(subgrade.getCarrier());
    }

    @Override
    protected void initView() {
        view.setImageSize(getIntVal("ow"), getIntVal("height"));
        view.draw(getStringVal("src"));
        view.setMaxLevel(getIntVal("maxScale"));
        view.setMinLevel(getIntVal("minScale"));
    }
}
