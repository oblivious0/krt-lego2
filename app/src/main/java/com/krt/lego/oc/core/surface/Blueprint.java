package com.krt.lego.oc.core.surface;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.text.TextUtils;

import androidx.lifecycle.LifecycleOwner;

import com.krt.base.util.ParseJsonUtil;
import com.krt.lego.R;
import com.krt.lego.config.LegoGlobal;
import com.krt.lego.oc.core.bean.AjaxBean;
import com.krt.lego.oc.core.bean.BroadCastBean;
import com.krt.lego.oc.core.bean.EventBean;
import com.krt.lego.oc.core.bean.ParamBean;
import com.krt.lego.oc.core.bean.StateBean;
import com.krt.lego.oc.core.tools.ModuleViewFactory;
import com.krt.lego.oc.core.tools.RequestBox;
import com.krt.lego.oc.imp.surface.Buildings;
import com.krt.lego.oc.imp.surface.Lump;
import com.krt.lego.oc.lifecyler.MLifecycleEvent;
import com.krt.lego.oc.lifecyler.MLifecycleObserver;
import com.krt.lego.oc.variable.JsonValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * @author: MaGua
 * @create_on:2021/9/23 17:17
 * @description 设计者
 */
public class Blueprint {
    /**
     * 作业类型
     */
    public String pageType;
    /**
     * 存有json文件数据的字段
     */
    private String sketch;
    /**
     * 作业本体
     */
    private Subgrade subgrade;

    /**
     * 作业生命周期事件
     */
    private ILifecycleEvent iLifecycleEvent;
    /**
     * 作业本体观察者
     */
    public MLifecycleObserver mObserver;

    /**
     * 广播，一般不会外部引用，限制访问范围
     */
    protected HashMap<String, BroadCastBean> broadCasts;

    /**
     * 当前作业全局变量
     */
    public HashMap<String, String> variables;

    /**
     * 当前作业内所有ok_go请求（包括列表组件的）
     */
    public HashMap<String, RequestBox> requestBoxes;

    /**
     * 当前作业所有组件（不包括列表子组件）
     */
    public HashMap<String, BaseWidget> widgets;

    /**
     * 当前作业包含事务
     */
    public HashMap<String, EventBean> orders;

    public Blueprint(String json, Subgrade subgrade) {
        this.sketch = json;
        this.subgrade = subgrade;
        variables = new HashMap<>();
        broadCasts = new HashMap<>();
        requestBoxes = new HashMap<>();
        widgets = new HashMap<>();
        orders = new HashMap<>();
        init();
    }

    /**
     * 初始化
     */
    private void init() {
        if (subgrade instanceof LifecycleOwner) {
            mObserver = new MLifecycleObserver((LifecycleOwner) subgrade);
            ((LifecycleOwner) subgrade).getLifecycle().addObserver(mObserver);
        }

        //获取界面类型
        pageType = Optional.ofNullable(ParseJsonUtil.getStringByKey(sketch, "pageType"))
                .orElse("");

        //解析页面变量和传入值
        String variableJson = ParseJsonUtil.getStringByKey(sketch, JsonValue.VARIABLE);
        if (!TextUtils.isEmpty(variableJson)) {
            List<ParamBean> variable = Optional.ofNullable(ParseJsonUtil.getBeanList(variableJson, ParamBean.class))
                    .orElse(new ArrayList<>());
            Observable.fromIterable(variable)
                    .subscribe(new Observer<ParamBean>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(ParamBean paramBean) {
                            variables.put(paramBean.getKeyName(), paramBean.getVal());
                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onComplete() {
                            Activity carrier = getCarrier();
                            if (carrier != null) {
                                List<ParamBean> paramBeanList = ParseJsonUtil.getBeanList(
                                        carrier.getIntent().getStringExtra("param"), ParamBean.class);
                                if (paramBeanList != null) {
                                    for (ParamBean bean : paramBeanList) {
                                        variables.put(bean.getKey(), bean.getVal());
                                    }
                                }
                            }
                        }
                    });
        }

        //解析生命周期
        String stateJson = ParseJsonUtil.getStringByKey(sketch, JsonValue.STATE);
        if (!TextUtils.isEmpty(stateJson) && mObserver != null) {
            if (iLifecycleEvent == null) {
                iLifecycleEvent = new ILifecycleEvent();
                mObserver.addLifecycleObserver(iLifecycleEvent);
            }
            List<StateBean> stateBeanList = Optional.ofNullable(ParseJsonUtil.getBeanList(stateJson, StateBean.class))
                    .orElse(new ArrayList<>());
            for (StateBean state : stateBeanList) {
                String type = Optional.ofNullable(state.getType()).orElse("");
                switch (type) {
                    case "created":
                        iLifecycleEvent.createdEvent.add(state);
                        break;
                    case "unload":
                        iLifecycleEvent.closedEvent.add(state);
                        break;
                    default:
                        iLifecycleEvent.visitEvent.add(state);
                        break;
                }
            }
        }

        //解析广播
        String broadcastJson = ParseJsonUtil.getStringByKey(sketch, JsonValue.BROADCAST);
        if (!TextUtils.isEmpty(broadcastJson)) {
            List<BroadCastBean> broadCastBeanList = ParseJsonUtil.getBeanList(broadcastJson, BroadCastBean.class);
            for (BroadCastBean bean : broadCastBeanList) {
                broadCasts.put(bean.getCid(), bean);
            }
        }

        //解析页面层级ajax
        String ajaxJson = ParseJsonUtil.getStringByKey(sketch, JsonValue.AJAX);
        if (!TextUtils.isEmpty(ajaxJson)) {
            List<AjaxBean> ajaxBeanList = ParseJsonUtil.getBeanList(ajaxJson, AjaxBean.class);
            for (AjaxBean ajaxBean : ajaxBeanList) {
                requestBoxes.put(ajaxBean.getCid(), new RequestBox(ajaxBean));
            }
        }

        //解析事件
        String eventJson = ParseJsonUtil.getStringByKey(sketch, JsonValue.EVENT);
        if (!TextUtils.isEmpty(eventJson)) {
            List<EventBean> eventBeanList = ParseJsonUtil.getBeanList(eventJson, EventBean.class);
            for (EventBean eventBean : eventBeanList) {
                orders.put(eventBean.getCid(), eventBean);
            }
        }


    }

    public int build() {
        switch (pageType) {
            case "list":
                return R.layout.m_layout_recycler;
            default:
                return R.layout.m_layout_base;
        }
    }

    public void draw(){
        String pageJson = ParseJsonUtil.getStringByKey(sketch, JsonValue.PAGE);
        ModuleViewFactory.assemble(pageJson, subgrade);
    }

    /**
     * 获得载体
     *
     * @return
     */
    private Activity getCarrier() {
        if (subgrade instanceof Lump) {
            return ((Lump) subgrade).getActivity();
        } else if (subgrade instanceof Buildings) {
            return ((Buildings) subgrade);
        }
        return null;
    }


    /**
     * 设计者监视载体运营事件
     */
    private class ILifecycleEvent implements MLifecycleEvent {

        List<StateBean> createdEvent;
        List<StateBean> visitEvent;
        List<StateBean> closedEvent;

        public ILifecycleEvent() {
            createdEvent = new ArrayList<>();
            visitEvent = new ArrayList<>();
            closedEvent = new ArrayList<>();
        }

        @Override
        public void onStart() {

        }

        @SuppressLint("CheckResult")
        @Override
        public void onResume() {
            Observable.fromIterable(visitEvent)
                    .subscribeOn(Schedulers.newThread())
                    .filter(stateBean -> {
                        switch (stateBean.getType()) {
                            case "login":
                                return LegoGlobal.isUserLogin();
                            case "logout":
                                return !LegoGlobal.isUserLogin();
                            default:
                                return true;
                        }
                    }).subscribeOn(AndroidSchedulers.mainThread())
                    .subscribe(stateBean -> {

                    });
        }

        @Override
        public void onPause() {

        }

        @SuppressLint("CheckResult")
        @Override
        public void onStop() {
            Observable.fromIterable(closedEvent)
                    .subscribeOn(Schedulers.newThread())
                    .subscribeOn(AndroidSchedulers.mainThread())
                    .subscribe(stateBean -> {

                    });
        }

        @Override
        public void onDestroy() {

        }

        /**
         * 页面创建完成事件
         */
        @SuppressLint("CheckResult")
        @Override
        public void onPrepare() {
            Observable.fromIterable(createdEvent)
                    .subscribeOn(Schedulers.newThread())
                    .subscribeOn(AndroidSchedulers.mainThread())
                    .subscribe(stateBean -> {

                    });
        }
    }
}
