package com.krt.lego.oc.core.bean;

import java.util.List;

/**
 * author: MaGua
 * create on:2021/4/17 15:11
 * description
 */
public class StyleLinkBean {

    /**
     * stateMent : [{"fields":["data%krt_Array%krt_gcId"],"type":"=","val":["932"],"valType":"common","mode":"2"},{"fields":["data%krt_Array%krt_insertTime"],"type":"<","val":["2021-04-21 00:00:00"],"valType":"date","mode":"1"}]
     * style : 52b054d8b92c2b3
     */

    private String style;
    private List<StateMentBean> stateMent;

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public List<StateMentBean> getStateMent() {
        return stateMent;
    }

    public void setStateMent(List<StateMentBean> stateMent) {
        this.stateMent = stateMent;
    }
}
