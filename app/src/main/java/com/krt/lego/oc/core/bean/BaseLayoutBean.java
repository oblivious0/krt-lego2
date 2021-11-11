package com.krt.lego.oc.core.bean;

import java.util.List;

/**
 * @author: MaGua
 * @create_on:2021/9/27 11:48
 * @description
 */
public class BaseLayoutBean {
    private String cid;
    private Object common;
    private String name;
    private String nid;
    private Object style;
    private String type;
    private boolean waitToCopy;
    private List<BaseLayoutBean> children;
    private List<AjaxBean> ajax;
    private StaticDataBean staticData;
    private List<EventBean> event;
    private AnimationBean animation;

    private List<BottomBean.CommonBean.LinksBean> links;

    private Object tag;

    public Object getTag() {
        return tag;
    }

    public void setTag(Object tag) {
        this.tag = tag;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public Object getCommon() {
        return common;
    }

    public void setCommon(Object common) {
        this.common = common;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNid() {
        return nid;
    }

    public void setNid(String nid) {
        this.nid = nid;
    }

    public Object getStyle() {
        return style;
    }

    public void setStyle(Object style) {
        this.style = style;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isWaitToCopy() {
        return waitToCopy;
    }

    public void setWaitToCopy(boolean waitToCopy) {
        this.waitToCopy = waitToCopy;
    }

    public List<BaseLayoutBean> getChildren() {
        return children;
    }

    public void setChildren(List<BaseLayoutBean> children) {
        this.children = children;
    }

    public List<AjaxBean> getAjax() {
        return ajax;
    }

    public void setAjax(List<AjaxBean> ajax) {
        this.ajax = ajax;
    }

    public StaticDataBean getStaticData() {
        return staticData;
    }

    public void setStaticData(StaticDataBean staticData) {
        this.staticData = staticData;
    }

    public List<EventBean> getEvent() {
        return event;
    }

    public void setEvent(List<EventBean> event) {
        this.event = event;
    }

    public AnimationBean getAnimation() {
        return animation;
    }

    public void setAnimation(AnimationBean animation) {
        this.animation = animation;
    }
}
