package com.krt.base.util;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Environment;
import android.os.IBinder;
import android.text.TextUtils;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.ServiceUtils;
import com.krt.base.inter.ClickListener;
import com.krt.base.net.callback.JsonCallback;
import com.krt.base.update.DownLoadService;
import com.krt.base.update.UpdateInfo;
import com.krt.base.view.BaseDialog;
import com.krt.base.view.UpdateProgressDialog;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;

import java.io.File;

import com.krt.lego.R;


/**
 * @author Marcus
 * @package krt.wid.update
 * @description
 * @time 2018/4/2
 */

public class MUpdate {

    private Context mContext;
    private String url;
    private boolean forceUpdate;
    private String title;
    private boolean checkNewVer;
    private Dialog downDialog;
    private boolean showUpdateProgressDialog;
    private UpdateProgressDialog dialog;
    private DownLoadService.DownBinder binder;
    private CustomDownloadDialogListener downListener;
    private CustomVersionDialogListener verListener;
    private String info;
    private String ver;
    private String downUrl;
    private boolean isInBack;


    private MUpdate(Builder builder) {
        this.mContext = builder.mContext;
        this.forceUpdate = builder.forceUpdate;
        this.url = builder.url;
        this.title = builder.dialogTitle;
        this.checkNewVer = builder.checkNewVer;
        this.downListener = builder.downListener;
        this.verListener = builder.verListener;
        this.isInBack = builder.isInBack;
        this.showUpdateProgressDialog = builder.showUpdateProgressDialog;
        check();
    }

    public static Builder newBuilder(Context context) {
        return new Builder(context);
    }


    /**
     * 检测是否需要进行更新操作
     */
    private void check() {
        OkGo.<UpdateInfo>get(url)
                .tag(this)
                .execute(new JsonCallback<UpdateInfo>() {
                    @Override
                    public void onSuc(Response<UpdateInfo> response) {
                        final UpdateInfo updateInfo = response.body();
                        if (updateInfo.getAndroid().getProd() != null && !TextUtils.isEmpty(updateInfo.getAndroid().getProd().getVersion())) {
                            ver = updateInfo.getAndroid().getProd().getVersion();
                            info = updateInfo.getAndroid().getProd().getInfo();
                            downUrl = updateInfo.getAndroid().getProd().getUrl();
                        } else {
                            if (TextUtils.isEmpty(updateInfo.getAndroid().getVer())) {
                                MToast.showToast(mContext, "获取更新信息失败,请重试!");
                            } else {
                                ver = updateInfo.getAndroid().getVer();
                                info = updateInfo.getAndroid().getInfo();
                                downUrl = updateInfo.getAndroid().getUrl();
                            }
                        }
                        if (MUtil.versionCheck(MUtil.getVersionName(mContext), ver)) {
                            if (isInBack) {
                                checkPermission();
                            } else {
                                if (verListener != null) {
                                    BaseDialog dialog = verListener.getCustomVerDialog(updateInfo);
                                    dialog.setListener(new ClickListener() {
                                        @Override
                                        public void confirm() {
                                            checkPermission();
                                        }

                                        @Override
                                        public void cancel(BaseDialog baseDialog) {
                                            if (baseDialog != null && baseDialog.isShowing()) {
                                                baseDialog.dismiss();
                                            }
                                        }
                                    });
                                } else {
                                    createBaseDialog();
                                }
                            }
                        } else {
                            if (checkNewVer) MToast.showToast(mContext, "当前已经是最新版本了");
                        }
                    }

                    @Override
                    public void onError(Response<UpdateInfo> response) {
                        super.onError(response);
                        MToast.showToast(mContext, "更新请求失败,请重试!");
                    }
                });
    }

    /**
     * 检查是否相关权限
     */
    private void checkPermission() {
        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        MPermissions.getInstance().request((FragmentActivity) mContext, permissions, value -> {
            if (value) {
                startDownload();
            } else {
                MToast.showToast(mContext, "您没有授权该权限，请在设置中打开授权!");
            }
        });
    }

    /**
     * 启动服务进行相关下载
     */
    private void startDownload() {
        final String path = Environment.getExternalStorageDirectory().getPath() + File.separator + AppUtils.getAppName()
                + File.separator + AppUtils.getAppName() + ver + ".apk";
        if (FileUtils.isFileExists(path)) {
            MUtil.getDownFileSize(downUrl, new MUtil.UtilListener<Integer>() {
                @Override
                public void callBack(Integer integer) {
                    File file = new File(path);
                    if (integer != file.length()) {
                        file.delete();
                        downLoad();
                    } else {
                        MUtil.installApp(mContext, file);
                    }
                }
            });
        } else {
            downLoad();
        }
    }

    private void downLoad() {
        final String path = Environment.getExternalStorageDirectory().getPath() + File.separator + AppUtils.getAppName()
                + File.separator;
        FileUtils.createOrExistsDir(path);
        if (ServiceUtils.isServiceRunning(DownLoadService.class)) {
            return;
        }
        Intent intent = new Intent(mContext, DownLoadService.class);
        intent.putExtra("url", downUrl);
        intent.putExtra("ver", ver);
        intent.putExtra("forceUpdate", forceUpdate);
        mContext.bindService(intent, new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                binder = (DownLoadService.DownBinder) service;
                binder.setProgressListener(new DownLoadService.ProgressListener() {
                    @Override
                    public void getProgress(int progress) {
                        if (downListener != null) {
                            if (downDialog != null && downDialog.isShowing()) {
                                if (progress != 100) {
                                    downListener.updateProgress(downDialog, progress);
                                } else {
                                    downDialog.dismiss();
                                }
                            }
                        } else {
                            if (dialog != null && dialog.isShowing()) {
                                if (progress != 100) {
                                    dialog.setProgress(progress);
                                } else {
                                    dialog.dismiss();
                                }
                            }
                        }
                    }
                });
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        }, Context.BIND_AUTO_CREATE);
        if (showUpdateProgressDialog) {
            if (downListener != null) {
                downDialog = downListener.getCustomDownloadingDialog(ver, info);
                downDialog.setCancelable(!forceUpdate);
                downDialog.setCanceledOnTouchOutside(!forceUpdate);
                downDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        if (binder != null) {
                            binder.stop();
                        }
                        if (forceUpdate && !checkNewVer) {
                            if (mContext instanceof Activity) {
                                ((Activity) mContext).finish();
                            }
                        }
                    }
                });
                downDialog.show();
            } else {
                showProgressDialog();
            }
        }

    }

    /**
     * 显示下载进度的对话框
     */
    private void showProgressDialog() {
        dialog = new UpdateProgressDialog(mContext);
        dialog.setCancelable(!forceUpdate);
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (binder != null) {
                    binder.stop();
                }
                if (forceUpdate && !checkNewVer) {
                    if (mContext instanceof Activity) {
                        ((Activity) mContext).finish();
                    }
                }
            }
        });
        dialog.show();
    }


    private void createBaseDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(title != null ? title : "新版本" + ver)
                .setMessage(info)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        checkPermission();
                    }
                });
        if (!forceUpdate) builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(!forceUpdate);
        dialog.setCancelable(false);
        dialog.show();
        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(mContext, R.color.color_1b9ef7));
        dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(mContext, R.color.color_1b9ef7));
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
         * 是否进行强制更新
         */
        private boolean forceUpdate;

        /**
         * 更新内容对话框的标题
         */
        private String dialogTitle;

        private boolean showUpdateProgressDialog = true;

        //衍生   检测更新功能 默认是自动检测
        private boolean checkNewVer = false;

        private CustomDownloadDialogListener downListener;

        private CustomVersionDialogListener verListener;

        private boolean isInBack;

        private Builder(Context context) {
            this.mContext = context;
        }

        public Builder setUrl(String url) {
            this.url = url;
            return this;
        }

        public Builder setForceUpdate(boolean forceUpdate) {
            this.forceUpdate = forceUpdate;
            return this;
        }

        public Builder setDialogTitle(String dialogTitle) {
            this.dialogTitle = dialogTitle;
            return this;
        }

        public Builder showProgressDialog(Boolean show) {
            this.showUpdateProgressDialog = show;
            return this;
        }

        public Builder setCheckNewVer(boolean checkNewVer) {
            this.checkNewVer = checkNewVer;
            return this;
        }

        public Builder setCustomDownloadDialogListenr(CustomDownloadDialogListener listener) {
            this.downListener = listener;
            return this;
        }

        public Builder setVerListener(CustomVersionDialogListener verListener) {
            this.verListener = verListener;
            return this;
        }

        public Builder setInBack(boolean inBack) {
            isInBack = inBack;
            return this;
        }

        public MUpdate build() {
            return new MUpdate(this);
        }
    }


    public interface CustomDownloadDialogListener {
        Dialog getCustomDownloadingDialog(String ver, String info);

        void updateProgress(Dialog dialog, int progress);
    }

    public interface CustomVersionDialogListener {
        BaseDialog getCustomVerDialog(UpdateInfo updateInfo);
    }
}
