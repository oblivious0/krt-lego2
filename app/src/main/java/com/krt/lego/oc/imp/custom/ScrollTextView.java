package com.krt.lego.oc.imp.custom;

import android.content.Context;
import android.text.method.ScrollingMovementMethod;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.appcompat.widget.AppCompatTextView;

/**
 * author: MaGua
 * create on:2020/12/28 15:47
 * description
 */
public class ScrollTextView extends AppCompatTextView {
    public ScrollTextView(Context context) {
        super(context);
        setMovementMethod(ScrollingMovementMethod.getInstance());
    }

    public ScrollTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setMovementMethod(ScrollingMovementMethod.getInstance());
    }

    public ScrollTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setMovementMethod(ScrollingMovementMethod.getInstance());
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        getParent().requestDisallowInterceptTouchEvent(true);
        return super.onTouchEvent(ev);
    }
}
