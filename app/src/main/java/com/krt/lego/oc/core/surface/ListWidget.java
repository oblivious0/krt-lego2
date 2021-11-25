package com.krt.lego.oc.core.surface;

import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.krt.lego.oc.core.bean.BaseLayoutBean;

/**
 * @author: MaGua
 * @create_on:2021/11/11 15:44
 * @description
 */
public abstract class ListWidget<T extends ViewGroup> extends BaseWidget<T> {

    public ListWidget(Subgrade imp, BaseLayoutBean obj, boolean isListChild) {
        super(imp, obj, isListChild);
    }

    /**
     * 获取列表类型组件的数据适配器
     * @return
     */
    public abstract RecyclerView.Adapter getAdapter();

}
