package com.krt.lego.oc.imp.widget;

import com.krt.base.util.MUtil;
import com.krt.base.view.CountDown;
import com.krt.lego.oc.core.bean.BaseLayoutBean;
import com.krt.lego.oc.core.surface.BaseWidget;
import com.krt.lego.oc.core.surface.Subgrade;

/**
 * @author: MaGua
 * @create_on:2021/11/9 10:45
 * @description 倒计时组件
 */
public class CountdownView extends BaseWidget<CountDown> {
    public CountdownView(Subgrade imp, BaseLayoutBean obj, boolean isListChild) {
        super(imp, obj, isListChild);
    }

    @Override
    protected void createView() {
        view = new CountDown(subgrade.getCarrier());
    }

    @Override
    protected void initView() {
        view.setIsShowBorder(getBooleanVal("isShowBorder"),
                getStringVal("borderColor"), getStringVal("bgColor"));
        view.setTextStyle(getIntVal("fontSize"),
                MUtil.getRealColor(getStringVal("color")));
        view.setSeparator(getStringVal("separator"),
                getIntVal("separatorSize"),
                MUtil.getRealColor(getStringVal("separatorColor")));
        view.setHidZeroDay(getBooleanVal("hideZeroDay"));
        view.isShow(getBooleanVal("showDays"),
                getBooleanVal("showHours"),
                getBooleanVal("showMinutes"),
                getBooleanVal("showSeconds"));
        view.setTimeStamp(getStringVal("timestamp"));
    }
}
