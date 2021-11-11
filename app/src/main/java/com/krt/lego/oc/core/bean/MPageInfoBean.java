package com.krt.lego.oc.core.bean;

/**
 * @author hyj
 * @time 2020/9/14 15:37
 * @class describe
 */
public class MPageInfoBean {

    private String id;
    private String insert_time;
    private String page_type_code;
    private String page_code;
    private String page_name;
    private String page_tag;
    private String page_config;
    private String page_config_md5;
    private String file_url;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInsert_time() {
        return insert_time;
    }

    public void setInsert_time(String insert_time) {
        this.insert_time = insert_time;
    }

    public String getPage_type_code() {
        return page_type_code;
    }

    public void setPage_type_code(String page_type_code) {
        this.page_type_code = page_type_code;
    }

    public String getPage_code() {
        return page_code;
    }

    public void setPage_code(String page_code) {
        this.page_code = page_code;
    }

    public String getPage_name() {
        return page_name;
    }

    public void setPage_name(String page_name) {
        this.page_name = page_name;
    }

    public String getPage_tag() {
        return page_tag;
    }

    public void setPage_tag(String page_tag) {
        this.page_tag = page_tag;
    }

    public String getPage_config() {
        return page_config;
    }

    public void setPage_config(String page_config) {
        this.page_config = page_config;
    }

    public String getPage_config_md5() {
        return page_config_md5;
    }

    public void setPage_config_md5(String page_config_md5) {
        this.page_config_md5 = page_config_md5;
    }

    public String getFile_url() {
        return file_url;
    }

    public void setFile_url(String file_url) {
        this.file_url = file_url;
    }

    public boolean equalsMd5(String md5){
        return page_config_md5.equals(md5);
    }
}
