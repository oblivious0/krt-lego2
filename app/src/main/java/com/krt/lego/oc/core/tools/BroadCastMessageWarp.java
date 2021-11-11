package com.krt.lego.oc.core.tools;

import java.util.HashMap;

/**
 * @author hyj
 * @time 2020/9/8 14:54
 * @class describe
 */
public class BroadCastMessageWarp {

    private String broadCastName = "";
    private HashMap<String, String> params;

    public BroadCastMessageWarp(String broadCastName, HashMap<String, String> params) {
        this.broadCastName = broadCastName;
        this.params = params;
    }

    public String getBroadCastName() {
        return broadCastName;
    }

    public void setBroadCastName(String broadCastName) {
        this.broadCastName = broadCastName;
    }

    public HashMap<String, String> getParams() {
        return params;
    }

    public void setParams(HashMap<String, String> params) {
        this.params = params;
    }
}
