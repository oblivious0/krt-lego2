package com.krt.base.util;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.krt.lego.R;

/**
 * @author Marcus
 * @package krt.wid.util
 * @description 标题基础类   左侧默认点击事件是finish当前acitivty 如果需要其他操作直接设置左侧监听重写就行
 * @time 2018/5/16
 */

public class MTitle extends LinearLayout implements ViewTreeObserver.OnGlobalLayoutListener {
    Context mContext;

    int width;

    int height;
    //单位为dp
    float leftMargin;
    //单位为dp
    float rightMargin;
    //单位为dp
    float topPadding;
    //单位为dp
    float bottomPadding;

    FrameLayout leftView;

    FrameLayout rightView;

    FrameLayout centerView;

    View leftCustomView;

    View rightCustomView;

    View centerCustomView;

    float left_textSize;

    float right_textSize;

    float center_textSize;

    int left_textColor;

    int right_textColor;

    int center_textColor;

    String center_text;

    String left_text;

    String right_text;

    int center_img;

    int left_img;

    int right_img;

    int color;

    float elevation;

    MTileLeftClikcListener mLeftClikcListener;

    MTitleRightClickListener mRightClickListener;

    public MTitle setLeftClikcListener(MTileLeftClikcListener leftClikcListener) {
        mLeftClikcListener = leftClikcListener;
        return this;
    }

    public MTitle setRightClickListener(MTitleRightClickListener rightClickListener) {
        mRightClickListener = rightClickListener;
        return this;
    }

    public void setCustomLeftView(View customLeftView) {
        if (leftView != null) {
            setCustomView(leftView, customLeftView);
        } else {
            this.leftCustomView = customLeftView;
        }

    }

    public void setCustomRightView(View customRightView) {
        if (rightView != null) {
            setCustomView(rightView, customRightView);
        } else {
            this.rightCustomView = customRightView;
        }

    }

    public void setCustomCenterView(View customCenterView) {
        if (centerView != null) {
            setCustomView(centerView, customCenterView);
        } else {
            this.centerCustomView = customCenterView;
        }
    }

    public MTitle(Context context) {
        this(context, null);
    }

    public MTitle(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MTitle(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        this.setOrientation(HORIZONTAL);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.MTitle);
        center_textSize = ta.getDimension(R.styleable.MTitle_center_textSize, -1);
        left_textSize = ta.getDimension(R.styleable.MTitle_left_textSize, -1);
        right_textSize = ta.getDimension(R.styleable.MTitle_right_textSize, -1);
        center_textColor = ta.getColor(R.styleable.MTitle_center_textColor, Color.WHITE);
        left_textColor = ta.getColor(R.styleable.MTitle_left_textColor, Color.WHITE);
        right_textColor = ta.getColor(R.styleable.MTitle_right_textColor, Color.WHITE);
        center_text = ta.getString(R.styleable.MTitle_center_text);
        left_text = ta.getString(R.styleable.MTitle_left_text);
        right_text = ta.getString(R.styleable.MTitle_right_text);
        center_img = ta.getResourceId(R.styleable.MTitle_center_img, -1);
        left_img = ta.getResourceId(R.styleable.MTitle_left_img, -1);
        right_img = ta.getResourceId(R.styleable.MTitle_right_img, -1);
        leftMargin = ta.getDimension(R.styleable.MTitle_leftMargin, 8);
        rightMargin = ta.getDimension(R.styleable.MTitle_rightMaring, 8);
        topPadding = ta.getDimension(R.styleable.MTitle_topPadding, 12);
        bottomPadding = ta.getDimension(R.styleable.MTitle_bottomPadding, 12);
        elevation = ta.getDimension(R.styleable.MTitle_elevation, -1);
        ta.recycle();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    /**
     * 初始化容器
     */
    private void initVesselView() {
        leftView = new FrameLayout(mContext);
        rightView = new FrameLayout(mContext);
        centerView = new FrameLayout(mContext);
        leftView.setLayoutParams(new FrameLayout.LayoutParams(width / 4, height));
        rightView.setLayoutParams(new FrameLayout.LayoutParams(width / 4, height));
        centerView.setLayoutParams(new FrameLayout.LayoutParams(width / 2, height));
        leftView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mLeftClikcListener != null) {
                    mLeftClikcListener.onLeftClick();
                } else {
                    if (mContext instanceof Activity) ((Activity) mContext).finish();
                }
            }
        });
        rightView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mRightClickListener != null) {
                    mRightClickListener.onRightClick();
                } else {

                }
            }
        });
        this.addView(leftView);
        this.addView(centerView);
        this.addView(rightView);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && elevation != -1)
            this.setElevation(elevation);
        initTextView();
        initImageView();
        initCustomView();
    }

    /**
     * 初始化TextView
     */
    private void initTextView() {
        if (left_text != null)
            leftView.addView(getTextView(left_text, left_textSize, left_textColor, 0));
        if (right_text != null)
            rightView.addView(getTextView(right_text, right_textSize, right_textColor, 2));
        if (center_text != null)
            centerView.addView(getTextView(center_text, center_textSize, center_textColor, 1));
    }

    /**
     * 初始化ImageView
     */
    private void initImageView() {
        if (left_img != -1) {
            leftView.removeAllViews();
            leftView.addView(getImageView(left_img, 0));
        }
        if (center_img != -1) {
            centerView.removeAllViews();
            centerView.addView(getImageView(center_img, 1));
        }
        if (right_img != -1) {
            rightView.removeAllViews();
            rightView.addView(getImageView(right_img, 2));
        }

    }

    /**
     * 初始化自定义view
     */
    private void setCustomView(FrameLayout parent, View customView) {
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.CENTER;
        parent.removeAllViews();
        customView.setLayoutParams(lp);
        parent.addView(customView);
    }

    /**
     * 初始化自定义view
     */
    private void initCustomView() {
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.CENTER;
        if (leftCustomView != null) {
            leftView.removeAllViews();
            leftCustomView.setLayoutParams(lp);
            leftView.addView(leftCustomView);
        }
        if (rightCustomView != null) {
            rightView.removeAllViews();
            rightCustomView.setLayoutParams(lp);
            rightView.addView(rightCustomView);
        }
        if (centerCustomView != null) {
            centerView.removeAllViews();
            centerCustomView.setLayoutParams(lp);
            centerView.addView(centerCustomView);
        }
    }

    /**
     * 配置TextView
     *
     * @param text
     * @param textSize
     * @param textColor
     * @return
     */
    private TextView getTextView(String text, float textSize, int textColor, int gravity) {
        TextView tv = new TextView(mContext);
        tv.setText(text);
        tv.setGravity(Gravity.CENTER_VERTICAL);
        tv.setPadding((int) MUtil.dpTopx(2), 0, 0, (int) MUtil.dpTopx(2));
        tv.setMaxLines(1);
        if (textSize != -1) tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        tv.setTextColor(textColor);
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        switch (gravity) {
            case 0:
                lp.gravity = Gravity.CENTER_VERTICAL;
                lp.setMargins((int) MUtil.dpTopx(leftMargin), 0, 0, 0);
                break;
            case 1:
                lp.gravity = Gravity.CENTER;
                break;
            case 2:
                lp.gravity = Gravity.CENTER_VERTICAL | Gravity.END;
                lp.setMargins(0, 0, (int) MUtil.dpTopx(rightMargin), 0);
                break;
        }
        tv.setLayoutParams(lp);
        return tv;
    }

    /**
     * 配置imageview
     *
     * @param gravity 代表位置 0左边 1中间 2右边  左右二边需要设置padding
     */
    private ImageView getImageView(int img, int gravity) {
        ImageView imageView = new ImageView(mContext);
        imageView.setImageResource(img);
        imageView.setPadding(0, (int) MUtil.dpTopx(topPadding), 0, (int) MUtil.dpTopx(bottomPadding));
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        switch (gravity) {
            case 0:
                lp.gravity = Gravity.CENTER_VERTICAL;
                lp.setMargins((int) MUtil.dpTopx(leftMargin), 0, 0, 0);
                break;
            case 1:
                lp.gravity = Gravity.CENTER;
                break;
            case 2:
                lp.gravity = Gravity.CENTER_VERTICAL | Gravity.END;
                lp.setMargins(0, 0, (int) MUtil.dpTopx(rightMargin), 0);
                break;
        }
        imageView.setLayoutParams(lp);
        return imageView;
    }

    @Override
    public void onGlobalLayout() {
        width = this.getWidth();
        height = this.getHeight();
        if (getBackground() == null) {
            setBackgroundColor(ContextCompat.getColor(mContext, android.R.color.white));
        }
        initVesselView();
        getViewTreeObserver().removeOnGlobalLayoutListener(this);  //当测量到视图的高度和宽度移除相应观察者
    }

    public void setCenterText(String text) {
        setCenterText(text, -1);
    }

    public void setCenterText(String text, int textSize) {
        setCenterText(text, textSize, -1);
    }

    public void setCenterText(String text, int textSize, int color) {
        setCenterText(text, textSize, TypedValue.COMPLEX_UNIT_DIP, color);
    }

    public void setCenterText(String text, int textSize, int unit, int color) {
        TextView textView = new TextView(mContext);
        textView.setText(text);
        if (textSize != -1) {
            textView.setTextSize(unit, textSize);
        }
        if (color != -1) {
            textView.setTextColor(color);
        }
        setCustomCenterView(textView);
    }

    public void setLeftText(String text) {
        setLeftText(text, -1);
    }

    public void setLeftText(String text, int textSize) {
        setLeftText(text, textSize, -1);
    }

    public void setLeftText(String text, int textSize, int color) {
        setLeftText(text, textSize, TypedValue.COMPLEX_UNIT_DIP, color);
    }

    public void setLeftText(String text, int textSize, int unit, int color) {
        TextView textView = new TextView(mContext);
        textView.setText(text);
        if (textSize != -1)
            textView.setTextSize(unit, textSize);
        if (color != -1)
            textView.setTextColor(color);
        setCustomLeftView(textView);
    }

    public void setRightText(String text) {
        setRightText(text, -1);
    }

    public void setRightText(String text, int textSize) {
        setRightText(text, textSize, -1);
    }

    public void setRightText(String text, int textSize, int color) {
        setRightText(text, textSize, TypedValue.COMPLEX_UNIT_DIP, color);
    }

    public void setRightText(String text, int textSize, int unit, int color) {
        TextView textView = new TextView(mContext);
        textView.setText(text);
        if (textSize != -1)
            textView.setTextSize(unit, textSize);
        if (color != -1)
            textView.setTextColor(color);
        setCustomRightView(textView);
    }


    public interface MTileLeftClikcListener {
        void onLeftClick();
    }

    public interface MTitleRightClickListener {
        void onRightClick();
    }
}
