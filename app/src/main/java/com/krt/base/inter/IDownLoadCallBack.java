package com.krt.base.inter;

import com.lzy.okgo.model.Response;

import java.io.File;

/**
 * @author Marcus
 * @package krt.wid.inter
 * @description
 * @time 2019/1/2
 */
public interface IDownLoadCallBack {
    void onSuccess(File file);

    void onError(Response<File> response);
}
