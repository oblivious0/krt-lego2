package com.krt.lego.oc.imp.widget.basics;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.krt.base.animate.FlubberAnimate;
import com.krt.base.util.MUtil;
import com.krt.lego.oc.core.bean.BaseLayoutBean;
import com.krt.lego.oc.core.bean.SkinIconBean;
import com.krt.lego.oc.core.surface.BaseWidget;
import com.krt.lego.oc.core.important.SkinManager;
import com.krt.lego.oc.core.surface.Subgrade;
import com.krt.lego.oc.util.CropUtil;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * @author: MaGua
 * @create_on:2021/10/3 10:51
 * @description 图片组件
 */
public class PicView extends BaseWidget<ImageView> {

    public PicView(Subgrade imp, BaseLayoutBean obj, boolean isListChild) {
        super(imp, obj, isListChild);
    }

    @Override
    public ImageView getRawView() {
        return view;
    }

    @Override
    protected void createView() {
        view = new ImageView(subgrade.getCarrier());
    }

    @Override
    protected void initView() {
        int radius = MUtil.getRealValue(getIntVal("borderRadius"));
        switch (Optional.ofNullable(getStringVal("mode")).orElse("")) {
            case "aspectFit":
                view.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                break;
            case "aspectFill":
                view.setScaleType(ImageView.ScaleType.CENTER_CROP);
                break;
            default:
                view.setScaleType(ImageView.ScaleType.FIT_XY);
                break;
        }

        if (getBooleanVal("isIcon")) {
            String iconFileName = getStringVal("iconFileName");
            if (!TextUtils.isEmpty(iconFileName)) {
                SkinIconBean ico = SkinManager.getPosition(getStringVal("iconFileParam"));
                CropUtil.getInstance().cropImg(subgrade.getCarrier(), SkinManager.defaultFile,
                        ico.getFileName(), bitmap -> {
                            RequestBuilder requestBuilder = Glide.with(subgrade.getCarrier())
                                    .load(bitmap);
                            if (radius > 0)
                                requestBuilder.apply(new RequestOptions()
                                        .transform(new CenterCrop(), new RoundedCorners(radius)));

                            requestBuilder.into(view);
                        });
            }
        } else {
            String src = getStringVal("src");
            RequestBuilder requestBuilder;
            if (src.contains(".gif")) {
                requestBuilder = Glide.with(subgrade.getCarrier())
                        .asGif()
                        .load(src);
            } else {
                requestBuilder = Glide.with(subgrade.getCarrier())
                        .load(src);
            }

            if (radius > 0)
                requestBuilder.apply(new RequestOptions()
                        .transform(new CenterCrop(), new RoundedCorners(radius)));

            requestBuilder.into(view);

        }
        view.setVisibility(getBooleanVal("isHidden") ? View.GONE : View.VISIBLE);
    }

    @Override
    protected boolean bindInNewThread() {
        return true;
    }

    @SuppressLint("CheckResult")
    @Override
    protected void bindInMainThread() {
        if (bean.getAnimation() != null) {
            Observable.timer(Math.round(bean.getAnimation().getDelay()) * 500, TimeUnit.MILLISECONDS)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(aLong -> {
                            FlubberAnimate.animate(bean.getAnimation(), view);
                            view.setVisibility(View.VISIBLE);
                    });
        }
    }

    @Override
    public void bindData(String cid, String key, Object val) {
        if (cid.equals(this.cid)) {
            switch (key) {
                case "src":
                    //要确认这次修改的网络图片被使用
                    changeStyle("isIcon", false);
                    break;
                default:
            }
            changeStyle(key, val);
        }
        initView();
    }
}
