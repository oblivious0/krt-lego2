package com.krt.lego.oc.core.bean;

import java.util.List;


/**
 * @author hyj
 * @time 2020/9/5 9:48
 * @class describe
 */
public class AjaxBean {
    /**
     * url : http://127.0.0.1:9000/api/list.json
     * method : GET
     * data : []
     * remark :
     * bindData : [{"originKey":"label%krt%c4k8za48m5%krt_text","bindKey":"data%krt_Array%krt_text"},{"originKey":"pic%krt%1vwvkq4ess%krt_src","bindKey":"data%krt_Array%krt_img"}]
     */

    /**
     * 接口地址
     */
    private String url;
    /**
     * POST OR GET
     */
    private String method;
    private String remark;
    /**
     * body参数
     */
    private List<ParamBean> data;
    /**
     * 绑定View和数据的关系
     */
    private List<BindDataBean> bindData;
    /**
     * header参数
     */
    private List<ParamBean> headers;
    /**
     * 如果是翻页接口，此属性对应代表body参数哪个字段是页码/每页显示的数量
     */
    private String pageField;
    private String sizeField;

    private boolean needLogin;
    private String cid;
    private List<TransferKeyBean> transferKey;

    private int basePathIdx = 99;

    private List<StyleLinkBean> styleLink;
    private List<ProcessBean> process;
    private List<CallbackBean> callback;

    public List<CallbackBean> getCallback() {
        return callback;
    }

    public void setCallback(List<CallbackBean> callback) {
        this.callback = callback;
    }

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

    public List<TransferKeyBean> getTransferKey() {
        return transferKey;
    }

    public void setTransferKey(List<TransferKeyBean> transferKeys) {
        this.transferKey = transferKeys;
    }

    public int getBasePathIdx() {
        return basePathIdx;
    }

    public void setBasePathIdx(int basePathIdx) {
        this.basePathIdx = basePathIdx;
    }

    public boolean isNeedLogin() {
        return needLogin;
    }

    public void setNeedLogin(boolean needLogin) {
        this.needLogin = needLogin;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getPageField() {
        return pageField;
    }

    public void setPageField(String pageField) {
        this.pageField = pageField;
    }

    public String getSizeField() {
        return sizeField;
    }

    public void setSizeField(String sizeField) {
        this.sizeField = sizeField;
    }

    public List<ParamBean> getHeaders() {
        return headers;
    }

    public void setHeaders(List<ParamBean> headers) {
        this.headers = headers;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public List<ParamBean> getData() {
        return data;
    }

    public void setData(List<ParamBean> data) {
        this.data = data;
    }

    public List<BindDataBean> getBindData() {
        return bindData;
    }

    public void setBindData(List<BindDataBean> bindData) {
        this.bindData = bindData;
    }

}