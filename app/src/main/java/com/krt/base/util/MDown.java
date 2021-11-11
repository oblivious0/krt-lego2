package com.krt.base.util;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Environment;
import android.text.TextUtils;

import com.blankj.utilcode.util.AppUtils;
import com.github.lzyzsd.circleprogress.DonutProgress;
import com.krt.base.inter.DownLoadCallBack;
import com.krt.base.view.CircleProgerssDialog;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.FileCallback;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;

import java.io.File;


/**
 * @author Marcus
 * @package krt.wid.util
 * @description 下载工具类
 * @time 2019/1/2
 */
public class MDown {
    private Context mContext;

    private String url;

    private String path;

    private String fileDir;

    private String fileName;

    private boolean showDialog;

    private CircleProgerssDialog dialog;

    private DonutProgress mProgress;


    private DownLoadCallBack downLoadListener;


    private MDown(Builder builder) {
        this.mContext = builder.mContext;
        this.url = builder.url;
        this.fileDir = builder.fileDir;
        this.fileName = builder.fileName;
        this.downLoadListener = builder.mCallBack;
        this.showDialog = builder.showDialog;
        dialog = new CircleProgerssDialog(mContext, "下载中...");
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                OkGo.getInstance().cancelTag(this);
            }
        });
        if (MUtil.checkSDCard()) {
            path = Environment.getExternalStorageDirectory().getPath() + File.separator + AppUtils.getAppName();
        } else {
            path = Environment.getDataDirectory().getPath() + File.separator + AppUtils.getAppName();
        }
        start();
    }

    private void start() {
        OkGo.<File>get(url)
                .tag(this)
                .execute(new FileCallback(TextUtils.isEmpty(fileDir) ? path : fileDir
                        , fileName) {
                    @Override
                    public void onStart(Request<File, ? extends Request> request) {
                        super.onStart(request);
                        if (showDialog) {
                            dialog.show();
                            mProgress = dialog.getdProgress();
                            mProgress.setMax(100);
                        }
                    }

                    @Override
                    public void onSuccess(Response<File> response) {
                        if (downLoadListener != null) {
                            if (dialog != null && dialog.isShowing()) dialog.dismiss();
                            downLoadListener.onSuccess(response.body());
                        }
                    }

                    @Override
                    public void onError(Response<File> response) {
                        super.onError(response);
                        if (dialog != null && dialog.isShowing()) {
                            dialog.dismiss();
                        }
                        downLoadListener.onError(response);
                    }

                    @Override
                    public void downloadProgress(Progress progress) {
                        super.downloadProgress(progress);
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
                    public void onFinish() {
                        super.onFinish();
                        if (dialog != null && dialog.isShowing()) dialog.dismiss();
                    }
                });
    }

    public static final class Builder {
        /**
         * 上下文参数
         */
        private Context mContext;
        /**
         * 更新的地址
         */
        private String url;
        /**
         * 是否需要显示进度对话框
         */
        private boolean showDialog;

        private String fileDir;

        private String fileName;

        private DownLoadCallBack mCallBack;

        public Builder(Context context) {
            mContext = context;
        }

        public Builder setUrl(String url) {
            this.url = url;
            return this;
        }

        public Builder setShowDialog(boolean showDialog) {
            this.showDialog = showDialog;
            return this;
        }


        public Builder setFileDir(String fileDir) {
            this.fileDir = fileDir;
            return this;
        }

        public Builder setFileName(String fileName) {
            this.fileName = fileName;
            return this;
        }

        public void execute(DownLoadCallBack downLoadCallBack) {
            this.mCallBack = downLoadCallBack;
            new MDown(this);
        }
    }
}
