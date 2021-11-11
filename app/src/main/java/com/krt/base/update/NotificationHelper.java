package com.krt.base.update;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.content.FileProvider;

import com.krt.lego.R;

import java.io.File;


import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * @author Marcus
 * @package krt.wid.update
 * @description
 * @time 2018/4/7
 */

public class NotificationHelper {
    private Context mContext;

    public NotificationCompat.Builder mBuilder;

    public NotificationManager manager;

    private boolean isDownloadSuccess = false;

    private boolean isFailed = false;

    private int currentProgress = 0;

    private String contentText;

    private final int NOTIFICATION_ID = 0;

    public NotificationHelper(Context context) {
        this.mContext = context;
        currentProgress = 0;
    }

    /**
     * 更新通知栏状态的操作
     *
     * @param progress
     */
    public void updateNotification(int progress) {
        if ((progress - currentProgress) > 0 && !isDownloadSuccess && !isFailed) {
            mBuilder.setContentIntent(null);
            mBuilder.setContentText(String.format(contentText, progress));
            mBuilder.setProgress(100, progress, false);
            manager.notify(NOTIFICATION_ID, mBuilder.build());
            currentProgress = progress;
        }
    }

    /**
     * 显示通知栏
     */
    public void showNotification() {
        isDownloadSuccess = false;
        isFailed = false;
        manager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        mBuilder = createNotification();
    }

    /**
     * 下载成功
     *
     * @param file
     */
    public void showDownloadCompleteNotification(File file) {
        isDownloadSuccess = true;
        Uri uri;
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(mContext, mContext.getPackageName() + ".versionProvider", file);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            uri = Uri.fromFile(file);
        }
        //设置intent类型
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, intent, 0);
        mBuilder.setContentIntent(pendingIntent);
        mBuilder.setContentText(mContext.getString(R.string.download_finish));
        mBuilder.setProgress(100, 100, false);
        manager.cancelAll();
        manager.notify(NOTIFICATION_ID, mBuilder.build());
    }

    /**
     * 下载失败
     */
    public void showDownloadFailedNotification() {
        isDownloadSuccess = false;
        isFailed = true;
        mBuilder.setContentIntent(null);
        mBuilder.setContentText(mContext.getString(R.string.download_fail));
        manager.notify(NOTIFICATION_ID, mBuilder.build());
    }

    /**
     * 取消相关下载
     */
    public void cancelNotification() {
        isDownloadSuccess = false;
        isFailed = false;
        manager.cancelAll();
    }

    /**
     * 创建相关通知栏
     */
    private NotificationCompat.Builder createNotification() {
        String channelId = "0";
        String channelName = "NOTIFICATION";
        NotificationCompat.Builder builder = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_LOW);
            notificationChannel.enableLights(false); //是否在桌面icon右上角展示小红点
            notificationChannel.setLightColor(Color.RED);//小红点颜色
            notificationChannel.enableVibration(true);//是否开启振动
            NotificationManager manager = (NotificationManager) mContext.getSystemService(NOTIFICATION_SERVICE);
            manager.createNotificationChannel(notificationChannel);
        }
        builder = new NotificationCompat.Builder(mContext, channelId);
        builder.setAutoCancel(true);//是否自动取消掉通知栏
        builder.setSmallIcon(R.drawable.ic_launcher); //通知栏显示的小图标
        //设置通知栏的标题
        String contentTitle = mContext.getString(R.string.app_name);
        builder.setContentTitle(contentTitle);

        String ticker = mContext.getString(R.string.download_ticker);
        builder.setTicker(ticker);

        contentText = mContext.getString(R.string.download_progress);
        builder.setContentText(String.format(contentText, 0));

        return builder;
    }

    public void onDestroy() {
        if (manager != null) {
            manager.cancelAll();
        }
    }
}
