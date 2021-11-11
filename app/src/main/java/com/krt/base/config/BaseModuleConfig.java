package com.krt.base.config;



import androidx.annotation.ColorRes;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import okhttp3.Interceptor;

/**
 * Created by marcus on 18/3/2.
 */

public class BaseModuleConfig {
    public static Builder newBuilder() {
        return new Builder();
    }


    private BaseModuleConfig(Builder builder) {
        this.dialogStyle = builder.dialogStyle;
        this.dialogColor = builder.dialogColor;
        this.headers = builder.headers;
        this.params = builder.params;
        this.jsValue = builder.jsValue;
        this.needGetCode = builder.needGetResult;
        this.mConnectTimeout = builder.mConnectTimeout;
        this.mReadTimeout = builder.mReadTimeout;
        this.needPrintLog = builder.needPrintLog;
        this.interceptors = builder.interceptors;
        this.serverSuccessCode = builder.serverSuccessCode;
    }

    /**
     * 默认LoadingView样式
     */
    private String dialogStyle;
    /**
     * 刷新控件的颜色
     */
    private int refreshColor;
    /**
     * LoadingDialog颜色
     *
     * @return
     */
    private int dialogColor;
    /**
     * http请求的公共变量
     */
    private Map<String, String> params;

    /**
     * http请求的公共头部
     */
    private Map<String, String> headers;
    /**
     * web注入js的值和对象
     */
    private Map<String, Object> jsValue;
    /**
     * 拦截器
     */
    private List<Interceptor> interceptors;


    private boolean needGetCode;

    private boolean needPrintLog;

    /**
     * 连接时间
     */
    private int mConnectTimeout;
    /**
     * 读取时间
     */
    private int mReadTimeout;

    /*
     * 服务端接口返回正常code值  默认200
     */
    private int serverSuccessCode;


    public String getDialogStyle() {
        return dialogStyle;
    }

    public int getDialogColor() {
        return dialogColor;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public boolean isNeedGetCode() {
        return needGetCode;
    }

    public int getConnectTimeout() {
        return mConnectTimeout;
    }

    public int getReadTimeout() {
        return mReadTimeout;
    }

    public List<Interceptor> getInterceptors() {
        return interceptors;
    }

    public boolean isNeedPrintLog() {
        return needPrintLog;
    }

    public int getServerSuccessCode() {
        return serverSuccessCode;
    }

    public Map<String, Object> getJsValue() {
        return jsValue;
    }

    public final static class Builder {
        private Builder() {

        }

        /**
         * 默认LoadingView样式
         */
        private String dialogStyle = "LineScaleIndicator";

        /**
         * LaddingView的颜色
         */
        private int dialogColor = -1;

        /**
         * http请求的公共变量
         */
        private Map<String, String> params;

        /**
         * http请求的公共头部
         */
        private Map<String, String> headers;
        /**
         * JS的注入名称及对象
         */
        private Map<String, Object> jsValue;
        /**
         * 添加拦截器
         */
        private List<Interceptor> interceptors;

        /**
         * 是否需要分发Http的请求结果  默认不分发  该功能适用于统一处理http的结果
         */
        private boolean needGetResult = false;
        /**
         * 是否需要打印相关库产生的一些日志 例如http请求等  默认不打开
         */
        private boolean needPrintLog = false;

        /**
         * 连接时间
         */
        private int mConnectTimeout = 8;
        /**
         * 读取时间
         */
        private int mReadTimeout = 8;

        /*
         * 服务端接口返回正常code值  默认200
         */
        private int serverSuccessCode = 200;


        /**
         * 设置LoadingView样式
         *
         * @param dialogStyle
         */
        public Builder setLoadingViewStyle(String dialogStyle) {
            this.dialogStyle = dialogStyle;
            return this;
        }


        public Builder setLoadingViewColor(@ColorRes int color) {
            this.dialogColor = color;
            return this;
        }

        public Builder addParams(String key, String value) {
            if (params == null) {
                params = new Hashtable<>();
            }
            params.put(key, value);
            return this;
        }

        public Builder addHeaders(String key, String value) {
            if (headers == null) {
                headers = new Hashtable<>();
            }
            headers.put(key, value);
            return this;
        }

        public Builder setNeedGetResult(boolean needGetResult) {
            this.needGetResult = needGetResult;
            return this;
        }

        public Builder setServerSuccessCode(int serverSuccessCode) {
            this.serverSuccessCode = serverSuccessCode;
            return this;
        }

        /**
         * 设置webview js的key 和对象注入
         */
        public Builder setJsValue(String key, Object object) {
            if (jsValue == null) {
                jsValue = new Hashtable<>();
            }
            jsValue.put(key, object);
            return this;
        }

        /**
         * 添加拦截器
         *
         * @return
         */
        public Builder addInterceptor(Interceptor interceptor) {
            if (interceptors == null) {
                interceptors = new ArrayList<>();
            }
            interceptors.add(interceptor);
            return this;
        }

        public Builder setConnectTimeout(int connectTimeout) {
            mConnectTimeout = connectTimeout;
            return this;
        }

        public Builder setReadTimeout(int readTimeout) {
            mReadTimeout = readTimeout;
            return this;
        }

        public Builder setNeedPrintLog(boolean needPrintLog) {
            this.needPrintLog = needPrintLog;
            return this;
        }

        public BaseModuleConfig build() {
            return new BaseModuleConfig(this);
        }
    }
}
