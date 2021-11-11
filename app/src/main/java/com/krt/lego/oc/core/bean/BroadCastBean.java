package com.krt.lego.oc.core.bean;

import java.util.List;

/**
 * @author hyj
 * @time 2020/9/8 10:22
 * @class describe
 */
public class BroadCastBean {
    private String name;
    private List<String> events;
    private String cid;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getEvents() {
        return events;
    }

    public void setEvents(List<String> events) {
        this.events = events;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

}
