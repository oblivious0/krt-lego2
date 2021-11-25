package com.krt.lego.oc.core.tools;

import android.annotation.SuppressLint;
import android.widget.FrameLayout;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.constant.TimeConstants;
import com.blankj.utilcode.util.TimeUtils;
import com.krt.lego.oc.core.bean.AjaxBean;
import com.krt.lego.oc.core.bean.BaseLayoutBean;
import com.krt.lego.oc.core.bean.BindDataBean;
import com.krt.lego.oc.core.bean.ProcessBean;
import com.krt.lego.oc.core.bean.StateMentBean;
import com.krt.lego.oc.core.bean.StaticDataBean;
import com.krt.lego.oc.core.bean.StyleLinkBean;
import com.krt.lego.oc.core.surface.BaseWidget;
import com.krt.lego.oc.core.surface.Subgrade;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


/**
 * author:Marcus
 * create on:2020/6/4 11:31
 * description 用来切割 binddata key 中的各个属性
 */
public class DataBinder {

    private String viewProperty = "";

    private String viewName = "";

    private String cid = "";


    public DataBinder(String originKey) {
        deal(originKey);
    }

    private void deal(String originKey) {
        String[] first = originKey.split("%krt_");
        //最后一个为视图属性值
        viewProperty = first[first.length - 1];
        String[] second = first[first.length - 2].split("%krt%");
        cid = second[second.length - 1];
        viewName = second[second.length - 2];
    }

    public String getViewProperty() {
        return viewProperty;
    }

    public String getViewName() {
        return viewName;
    }

    public String getCid() {
        return cid;
    }

    public static List<Object> getDatas(BaseLayoutBean bean) {

        List<Object> list = null;
        if (bean.getStaticData() != null) {
            if (bean.getStaticData().getData() != null && !bean.getStaticData().getData().isEmpty()) {
                list = bean.getStaticData().getData();
            }
        }

        return list;

    }

    @SuppressLint("CheckResult")
    public static List<BaseWidget> bindListDatas(BaseLayoutBean bean, Subgrade imp, FrameLayout frameLayout, Object item) {
        List<BindDataBean> bindDatas = new ArrayList<>();
        List<BaseWidget> views = new ArrayList<>();
        frameLayout.removeAllViews();
        ModuleViewFactory.createViews(bean.getChildren(), imp, frameLayout, views, true, item);

        if (bean.getAjax() != null && bean.getAjax().size() != 0) {
            bindDatas.addAll(bean.getAjax().get(0).getBindData());
        } else if (bean.getStaticData() != null && bean.getStaticData().getBindData() != null) {
            bindDatas.addAll(bean.getStaticData().getBindData());
        }

        if (bindDatas.size() == 0) return views;

        for (BaseWidget baseV : views) {
            for (int j = 0; j < bindDatas.size(); j++) {
                DataBinder util = new DataBinder(bindDatas.get(j).getOriginKey());
                String val = VariableFilter.getProperty(bindDatas.get(j).getBindKeys(), item);
                baseV.bindData(util.getCid(), util.getViewProperty(), val);
            }
        }

        return views;
    }

    /**
     * 瀑布流绑定样式数据
     *
     * @param bean
     * @param imp
     * @param frameLayout
     * @param item
     * @return
     */
    @SuppressLint("CheckResult")
    public static List<BaseWidget> bindListDataPosition(BaseLayoutBean bean, Subgrade imp, FrameLayout frameLayout, Object item, List<BaseLayoutBean> childrenBean) {
        List<BindDataBean> bindDatas = new ArrayList<>();
        List<BaseWidget> views = new ArrayList<>();
        frameLayout.removeAllViews();
//        ModuleViewFactory.createViews(bean.getChildren().get(position).getChildren(), imp, frameLayout, views, true);
        ModuleViewFactory.createViews(childrenBean, imp, frameLayout, views, true, item);
        if (item != null) {
            if (bean.getAjax() != null && bean.getAjax().size() != 0) {
                bindDatas.addAll(bean.getAjax().get(0).getBindData());
            } else if (bean.getStaticData() != null && bean.getStaticData().getBindData() != null) {
                bindDatas.addAll(bean.getStaticData().getBindData());
            }
            if (bindDatas.size() == 0) return views;
            for (BaseWidget baseV : views) {
                for (int j = 0; j < bindDatas.size(); j++) {
                    DataBinder util = new DataBinder(bindDatas.get(j).getOriginKey());
                    String val = VariableFilter.getProperty(bindDatas.get(j).getBindKeys(), item);
                    baseV.bindData(util.getCid(), util.getViewProperty(), val);
                }
            }
        }
        return views;
    }

    /**
     * 瀑布流绑定左上角容器数据样式，左上角容器数数据写死
     *
     * @param imp
     * @param frameLayout
     * @return
     */
    @SuppressLint("CheckResult")
    public static List<BaseWidget> bindLetListDataPosition(Subgrade imp, FrameLayout frameLayout, List<BaseLayoutBean> childrenBean) {
        List<BaseWidget> views = new ArrayList<>();
        frameLayout.removeAllViews();
        ModuleViewFactory.createViews(childrenBean, imp, frameLayout, views, true, null);
        return views;
    }

    /**
     * 解析多样式列表
     *
     * @param imp
     * @param frameLayout
     * @param children
     * @param ajax
     * @return
     */
    public static List<BaseWidget> bindMutiListData(Subgrade imp, FrameLayout frameLayout, List<BaseLayoutBean> children, Object item, AjaxBean ajax) {
        HashMap<String, BaseLayoutBean> styleContent = new HashMap<>();
        for (BaseLayoutBean bean : children) {
            if (bean.getType().equals("layout")) {
                styleContent.put(bean.getCid(), bean);
            }
        }

        List<ProcessBean> processes = ajax.getProcess();
        List<StyleLinkBean> links = ajax.getStyleLink();

        //先过滤样式
        String layoutCid = getMutiBindCid(links, styleContent.keySet().iterator().next(), item, imp);
        BaseLayoutBean layout = styleContent.get(layoutCid);
        //重置定位
        JSONObject poi = JSON.parseObject(JSON.toJSONString(layout.getCommon()));
        poi.put("x", 0);
        poi.put("y", 0);
        layout.setCommon(poi);
        //再生产组件
        List<BindDataBean> bindDatas = ajax.getBindData();
        List<BaseWidget> views = new ArrayList<>();
        frameLayout.removeAllViews();

        ModuleViewFactory.createViews(Arrays.asList(layout), imp, frameLayout, views, true, item);
        //再组件绑定数据
        for (BaseWidget baseV : views) {
            for (int j = 0; j < bindDatas.size(); j++) {
                DataBinder util = new DataBinder(bindDatas.get(j).getOriginKey());
                String val = VariableFilter.getProperty(bindDatas.get(j).getBindKeys(), item);
                baseV.bindData(util.getCid(), util.getViewProperty(),
                        VariableFilter.map(val, util.getViewProperty(), processes));
            }
        }
        return views;
    }

    public static List<BaseWidget> bindMutiListData(Subgrade imp, FrameLayout frameLayout, List<BaseLayoutBean> children, Object item, StaticDataBean ajax) {
        HashMap<String, BaseLayoutBean> styleContent = new HashMap<>();
        for (BaseLayoutBean bean : children) {
            if (bean.getType().equals("layout")) {
                styleContent.put(bean.getCid(), bean);
            }
        }

        List<ProcessBean> processes = ajax.getProcess();
        List<StyleLinkBean> links = ajax.getStyleLink();

        //先过滤样式
        String layoutCid = getMutiBindCid(links, styleContent.keySet().iterator().next(), item, imp);
        BaseLayoutBean layout = styleContent.get(layoutCid);
        //重置定位
        JSONObject poi = JSON.parseObject(JSON.toJSONString(layout.getCommon()));
        poi.put("x", 0);
        poi.put("y", 0);
        layout.setCommon(poi);
        //再生产组件
        List<BindDataBean> bindDatas = ajax.getBindData();
        List<BaseWidget> views = new ArrayList<>();
        frameLayout.removeAllViews();

        ModuleViewFactory.createViews(Arrays.asList(layout), imp, frameLayout, views, true, item);
        //再组件绑定数据
        for (BaseWidget baseV : views) {
            for (int j = 0; j < bindDatas.size(); j++) {
                DataBinder util = new DataBinder(bindDatas.get(j).getOriginKey());
                String val = VariableFilter.getProperty(bindDatas.get(j).getBindKeys(), item);
                baseV.bindData(util.getCid(), util.getViewProperty(),
                        VariableFilter.map(val, util.getViewProperty(), processes));
            }
        }
        return views;
    }


    private static String getMutiBindCid(List<StyleLinkBean> links, String def, Object data, Subgrade subgrade) {
        for (StyleLinkBean bean : links) {
            if (Conditioner.judge(bean, data, subgrade)) {
                return bean.getStyle();
            }
        }
        return def;
    }

}
