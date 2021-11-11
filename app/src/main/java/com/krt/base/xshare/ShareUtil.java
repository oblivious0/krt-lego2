package com.krt.base.xshare;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.blankj.utilcode.util.AppUtils;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.krt.base.util.BaseUtil;
import com.krt.base.util.MToast;
import com.krt.lego.R;
import com.tencent.connect.share.QQShare;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXImageObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import krt.wid.bit.Buried;


/**
 * @author: MaGua
 * @create_on:2021/8/24 16:59
 * @description
 */
public class ShareUtil implements IUiListener {

    private static String WX_ID, TENCENT_ID;

    private IWXAPI api;

    private ShareInfo info;

    private Context mContext;

    private BottomSheetDialog dialog;

    private Tencent mTencent;

    private int mType = 1;
    private String buried;

    public static void xShareConfig(String wxid, String tencent) {
        WX_ID = wxid;
        TENCENT_ID = tencent;
    }

    public ShareUtil(Context context, ShareInfo info, String buried) {
        this(context, info);
        this.buried = buried;
    }

    public ShareUtil(Context context, ShareInfo info) {
        api = WXAPIFactory.createWXAPI(context, null);
        mTencent = Tencent.createInstance(TENCENT_ID, context);
        api.registerApp(WX_ID);
        mContext = context;
        dialog = new BottomSheetDialog(context);
        this.info = info;
    }

    public void init() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_popupshare, null);
        View wx = view.findViewById(R.id.wx_layout);
        View circle = view.findViewById(R.id.circle_layout);
        View qq = view.findViewById(R.id.qq_layout);
        View zone = view.findViewById(R.id.zone_layout);
        if (!TextUtils.isEmpty(TENCENT_ID)) {
            qq.setVisibility(View.GONE);
            zone.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(WX_ID)) {
            wx.setVisibility(View.GONE);
            circle.setVisibility(View.GONE);
        }

        dialog.setContentView(view);
        wx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!AppUtils.isAppInstalled("com.tencent.mm")) {
                    Toast.makeText(mContext, "未安装微信...", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (mType == 0) {
                    shareWxImg(0);
                } else {
                    shareWx(0);
                }

                dialog.dismiss();
            }
        });
        circle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!AppUtils.isAppInstalled("com.tencent.mm")) {
                    Toast.makeText(mContext, "未安装微信...", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (mType == 0) {
                    shareWxImg(1);
                } else {
                    shareWx(1);
                }

                dialog.dismiss();
            }
        });
        qq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!AppUtils.isAppInstalled("com.tencent.mobileqq")) {
                    Toast.makeText(mContext, "未安装QQ...", Toast.LENGTH_SHORT).show();
                    return;
                }
                shareQQ();
                dialog.dismiss();
            }
        });
        zone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!AppUtils.isAppInstalled("com.tencent.mobileqq")) {
                    Toast.makeText(mContext, "未安装QQ...", Toast.LENGTH_SHORT).show();
                    return;
                }
                shareQQZoneImg();
                dialog.dismiss();
            }
        });
        if (!TextUtils.isEmpty(WX_ID) || TextUtils.isEmpty(TENCENT_ID))
            dialog.show();
        else
            MToast.showToast(mContext,"当前不存在可分享应用");
    }

    /**
     * 分享到微信u
     */
    public void shareWx(int type) {
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = info.getUrl();
        final WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = info.getTitle();//标题
        msg.description = info.getDescription();//描述
        Bitmap bitmap = BaseUtil.drawableToBitamp(mContext.getResources().getDrawable(R.mipmap.ic_launcher));
        msg.thumbData = BaseUtil.bmpToByteArray(bitmap, true);
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = String.valueOf(System.currentTimeMillis());
        req.message = msg;

        req.scene = type == 0 ? SendMessageToWX.Req.WXSceneSession
                : SendMessageToWX.Req.WXSceneTimeline;
        api.sendReq(req);
        complete();
    }

    /**
     * 分享到微信u
     */
    public void shareWxImg(int type) {

        Bitmap bmp = BitmapFactory.decodeFile(info.getPath());

        //初始化 WXImageObject 和 WXMediaMessage 对象
        WXImageObject imgObj = new WXImageObject(bmp);
        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = imgObj;

        //设置缩略图
        Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, 100, 100, true);
        bmp.recycle();
        msg.thumbData = BaseUtil.bmpToByteArray(thumbBmp, true);

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = String.valueOf(System.currentTimeMillis());
        req.message = msg;
        req.scene = type == 0 ? SendMessageToWX.Req.WXSceneSession
                : SendMessageToWX.Req.WXSceneTimeline;
        api.sendReq(req);
        complete();
    }


    /**
     * 分享到QQ
     */
    public void shareQQ() {
        Bundle params = new Bundle();
        //这条分享消息被好友点击后的跳转URL。
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
        params.putString(QQShare.SHARE_TO_QQ_TITLE, info.getTitle());
        params.putString(QQShare.SHARE_TO_QQ_SUMMARY, info.getDescription() + "(分享自" + mContext.getResources().getString(R.string.app_name)
                + ")");
        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, info.getUrl());
        params.putInt(QQShare.SHARE_TO_QQ_IMAGE_URL, R.mipmap.ic_launcher);
        params.putString(QQShare.SHARE_TO_QQ_APP_NAME, "吉安旅游");
        mTencent.shareToQQ((Activity) mContext, params, this);
        complete();
    }

    /**
     * 分享到QQ
     */
    public void shareQQImg() {
        Bundle params = new Bundle();
        //这条分享消息被好友点击后的跳转URL。
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_LOCAL_URL, info.getPath());
        params.putString(QQShare.SHARE_TO_QQ_APP_NAME, "");
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_IMAGE);
        params.putInt(QQShare.SHARE_TO_QQ_EXT_INT, QQShare.SHARE_TO_QQ_FLAG_QZONE_ITEM_HIDE);
        mTencent.shareToQQ((Activity) mContext, params, this);
        complete();
    }

    /**
     * 分享到QQ
     */
    public void shareQQZoneImg() {
        Bundle params = new Bundle();
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
        params.putString(QQShare.SHARE_TO_QQ_TITLE, info.getTitle());
        params.putString(QQShare.SHARE_TO_QQ_SUMMARY, info.getDescription() + "(分享自" + mContext.getResources().getString(R.string.app_name)
                + ")");
        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, info.getUrl());
        params.putInt(QQShare.SHARE_TO_QQ_IMAGE_LOCAL_URL, R.mipmap.ic_launcher);
        params.putString(QQShare.SHARE_TO_QQ_APP_NAME, "吉安旅游");
        params.putInt(QQShare.SHARE_TO_QQ_EXT_INT, QQShare.SHARE_TO_QQ_FLAG_QZONE_AUTO_OPEN);
        mTencent.shareToQQ((Activity) mContext, params, this);
        complete();
    }

    public void complete() {
        if (TextUtils.isEmpty(buried)) {
            return;
        }
        Buried.bury(buried);
    }


    @Override
    public void onComplete(Object o) {
    }

    @Override
    public void onError(UiError uiError) {
    }

    @Override
    public void onCancel() {

    }

}
