package com.krt.base.inter;

import com.lzy.okgo.model.Response;

import java.io.File;

/**
 * author:Marcus
 * create on:2020/1/19 9:36
 * description
 */
public abstract class DownLoadCallBack implements IDownLoadCallBack {
    @Override
    public void onError(Response<File> response) {

    }
}
