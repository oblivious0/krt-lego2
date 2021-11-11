package com.krt.lego.oc.core.tools;

import android.text.TextUtils;

import com.krt.base.util.ParseJsonUtil;
import com.krt.lego.AppLibManager;
import com.krt.lego.oc.core.bean.ParamBean;
import com.krt.lego.oc.core.bean.ProcessBean;
import com.krt.lego.oc.core.surface.BaseWidget;
import com.krt.lego.oc.core.surface.Subgrade;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

/**
 * author: MaGua
 * create on:2021/3/26 9:34
 * description
 */
public class VariableFilter {

    /**
     * 用于事件的变量过滤或其他过滤
     *
     * @param subgrade
     * @param objects
     * @return
     */
    public static List<ParamBean> filter(Subgrade subgrade, List<ParamBean> objects) {
        List<ParamBean> paramBeans = new ArrayList<>();
        for (ParamBean bodyParam : objects) {
            if ("props".equals(bodyParam.getSource()) || "variable".equals(bodyParam.getSource())) {
                Object val = subgrade.getDesigner().variables.get(bodyParam.getVal());
                paramBeans.add(new ParamBean(bodyParam.getKey(), val == null ? bodyParam.getVal() : val.toString()));
            } else if ("storage".equals(bodyParam.getSource())) {
                Object val = AppLibManager.getStorageVal(bodyParam.getVal(), subgrade.getCarrier());
                paramBeans.add(new ParamBean(bodyParam.getKey(), val == null ? bodyParam.getVal() : val.toString()));
            } else {
                //static
                paramBeans.add(new ParamBean(bodyParam.getKey(), bodyParam.getVal()));
            }
        }
        return paramBeans;
    }

    /**
     * 用于列表对应的变量过滤
     *
     * @param subgrade
     * @param key
     * @param source
     * @param data
     * @return
     */
    public static String filter(Subgrade subgrade, String key, String source, Object data) {
        if (source == null) source = "transKey";
        switch (source) {
            case "props":
            case "variable":
                Object val = subgrade.getDesigner().variables.get(key);
                return val.toString();

            case "storage":
                Object val1 = AppLibManager.getStorageVal(key, subgrade.getCarrier());
                return val1.toString();

            case "transKey":
                if (key.contains("%krt_")) {
                    String val2 = getProperty(key.split("%krt_"), data);
                    return val2;
                }
                return ParseJsonUtil.getStringByKey(data.toString(), key);
            default:
                return key;
        }
    }

    /**
     * 列表字段截取
     *
     * @param bindKeys
     * @param data
     * @return
     */
    public static String getProperty(String[] bindKeys, Object data) {
        //Array%krt_title
        //data%krt_Array%krt_goodsPrice
        //data%krt_Array%krt_gzcardViewspot%krt_num
        if (data == null) return "";

        String json = data.toString();
        String jstring = "";
        String val = "";

        if (bindKeys.length == 1 && bindKeys[0].equals("data")) {
            return json;
        }

        for (int i = 0; i < bindKeys.length; i++) {
            if (!bindKeys[i].equals("data") && !bindKeys[i].equals("Array")) {
                if (i != bindKeys.length - 1) {
                    if (TextUtils.isEmpty(jstring)) {
                        jstring = ParseJsonUtil.getStringByKey(json, bindKeys[i]);
                    } else {
                        jstring = ParseJsonUtil.getStringByKey(jstring, bindKeys[i]);
                    }
                } else {
                    if (TextUtils.isEmpty(jstring)) {
                        jstring = json;
                    }
                    val = ParseJsonUtil.getStringByKey(jstring, bindKeys[i]);
                }
            }
        }
        return val;
    }

    /**
     * 数据处理
     *
     * @param data      需要处理的原数据
     * @param masterKey 需要处理的原字段名
     * @param process   处理公类集合
     * @return
     */
    public static String map(String data, String masterKey, List<ProcessBean> process) {
//        data%krt_Array%krt_goodsPrice
        for (ProcessBean pro : Optional.ofNullable(process).orElse(new ArrayList<>())) {
            String[] keys = pro.getField().split("%krt_");
            String key = keys[keys.length - 1];
            if (key.equals(masterKey)) {
                switch (pro.getType()) {
                    case "prefix":
                        //加前缀
                        data = pro.getStr() + data;
                        break;
                    case "postfix":
                        //加后缀
                        data = data + pro.getStr();
                        break;
                    case "map":
                        //数据映射
                        for (ProcessBean.TableBean table : pro.getTable()) {
                            if (table.getKey().equals(data)) {
                                data = table.getVal();
                            }
                        }
                        break;
                }
            }
        }
        return data;
    }

    /**
     * 事件参数过滤
     *
     * @param subgrade
     * @param source
     * @param json
     * @return
     */
    public static HashMap<String, String> filterParam(Subgrade subgrade, List<ParamBean> source, Object json) {
        HashMap<String, String> params = new HashMap<>();
        //判断参数集是否为空，不为空将遍历参数集，把传出值替换为实际数据；
        if (source != null) {
            for (ParamBean paramBean : source) {
                switch (paramBean.getSource()) {
                    case "props":
                    case "variable":
                        Object val = subgrade.getDesigner().variables
                                .get(paramBean.getVal());
                        params.put(paramBean.getKey(), val.toString());
                        break;
                    case "storage":
                        Object val1 = AppLibManager.getStorageVal(paramBean.getVal(), subgrade.getCarrier());
                        params.put(paramBean.getKey(), val1.toString());
                        break;
                    case "transKey":
                        if (paramBean.getVal().contains("%krt_")) {
                            String val2 = VariableFilter.getProperty(paramBean.getVal().split("%krt_"), json);
                            params.put(paramBean.getKey(), val2.toString());
                        }
                        break;
                    case "widget":
                        params.put(paramBean.getKey(), getBaseViewAttr(subgrade, paramBean.getTarget(), paramBean.getAttr()));
                        break;
                    default:
                        params.put(paramBean.getKey(), paramBean.getVal());
                        break;
                }
            }
        }
        return params;
    }

    /**
     * 获取组件元素
     *
     * @param subgrade
     * @param target
     * @param attr
     * @return
     */
    public static String getBaseViewAttr(Subgrade subgrade, String target, String attr) {
        try {
            String[] targets = target.split("%krt%");
            BaseWidget view = subgrade.getDesigner().widgets.get(targets[targets.length - 1]);
            return view.getAttr(attr).toString();
        } catch (Exception e) {
        }
        return "";
    }
}
