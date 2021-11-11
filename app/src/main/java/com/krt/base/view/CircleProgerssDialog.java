package com.krt.base.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Window;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.github.lzyzsd.circleprogress.DonutProgress;

import com.krt.lego.R;
/**
 * Created by Administrator on 2017/12/14.
 */

public class CircleProgerssDialog extends Dialog {

    private DonutProgress dProgress;

    private String content;


    public CircleProgerssDialog(@NonNull Context context) {
        super(context);
    }

    public CircleProgerssDialog(@NonNull Context context, String content) {
        super(context);
        this.content = content;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.layout_progressdialog);
        setCanceledOnTouchOutside(false);
        dProgress = (DonutProgress) findViewById(R.id.donutprogress);
        if (!TextUtils.isEmpty(content)) {
            ((TextView) findViewById(R.id.content)).setText(content);
        }
    }

    public DonutProgress getdProgress() {
        return dProgress;
    }
}
