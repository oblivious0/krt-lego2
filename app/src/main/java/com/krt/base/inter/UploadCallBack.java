package com.krt.base.inter;

import com.lzy.okgo.model.Response;

/**
 * author:Marcus
 * create on:2020/1/19 9:27
 * description
 */
public abstract class UploadCallBack<T> implements IUploadCallBack<T> {
    @Override
    public void onError(Response<T> response) {

    }
}
