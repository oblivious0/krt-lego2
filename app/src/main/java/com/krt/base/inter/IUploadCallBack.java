package com.krt.base.inter;


import com.lzy.okgo.model.Response;

/**
 * @author Marcus
 * @package krt.wid.tour_gz.utils.upload
 * @description
 * @time 2018/3/7
 */

public interface IUploadCallBack<T> {
    void onSuccess(T data);

    void onError(Response<T> response);
}
