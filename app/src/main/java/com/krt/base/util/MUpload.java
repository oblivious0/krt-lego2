package com.krt.base.util;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.Toast;

import com.github.lzyzsd.circleprogress.DonutProgress;
import com.krt.base.inter.IUpload;
import com.krt.base.inter.UploadCallBack;
import com.krt.base.net.callback.JsonCallback;
import com.krt.base.view.CircleProgerssDialog;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.PostRequest;
import com.lzy.okgo.request.base.Request;

import java.io.File;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import top.zibin.luban.Luban;

import com.krt.lego.R;

/**
 * @author Marcus
 * @package krt.wid.http.upload
 * @description
 * @time 2018/2/23
 */

public class MUpload<T> implements IUpload {
    //上下文对象~
    private Context mContext;
    //上传接口地址
    private String url;
    //回调函数
    private UploadCallBack<T> mUploadCallBack;

    private CircleProgerssDialog dialog;

    private DonutProgress mProgress;

    private Map<String, String> params;

    private Map<String, String> headers;

    private Map<String, List<String>> files;

    private boolean needCompress;

    private String fileKey;

    private List<String> paths;

    private boolean showDialog;


    public static <E> Builder<E> newBuilder(Context context) {
        return new Builder<>(context);
    }


    private MUpload(Builder<T> builder) {
        this.mContext = builder.mContext;
        this.url = builder.url;
        this.params = builder.params;
        this.headers = builder.headers;
        this.files = builder.files;
        this.showDialog = builder.showDialog;
        this.needCompress = builder.needCompress;
        this.mUploadCallBack = builder.mUploadCallBack;
        for (String key : files.keySet()) {
            fileKey = key;
            paths = files.get(key);
        }
        dialog = new CircleProgerssDialog(mContext);
        dialog.setOnCancelListener(dialog -> OkGo.getInstance().cancelTag(MUpload.this));
    }

    @SuppressLint("CheckResult")
    @Override
    public void compress() {
        //代表传入的为文件路径集
        if (paths != null && !paths.isEmpty()) {
            Observable.just(paths)
                    .map(new Function<List<String>, List<File>>() {
                        @Override
                        public List<File> apply(List<String> strings) {
                            try {
                                return Luban.with(mContext).load(strings).get();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            return null;
                        }
                    })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<List<File>>() {
                        @Override
                        public void accept(List<File> files) throws Exception {
                            if (files != null && !files.isEmpty()) {
                                doUpload(files);
                            } else {
                                Toast.makeText(mContext, R.string.error_compress, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    @Override
    public void doUpload(List<File> files) {
        if (files == null || files.isEmpty()) return;
        if (url == null || url.equals("")) {
            MToast.showToast(mContext, "上传地址不能为空!");
            return;
        }
        PostRequest<T> request = OkGo.post(url);
        if (params != null && !params.keySet().isEmpty()) {
            for (String key : params.keySet()) {
                request.params(key, params.get(key));
            }
        }
        if (headers != null && !headers.keySet().isEmpty()) {
            for (String key : headers.keySet()) {
                request.headers(key, headers.get(key));
            }
        }
        //获取传入的泛型
        Type genType = mUploadCallBack.getClass().getGenericSuperclass();
        Type type = ((ParameterizedType) genType).getActualTypeArguments()[0];
        request.addFileParams(fileKey, files)
                .tag(this)
                .execute(new JsonCallback<T>(type) {

                    @Override
                    public void onSuc(Response<T> response) {
                        mUploadCallBack.onSuccess(response.body());
                    }

                    @Override
                    public void onStart(Request<T, ? extends Request> request) {
                        super.onStart(request);
                        if (showDialog) {
                            dialog.show();
                            mProgress = dialog.getdProgress();
                            mProgress.setMax(100);
                        }
                    }

                    @Override
                    public void uploadProgress(Progress progress) {
                        super.uploadProgress(progress);
                        if ((int) (progress.fraction * 100) == 100) {
                            if (dialog != null && dialog.isShowing()) {
                                dialog.dismiss();
                            }
                        } else {
                            if (dialog != null && dialog.isShowing()) {
                                mProgress.setProgress((int) (progress.fraction * 100));
                            }
                        }

                    }


                    @Override
                    public void onError(Response<T> response) {
                        super.onError(response);
                        if (dialog != null && dialog.isShowing()) {
                            dialog.dismiss();
                        }
                        mUploadCallBack.onError(response);
                    }
                });
    }

    private void upload() {
        //  当是上传图片的时候进行压缩操作
        if (needCompress) {
            compress();
        } else {
            List<File> files = new ArrayList<>();
            for (String path : paths) {
                files.add(new File(path));
            }
            doUpload(files);
        }

    }


    public static class Builder<T> {
        //上下文对象~
        private Context mContext;
        //上传接口地址
        private String url;
        //当上传的文件为图片时。是否调用算法进行图片压缩
        private boolean needCompress = false;

        private Map<String, String> params;

        private Map<String, String> headers;

        private Map<String, List<String>> files;

        private UploadCallBack<T> mUploadCallBack;


        private boolean showDialog = true;


        public Builder(Context context) {
            this.mContext = context;
            params = new Hashtable<>();
            headers = new Hashtable<>();
        }

        public Builder<T> addFile(String fileKey, List<String> path) {
            files = new Hashtable<>();
            files.put(fileKey, path);
            return this;
        }

        public Builder<T> setUrl(String url) {
            this.url = url;
            return this;
        }

        public Builder<T> addParam(String key, String value) {
            if (params != null) {
                params.put(key, value);
            }
            return this;
        }

        public Builder<T> addHeader(String key, String value) {
            if (headers != null) {
                headers.put(key, value);
            }
            return this;
        }

        public Builder<T> setNeedCompress(boolean needCompress) {
            this.needCompress = needCompress;
            return this;
        }

        public void execute(UploadCallBack<T> uploadListener) {
            this.mUploadCallBack = uploadListener;
            new MUpload<>(this).upload();
        }

        public Builder<T> setShowDialog(boolean showDialog) {
                this.showDialog = showDialog;
                return this;
        }

    }

}
