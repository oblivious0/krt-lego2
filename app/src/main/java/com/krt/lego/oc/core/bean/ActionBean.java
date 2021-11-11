package com.krt.lego.oc.core.bean;

import java.util.List;

/**
 * @author hyj
 * @time 2020/9/5 10:42
 * @class describe
 */
public class ActionBean {

    private String name;
    private String target;
    private String[] targets;
    private List<Attr> attrList;
    private String type;

    public static class Attr{
        private String attr;
        private String target;
        private boolean vari;

        public boolean isVari() {
            return vari;
        }

        public void setVari(boolean vari) {
            this.vari = vari;
        }

        public String getAttr() {
            return attr;
        }

        public void setAttr(String attr) {
            this.attr = attr;
        }

        public String getTarget() {
            return target;
        }

        public void setTarget(String target) {
            this.target = target;
        }
    }

    public String[] getTargets() {
        return targets;
    }

    public void setTargets(String[] targets) {
        this.targets = targets;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public List<Attr> getAttrList() {
        return attrList;
    }

    public void setAttrList(List<Attr> attrList) {
        this.attrList = attrList;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
