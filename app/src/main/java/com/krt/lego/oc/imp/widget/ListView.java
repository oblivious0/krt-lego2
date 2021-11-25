package com.krt.lego.oc.imp.widget;

import android.graphics.drawable.GradientDrawable;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.krt.base.util.MUtil;
import com.krt.lego.R;
import com.krt.lego.oc.core.bean.AjaxBean;
import com.krt.lego.oc.core.bean.BaseLayoutBean;
import com.krt.lego.oc.core.surface.ListWidget;
import com.krt.lego.oc.core.surface.Subgrade;
import com.krt.lego.oc.core.tools.DataBinder;
import com.krt.lego.oc.core.tools.RequestBox;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author: MaGua
 * @create_on:2021/11/13 10:33
 * @description
 */
public class ListView extends ListWidget<RecyclerView> {

    private List<Object> list = new ArrayList<>();
    private Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    public ListView(Subgrade imp, BaseLayoutBean obj, boolean isListChild) {
        super(imp, obj, isListChild);
    }

    @Override
    public RecyclerView.Adapter getAdapter() {
        return adapter;
    }

    @Override
    protected void createView() {
        view = new RecyclerView(subgrade.getCarrier());
        if (getStringVal("direction").equals("horizontal")) {
            if (getBooleanVal("wrap")) {
                layoutManager = new GridLayoutManager(subgrade.getCarrier(), getIntVal("horizontalNum"));
            } else {
                layoutManager = new LinearLayoutManager(subgrade.getCarrier(), RecyclerView.HORIZONTAL, false);
            }
        } else {
            layoutManager = new LinearLayoutManager(subgrade.getCarrier());
        }
        view.setLayoutManager(layoutManager);
        adapter = new Adapter();
        view.setAdapter(adapter);
    }

    @Override
    protected void initView() {
        list = Optional.ofNullable(DataBinder.getDatas(bean)).orElse(new ArrayList<>());
        if (list.isEmpty()) {
            //ajax数据
            AjaxBean ajax = Optional.ofNullable(Optional.ofNullable(bean.getAjax()).get().get(0)).orElse(null);
            if (ajax != null) {
                RequestBox box = new RequestBox(ajax, cid);
                subgrade.getDesigner().requestBoxes.put(ajax.getCid(), box);
                box.okGo(subgrade);
            }
        } else {
            //静态数据
            adapter.setNewData(list);
        }
    }

    private class Adapter extends BaseQuickAdapter<Object, BaseViewHolder> {

        public Adapter() {
            super(R.layout.item_frame, list);
        }

        @Override
        protected void convert(BaseViewHolder helper, Object item) {
            FrameLayout frameLayout = (FrameLayout) helper.itemView;
            FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(getIntVal("itemW"), getIntVal("itemH"));
            lp.setMargins(getIntVal("xPadding"), getIntVal("yPadding"), 0, 0);
            frameLayout.setLayoutParams(lp);
            GradientDrawable drawable = MUtil.getBgDrawable(getStringVal("bgColor"),
                    GradientDrawable.RECTANGLE, getIntVal("borderRadius"), getIntVal("borderWidth"),
                    getStringVal("borderColor"));
            frameLayout.setBackgroundDrawable(drawable);
            try {
                AjaxBean ajax = bean.getAjax().get(0);
                DataBinder.bindMutiListData(subgrade, frameLayout, bean.getChildren(), item, ajax);
            } catch (Exception e) {
                DataBinder.bindMutiListData(subgrade, frameLayout, bean.getChildren(), item, bean.getStaticData());
            }
        }
    }
}
