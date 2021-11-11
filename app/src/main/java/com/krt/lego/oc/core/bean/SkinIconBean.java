package com.krt.lego.oc.core.bean;

/**
 * author: MaGua
 * create on:2021/1/20 14:14
 * description
 */
public class SkinIconBean {
    private String x;
    private String y;
    private String code;
    private String width;
    private String height;

    public String getX() {
        return x;
    }

    public void setX(String x) {
        this.x = x;
    }

    public String getY() {
        return y;
    }

    public void setY(String y) {
        this.y = y;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getFileName() {
        return width + "_" + height + "_" + x + "_" + y;
    }
}
