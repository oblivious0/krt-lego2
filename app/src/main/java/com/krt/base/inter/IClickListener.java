package com.krt.base.inter;

import com.krt.base.view.BaseDialog;

/**
 * author:Marcus
 * create on:2020/1/19 9:31
 * description
 */
public interface IClickListener {
    void confirm();

    void cancel(BaseDialog dialog);
}
