package com.krt.base.view;

import android.content.Context;
import android.util.TypedValue;

import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;

/**
 * @author: MaGua
 * @create_on:2021/10/27 14:29
 * @description
 */
public class ISimplePagerTitleView extends SimplePagerTitleView {

    protected float fontSize;
    protected float selectedFontSize;

    public float getFontSize() {
        return fontSize;
    }

    public void setFontSize(float fontSize) {
        this.fontSize = fontSize;
    }

    public float getSelectedFontSize() {
        return selectedFontSize;
    }

    public void setSelectedFontSize(float selectedFontSize) {
        this.selectedFontSize = selectedFontSize;
    }

    public ISimplePagerTitleView(Context context) {
        super(context);
    }

    @Override
    public void onSelected(int index, int totalCount) {
        super.onSelected(index, totalCount);
        setTextSize(TypedValue.COMPLEX_UNIT_PX, selectedFontSize);
    }

    @Override
    public void onDeselected(int index, int totalCount) {
        super.onDeselected(index, totalCount);
        setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSize);
    }


}
