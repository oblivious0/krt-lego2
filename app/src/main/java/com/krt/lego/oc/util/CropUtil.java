package com.krt.lego.oc.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.krt.lego.Constants;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * author:Marcus
 * create on:2019/3/26 17:24
 * description
 */
public class CropUtil {
    private static CropUtil cropUtil;
    private static Bitmap BaseBitmap;

    public static CropUtil getInstance() {
        if (cropUtil == null) {
            cropUtil = new CropUtil();
        }
        return cropUtil;
    }

//    @SuppressLint("checkResult")
//    public void cropBottomImg(final Context context, List<BottomBean.CommonBean.LinksBean> list, final File file, final CallBack callBack) {
//        Observable.fromIterable(list)
//                .map(linksBean -> {
//                    if (TextUtils.isEmpty(linksBean.getOriginSkin())) {
//                        return new Object();
//                    }
//                    if (linksBean.isSkinIcon()) {
//                        Bitmap bitmap = BitmapFactory.decodeFile(file.getPath());
//                        String[] origin = linksBean.getOriginSkin().split("_");
//                        Bitmap originImg = Bitmap.createBitmap(bitmap,
//                                Integer.parseInt(origin[2]) > 1 ? Integer.parseInt(origin[2]) - 1 : Integer.parseInt(origin[2]),
//                                Integer.parseInt(origin[3]),
//                                Integer.parseInt(origin[0]) - 1,
//                                Integer.parseInt(origin[1]) - 1);
//                        String[] select = linksBean.getSelectSkin().split("_");
//                        Bitmap selectImg = Bitmap.createBitmap(bitmap,
//                                Integer.parseInt(select[2]) > 1 ? Integer.parseInt(select[2]) - 1 : Integer.parseInt(select[2]),
//                                Integer.parseInt(select[3]),
//                                Integer.parseInt(select[0]) - 1,
//                                Integer.parseInt(select[1]) - 1);
//                        linksBean.setOriginImg(saveBitmap(originImg, linksBean.getOriginSkin() + ".png"));
//                        linksBean.setSelectImg(saveBitmap(selectImg, linksBean.getSelectSkin() + ".png"));
//
//                    }
//                    return new Object();
//                })
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .doFinally(() -> callBack.callback())
//                .subscribe(aVoid -> {
//
//                });
//    }

    @SuppressLint("checkResult")
    public void cropImg(final Context context, final String content, final CropListener listener) {
        cropImg(context, "CustomerSkin.png", content, listener);
    }

    @SuppressLint("checkResult")
    public void cropImg(final Context context, final String name, final String content, final CropListener listener) {
        final File file = new File(Constants.iconPath + content);
        if (file.exists()) {
            Bitmap bitmap = BitmapFactory.decodeFile(Constants.iconPath + content);
            listener.callback(bitmap);
        } else {
            Observable.create((ObservableOnSubscribe<Bitmap>) emitter -> {
                try {
                    if (BaseBitmap == null) {
                        BaseBitmap = BitmapFactory.decodeFile(Constants.imgPath + name);
                    }
                    String[] contents = content.split("_");
                    Bitmap cropImg = Bitmap.createBitmap(BaseBitmap,
                            Integer.parseInt(contents[2]),
                            Integer.parseInt(contents[3]) != 0 ? Integer.parseInt(contents[3]) - 1 :
                                    Integer.parseInt(contents[3]),
                            Integer.parseInt(contents[0]),
                            Integer.parseInt(contents[1]));


                    if (!new File(Constants.iconPath).exists()) {
                        new File(Constants.iconPath).mkdirs();
                    }
                    Bitmap.CompressFormat format = Bitmap.CompressFormat.PNG;
                    int quality = 100;
                    OutputStream stream = null;
                    stream = new FileOutputStream(new File(Constants.iconPath + content + ".png"));
                    cropImg.compress(format, quality, stream);
                    stream.close();

                    emitter.onNext(cropImg);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                emitter.onComplete();
            }).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(b -> {
                        if (b != null) {
//                            saveBitmap(b, content + ".png");
                            listener.callback(b);
                        }
                    });
        }

    }

    public interface CallBack {
        void callback();
    }


    public interface CropListener {
        void callback(Bitmap bitmap);

    }

    private byte[] getByte(Bitmap bitmap) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] bytes = baos.toByteArray();
            baos.close();
            return bytes;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @SuppressLint("CheckResult")
    private void saveBitmap(Bitmap bmp, String fileName) {
        Observable.create((ObservableOnSubscribe<Object>) emitter -> {
            try {
                if (!new File(Constants.iconPath).exists()) {
                    new File(Constants.iconPath).mkdirs();
                }
                Bitmap.CompressFormat format = Bitmap.CompressFormat.PNG;
                int quality = 100;
                OutputStream stream = null;
                stream = new FileOutputStream(new File(Constants.iconPath + fileName));
                bmp.compress(format, quality, stream);
                stream.close();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                bmp.recycle();
            }
            emitter.onNext(new Object());
            emitter.onComplete();
        }).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(b -> {
                });
    }
}
