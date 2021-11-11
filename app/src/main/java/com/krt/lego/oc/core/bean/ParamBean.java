package com.krt.lego.oc.core.bean;

/**
 * @author hyj
 * @time 2020/9/5 9:51
 * @class describe
 */
public class ParamBean {
    /**
     * 传递的字段名称
     */
    private String key;
    private String keyName;
    private String val;
    private String source = "";
    private String target;
    private String attr;

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getAttr() {
        return attr;
    }

    public void setAttr(String attr) {
        this.attr = attr;
    }

    public ParamBean() {
    }

    public ParamBean(String key, String val) {
        this.key = key;
        this.val = val;
    }

    public String getKeyName() {
        return keyName;
    }

    public void setKeyName(String keyName) {
        this.keyName = keyName;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getVal() {
        return val;
    }

    public void setVal(String val) {
        this.val = val;
    }
}