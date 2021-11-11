package com.krt.base.util;

import android.content.Context;
import android.widget.Toast;

/**
 * @author Marcus
 * @package krt.wid.util
 * @description
 * @time 2018/3/1
 */

public class MToast {
    private static Toast toast;

    public static void showToast(Context context, String info) {
        if (toast == null) {
            toast = Toast.makeText(context, info, Toast.LENGTH_SHORT);
        } else {
            toast.setText(info);
            toast.setDuration(Toast.LENGTH_SHORT);
        }
        toast.show();
    }

    public static void cancelToast() {
        if (toast != null) {
            toast.cancel();
        }
    }
}
