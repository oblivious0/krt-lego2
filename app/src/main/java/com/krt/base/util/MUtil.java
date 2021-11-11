package com.krt.base.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

import androidx.core.content.FileProvider;

import com.blankj.utilcode.util.ScreenUtils;
import com.blankj.utilcode.util.Utils;
import com.krt.lego.Constants;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * @author Marcus
 * @package krt.wid.util
 * @description
 * @time 2018/4/2
 */

public class MUtil {
    /**
     * 比较版本号
     *
     * @param oldver 旧版本号
     * @param newver 新版本号
     * @return
     */
    public static boolean versionCheck(String oldver, String newver) {
        List<String> compareOld = new ArrayList<>();
        List<String> compareNew = new ArrayList<>();
        String[] vs1 = oldver.split("\\.");
        String[] vs2 = newver.split("\\.");
        for (String a : vs1) {
            compareOld.add(a);
        }
        for (String a : vs2) {
            compareNew.add(a);
        }
        if (compareOld.size() != 3) compareOld.add("0");
        if (compareNew.size() != 3) compareNew.add("0");
        try {
            for (int i = 0; i < compareOld.size(); i++) {
                if (Integer.valueOf(compareNew.get(i)) > Integer.valueOf(compareOld.get(i))) {
                    return true;
                } else if (Integer.valueOf(compareNew.get(i)) == Integer.valueOf(compareOld.get(i))) {
                    continue;
                } else {
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public static String getVersionName(Context mContext) {
        PackageManager pm = mContext.getPackageManager();
        try {
            PackageInfo pi = pm.getPackageInfo(mContext.getPackageName(), 0);
            return pi.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "2.0";
        }
    }

    public static void installApp(Context mContext, File file) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(mContext, mContext.getPackageName() + ".versionProvider", file);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            uri = Uri.fromFile(file);
        }
        intent.setDataAndType(uri,
                "application/vnd.android.package-archive");
        mContext.startActivity(intent);
    }


    /**
     * 检查判断是否有sd卡
     */
    public static boolean checkSDCard() {
        boolean sdCardExist = Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
        return sdCardExist;

    }

    /**
     * 获取当前屏幕的密度
     */
    public static float dpTopx(float dp) {
        DisplayMetrics displayMetrics = Resources.getSystem().getDisplayMetrics();
        return dp * displayMetrics.density;
    }

    /**
     * 根据750的比例转换坐标
     */
    public static int getScale(int value) {
        return ScreenUtils.getScreenWidth() * value / 750;
    }


    /**
     * 判断是否存在虚拟导航栏
     *
     * @param context
     * @return
     */
    public static boolean checkDeviceHasNavigationBar(Context context) {
        boolean hasNavigationBar = false;
        Resources rs = context.getResources();
        int id = rs.getIdentifier("config_showNavigationBar", "bool", "android");
        if (id > 0) {
            hasNavigationBar = rs.getBoolean(id);
        }
        try {
            Class systemPropertiesClass = Class.forName("android.os.SystemProperties");
            Method m = systemPropertiesClass.getMethod("get", String.class);
            String navBarOverride = (String) m.invoke(systemPropertiesClass, "qemu.hw.mainkeys");
            if ("1".equals(navBarOverride)) {
                hasNavigationBar = false;
            } else if ("0".equals(navBarOverride)) {
                hasNavigationBar = true;
            }
        } catch (Exception e) {

        }
        return hasNavigationBar;
    }

    /**
     * 获取虚拟导航栏的高度
     */
    /**
     * 获取虚拟功能键高度
     *
     * @param context
     * @return
     */
    public static int getNavBarHeight(Context context) {
        int vh = 0;
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        DisplayMetrics dm = new DisplayMetrics();
        try {
            @SuppressWarnings("rawtypes")
            Class c = Class.forName("android.view.Display");
            @SuppressWarnings("unchecked")
            Method method = c.getMethod("getRealMetrics", DisplayMetrics.class);
            method.invoke(display, dm);
            vh = dm.heightPixels - windowManager.getDefaultDisplay().getHeight();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return vh;
    }

    public static int getScreenRealHeight(Context context) {
        return ScreenUtils.getScreenHeight() - getNavBarHeight(context);
    }

    public static void hideNavBar(Activity activity) {
        View decorView = activity.getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
    }

    /**
     * 获取需要下载的文件大小
     */
    @SuppressLint("checkResult")
    public static void getDownFileSize(final String fileUrl, final UtilListener<Integer> listener) {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                try {
                    URL url = new URL(fileUrl);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    int fileLength = connection.getContentLength();
                    Log.d("marcus", "文件大小为：" + fileLength + "btye");
                    emitter.onNext(fileLength);
                    emitter.onComplete();
                } catch (IOException e) {
                    e.printStackTrace();
                    emitter.onError(e);
                }
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        listener.callBack(integer);
                    }
                });
    }

    public static int getDiffBetweenDay(Date beginDate, Date endDate) {
        Calendar beginCalendar = Calendar.getInstance();
        beginCalendar.setTime(beginDate);
        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTime(endDate);
        long beginTime = beginCalendar.getTime().getTime();
        long endTime = endCalendar.getTime().getTime();
        int betweenDays = (int) ((endTime - beginTime) / (1000 * 60 * 60 * 24));

        endCalendar.add(Calendar.DAY_OF_MONTH, -betweenDays);
        endCalendar.add(Calendar.DAY_OF_MONTH, -1);
        if (beginCalendar.get(Calendar.DAY_OF_MONTH) == endCalendar.get(Calendar.DAY_OF_MONTH)) {
            //相等说明确实跨天了
            return betweenDays + 1;
        } else {
            //不相等说明确实未跨天
            return betweenDays + 0;
        }
    }

    public static Date getDate(String time, String formatString) {
        try {
            SimpleDateFormat df = new SimpleDateFormat(formatString);
            return df.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new Date();
    }

    public interface UtilListener<T> {
        void callBack(T t);
    }

    public static String getJson(File file) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            FileInputStream is = new FileInputStream(file);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    /**
     * rgba 转 16进制
     *
     * @param rgba 格式：rgbargba(142, 138, 138, 1)
     * @return
     */
    private static int rgba2HexString(String rgba) {
        String[] ints = rgba.split("\\(")[1].split("\\)")[0].split(",");
        return Color.argb((int) (Float.parseFloat(ints[3].trim()) * 255),
                Integer.parseInt(ints[0].trim()),
                Integer.parseInt(ints[1].trim()),
                Integer.parseInt(ints[2].trim()));

    }

    /**
     * 获取真正的颜色值
     *
     * @param color
     * @return
     */
    public static int getRealColor(String color) {
        if (TextUtils.isEmpty(color)) {
            return Color.TRANSPARENT;
        }
        if (color.contains("#")) {
            return Color.parseColor(color);
        } else if (color.contains("rgb")) {
            return rgba2HexString(color);
        } else {
            return Integer.parseInt(color);
        }
    }

    public static void getAssestsJson(Context context, String name) {

        AssetManager assetManager = context.getAssets();
        try {
            //filename是assets目录下的图片名
            InputStream inputStream = assetManager.open(name);
            if (!new File(Constants.jsonPath).exists()) {
                new File(Constants.jsonPath).mkdirs();
            }
            String[] fileDir = name.split("/");
            saveJson(Constants.jsonPath + fileDir[fileDir.length - 1], inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void saveJson(String destination, InputStream input)
            throws IOException {
        int index;
        byte[] bytes = new byte[1024];
        FileOutputStream downloadFile = new FileOutputStream(destination);
        while ((index = input.read(bytes)) != -1) {
            downloadFile.write(bytes, 0, index);
            downloadFile.flush();
        }
        downloadFile.close();
        input.close();
    }


    private static boolean saveBitmap(Bitmap bmp, String path) {
        File f = new File(path);
        try {
            f.createNewFile();
            FileOutputStream fOut = new FileOutputStream(f);
            bmp.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();
            fOut.close();
            return true;
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return false;
    }

    public static GradientDrawable getBgDrawable(String color, int shape, float corner) {
        return getBgDrawable(color, shape, new float[]{corner}, 0, "");
    }

    public static GradientDrawable getBgDrawable(String color, int shape, float[] corner) {
        return getBgDrawable(color, shape, corner, 0, "");
    }

    public static GradientDrawable getBgDrawable(String color, int shape, float corner, int stroke, String strokeColor) {
        return getBgDrawable(color, shape, new float[]{corner}, stroke, strokeColor);
    }

    /**
     * @return
     */
    private static GradientDrawable getBgDrawable(String color, int shape, float[] corner, int stroke, String strokeColor) {
        GradientDrawable drawable = new GradientDrawable();
        if (TextUtils.isEmpty(color)) color = "#00ffffff";
        drawable.setColor(getRealColor(color));
        drawable.setShape(shape);
        if (corner.length == 1) {
            if (corner[0] != 0) {
                drawable.setCornerRadius(corner[0]);
            }
        } else {
            drawable.setCornerRadii(corner);
        }
        if (stroke != 0 && stroke != -1 && !TextUtils.isEmpty(strokeColor)) {
            drawable.setStroke(stroke, getRealColor(strokeColor));
        }
        return drawable;
    }

    public static int getRealValue(int value) {
        return value * getScreenWidth() / 750;
    }

    public static int getScreenWidth() {
        WindowManager wm = (WindowManager) Utils.getApp().getSystemService(Context.WINDOW_SERVICE);
        if (wm == null) {
            return Utils.getApp().getResources().getDisplayMetrics().widthPixels;
        }
        Point point = new Point();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            wm.getDefaultDisplay().getRealSize(point);
        } else {
            wm.getDefaultDisplay().getSize(point);
        }
        return point.x;
    }
}
