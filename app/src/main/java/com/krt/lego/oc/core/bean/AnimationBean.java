package com.krt.lego.oc.core.bean;

/**
 * @author hyj
 * @time 2020/9/5 9:53
 * @class describe
 */
public class AnimationBean {

    /**
     * scaleY : 1
     * animation : fadeIn
     * damping : 1
     * force : 1
     * x : 0
     * scaleX : 1
     * autostart : false
     * rotate : 0
     * curve : ease-in
     * delay : 0
     * opacity : 1
     * velocity : 0
     * autohide : false
     * isHidden : false
     * repeatCount : 0
     * duration : 0.1
     * y : 0
     * tagId : 1258
     * isAnimate : false
     * sort : 0
     */

    private int scaleY;
    private String animation = "";
    private int damping;
    private int force;
    private int x;
    private int scaleX;
    private boolean autostart;
    private int rotate;
    private String curve;
    private int delay;
    private int opacity;
    private int velocity;
    private boolean autohide;
    private boolean isHidden;
    private int repeatCount;
    private double duration;
    private int y;
    private int tagId;
    private boolean isAnimate;
    private int sort;

    public int getScaleY() {
        return scaleY;
    }

    public void setScaleY(int scaleY) {
        this.scaleY = scaleY;
    }

    public String getAnimation() {
        return animation;
    }

    public void setAnimation(String animation) {
        this.animation = animation;
    }

    public int getDamping() {
        return damping;
    }

    public void setDamping(int damping) {
        this.damping = damping;
    }

    public int getForce() {
        return force;
    }

    public void setForce(int force) {
        this.force = force;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getScaleX() {
        return scaleX;
    }

    public void setScaleX(int scaleX) {
        this.scaleX = scaleX;
    }

    public boolean isAutostart() {
        return autostart;
    }

    public void setAutostart(boolean autostart) {
        this.autostart = autostart;
    }

    public int getRotate() {
        return rotate;
    }

    public void setRotate(int rotate) {
        this.rotate = rotate;
    }

    public String getCurve() {
        return curve;
    }

    public void setCurve(String curve) {
        this.curve = curve;
    }

    public int getDelay() {
        return delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    public int getOpacity() {
        return opacity;
    }

    public void setOpacity(int opacity) {
        this.opacity = opacity;
    }

    public int getVelocity() {
        return velocity;
    }

    public void setVelocity(int velocity) {
        this.velocity = velocity;
    }

    public boolean isAutohide() {
        return autohide;
    }

    public void setAutohide(boolean autohide) {
        this.autohide = autohide;
    }

    public boolean isIsHidden() {
        return isHidden;
    }

    public void setIsHidden(boolean isHidden) {
        this.isHidden = isHidden;
    }

    public int getRepeatCount() {
        return repeatCount;
    }

    public void setRepeatCount(int repeatCount) {
        this.repeatCount = repeatCount;
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getTagId() {
        return tagId;
    }

    public void setTagId(int tagId) {
        this.tagId = tagId;
    }

    public boolean isIsAnimate() {
        return isAnimate;
    }

    public void setIsAnimate(boolean isAnimate) {
        this.isAnimate = isAnimate;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }
}
