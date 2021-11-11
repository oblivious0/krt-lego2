package com.krt.lego.oc.core.bean;

import java.util.List;

/**
 * author: MaGua
 * create on:2021/4/21 9:02
 * description
 */
public class CallbackBean {
    private List<StateMentBean> stateMent;
    private List<String> events;

    public List<StateMentBean> getStateMent() {
        return stateMent;
    }

    public void setStateMent(List<StateMentBean> stateMent) {
        this.stateMent = stateMent;
    }

    public List<String> getEvents() {
        return events;
    }

    public void setEvents(List<String> events) {
        this.events = events;
    }
}
