package com.krt.lego.oc.imp.widget;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.krt.base.util.MUtil;
import com.krt.lego.oc.core.bean.BaseLayoutBean;
import com.krt.lego.oc.core.surface.BaseWidget;
import com.krt.lego.oc.core.surface.Subgrade;
import com.krt.lego.oc.imp.custom.JustifyTextView;

/**
 * @author: MaGua
 * @create_on:2021/10/3 9:16
 * @description
 */
public class LabelView extends BaseWidget<TextView> {

    public LabelView(Subgrade imp, BaseLayoutBean obj, boolean isListChild) {
        super(imp, obj, isListChild);
    }

    @Override
    public TextView getRawView() {
        return view;
    }

    @Override
    protected void createView() {
        if (getStringVal("textAlign").equals("justify")) {
            view = new JustifyTextView(subgrade.getCarrier());
        } else {
            view = new TextView(subgrade.getCarrier());
        }
    }

    @Override
    protected void initView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            view.setLineHeight(MUtil.getRealValue(getIntVal("lineHeight")));
        }
        int breakLines = getIntVal("breakLines");
        if (breakLines != 0) {
            view.setMaxLines(breakLines);
        }
        if (getBooleanVal("lineBreakMode")) {
            view.setEllipsize(TextUtils.TruncateAt.END);
        }

        view.setText(getStringVal("text"));
        if (getIntVal("textIndent") != 0) {
            String paddingText = "";
            for (int i = 0; i < getIntVal("textIndent"); i++) {
                paddingText += "缩";
            }
            SpannableStringBuilder span = new SpannableStringBuilder(paddingText + view.getText());
            span.setSpan(new ForegroundColorSpan(Color.TRANSPARENT), 0, getIntVal("textIndent"),
                    Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            view.setText(span);
        }
        view.setTextColor(MUtil.getRealColor(getStringVal("color")));
        view.setIncludeFontPadding(false);
        view.setTextSize(TypedValue.COMPLEX_UNIT_PX, MUtil.getRealValue(getIntVal("fontSize")));
        if (!(view instanceof JustifyTextView)) {
            int v = Gravity.TOP, h = Gravity.LEFT;
            switch (getStringVal("textAlign")) {
                case "left":
                    h = (Gravity.LEFT);
                    break;
                case "right":
                    h = (Gravity.RIGHT);
                    break;
                case "center":
                    h = (Gravity.CENTER_HORIZONTAL);
                    break;
                default:
            }
            if (!TextUtils.isEmpty(getStringVal("vAlign"))) {
                switch (getStringVal("vAlign")) {
                    case "top":
                        v = Gravity.TOP;
                        break;
                    case "bottom":
                        v = Gravity.BOTTOM;
                        break;
                    case "center":
                        v = Gravity.CENTER_VERTICAL;
                        break;
                    default:
                }
            }
            view.setGravity(v | h);
        }
        GradientDrawable drawable = MUtil.getBgDrawable(getStringVal("bgColor"),
                GradientDrawable.RECTANGLE, getIntVal("borderRadius"), getIntVal("borderWidth"),
                getStringVal("borderColor"));
        view.setBackground(drawable);
        view.setVisibility(getBooleanVal("isHidden") ? View.GONE : View.VISIBLE);
    }

    @Override
    public void bindData(String cid, String key, Object val) {
        if (cid.equals(this.cid)){
            style_1.put(key, val);
            initView();
        }
    }
}
