package com.krt.lego.oc.core.bean;

import java.util.List;

/**
 * author: MaGua
 * create on:2021/4/14 10:59
 * description
 */
public class FixedBean {

    private List<BaseLayoutBean> top;
    private List<BaseLayoutBean> bottom;
    private List<BaseLayoutBean> left;
    private List<BaseLayoutBean> right;

    private int leftW;
    private int rightW;
    private int botH;
    private int topH;

    public List<BaseLayoutBean> getTop() {
        return top;
    }

    public void setTop(List<BaseLayoutBean> top) {
        this.top = top;
    }

    public List<BaseLayoutBean> getBottom() {
        return bottom;
    }

    public void setBottom(List<BaseLayoutBean> bottom) {
        this.bottom = bottom;
    }

    public List<BaseLayoutBean> getLeft() {
        return left;
    }

    public void setLeft(List<BaseLayoutBean> left) {
        this.left = left;
    }

    public List<BaseLayoutBean> getRight() {
        return right;
    }

    public void setRight(List<BaseLayoutBean> right) {
        this.right = right;
    }

    public int getLeftW() {
        return leftW;
    }

    public void setLeftW(int leftW) {
        this.leftW = leftW;
    }

    public int getRightW() {
        return rightW;
    }

    public void setRightW(int rightW) {
        this.rightW = rightW;
    }

    public int getBotH() {
        return botH;
    }

    public void setBotH(int botH) {
        this.botH = botH;
    }

    public int getTopH() {
        return topH;
    }

    public void setTopH(int topH) {
        this.topH = topH;
    }
}
