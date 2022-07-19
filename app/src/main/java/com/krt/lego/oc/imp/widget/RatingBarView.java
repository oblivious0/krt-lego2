package com.krt.lego.oc.imp.widget;

import android.util.Log;

import com.krt.lego.R;
import com.krt.lego.oc.core.bean.BaseLayoutBean;
import com.krt.lego.oc.core.surface.BaseWidget;
import com.krt.lego.oc.core.surface.Subgrade;
import com.willy.ratingbar.BaseRatingBar;
import com.willy.ratingbar.ScaleRatingBar;

import static android.content.ContentValues.TAG;

/**
 * @author: MaGua
 * @create_on:2021/11/10 14:54
 * @description 评星组件
 */
public class RatingBarView extends BaseWidget<ScaleRatingBar> {

    final int TATE = 2;

    public RatingBarView(Subgrade imp, BaseLayoutBean obj, boolean isListChild) {
        super(imp, obj, isListChild);
    }

    @Override
    protected void createView() {
        view = new ScaleRatingBar(subgrade.getCarrier());
        view.setIsIndicator(true);
        view.setStarPadding(1);
        view.setStepSize(0.5f);
        view.setEmptyDrawableRes(R.drawable.empty);
        view.setFilledDrawableRes(R.drawable.filled);
    }

    @Override
    protected void initView() {
        view.setNumStars(getIntVal("maxlevel"));
        view.setMinimumStars(1);
        view.setStarWidth(getIntVal("iconsize") * TATE);
        view.setStarHeight(getIntVal("iconsize") * TATE);
        view.setRating(getIntVal("level"));
//        view.setOnRatingChangeListener(new BaseRatingBar.OnRatingChangeListener() {
//            @Override
//            public void onRatingChange(BaseRatingBar ratingBar, float rating, boolean fromUser) {
//                Log.e(TAG, "onRatingChange: " + rating);
//            }
//        });
    }

    @Override
    public void bindData(String cid, String key, Object val) {
        if (cid.equals(this.cid)) {
            try {
                switch (key) {
                    case "level":
                        view.setRating(Integer.parseInt(val.toString()));
                        break;
                    case "iconsize":
                        int starHeight = Integer.parseInt(val.toString()) * TATE;
                        view.setStarWidth(starHeight);
                        view.setStarHeight(starHeight);
                        break;
                    case "maxlevel":
                        view.setNumStars(Integer.parseInt(val.toString()));
                        break;
                    default:
                        break;
                }
            } catch (Exception e) {
            }
        }
    }
}
