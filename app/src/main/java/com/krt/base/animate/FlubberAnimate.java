package com.krt.base.animate;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;
import android.widget.ImageView;

import com.appolica.flubber.AnimationBody;
import com.appolica.flubber.Flubber;
import com.daimajia.androidanimations.library.YoYo;
import com.krt.lego.oc.core.bean.AnimationBean;


/**
 * @author Marcus
 * @package krt.wid.nohttp.db.animate
 * @description
 * @time 2018/8/7
 */
public class FlubberAnimate {

    public static void animate(AnimationBean bean, final ImageView img) {
        AnimatorSet animatorSet = new AnimatorSet();
        AnimationBody.Builder builder = common(bean);
        if(bean.getAnimation()==null)return;
        switch (bean.getAnimation()) {
            case "fadeInRight":
            case "slideRight"://飞向右边
                builder.animation(Flubber.AnimationPreset.FADE_IN_RIGHT);
                break;
            case "fadeInLeft":
            case "slideLeft":
                builder.animation(Flubber.AnimationPreset.FADE_IN_LEFT);
                break;
            case "zoomIn":
                builder.animation(Flubber.AnimationPreset.ZOOM_IN);
                break;
            case "fadeInUp":
            case "slideUp":
                builder.animation(Flubber.AnimationPreset.FADE_IN_UP);
                break;
            case "fadeIn":
                builder.animation(Flubber.AnimationPreset.FADE_IN);
                break;
            case "fadeInDown":
            case "slideDown":
                builder.animation(Flubber.AnimationPreset.FADE_IN_DOWN);
                break;
            case "flash":
                builder.animation(Flubber.AnimationPreset.FLASH);
                break;
            case "Round":
                builder.animation(Flubber.AnimationPreset.FADE_IN_LEFT);
                break;
            default:
                builder.animation(Flubber.AnimationPreset.FADE_IN_UP);
                break;
        }
        AnimatorSet.Builder aBuilder =
                animatorSet.play(builder.createFor(img));
        if (bean.getAnimation().equals("Round")) {
            aBuilder.before(builder.animation(new Flubber.AnimationProvider() {
                @Override
                public Animator createAnimationFor(AnimationBody animationBody, View view) {
                    return ObjectAnimator.ofFloat(view, "rotation", -360, 0);
                }
            }).duration(36 * 1000).repeatCount(YoYo.INFINITE).createFor(img));
        }
        aBuilder.with(getScaleXAnimate(bean, img));
        aBuilder.with(getScaleYAnimate(bean, img));
        animatorSet.start();
    }

    private static Animator getScaleXAnimate(AnimationBean bean, ImageView img) {
        return common(bean)
                .animation(Flubber.AnimationPreset.SCALE_X)
                .scaleX(bean.getScaleX(), 1f)
                .createFor(img);
    }

    private static Animator getScaleYAnimate(AnimationBean bean, ImageView img) {
        return common(bean)
                .animation(Flubber.AnimationPreset.SCALE_Y)
                .scaleY(bean.getScaleY(), 1f)
                .createFor(img);
    }


    private static AnimationBody.Builder common(AnimationBean bean) {
        return Flubber.with()
                .interpolator(Flubber.Curve.BZR_EASE_IN)
                .damping(bean.getDamping())
                .velocity(bean.getVelocity())
                .force(bean.getForce())
                .autoStart(bean.isAutostart())
                .duration(bean.getDuration() == 0 ? 8 * 100 : Math.round(bean.getDuration()) * 1000);
    }

}
