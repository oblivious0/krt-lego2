package com.krt.simple.base;

import android.app.Application;
import android.text.TextUtils;
import android.util.Log;


import com.krt.lego.config.LegoConfig;
import com.krt.simple.module.BaseEventHandler;
import com.krt.simple.module.MFragmentFactory;
import com.krt.simple.module.ModuleActivity;
import com.krt.simple.module.ModuleFragment;
import com.krt.simple.web.X5WebFragment;
import com.tencent.smtt.export.external.TbsCoreSettings;
import com.tencent.smtt.sdk.QbSdk;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.UUID;
import java.util.stream.Collector;

import io.reactivex.plugins.RxJavaPlugins;


/**
 * author: MaGua
 * create on:2021/3/25 10:00
 * description
 */
public class App extends Application implements LegoConfig.LegoAppWatcher{
    private static App instance;

    public static String token;

    private String uuid;


    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        RxJavaPlugins.setErrorHandler(throwable -> {
//            LogUtils.e("RxJava全局异常捕获", throwable.getMessage());
        });

        LegoConfig.setActivityClazz(ModuleActivity.class);
        LegoConfig.setFragmentClazz(ModuleFragment.class);
        LegoConfig.setWebFragmentClazz(X5WebFragment.class);
        LegoConfig.setFragmentFactory(MFragmentFactory.class);
        LegoConfig.setLegoAppWatcher(this);
        LegoConfig.setEventHander(new BaseEventHandler());

        initWebView();
//        Buried.initialize(this);
//        ShareUtil.xShareConfig(Constans.WX_ID, Constans.APP_ID);
//        LocationUtils.getInstance(this);

//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("itemType", "launch");
//        jsonObject.put("itemCode", "0");
//        jsonObject.put("itemName", "应用启动");
//        Buried.bury(jsonObject.toJSONString());
    }

    public static App getInstance() {
        return instance;
    }

    @Override
    public boolean isLogin() {
        return false;
    }

    @Override
    public String[] registerAuthorization() {
        return new String[]{"null", "null"};
    }

    private void initWebView(){
        HashMap map = new HashMap();
        map.put(TbsCoreSettings.TBS_SETTINGS_USE_SPEEDY_CLASSLOADER, true);
        map.put(TbsCoreSettings.TBS_SETTINGS_USE_DEXLOADER_SERVICE, true);
        QbSdk.initTbsSettings(map);
        QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {

            @Override
            public void onViewInitFinished(boolean arg0) {
                // TODO Auto-generated method stub
                //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
                Log.d("app", " onViewInitFinished is " + arg0);
            }

            @Override
            public void onCoreInitFinished() {
                // TODO Auto-generated method stub
            }
        };
        //x5内核初始化接口
        QbSdk.initX5Environment(getApplicationContext(), cb);
    }

//    @Override
//    public String getAppUUID() {
//        if (TextUtils.isEmpty(uuid)) {
//            UUID uuid1 = UUID.randomUUID();
//            uuid = uuid1.toString().replace("-", "");
//        }
//        return uuid;
//    }
//
//    @Override
//    public String getKrtNo() {
//        return "";
//    }
//
//    @Override
//    public String getKrtTag() {
//        return "APP-000046";
//    }
}
