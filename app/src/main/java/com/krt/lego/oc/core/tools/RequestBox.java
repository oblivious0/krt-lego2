package com.krt.lego.oc.core.tools;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.krt.base.net.callback.JsonCallback;
import com.krt.base.util.MToast;
import com.krt.lego.AppLibManager;
import com.krt.lego.oc.core.bean.AjaxBean;
import com.krt.lego.oc.core.bean.BindDataBean;
import com.krt.lego.oc.core.bean.CallbackBean;
import com.krt.lego.oc.core.bean.EventBean;
import com.krt.lego.oc.core.bean.ParamBean;
import com.krt.lego.oc.core.bean.ProcessBean;
import com.krt.lego.oc.core.bean.StateMentBean;
import com.krt.lego.oc.core.bean.StyleLinkBean;
import com.krt.lego.oc.core.bean.TransferKeyBean;
import com.krt.lego.oc.core.surface.BaseWidget;
import com.krt.lego.oc.core.important.NetDispatch;
import com.krt.lego.oc.core.surface.Subgrade;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.GetRequest;
import com.lzy.okgo.request.PostRequest;
import com.lzy.okgo.request.base.Request;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;


/**
 * @author: MaGua
 * @create_on:2021/9/26 14:54
 * @description
 */
public class RequestBox {

    private String ID;
    private String bindID;
    /**
     * request信息
     */
    private int pathIndex = 99;
    private String url;
    private String method;
    private String dataType = "x-www-form-urlencoded;charset=UTF-8";

    /**
     * http参数
     */
    private HashMap<String, String> params;
    private HashMap<String, String> headers;

    /**
     * 列表翻页参数
     * pageField: 当前页数字段名；  sizeField：数据量字段名；
     * init: 起始页码；  page: 当前页码；  size: 每页数据量
     */
    private String pageField;
    private String sizeField;

    private int init = 0;
    private int page = 0;
    private int size = 10;

    /**
     * response处理信息
     */
    private List<BindDataBean> bindData;
    private List<TransferKeyBean> transferKey;
    private List<StyleLinkBean> styleLink;
    private List<ProcessBean> process;
    private List<CallbackBean> callback;

    public RequestBox(AjaxBean bean) {
        cloneData(bean);
    }

    public RequestBox(AjaxBean bean, String listCid) {
        this.bindID = listCid;
        this.pageField = bean.getPageField();
        this.sizeField = bean.getPageField();
        cloneData(bean);
    }

    private void cloneData(AjaxBean ajax) {
        this.ID = ajax.getCid();
        this.pathIndex = ajax.getBasePathIdx();
        this.url = ajax.getUrl();
        this.method = ajax.getMethod();
        this.bindData = ajax.getBindData();
        this.transferKey = ajax.getTransferKey();
        this.styleLink = ajax.getStyleLink();
        this.process = ajax.getProcess();
        this.callback = ajax.getCallback();

        headers = new HashMap<>();
        params = new HashMap<>();

        if ("POST".equals(method)) {
            for (ParamBean param : ajax.getHeaders()) {
                if (param.getKey().equals("Content-Type")) {
                    dataType = param.getVal();
                }
                headers.put(param.getKey(), addFilterQueue(param));
            }
        }

        for (ParamBean param : ajax.getData()) {
            //如果是可翻页的列表请求,过滤掉翻页参数
            if (!TextUtils.isEmpty(bindID)) {
                if (param.equals(pageField)) {
                    init = Optional.ofNullable(Integer.parseInt(param.getVal())).orElse(0);
                    continue;
                } else if (param.equals(sizeField)) {
                    page = Optional.ofNullable(Integer.parseInt(param.getVal())).orElse(0);
                    continue;
                }
            }
            params.put(param.getKey(), addFilterQueue(param));
        }

    }

    /**
     * 对需要映射的数据做特殊标记
     *
     * @param param
     * @return
     */
    private String addFilterQueue(ParamBean param) {
        switch (param.getSource()) {
            case "variable":
            case "storage":
                return param.getSource() + "%filter%" + param.getVal();
            case "widget":
                return param.getSource() + "%filter%" + param.getAttr() + "%filter%" + param.getTarget();
            default:
                return param.getVal();
        }
    }

    /**
     * 对标有特殊标记的数据映射
     *
     * @param subgrade
     * @param val
     * @return
     */
    private String filtering(Subgrade subgrade, String val) {

        if (TextUtils.isEmpty(val)) return "";

        if (val.indexOf("%filter%") > 0) {
            String result = val;
            String[] structure = val.split("%filter%");

            switch (structure[0]) {
                case "variable":
                    //从页面变量获取
                    result = Optional.ofNullable
                            (subgrade.getDesigner().variables.get(structure[1]))
                            .orElse("");
                    break;
                case "storage":
                    //从项目全局（物理）变量集中获取
                    result = Optional.ofNullable(AppLibManager.getStorageVal
                            (structure[1], subgrade.getCarrier())).orElse("");
                    break;
                case "widget":
                    //组件属性中获取
                    try {
                        String a = structure[3].split("%krt%")[1];
                        result = Optional.of
                                (subgrade.getDesigner()
                                        .widgets
                                        .get(a)
                                        .getAttr(structure[1]).toString())
                                .orElse("");
                    } catch (Exception e) {
                    }
                    break;
                default:
                    result = structure[1];
                    break;
            }

            return result;
        } else {
            return val;
        }
    }

    /**
     * 执行
     *
     * @param subgrade
     */
    public void okGo(Subgrade subgrade) {

        Request request;

        if ("GET".equals(method))
            request = okGo_GET(subgrade);
        else
            request = okGo_POST(subgrade);

        request.execute(new JsonCallback() {
            @Override
            public void onSuc(Response response) {
                Object result = response.body();
                JSONObject responseBody = JSON.parseObject(JSON.toJSONString(result));
                if (responseBody.getInteger("code") != 200) {
                    String msg = Optional.ofNullable(responseBody.getString("message"))
                            .orElse(responseBody.getString("msg"));
                    MToast.showToast(subgrade.getCarrier(), msg);
                }

                if (TextUtils.isEmpty(bindID)) {
                    //先暂存导出值（非列表接口特有）
                    for (TransferKeyBean transfer : transferKey) {
                        String data = JSON.toJSONString(result);
                        String[] bindK = transfer.getKey().split("%krt_");
                        for (String key : bindK) {
                            data = JSON.parseObject(data).get(key).toString();
                            if (transfer.getTransferType().contains("1")) {
                                subgrade.getDesigner().variables.put
                                        (transfer.getVariableName(), data);
                            }
                            if (transfer.getTransferType().contains("2")) {
                                AppLibManager.putStorageVal
                                        (transfer.getStorageName(), data, subgrade.getCarrier());
                            }
                        }
                    }

                    //如果未绑定列表组件，绑定全局组件属性
                    for (BindDataBean bind :
                            Optional.ofNullable(bindData).orElse(new ArrayList<>())) {
                        //先挑出需要绑定的数据
                        String data = JSON.toJSONString(result);
                        String masterKey = "";
                        String[] bindK = bind.getBindKeys();
                        for (String key : bindK) {
                            data = JSON.parseObject(data).get(key).toString();
                            masterKey = key;
                        }
                        data = VariableFilter.map(data, masterKey, process);
                        String[] bindV = bind.getOriginKey().split("%krt%");
                        String[] v = bindV[bindV.length - 1].split("%krt_");
                        BaseWidget baseWidget = subgrade.getDesigner().widgets.get(v[0]);
                        if (baseWidget != null) {
                            baseWidget.bindData(v[0], v[1], data);
                        }
                    }
                } else {
                    //如果绑定了列表组件，更替列表adapter数据
                    BindDataBean bind = bindData.get(0);
                    String[] bindK = bind.getBindKeys();
                    String data = JSON.toJSONString(result);
                    JSONArray jsonArray = null;
                    for (String key : bindK) {
                        if (key.equals("Array")) {
                            jsonArray = JSON.parseArray(key);
                            break;
                        } else {
                            data = JSON.parseObject(data).get(key).toString();
                        }
                    }
                    //data%krt_Array%krt_img
                    BaseWidget baseWidget = subgrade.getDesigner().widgets.get(bindID);
                    if (baseWidget != null) {
                        baseWidget.bindData(bindID, "adapter", jsonArray.toJSONString());
                    }
                }

                if (callback != null) {
                    for (CallbackBean callback : callback) {
                        boolean isTrue = true;
                        //先判断callback条件
                        for (StateMentBean stateMent : callback.getStateMent()) {
                            if (Conditioner.judgeStateMent(stateMent, result, subgrade)) {
                                isTrue = false;
                                break;
                            }
                        }

                        if (isTrue) {
                            for (String eventCid : callback.getEvents()) {
                                EventBean eventBean = subgrade.getDesigner().orders.get(eventCid);
                                if (eventBean != null) {
//                                    LegoConfig.getEventHander().execute(sub, eventBean);
                                }
                            }
                        }

                    }
                }

            }
        });
    }

    /**
     * Get请求
     *
     * @param subgrade
     * @return
     */
    private Request okGo_GET(Subgrade subgrade) {
        String baseUrl = NetDispatch.getApiUrl(pathIndex);
        GetRequest request = OkGo.get(baseUrl + this.url);
        for (String k : headers.keySet()) {
            request.headers(k, filtering(subgrade, headers.get(k)));
        }
        for (String k : params.keySet()) {
            request.params(k, filtering(subgrade, params.get(k)));
        }
        //如果有翻页
        if (TextUtils.isEmpty(bindID)) {
            request.params(sizeField, size);
            request.params(pageField, page);
        }
        return request;
    }

    /**
     * Post请求
     *
     * @param subgrade
     * @return
     */
    private Request okGo_POST(Subgrade subgrade) {
        String baseUrl = NetDispatch.getApiUrl(pathIndex);
        PostRequest request = OkGo.post(baseUrl + this.url);
        for (String k : headers.keySet()) {
            request.headers(k, filtering(subgrade, headers.get(k)));
        }

        if (dataType.equals("application/json;charset=utf-8")) {
            JSONObject jsonParam = new JSONObject();
            for (String k : params.keySet()) {
                jsonParam.put(k, filtering(subgrade, params.get(k)));
            }
            //如果有翻页
            if (TextUtils.isEmpty(bindID)) {
                jsonParam.put(sizeField, size);
                jsonParam.put(pageField, page);
            }
            request.upJson(jsonParam.toJSONString());
        } else {
            for (String k : params.keySet()) {
                request.params(k, filtering(subgrade, params.get(k)));
            }
            //如果有翻页
            if (TextUtils.isEmpty(bindID)) {
                request.params(sizeField, size);
                request.params(pageField, page);
            }
        }

        return request;
    }


}
