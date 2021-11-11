//package com.krt.lego.oc.core.tools;
//
//import android.text.TextUtils;
//
//import com.krt.base.util.ParseJsonUtil;
//import com.krt.lego.oc.core.bean.ProcessBean;
//
//import java.util.List;
//
///**
// * @author hyj
// * @time 2020/8/20 10:56
// * @class describe
// */
//public class PropertyBindTool {
//
//    public static String getProperty(String[] bindKeys, Object data) {
//        //Array%krt_title
//        //data%krt_Array%krt_goodsPrice
//        //data%krt_Array%krt_gzcardViewspot%krt_num
//        if (data == null) return "";
//
//        String json = data.toString();
//        String jstring = "";
//        String val = "";
//
//        if (bindKeys.length == 1 && bindKeys[0].equals("data")) {
//            return json;
//        }
//
//        for (int i = 0; i < bindKeys.length; i++) {
//            if (!bindKeys[i].equals("data") && !bindKeys[i].equals("Array")) {
//                if (i != bindKeys.length - 1) {
//                    if (TextUtils.isEmpty(jstring)) {
//                        jstring = ParseJsonUtil.getStringByKey(json, bindKeys[i]);
//                    } else {
//                        jstring = ParseJsonUtil.getStringByKey(jstring, bindKeys[i]);
//                    }
//                } else {
//                    if (TextUtils.isEmpty(jstring)) {
//                        jstring = json;
//                    }
//                    val = ParseJsonUtil.getStringByKey(jstring, bindKeys[i]);
//                }
//
//            }
//        }
//        return val;
//    }
//
//    /**
//     * 数据映射
//     *
//     * @param val          数据原值
//     * @param processBeans 映射关系实体类
//     * @param key          映射的对应字段
//     * @return 获得映射后的值
//     */
//    public static String getProcessProperty(String val, List<ProcessBean> processBeans, String key) {
//        if (processBeans != null) {
//            for (ProcessBean process : processBeans) {
//                if (process.getField().equals(key)) {
//                    if (process.getType().equals("map"))
//                        return getKeyWord(process.getTable(), val);
//                }
//            }
//        }
//        return val;
//    }
//
//    /**
//     * 匹配映射关系
//     *
//     * @param table
//     * @param val
//     * @return
//     */
//    private static String getKeyWord(List<ProcessBean.TableBean> table, String val) {
//        for (ProcessBean.TableBean tab : table) {
//            if (tab.getKey().equals(val)) return tab.getVal();
//        }
//        return val;
//    }
//
//}
