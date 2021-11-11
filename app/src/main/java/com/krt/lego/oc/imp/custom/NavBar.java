package com.krt.lego.oc.imp.custom;

import android.content.Context;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.krt.base.util.MTitle;
import com.krt.base.util.MUtil;
import com.krt.lego.oc.util.CropUtil;

/**
 * @author: MaGua
 * @create_on:2021/10/20 10:21
 * @description
 */
public class NavBar {
    public static class Builder {
        private Context context;

        private MTitle mTitle;



        public Builder(Context context, int height) {
            this.context = context;
            mTitle = new MTitle(context);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height);
            mTitle.setLayoutParams(layoutParams);
        }

        public Builder setLeftText(String text, int color, int textsize) {
            mTitle.setLeftText(text, textsize, TypedValue.COMPLEX_UNIT_PX, color);
            return this;
        }

        public Builder setRightText(String text, int color, int textsize) {
            mTitle.setRightText(text, textsize, TypedValue.COMPLEX_UNIT_PX, color);
            return this;
        }

        public Builder setBackColor(String text) {
            mTitle.setBackgroundColor(MUtil.getRealColor(text));
            return this;
        }


        public Builder setCenterText(String text, int color, int textsize) {
            mTitle.setCenterText(text, textsize, TypedValue.COMPLEX_UNIT_PX, color);
            return this;
        }

        public Builder setLeftImage(String url) {
            ImageView imageView = new ImageView(context);
            Glide.with(context)
                    .load(url)
                    .override(50,50)
                    .into(imageView);
            mTitle.setCustomLeftView(imageView);
            return this;
        }

        public Builder setLeftImage(String url,String local) {
            ImageView imageView = new ImageView(context);
            CropUtil.getInstance().cropImg(context, url, local, bitmap -> imageView.setImageBitmap(bitmap));
            mTitle.setCustomLeftView(imageView);
            return this;
        }

        public Builder setRightImage() {
            ImageView imageView = new ImageView(context);
            mTitle.setCustomRightView(imageView);
            return this;
        }

        public MTitle build() {
            return mTitle;
        }
    }
}
