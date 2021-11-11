package com.krt.base.base;


import androidx.multidex.MultiDexApplication;

import com.blankj.utilcode.util.Utils;
import com.krt.base.config.BaseModule;
import com.krt.base.config.BaseModuleConfig;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.interceptor.HttpLoggingInterceptor;
import com.lzy.okgo.model.HttpHeaders;
import com.lzy.okgo.model.HttpParams;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;


public abstract class MApp extends MultiDexApplication {

    private static MApp instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        Utils.init(this);
        init();
        initHttp();
    }

    private void initHttp() {
        BaseModuleConfig config = BaseModule.getBaseModuleConfig();
        OkGo builder = OkGo.getInstance().init(this);
        Map<String, String> params = config.getParams();
        Map<String, String> headers = config.getHeaders();
        List<Interceptor> interceptors = config.getInterceptors();
        if (headers != null && !headers.keySet().isEmpty()) {
            HttpHeaders httpHeaders = new HttpHeaders();
            for (String key : headers.keySet()) {
                httpHeaders.put(key, headers.get(key));
            }
            builder.addCommonHeaders(httpHeaders);
        }
        if (params != null && !params.keySet().isEmpty()) {
            HttpParams httpParams = new HttpParams();
            for (String key : params.keySet()) {
                httpParams.put(key, params.get(key));
            }
            builder.addCommonParams(httpParams);
        }
        OkHttpClient.Builder client = new OkHttpClient.Builder();
        client.addInterceptor(getLoggingInterceptor());
        if (interceptors != null && !interceptors.isEmpty()) {
            for (Interceptor interceptor : interceptors) {
                client.addInterceptor(interceptor);
            }
        }
        client.readTimeout(config.getReadTimeout(), TimeUnit.SECONDS);
        client.writeTimeout(config.getReadTimeout(), TimeUnit.SECONDS);
        client.connectTimeout(config.getConnectTimeout(), TimeUnit.SECONDS);
        builder.setOkHttpClient(client.build());
    }

    private HttpLoggingInterceptor getLoggingInterceptor() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor("HttpLog");
        interceptor.setPrintLevel((HttpLoggingInterceptor.Level.BODY));
        interceptor.setColorLevel(Level.INFO);
        return interceptor;
    }


    /**
     * 获取全局的application对象
     *
     * @return
     */
    public static MApp getInstance() {
        return instance;
    }

    /**
     * 执行相关项目的一些的初始化配置
     */
    protected abstract void init();
}
