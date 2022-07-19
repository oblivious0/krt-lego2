package com.krt.lego.oc.imp.widget;

import android.text.TextUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.krt.base.util.MTitle;
import com.krt.base.util.MUtil;
import com.krt.lego.oc.core.bean.BaseLayoutBean;
import com.krt.lego.oc.core.bean.EventBean;
import com.krt.lego.oc.core.surface.BaseWidget;
import com.krt.lego.oc.core.surface.Subgrade;
import com.krt.lego.oc.imp.custom.NavBar;
import com.krt.lego.oc.util.CropUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author: MaGua
 * @create_on:2021/10/20 10:06
 * @description 标题栏
 */
public class NavbarView extends BaseWidget<LinearLayout> {

    private MTitle mTitle;
    private List<EventBean> rightEvent = new ArrayList<>();

    public NavbarView(Subgrade imp, BaseLayoutBean obj, boolean isListChild) {
        super(imp, obj, isListChild);
    }

    @Override
    protected void createView() {
        view = new LinearLayout(subgrade.getCarrier());
        view.setOrientation(LinearLayout.VERTICAL);
    }

    @Override
    protected void initView() {
        view.setBackgroundColor(MUtil.getRealColor(getStringVal("bgColor")));

        NavBar.Builder build = new NavBar.Builder(subgrade.getCarrier(), getIntVal("height"));
        build.setBackColor(getStringVal("bgColor"));
        build.setCenterText(getStringVal("title"),
                MUtil.getRealColor(getStringVal("titleColor")) == -1 ?
                        -2 : MUtil.getRealColor(getStringVal("titleColor")),
                MUtil.getRealValue(getIntVal("titleFontSize")));

        mTitle = build.build();

        //左边图标按钮设置
        if (getBooleanVal("backIcon")) {
            ImageView imageView = new ImageView(subgrade.getCarrier());
            if (!TextUtils.isEmpty(getStringVal("backImgUrl"))) {
                Glide.with(subgrade.getCarrier())
                        .load(getStringVal("backImgUrl"))
                        .override(50, 50)
                        .into(imageView);

            } else if (!TextUtils.isEmpty(getStringVal("backIconFileName"))) {
                String[] dir =getStringVal("backIconFileName").split("/");
                CropUtil.getInstance().cropImg(subgrade.getCarrier(),
                        dir[dir.length - 1],
                        getStringVal("backIconParam"), bitmap -> imageView.setImageBitmap(bitmap));
            }
            mTitle.setCustomLeftView(imageView);
        } else {
            mTitle.setLeftText(getStringVal("backText"),
                    MUtil.getRealColor(getStringVal("backTextColor")),
                    MUtil.getRealValue(getIntVal("leftFontSize")));
        }

        //右边图标按钮设置
        if (getBooleanVal("rightIcon")) {
            ImageView imageView = new ImageView(subgrade.getCarrier());
            if (!TextUtils.isEmpty(getStringVal("rightImgUrl"))) {
                Glide.with(subgrade.getCarrier())
                        .load(getStringVal("rightImgUrl"))
                        .override(50, 50)
                        .into(imageView);
            } else if (!TextUtils.isEmpty(getStringVal("rightIconFileName"))) {
                String[] dir = getStringVal("rightIconFileName").split("/");
                CropUtil.getInstance().cropImg(subgrade.getCarrier(),
                        dir[dir.length - 1],
                        getStringVal("rightIconFileName"), bitmap -> imageView.setImageBitmap(bitmap));
            }
            mTitle.setCustomRightView(imageView);
        } else {
            mTitle.setRightText(getStringVal("rightText"),
                    getIntVal("rightFontSize") / 2,
                    MUtil.getRealColor(getStringVal("rightTextColor")) == -1 ?
                            -2 : MUtil.getRealColor(getStringVal("rightTextColor")));
        }


        LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, getIntVal("height"));
        lp1.setMargins(0, 60, 0, 0);
        mTitle.setLayoutParams(lp1);
        view.addView(mTitle);

    }

}
