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
    private List<StyleLinkBean> styleLink;
    private List<ProcessBean> process;

    public List<StyleLinkBean> getStyleLink() {
        return styleLink;
    }

    public void setStyleLink(List<StyleLinkBean> styleLink) {
        this.styleLink = styleLink;
    }

    public List<ProcessBean> getProcess() {
        return process;
    }

    public void setProcess(List<ProcessBean> process) {
        this.process = process;
    }

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
