package com.krt.lego.oc.imp.widget;

import android.view.View;
import android.webkit.WebView;
import android.widget.FrameLayout;

import com.krt.lego.oc.core.bean.BaseLayoutBean;
import com.krt.lego.oc.core.surface.BaseWidget;
import com.krt.lego.oc.core.surface.Subgrade;

/**
 * @author: MaGua
 * @create_on:2021/11/10 14:30
 * @description
 */
public class RichTextView extends BaseWidget<WebView> {

    public RichTextView(Subgrade imp, BaseLayoutBean obj, boolean isListChild) {
        super(imp, obj, isListChild);
    }

    @Override
    protected void createView() {
        view = new WebView(subgrade.getCarrier());
    }

    @Override
    protected void setViewPoi() {
        if (subgrade.getDesigner().pageType.equals("richText")) {
            FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams
                    (getIntVal("width"), FrameLayout.LayoutParams.MATCH_PARENT);
            lp.setMargins(0, getIntVal("y"), 0, 0);
            view.setLayoutParams(lp);
        } else {
            super.setViewPoi();
        }
    }

    @Override
    protected void initView() {
        view.loadDataWithBaseURL(null,
                addStyle(getStringVal("text")), "text/html; charset=utf-8", "utf-8", null);
        view.setVisibility(getBooleanVal("isHidden") ? View.GONE : View.VISIBLE);
    }

    private String addStyle(String s) {
        StringBuffer sb = new StringBuffer(s);
        sb.append("<style>\n" +
                "    img{ display: block; width: 100% !important; height: auto !important;; }\n" +
                "    video{ display: block; width: 100% !important; height: auto !important;; }\n" +
                "  </style>");
        return sb.toString();
    }
}
