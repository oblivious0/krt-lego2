package com.krt.lego.oc.core.surface;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.LogUtils;
import com.jakewharton.rxbinding3.view.RxView;
import com.krt.Lego;
import com.krt.base.util.ParseJsonUtil;
import com.krt.lego.config.LegoConfig;
import com.krt.lego.oc.core.bean.BaseLayoutBean;
import com.krt.lego.oc.core.bean.EventBean;
import com.krt.lego.oc.core.surface.Subgrade;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author: MaGua
 * @create_on:2021/9/27 11:44
 * @description
 */
public abstract class BaseWidget<T extends View> {
    /**
     * 组件的CID
     */
    public String cid;
    /**
     * 组件的具体实例
     */
    public T view;
    /**
     * 此组件是否为某列表组件的子组件
     */
    protected boolean isListChild = false;

    /**
     * 此组件所在的全局（Activity或者Fragment)
     */
    protected Subgrade subgrade;

    /**
     * 组件类别
     */
    public String type;

    /**
     * 组件初始化配置实例
     */
    protected BaseLayoutBean bean;

    protected JSONObject style_1;
    private JSONObject style_2;

    protected Object data;

    public BaseWidget(Subgrade imp, BaseLayoutBean obj, boolean isListChild) {
        this.isListChild = isListChild;
        this.subgrade = imp;
        this.bean = obj;
        this.data = obj.getTag();
        generate();
    }

    @SuppressLint("CheckResult")
    private void generate() {
        cid = bean.getCid();
        type = bean.getType();
        style_1 = JSON.parseObject(JSON.toJSONString(bean.getCommon()));
        style_2 = JSON.parseObject(JSON.toJSONString(bean.getStyle()));
        try {
            createView();
            setViewPoi();
            initView();
            bindEvent();
            view.post(() -> Observable.just(bean)
                    .subscribeOn(Schedulers.io())
                    .observeOn(Schedulers.newThread())
                    .filter(baseLayoutBean -> bindInNewThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(baseLayoutBean -> {
                        bindInMainThread();
                    }));
        } catch (Exception e) {
            LogUtils.e(type + ":" + cid + " widget init err !",
                    e.getMessage()
            );
            e.printStackTrace();
        }
    }

    public T getRawView() {
        return view;
    }

    protected abstract void createView();

    protected void setViewPoi() {

        if (TextUtils.isEmpty(getStringVal("ftype"))) {
            FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(getIntVal("width"), getIntVal("height"));
            lp.setMargins(getIntVal("x"), getIntVal("y"), 0, 0);
            view.setLayoutParams(lp);
        } else {
            RelativeLayout.LayoutParams lp =
                    new RelativeLayout.LayoutParams(getIntVal("width"), getIntVal("height"));
            lp.setMargins(getIntVal("x"), getIntVal("y"), getIntVal("r"), getIntVal("b"));
            switch (getStringVal("ftype")) {
                case "lt":
                    lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                    lp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                    break;
                case "lb":
                    lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                    lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                    break;
                case "rt":
                    lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                    lp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                    break;
                case "rb":
                    lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                    lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                    break;
                default:
            }
            view.setLayoutParams(lp);
        }

    }

    /**
     * 组件所有初始化操作必须在主线程内
     */
    protected abstract void initView();

    /**
     * 初始化完成后，如需执行除了访问接口以外的耗时操作的时间在此方法完成，比如：倒计时
     * 所有影响组件外观样式操作禁止放入此线程操作内，如果是初始化项请在initView()执行
     * 如果需要中止之后操作，返回false，则不会执行bindInMainThread
     *
     * @return
     */
    protected boolean bindInNewThread(){
        return true;
    }

    /**
     * 当组件完成初始化操作之后需要执行的事件
     * 注意：此方法在bindInNewThread()完成之后执行，并且可被bindInNewThread()方法中断
     */
    protected void bindInMainThread(){

    }

    /**
     * 设置组件的属性方法
     * 注意：如果组件类型为容器类控件(不包括组合控件)组件，需要自行负责子组件的绑定，或则直接丢给子组件绑定
     * 可参照LayoutView绑定方式
     *
     * @param cid 需要设置组件的cid
     * @param key 需要设置组件的属性
     * @param val 设置的内容
     */
    public void bindData(String cid, String key, Object val) {
        if (cid.equals(this.cid)) {
            changeStyle(key, val);
        }
        initView();
    }

    protected void changeStyle(String key, Object val) {
        style_1.put(key, val);
    }

    /**
     * 绑定组件事件：当terminal没有绑定端，默认为所有端都需要执行的事件 xi
     */
    @SuppressLint("CheckResult")
    public void bindEvent() {
        List<EventBean> events = matchingEvent();
        if (events.size() != 0) {
            RxView.clicks(view)
//                    .throttleFirst(2, TimeUnit.SECONDS)
                    .subscribe(o -> {
//                        LegoConfig.getEventHander().onViewClick(this, events);
                    });
        }
    }

    public List<EventBean> matchingEvent() {
        List<EventBean> events = new ArrayList<>();
        if (bean.getEvent() != null) {
            if (bean.getEvent().size() != 0) {
                for (int z = 0; z < bean.getEvent().size(); z++) {
                    if (bean.getEvent().get(z).getTerminal() == null ||
                            bean.getEvent().get(z).getTerminal().size() == 0) {
                        events.add(bean.getEvent().get(z));
                        continue;
                    }
                    if (bean.getEvent().get(z).getTerminal().contains(String.valueOf(Lego.getLegoInfo().terminal))) {
                        events.add(bean.getEvent().get(z));
                    }
                }
            }
        }
        return events;
    }

    /**
     * 获取属性
     *
     * @param attr
     * @return
     */
    public Object getAttr(String attr) {
        Object val1 = style_1.get(attr);
        if (val1 == null && style_2 != null) {
            return style_2.get(attr);
        }
        return val1;
    }

    protected String getStringVal(String attr) {
        Object obj = getAttr(attr);
        return obj != null ? obj.toString() : null;
    }

    protected int getIntVal(String attr) {
        Object obj = getAttr(attr);
        if (obj == null) {
            return 0;
        }
        try {
            return Integer.parseInt(obj.toString());
        } catch (Exception e) {
            return 0;
        }
    }

    protected float getFloatVal(String attr) {
        Object obj = getAttr(attr);
        if (obj == null) {
            return 0;
        }
        try {
            return Float.parseFloat(obj.toString());
        } catch (Exception e) {
            return 0;
        }
    }

    protected double getDoubleVal(String attr) {
        Object obj = getAttr(attr);
        if (obj == null) {
            return 0;
        }
        try {
            return Double.parseDouble(obj.toString());
        } catch (Exception e) {
            return 0;
        }
    }

    protected boolean getBooleanVal(String attr) {
        Object obj = getAttr(attr);
        if (obj == null) {
            return false;
        }
        return Boolean.parseBoolean(obj.toString());
    }

    public Subgrade getDependent() {
        return subgrade;
    }
}
