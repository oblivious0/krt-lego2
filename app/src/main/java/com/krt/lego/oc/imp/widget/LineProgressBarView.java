package com.krt.lego.oc.imp.widget;

import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.krt.base.util.MUtil;
import com.krt.lego.R;
import com.krt.lego.oc.core.bean.BaseLayoutBean;
import com.krt.lego.oc.core.surface.BaseWidget;
import com.krt.lego.oc.core.surface.Subgrade;

/**
 * @author: MaGua
 * @create_on:2021/11/10 9:26
 * @description 线型进度条
 */
public class LineProgressBarView extends BaseWidget<FrameLayout> {

    private ProgressBar mProgressBar;
    private TextView textView;

    public LineProgressBarView(Subgrade imp, BaseLayoutBean obj, boolean isListChild) {
        super(imp, obj, isListChild);
    }

    @Override
    protected void createView() {
        view = new FrameLayout(subgrade.getCarrier());
    }

    @Override
    protected void initView() {
        mProgressBar = new ProgressBar(subgrade.getCarrier(), null,
                android.R.attr.progressBarStyleHorizontal);

        //设置圆角半径
        int roundRadius = getIntVal("height") / 2;

        //准备progressBar带圆角的背景Drawable
        //准备progressBar带圆角的进度条Drawable
        //设置圆角弧度
        GradientDrawable progressBg = new GradientDrawable();
        GradientDrawable progressContent = new GradientDrawable();
        if (getBooleanVal("round")) {
            progressBg.setCornerRadius(roundRadius);
            progressContent.setCornerRadius(roundRadius);
        }

        progressBg.setColor(MUtil.getRealColor(getStringVal("inactiveColor")));

        //设置绘制颜色，此处可以自己获取不同的颜色
        progressContent.setColor(MUtil.getRealColor(getStringVal("activeColor")));

        //ClipDrawable是对一个Drawable进行剪切操作，可以控制这个drawable的剪切区域，以及相相对于容器的对齐方式
        ClipDrawable progressClip = new ClipDrawable(progressContent, Gravity.LEFT, ClipDrawable.HORIZONTAL);
        //Setup LayerDrawable and assign to progressBar
        //待设置的Drawable数组
        Drawable[] progressDrawables = {progressBg, progressClip};
        LayerDrawable progressLayerDrawable = new LayerDrawable(progressDrawables);

        //根据ID设置progressBar对应内容的Drawable
        progressLayerDrawable.setId(0, android.R.id.background);
        progressLayerDrawable.setId(1, android.R.id.progress);
        //设置progressBarDrawable
        mProgressBar.setProgressDrawable(progressLayerDrawable);

        FrameLayout.LayoutParams plp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        plp.gravity = Gravity.CENTER;
        mProgressBar.setLayoutParams(plp);
        //最大数量
        int allNum = getIntVal("activeNum") + getIntVal("inactiveNum");
        mProgressBar.setMax(100);
        mProgressBar.setProgress(getIntVal("activeNum") * 100 / allNum);
        view.addView(mProgressBar);
        //显示百分比
        if (getBooleanVal("showPercent")) {
            textView = new TextView(subgrade.getCarrier());
            textView.setTextColor(ContextCompat.getColor(subgrade.getCarrier(), R.color.search_icon_color));
            textView.setText(String.valueOf(getIntVal("activeNum") * 100 / allNum) + "%");
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, MUtil.getRealValue(20));
            FrameLayout.LayoutParams tvlp = new FrameLayout.LayoutParams
                    (FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
            tvlp.gravity = Gravity.LEFT | Gravity.CENTER_VERTICAL;
            tvlp.setMarginStart(100);
            textView.setLayoutParams(tvlp);
            view.addView(textView);
        }
    }


    @Override
    public void bindData(String cid, String key, Object val) {
        if (cid.equals(this.cid)) {
            switch (key) {
                case "percent":
                    try {
                        setProgress(Float.parseFloat(val.toString()));
                        textView.setText(Float.parseFloat(val.toString()) + "%");
                    } catch (Exception e) {
                        setProgress(0);
                        textView.setText("0%");
                    }
                    break;
                default:
            }
        }
    }

    private void setProgress(float i) {
        mProgressBar.setProgress((int) i);
    }
}
