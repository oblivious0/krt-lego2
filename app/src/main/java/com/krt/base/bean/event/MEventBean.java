package com.krt.base.bean.event;

/**
 * @author Marcus
 * @package krt.wid.bean
 * @description
 * @time 2018/5/31
 */
public class MEventBean {
    public int action;

    public Object result;

    public MEventBean(int action, Object result) {
        this.action = action;
        this.result = result;
    }

}
