package com.krt.base.update;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.krt.base.util.MUtil;
import com.krt.lego.R;

/**
 * @author Marcus
 * @package krt.wid.nohttp
 * @description
 * @time 2018/7/25
 */

public class CustomProgress extends RelativeLayout {
    ProgressBar mBar;

    ImageView img;

    TextView tv;

    float progressWidth;


    public CustomProgress(Context context) {
        super(context);
    }

    public CustomProgress(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomProgress(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        mBar = findViewById(R.id.progress);
        img = findViewById(R.id.img);
        tv = findViewById(R.id.progress_tv);
        mBar.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                progressWidth = mBar.getWidth();
                mBar.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }

    public void setProgress(int progress) {
        mBar.setProgress(progress);
        tv.setText(progress + "%");
        img.setTranslationX(progress * progressWidth / 100 + 6);
        tv.setTranslationX(progress * progressWidth / 100 - 6);
        if (progress == 100) {   //考虑到原点越出距离的问题设置下编剧
            LayoutParams imglp = (LayoutParams) img.getLayoutParams();
            LayoutParams tvlp = (LayoutParams) tv.getLayoutParams();
            imglp.setMarginEnd((int) MUtil.dpTopx(1 / 2));
            tvlp.setMarginEnd((int) MUtil.dpTopx(12));
            img.setLayoutParams(imglp);
            tv.setLayoutParams(tvlp);
        }
        img.invalidate();
        tv.invalidate();
    }

    public int getProgress() {
        return mBar.getProgress();
    }
}
