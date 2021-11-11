package com.krt.base.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.request.RequestOptions;

import jp.wasabeef.glide.transformations.GrayscaleTransformation;

/**
 * author:Marcus
 * create on:2020/4/8 10:29
 * description
 */
public class MGlideUtil {
    public static boolean gray;


    public static void load(Context context, Object object, ImageView iv) {
        load(context, object, gray, iv);
    }

    private static void load(Context context, Object object, boolean gray, ImageView iv) {
        RequestBuilder<Drawable> request = Glide.with(context).load(object);
        if (gray) request.apply(RequestOptions.bitmapTransform(new GrayscaleTransformation()));
        request.into(iv);
    }

    public static void loadPlaceHolder(Context context, Object object, int placeHolder, ImageView iv) {
        loadPlaceHolder(context, object, placeHolder, gray, iv);
    }

    private static void loadPlaceHolder(Context context, Object object, int placeHolder, boolean gray, ImageView iv) {
        RequestBuilder<Drawable> request = Glide.with(context).load(object);
        if (gray) request.apply(RequestOptions.bitmapTransform(new GrayscaleTransformation()));
        request.placeholder(placeHolder)
                .error(placeHolder)
                .into(iv);
    }

    public static void loadTransform(Context context, Object object, Transformation<Bitmap> transform, ImageView iv) {
        loadTransform(context, object, -1, transform, gray, iv);
    }


    public static void loadTransform(Context context, Object object, int placeHolder, Transformation<Bitmap> transform, ImageView iv) {
        loadTransform(context, object, placeHolder, transform, gray, iv);
    }

    @SuppressLint("CheckResult")
    private static void loadTransform(Context context, Object object, int placeHolder, Transformation<Bitmap> transform, boolean gray, ImageView iv) {
        RequestBuilder<Drawable> request = Glide.with(context).load(object);
        if (gray) {
            request.apply(RequestOptions.bitmapTransform(new MultiTransformation<Bitmap>(new GrayscaleTransformation(), transform)));
        } else {
            request.apply(RequestOptions.bitmapTransform(transform));
        }
        if (placeHolder != -1) {
            request.placeholder(placeHolder).error(placeHolder);
        }
        request.into(iv);
    }
}
