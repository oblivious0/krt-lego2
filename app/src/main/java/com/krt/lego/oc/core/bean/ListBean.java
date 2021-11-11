package com.krt.lego.oc.core.bean;

/**
 * @author: MaGua
 * @create_on:2021/11/10 15:52
 * @description
 */
public class ListBean {

    private String icon;
    private String text;
    private String url;
    private String code;
    private String name;
    private Object event;
    private boolean radioFlag;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getEvent() {
        return event;
    }

    public void setEvent(Object event) {
        this.event = event;
    }

    public boolean isRadioFlag() {
        return radioFlag;
    }

    public void setRadioFlag(boolean radioFlag) {
        this.radioFlag = radioFlag;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


}
