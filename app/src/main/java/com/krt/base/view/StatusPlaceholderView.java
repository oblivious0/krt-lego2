package com.krt.base.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.BarUtils;

/**
 * @author: MaGua
 * @create_on:2021/8/10 15:30
 * @description
 */
public class StatusPlaceholderView extends View {
    public StatusPlaceholderView(Context context) {
        this(context, null);
    }

    public StatusPlaceholderView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StatusPlaceholderView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setBackgroundColor(Color.TRANSPARENT);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSpec = MeasureSpec.getSize(widthMeasureSpec);
        //高度我们要设置成statusbar的高度
        int measureHeight = BarUtils.getStatusBarHeight();
        setMeasuredDimension(widthSpec, measureHeight);

    }
}
