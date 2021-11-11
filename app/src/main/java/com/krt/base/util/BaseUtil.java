package com.krt.base.util;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Environment;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.StrikethroughSpan;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import com.blankj.utilcode.util.ScreenUtils;
import com.krt.lego.Constants;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author xzy
 * @package com.krt.tour_ja.util
 * @description
 * @time 2020/7/10
 */
public class BaseUtil {

    /**
     * 新增字体删除线
     * @param str
     */
    public static SpannableString DeleteLine(String str) {
        SpannableString ss = new SpannableString(str);
        //设置删除线
        StrikethroughSpan strikethroughSpan = new StrikethroughSpan();
        ss.setSpan(strikethroughSpan, 0, str.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        return ss;
    }

    /**
     * 是否要拨打电话
     *
     * @param context
     * @param number
     */
    public static void callTel(final Context context, final String number) {
        String[] permissions = {Manifest.permission.CALL_PHONE};
        MPermissions.getInstance().request((FragmentActivity) context, permissions, new MPermissions.PermissionListener() {
            @Override
            public void callBack(boolean value) {
                if (value) {

                    new AlertDialog.Builder(context)
                            .setTitle("呼叫")
                            .setMessage(number)
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @SuppressLint("MissingPermission")
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Uri uri = Uri.parse("tel:" + number);
                                    Intent intent = new Intent(Intent.ACTION_CALL, uri);
                                    context.startActivity(intent);
                                    dialog.dismiss();
                                }
                            })
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).show();
                } else {
                    Toast.makeText(context, "您没有授权该权限，请在设置中打开授权!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    /**
     * 是否要拨打电话
     *
     * @param context
     * @param number
     */
    public static void callTelNow(final Context context, final String number) {
        String[] permissions = {Manifest.permission.CALL_PHONE};
        MPermissions.getInstance().request((FragmentActivity) context, permissions, new MPermissions.PermissionListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void callBack(boolean value) {
                if (value) {
                    Uri uri = Uri.parse("tel:" + number);
                    Intent i = new Intent(Intent.ACTION_CALL, uri);
                    context.startActivity(i);
                } else {
                    Toast.makeText(context, "您没有授权该权限，请在设置中打开授权!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }



    /**
     * 调用本机地图类app类进行导航如何没有的话则通过webview来导航
     */
//    public static void navigation(final Context mContext, final LatLng start, final Double lat, final Double lng) {
//        List<AppUtils.AppInfo> list = new ArrayList<>();
//        String[] paks = {"com.baidu.BaiduMap", "com.autonavi.minimap", "com.tencent.map"};
//        for (String pak : paks) {
//            AppUtils.AppInfo info = AppUtils.getAppInfo(pak);
//            if (info != null) {
//                list.add(info);
//            }
//        }
//        if (list.isEmpty()) {
//            new AlertDialog.Builder(mContext)
//                    .setMessage("您的手机中没有安装地图导航工具,我们将打开浏览器进行导航!")
//                    .setPositiveButton("继续导航", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            dialog.dismiss();
//                            NaviParaOption para = new NaviParaOption()
//                                    .startPoint(start)
//                                    .endPoint(new LatLng(lat, lng));
//                            BaiduMapNavigation.openWebBaiduMapNavi(para, mContext);
//                        }
//                    }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    dialog.dismiss();
//                }
//            }).show();
//        } else {
//            RecyclerView recyclerView = new RecyclerView(mContext);
//            recyclerView.setLayoutManager(new GridLayoutManager(mContext, list.size()));
//            final MapAppAdapter mAdapter = new MapAppAdapter(list);
//            recyclerView.setAdapter(mAdapter);
//            TextView textView = new TextView(mContext);
//            textView.setGravity(Gravity.CENTER);
//            textView.setText("选择您需要打开的应用");
//            textView.setPadding(0, 20, 0, 20);
//            textView.setTextColor(ContextCompat.getColor(mContext, R.color.defaultTitleText));
//            textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
//            AlertDialog.Builder builder = new AlertDialog.Builder(mContext, R.style.MapInfoDialog)
//                    .setCustomTitle(textView)
//                    .setView(recyclerView)
//                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            dialog.dismiss();
//                        }
//                    });
//            final AlertDialog dialog = builder.show();
//            mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
//                @Override
//                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//                    Intent naviIntent = null;
//                    LngLat lngLat = new LngLat(lng, lat);
//                    LngLat decLngLat = CoodinateCovertor.bd_decrypt(lngLat);
//                    switch (mAdapter.getData().get(position).getPackageName()) {
//                        case "com.baidu.BaiduMap":
//                            naviIntent = new Intent("android.intent.action.VIEW", Uri.parse("baidumap://map/geocoder?location=" + lat + "," + lng));
//                            break;
//                        case "com.autonavi.minimap":
//                            naviIntent = new Intent("android.intent.action.VIEW", Uri.parse("androidamap://route?sourceApplication=appName&slat=&slon=&sname=我的位置&dlat=" + decLngLat.getLantitude() + "&dlon=" + decLngLat.getLongitude() + "&dname=目的地&dev=0&t=2"));
//                            break;
//                        case "com.tencent.map":
//                            naviIntent = new Intent("android.intent.action.VIEW", Uri.parse("qqmap://map/routeplan?type=drive&from=&fromcoord=&to=目的地&tocoord=" + decLngLat.getLantitude() + "," + decLngLat.getLongitude() + "&policy=0&referer=appName"));
//                            break;
//                        default:
//                            break;
//                    }
//                    dialog.dismiss();
//                    mContext.startActivity(naviIntent);
//                }
//            });
//        }
//    }



    /**
     * 获取二点间的距离
     *
     * @param start
     * @param end
     * @return
     */
//    public static String getDistance(LatLng start, LatLng end) {
//        if (start.latitude != 0 && start.longitude != 0) {
//            double distanse = DistanceUtil.getDistance(start, end);
//            return String.format("%.2f", (distanse) / 1000) + "km";
//        }
//        return "0km";
//    }


    /**
     *  屏幕宽度（像素）
     */

    public static int getWindowWidth(Activity context) {
        DisplayMetrics metric = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(metric);
        return metric.widthPixels;
    }

    /**
     * 屏幕高度（像素）
     * @param context
     * @return
     */
    public static int getWindowHeight(Activity context) {
        DisplayMetrics metric = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(metric);
        return metric.heightPixels;
    }

    /**
     * 压缩图片（质量压缩）
     *
     * @param bitmap
     */
    public static File compressImage(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 500) {  //循环判断如果压缩后图片是否大于500kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            options -= 10;//每次都减少10
            bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            long length = baos.toByteArray().length;
        }
        File mkdFile = new File(Constants.usePath);
        if (!mkdFile.exists()) {
            mkdFile.mkdirs();
        }
        File file = new File(mkdFile, System.currentTimeMillis() + ".png");
        try {
            FileOutputStream fos = new FileOutputStream(file);
            try {
                fos.write(baos.toByteArray());
                fos.flush();
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return file;
    }

    public static void launchMiniProgram(Context context, String path) {
        // 填应用AppId
//        String appId = BuildConfig.WX_ID;
//        IWXAPI api = WXAPIFactory.createWXAPI(context, appId);
//        WXLaunchMiniProgram.Req req = new WXLaunchMiniProgram.Req();
//        // 填小程序原始id
//        req.userName = Constants.MINI_PROGRAM_ID;
//        ////拉起小程序页面的可带参路径，不填默认拉起小程序首页，对于小游戏，可以只传入 query 部分，来实现传参效果，如：传入 "?foo=bar"。
//        if (!TextUtils.isEmpty(path)) req.path = path;
//        // 可选打开 开发版，体验版和正式版
//        req.miniprogramType = WXLaunchMiniProgram.Req.MINIPTOGRAM_TYPE_RELEASE;
//        api.sendReq(req);
    }

    public static byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, output);
        if (needRecycle) {
            bmp.recycle();
        }

        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public static Bitmap drawableToBitamp(Drawable drawable) {
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();
        Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                : Bitmap.Config.RGB_565;
        Bitmap bitmap = Bitmap.createBitmap(w, h, config);
        // 注意，下面三行代码要用到，否在在View或者surfaceview里的canvas.drawBitmap会看不到图
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, w, h);
        drawable.draw(canvas);
        return bitmap;
    }


    /**
     * 根据750做基础适配
     *
     * @return
     */
    public static int getRealValue(int value) {
        return value * ScreenUtils.getScreenWidth() / 750;
    }


    /**
     * 根据750做基础适配获取高度
     * *
     *
     * @return
     */
    public static int getRealHValue(int hValue) {
        return hValue * ScreenUtils.getScreenWidth() / 750;
    }


    /**
     * 时间工具
     */
    public static String showTimeFormat(String time ) {

        if(time.contains("T")){
            time=time.replace("T", " ");
        }
        try {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = df.parse(time);
            Date now = new Date();
            Long diff = now.getTime() - date.getTime();
            Long day = diff / (1000 * 60 * 60 * 24);          //以天数为单位取整
            Long hour = (diff / (60 * 60 * 1000) - day * 24);             //以小时为单位取整
            Long min = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60);    //以分钟为单位取整
            if (day >= 1) {
                return day + "天前";
            } else if (hour >= 1) {
                return hour + "小时前";
            } else if (min > 1) {
                return min + "分钟前";
            } else {
                return "1分钟前";
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static File getFile(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        File file = new File(Environment.getExternalStorageDirectory() + "/temp.jpg");
        try {
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            InputStream is = new ByteArrayInputStream(baos.toByteArray());
            int x = 0;
            byte[] b = new byte[1024 * 100];
            while ((x = is.read(b)) != -1) {
                fos.write(b, 0, x);
            }
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }

    /**
     * 将时间转换为时间戳
     */
    public static long dateToStamp(String s) {

        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            Date date = null;
            date = simpleDateFormat.parse(s);
            long ts = date.getTime();
            return ts;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return -1;
    }


    public static String getTime(String time) {
        try {
            SimpleDateFormat sf = new SimpleDateFormat("HH:mm");
            SimpleDateFormat sf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return sf.format(sf2.parse(time));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getTimeWithNoYear(String time) {
        try {
            SimpleDateFormat sf = new SimpleDateFormat("MM.dd HH:mm");
            SimpleDateFormat sf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return sf.format(sf2.parse(time));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 处理隐藏手机号
     *
     * @param number
     * @return
     */
    public static String dealPhoneNumber(String number) {
        return number.substring(0, 3) + "****" + number.substring(7, number.length());
    }


    /**
     * @return
     */
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
        drawable.setColor(Color.parseColor(color));
        drawable.setShape(shape);
        if (corner.length == 1) {
            if (corner[0] != 0) {
                drawable.setCornerRadius(corner[0]);
            }
        } else {
            drawable.setCornerRadii(corner);
        }
        if (stroke != 0 && !TextUtils.isEmpty(strokeColor)) {
            drawable.setStroke(stroke, Color.parseColor(strokeColor));
        }
        return drawable;
    }

    /**
     * dp转像素单位
     * @param dip
     * @return
     */
    public static int dipToPx(Context context,int dip) {
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager wmg = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        wmg.getDefaultDisplay().getMetrics(dm);
        return (int) (dip * dm.density + 0.5f);
    }


    /**
     * 获取当前版本号
     * @param context
     * @return
     */
    public static String getVersion(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);

            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "error";
        }
    }

    /**
     * base64转成图片
     * @param string
     * @return
     */
    public static Bitmap stringtoBitmap(String string) {
        //将字符串转换成Bitmap类型
        Bitmap bitmap = null;
        try {
            byte[] bitmapArray;
            bitmapArray = Base64.decode(string, Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }


}
