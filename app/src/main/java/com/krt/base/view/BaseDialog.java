package com.krt.base.view;

import android.app.Dialog;
import android.content.Context;

import com.krt.base.inter.ClickListener;

/**
 * author:Marcus
 * create on:2019/12/27 14:08
 * description
 * 更新类基础对话框
 */
public abstract class BaseDialog extends Dialog {
    public ClickListener listener;

    public BaseDialog(Context context) {
        super(context);
    }

    public void setListener(ClickListener listener) {
        this.listener = listener;
    }


}
