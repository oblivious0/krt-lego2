package com.krt.lego;

import android.os.Environment;

import com.blankj.utilcode.util.AppUtils;

import java.io.File;

/**
 * author: MaGua
 * create on:2021/2/27 15:30
 * description
 */
public class Constants {
    /**
     * 基础路径
     */
    public static final String basePath = Environment.getExternalStorageDirectory().getPath() + File.separator
            + AppUtils.getAppName() + File.separator;

    public static final String defPath = basePath + "module/";

    /**
     * 图片路径
     */
    public static final String imgPath = basePath + "module/image/";

    /**
     * 切图路径
     */
    public static final String iconPath = basePath + "module/image/icon/";

    /**
     * json路径
     */
    public static final String jsonPath = basePath + "module/json/";

    /**
     * 用户路径
     */
    public static final String usePath = basePath + "file/";
}
