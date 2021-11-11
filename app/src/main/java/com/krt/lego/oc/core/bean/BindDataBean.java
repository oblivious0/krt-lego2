package com.krt.lego.oc.core.bean;

/**
 * @author hyj
 * @time 2020/9/5 9:49
 * @class describe
 */
public class BindDataBean {
    /**
     * originKey : label%krt%c4k8za48m5%krt_text
     * bindKey : data%krt_Array%krt_text
     */

    private String originKey;
    private String bindKey;
    private String bindType;

    public String getOriginKey() {
        return originKey;
    }


    public void setOriginKey(String originKey) {
        this.originKey = originKey;
    }

    public String[] getBindKeys() {
        return bindKey.split("%krt_");
    }

    public String getBindKey() {
        return bindKey;
    }

    public void setBindKey(String bindKey) {
        this.bindKey = bindKey;
    }

    public String[] getBindType() {
        return bindType.split("%krt_");
    }

    public void setBindType(String bindType) {
        this.bindType = bindType;
    }
}
