package com.krt.lego.oc.core.bean;

import java.util.List;

/**
 * @author hyj
 * @time 2020/9/5 9:50
 * @class describe
 */
public class StaticDataBean {
    private List<Object> data;
    private List<BindDataBean> bindData;

    public List<Object> getData() {
        return data;
    }

    public void setData(List<Object> data) {
        this.data = data;
    }

    public List<BindDataBean> getBindData() {
        return bindData;
    }

    public void setBindData(List<BindDataBean> bindData) {
        this.bindData = bindData;
    }
}
