package com.krt.lego.oc.imp.widget.basics;

import android.text.InputType;
import android.view.Gravity;
import android.widget.EditText;

import com.krt.lego.oc.core.bean.BaseLayoutBean;
import com.krt.lego.oc.core.surface.BaseWidget;
import com.krt.lego.oc.core.surface.Subgrade;

import java.util.Optional;

/**
 * @author: MaGua
 * @create_on:2021/10/9 11:37
 * @description 输入框组件
 */
public class InputView extends BaseWidget<EditText> {

    public InputView(Subgrade imp, BaseLayoutBean obj, boolean isListChild) {
        super(imp, obj, isListChild);
    }

    @Override
    public EditText getRawView() {
        return view;
    }

    @Override
    protected void createView() {
        view = new EditText(subgrade.getCarrier());
    }

    @Override
    protected void initView() {
        view.setBackground(null);
        view.setHint(Optional.ofNullable(getStringVal("placeholder")).orElse(""));
        view.setTextSize(16);

        switch (Optional.ofNullable(getStringVal("inputAlign")).orElse("")) {
            case "left":
                view.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
                break;
            case "right":
                view.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
                break;
            case "center":
                view.setGravity(Gravity.CENTER);
                break;
            default:
                view.setGravity(Gravity.CENTER_VERTICAL);
                break;
        }
        switch (Optional.ofNullable(getStringVal("type")).orElse("")){
            case "text":
                view.setInputType(InputType.TYPE_CLASS_TEXT);
                break;
            case "password":
                view.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                break;
        }
    }

}
