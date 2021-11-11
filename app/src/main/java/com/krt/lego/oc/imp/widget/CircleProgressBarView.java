package com.krt.lego.oc.imp.widget;

import com.github.lzyzsd.circleprogress.DonutProgress;
import com.krt.base.util.MUtil;
import com.krt.lego.oc.core.bean.BaseLayoutBean;
import com.krt.lego.oc.core.surface.BaseWidget;
import com.krt.lego.oc.core.surface.Subgrade;

/**
 * @author: MaGua
 * @create_on:2021/11/10 11:41
 * @description
 */
public class CircleProgressBarView extends BaseWidget<DonutProgress> {

    public CircleProgressBarView(Subgrade imp, BaseLayoutBean obj, boolean isListChild) {
        super(imp, obj, isListChild);
    }

    @Override
    protected void createView() {
        view = new DonutProgress(subgrade.getCarrier());
    }

    @Override
    protected void initView() {
        view.setMax(100);
        view.setProgress(getIntVal("percent"));
        view.setTextSize(30);
        view.setFinishedStrokeWidth(20);
        view.setUnfinishedStrokeWidth(20);
        view.setUnfinishedStrokeColor(MUtil.getRealColor(getStringVal("inactiveColor")));
        view.setFinishedStrokeColor(MUtil.getRealColor(getStringVal("activeColor")));
    }

    @Override
    public void bindData(String cid, String key, Object val) {
        if (cid.equals(this.cid)) {
            switch (key) {
                case "percent":
                    try {
                        view.setProgress(Float.parseFloat(val.toString()));
                    } catch (Exception e) {
                        view.setProgress(0);
                    }
                    break;
            }
            style_1.put(key,val);
        }
    }
}
