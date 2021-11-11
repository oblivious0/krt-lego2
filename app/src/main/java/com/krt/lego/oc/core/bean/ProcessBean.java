package com.krt.lego.oc.core.bean;

import java.util.List;

/**
 * author: MaGua
 * create on:2021/4/17 15:12
 * description
 */
public class ProcessBean {

    /**
     * field : data%krt_Array%krt_gcId
     * type : map
     * table : [{"key":"1","val":"1"}]
     */

    private String field;
    private String type;
    private List<TableBean> table;
    private String str;

    public String getStr() {
        return str;
    }

    public void setStr(String str) {
        this.str = str;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<TableBean> getTable() {
        return table;
    }

    public void setTable(List<TableBean> table) {
        this.table = table;
    }

    public static class TableBean {
        /**
         * key : 1
         * val : 1
         */

        private String key;
        private String val;

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getVal() {
            return val;
        }

        public void setVal(String val) {
            this.val = val;
        }
    }
}
