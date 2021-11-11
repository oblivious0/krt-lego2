package com.krt.lego.oc.imp.widget;

import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.krt.base.util.MGlideUtil;
import com.krt.base.util.MToast;
import com.krt.lego.R;
import com.krt.lego.oc.core.bean.BaseLayoutBean;
import com.krt.lego.oc.core.surface.BaseWidget;
import com.krt.lego.oc.core.surface.Subgrade;
import com.krt.lego.oc.lifecyler.MLifecycleEvent;
import com.tencent.smtt.sdk.TbsVideo;

import java.util.Optional;

/**
 * @author: MaGua
 * @create_on:2021/11/10 10:44
 * @description
 */
public class MVideoView extends BaseWidget<FrameLayout> {
    private ImageView thump, playImg;
    /**
     * 视频地址
     */
    private String videoUrl = "";

    public MVideoView(Subgrade imp, BaseLayoutBean obj, boolean isListChild) {
        super(imp, obj, isListChild);
    }

    @Override
    protected void createView() {
        view = new FrameLayout(subgrade.getCarrier());
        thump = new ImageView(subgrade.getCarrier());
        playImg = new ImageView(subgrade.getCarrier());
        view.addView(thump);
        view.addView(playImg);
    }

    @Override
    protected void initView() {
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
                getIntVal("width"), getIntVal("height")
        );
        thump.setLayoutParams(lp);
        MGlideUtil.load(subgrade.getCarrier(), getStringVal("poster"), thump);

        if (!Optional.ofNullable(getStringVal("src")).orElse("").isEmpty()) {
            videoUrl = getStringVal("src");
        }
        MGlideUtil.load(subgrade.getCarrier(), R.mipmap.play, playImg);
        FrameLayout.LayoutParams lp1 = new FrameLayout.LayoutParams(60, 60);
        lp1.gravity = Gravity.CENTER;
        playImg.setLayoutParams(lp1);

        playImg.setOnClickListener(view -> {
            if (TbsVideo.canUseTbsPlayer(subgrade.getCarrier()) && !TextUtils.isEmpty(videoUrl)) {
                TbsVideo.openVideo(subgrade.getCarrier(), videoUrl);
            }
        });
    }

    @Override
    public void bindData(String cid, String key, Object val) {
        if (cid.equals(this.cid)) {
            switch (key) {
                case "src":
                    if (!TextUtils.isEmpty(val.toString())) {
                        videoUrl = val.toString();
                        playImg.performClick();
                    }
                    break;
                case "poster":
                    if (!TextUtils.isEmpty(val.toString())) {
                        MGlideUtil.load(subgrade.getCarrier(), val, thump);
                    }
                    break;
                default:
                    break;
            }
        }
    }
}
