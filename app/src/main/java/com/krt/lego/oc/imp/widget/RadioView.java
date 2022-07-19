package com.krt.lego.oc.imp.widget;

import android.graphics.Color;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.krt.base.util.MUtil;
import com.krt.lego.R;
import com.krt.lego.oc.core.bean.BaseLayoutBean;
import com.krt.lego.oc.core.bean.ListBean;
import com.krt.lego.oc.core.bean.SkinIconBean;
import com.krt.lego.oc.core.important.SkinManager;
import com.krt.lego.oc.core.surface.BaseWidget;
import com.krt.lego.oc.core.surface.Subgrade;
import com.krt.lego.oc.util.CropUtil;

import java.util.List;

/**
 * @author: MaGua
 * @create_on:2021/11/10 15:41
 * @description 单选按钮组件
 */
public class RadioView extends BaseWidget<FrameLayout> {

    private MyAdapter myAdapter;
    private RecyclerView recycler;

    public RadioView(Subgrade imp, BaseLayoutBean obj, boolean isListChild) {
        super(imp, obj, isListChild);
    }

    @Override
    protected void createView() {
        view = new FrameLayout(subgrade.getCarrier());
        recycler = new RecyclerView(subgrade.getCarrier());
        LinearLayoutManager manager = new LinearLayoutManager(subgrade.getCarrier());
        manager.setOrientation(getBooleanVal("wrap") ? LinearLayoutManager.VERTICAL : LinearLayoutManager.HORIZONTAL);
        recycler.setLayoutManager(manager);
        List<ListBean> list = JSONObject.parseArray(getStringVal("list"), ListBean.class);
        if (list.size() > 0) {
            boolean isVal = false;
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getCode().equals(getStringVal("val"))) {
                    isVal = true;
                    list.get(i).setRadioFlag(true);
                }
            }
            if (!isVal) {
                changeStyle("val", list.get(0).getCode());
                list.get(0).setRadioFlag(true);
            }
            myAdapter = new MyAdapter(list);
            recycler.setAdapter(myAdapter);
            myAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    for (int i = 0; i < myAdapter.getData().size(); i++) {
                        if (myAdapter.getData().get(i).isRadioFlag() && i != position) {
                            myAdapter.getData().get(i).setRadioFlag(false);
                            myAdapter.notifyItemChanged(i);
                        }
                    }
                    changeStyle("val", myAdapter.getData().get(position).getCode());
                    myAdapter.getData().get(position).setRadioFlag(true);
                    myAdapter.notifyItemChanged(position);

                }
            });
        }
        view.addView(recycler);
    }

    @Override
    protected void initView() {

    }

    private class MyAdapter extends BaseQuickAdapter<ListBean, BaseViewHolder> {

        public MyAdapter(@Nullable List<ListBean> data) {
            super(R.layout.item_radio, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, ListBean item) {
            ImageView img = helper.getView(R.id.imgState);
            TextView text = helper.getView(R.id.text);
            text.setTextColor(MUtil.getRealColor(item.isRadioFlag() ?
                    getStringVal("activeTextColor") : getStringVal("inActiveTextColor")));
            text.setTextSize(TypedValue.COMPLEX_UNIT_PX, MUtil.getRealValue(getIntVal("textSize")));
            text.setText(item.getName());

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams
                    (getIntVal("iconWidth"), getIntVal("iconHeight"));
            params.gravity = Gravity.CENTER_VERTICAL;
            img.setLayoutParams(params);

            //使用皮肤文件
            if (getBooleanVal("useSkin")) {
                SkinIconBean ico = SkinManager.getPosition(item.isRadioFlag() ?
                        getStringVal("activeSkin") : getStringVal("inActiveSkin"));
                CropUtil.getInstance().cropImg(subgrade.getCarrier(), SkinManager.defaultFile,
                        ico.getFileName(), bitmap -> img.setImageBitmap(bitmap));
                return;
            }

            //使用图片
            if (getBooleanVal("usePic")) {
                Glide.with(subgrade.getCarrier())
                        .load(item.isRadioFlag() ?
                                getStringVal("activePic") : getStringVal("inActivePic"))
                        .into(img);
                return;
            }

            //使用圆角图片
            if (getStringVal("shape").equals("circle")) {
                img.setImageDrawable(subgrade.getCarrier().getDrawable(item.isRadioFlag() ?
                        R.mipmap.unsingle_circle : R.mipmap.single_circle));

            } else {
                img.setImageDrawable(subgrade.getCarrier().getDrawable(item.isRadioFlag() ?
                        R.mipmap.single_squre : R.mipmap.unsingle_squre));

            }


        }
    }
}
