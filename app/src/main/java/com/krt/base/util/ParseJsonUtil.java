package com.krt.base.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.lang.reflect.Type;
import java.util.List;

public class ParseJsonUtil {

    /**
     * 根据Key获取Json中的Value
     *
     * @param jsonStr json字符串
     * @param key     Key值
     * @return 值
     */
    public static String getStringByKey(String jsonStr, String key) {
        String value = null;
        try {
            JSONObject jsonObject = JSON.parseObject(jsonStr);
            value = jsonObject.getString(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }


    /**
     * 获取Bean实体
     *
     * @param jsonStr json字符串
     * @param bean    实体Class
     */
    public static <T> T getBean(String jsonStr, Class<T> bean) {
        T t = null;
        try {
            t = JSON.parseObject(jsonStr, bean);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return t;
    }

    /**
     * 获取Bean实体
     */
    public static <T> T getBean(String jsonStr, Type type) {
        T t = null;
        try {
            t = JSON.parseObject(jsonStr, type);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return t;
    }

    /**
     * 获取Bean实体 List集合
     *
     * @param jsonStr *
     * @param bean    实体Class
     */
    public static <T> List<T> getBeanList(String jsonStr, Class<T> bean) {
        List<T> list = null;
        try {
            list = JSON.parseArray(jsonStr, bean);
        } catch (Exception e) {
            e.printStackTrace();

        }
        return list;
    }

    /**
     * 把实体类转换成json字符串
     */
    public static <T> String toJson(T bean) {
        return JSON.toJSONString(bean);
    }


}
