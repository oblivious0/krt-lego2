package com.krt.lego.oc.imp.widget;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.krt.base.util.MUtil;
import com.krt.lego.oc.core.bean.BaseLayoutBean;
import com.krt.lego.oc.core.surface.BaseWidget;
import com.krt.lego.oc.core.surface.Subgrade;
import com.krt.lego.oc.imp.custom.ScrollTextView;

import java.util.Optional;

/**
 * @author: MaGua
 * @create_on:2021/11/10 9:40
 * @description
 */
public class ScorllLabelView extends BaseWidget<TextView> {

    public ScorllLabelView(Subgrade imp, BaseLayoutBean obj, boolean isListChild) {
        super(imp, obj, isListChild);
    }

    @Override
    protected void createView() {
        view = new ScrollTextView(subgrade.getCarrier());
        view.setHorizontalScrollBarEnabled(false);
        view.setVerticalScrollBarEnabled(true);
        view.setSingleLine(false);
    }

    @Override
    protected void initView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            view.setLineHeight(MUtil.getRealValue(getIntVal("lineBreakLines")));
        }
        if (getIntVal("lineBreakLines") != 0) {
            view.setMaxLines(getIntVal("lineBreakLines"));
        }
        if (getBooleanVal("lineBreakMode")) {
            view.setEllipsize(TextUtils.TruncateAt.valueOf("END"));
        }

        view.setText(Optional.ofNullable(getStringVal("text")).orElse("").trim());
        String paddingText = "";
        if (getIntVal("textIndent") != 0) {
            for (int i = 0; i < getIntVal("textIndent") / 2; i++) {
                paddingText += "缩";
            }
        }
        SpannableStringBuilder span = new SpannableStringBuilder(paddingText + view.getText());
        span.setSpan(new ForegroundColorSpan(Color.TRANSPARENT), 0, getIntVal("textIndent") / 2,
                Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        view.setText(span);
        view.setTextColor(MUtil.getRealColor(getStringVal("color")));

        view.setSingleLine(false);
        view.setIncludeFontPadding(false);
//        view.setPadding(bean.getStyle().getTextIndent(), 0, 0, 0);
        view.setLineSpacing(0, getIntVal("lineHeight") / getIntVal("fontSize"));
        view.setTextSize(TypedValue.COMPLEX_UNIT_PX, MUtil.getRealValue(getIntVal("fontSize")));
        view.setMovementMethod(ScrollingMovementMethod.getInstance());
        switch (getStringVal("textAlign")) {
            case "left":
                view.setGravity(Gravity.START);
                break;
            case "right":
                view.setGravity(Gravity.END);
                break;
            case "center":
                view.setGravity(Gravity.CENTER);
                break;
            default:
                view.setGravity(Gravity.CENTER_VERTICAL);
                break;
        }

        GradientDrawable drawable = MUtil.getBgDrawable(getStringVal("bgColor"),
                GradientDrawable.RECTANGLE, getIntVal("borderRadius"), getIntVal("borderWidth"),
                getStringVal("borderColor"));
        view.setBackgroundDrawable(drawable);
        view.setVisibility(getBooleanVal("isHidden") ? View.GONE : View.VISIBLE);
    }
}
