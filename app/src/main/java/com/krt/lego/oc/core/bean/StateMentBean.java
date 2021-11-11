package com.krt.lego.oc.core.bean;

import java.util.List;

/**
 * author: MaGua
 * create on:2021/4/19 16:50
 * description
 */
public class StateMentBean {
    /**
     * fields : ["data%krt_Array%krt_gcId"]
     * type : =
     * val : ["932"]
     * valType : common
     * mode : 2
     */

    private String type;
    private String valType;
    private String mode;
    private List<String> fields;
    private List<String> val;
    private String dateFormat;
    private String source;

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDateFormat() {
        return dateFormat;
    }

    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValType() {
        return valType;
    }

    public void setValType(String valType) {
        this.valType = valType;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public List<String> getFields() {
        return fields;
    }

    public void setFields(List<String> fields) {
        this.fields = fields;
    }

    public List<String> getVal() {
        return val;
    }

    public void setVal(List<String> val) {
        this.val = val;
    }
}
