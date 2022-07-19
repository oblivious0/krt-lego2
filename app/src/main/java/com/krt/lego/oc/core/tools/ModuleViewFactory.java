package com.krt.lego.oc.core.tools;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.LogUtils;
import com.bumptech.glide.Glide;
import com.krt.base.util.MUtil;
import com.krt.base.util.ParseJsonUtil;
import com.krt.lego.R;
import com.krt.lego.oc.core.bean.BaseLayoutBean;
import com.krt.lego.oc.core.bean.FixedBean;
import com.krt.lego.oc.core.surface.BaseWidget;
import com.krt.lego.oc.core.surface.Subgrade;
import com.krt.lego.oc.imp.widget.ListView;
import com.krt.lego.oc.imp.widget.basics.ButtonView;
import com.krt.lego.oc.imp.widget.CircleProgressBarView;
import com.krt.lego.oc.imp.widget.CountdownView;
import com.krt.lego.oc.imp.widget.basics.DefaultView;
import com.krt.lego.oc.imp.widget.basics.DrawMapView;
import com.krt.lego.oc.imp.widget.basics.InputView;
import com.krt.lego.oc.imp.widget.basics.LabelView;
import com.krt.lego.oc.imp.widget.LayoutView;
import com.krt.lego.oc.imp.widget.LineProgressBarView;
import com.krt.lego.oc.imp.widget.basics.MVideoView;
import com.krt.lego.oc.imp.widget.NavbarView;
import com.krt.lego.oc.imp.widget.basics.PicView;
import com.krt.lego.oc.imp.widget.RadioView;
import com.krt.lego.oc.imp.widget.RatingBarView;
import com.krt.lego.oc.imp.widget.RichTextView;
import com.krt.lego.oc.imp.widget.basics.ScorllLabelView;
import com.krt.lego.oc.imp.widget.ScrollLayoutView;
import com.krt.lego.oc.imp.widget.TabTitleView;
import com.krt.lego.oc.imp.widget.TagView;
import com.krt.lego.oc.imp.widget.WeatherView;
import com.krt.lego.oc.util.TerminalUtil;
import com.krt.lego.oc.variable.JsonValue;
import com.krt.lego.oc.variable.ViewValue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


/**
 * @author hyj
 * @time 2020/8/20 9:16
 * @class describe 模块化组件工厂
 */
public class ModuleViewFactory {

    /**
     * 创建子组件
     *
     * @param list     子组件原型
     * @param subgrade 公环境
     * @param vg       父容器
     * @param views    需要导出的子组件集合（容器组件的输出参数）
     * @param isChild  是否属于列表子组件
     * @param tag      如果是列表子组件，则附加对应数据
     */
    public static void createViews(List<BaseLayoutBean> list, Subgrade subgrade,
                                   final ViewGroup vg, List<BaseWidget> views, boolean isChild, Object tag) {

        Collections.sort(list, (o1, o2) ->
                Integer.parseInt(ParseJsonUtil.getStringByKey(ParseJsonUtil.toJson(o1.getCommon()), "zIndex"))
                        - Integer.parseInt(ParseJsonUtil.getStringByKey(ParseJsonUtil.toJson(o2.getCommon()), "zIndex"))
        );

        for (BaseLayoutBean bean : list) {
            String type = ParseJsonUtil.getStringByKey(ParseJsonUtil.toJson(bean), JsonValue.TYPE);
            if (TextUtils.isEmpty(type)) {
                return;
            }
            if (isChild) bean.setTag(tag);
            BaseWidget baseView = null;
            switch (type) {
//                case ViewValue.LISTMENUS:
//                    baseView = new ListMenuView(subgrade, bean, isChild);
//                    break;
                case ViewValue.LAYOUT:
                    baseView = new LayoutView(subgrade, bean, isChild);
                    break;
                case ViewValue.PIC:
                    baseView = new PicView(subgrade, bean, isChild);
                    break;
                case ViewValue.LABEL:
                    baseView = new LabelView(subgrade, bean, isChild);
                    break;
                case ViewValue.LIST:
                    baseView = new ListView(subgrade, bean, isChild);
                    break;
                case ViewValue.BUTTON:
                    baseView = new ButtonView(subgrade, bean, isChild);
                    break;
                case ViewValue.NAVBAR:
                    baseView = new NavbarView(subgrade, bean, isChild);
                    break;
//                case ViewValue.BANNER:
//                    baseView = new BannerView(subgrade, bean, isChild);
//                    break;
                case ViewValue.TABTITLE:
                    baseView = new TabTitleView(subgrade, bean, isChild);
                    break;
                case ViewValue.COUNTDOWN:
                    baseView = new CountdownView(subgrade, bean, isChild);
                    break;
                case ViewValue.LINEPROGRESS:
                    baseView = new LineProgressBarView(subgrade, bean, isChild);
                    break;
                case ViewValue.INPUT:
                    baseView = new InputView(subgrade, bean, isChild);
                    break;
//                case ViewValue.SELECTLIST:
//                    baseView = new OptionListView(subgrade, bean, isChild);
//                    break;
                case ViewValue.SCROLLLABEL:
                    baseView = new ScorllLabelView(subgrade, bean, isChild);
                    break;
                case ViewValue.WEATHER:
                    baseView = new WeatherView(subgrade, bean, isChild);
                    break;
//                case ViewValue.WATERFALL:
//                    baseView = new WaterFallListView(subgrade, bean, isChild);
//                    break;
                case ViewValue.VIDEO:
                    baseView = new MVideoView(subgrade, bean, isChild);
                    break;
                case ViewValue.SCROLLVIEW:
                    baseView = new ScrollLayoutView(subgrade, bean, isChild);
                    break;
                case ViewValue.PICPREVIEW:
                    baseView = new DrawMapView(subgrade, bean, isChild);
                    break;
//                case ViewValue.MUITSTYLELIST:
//                    baseView = new MutiStyleListView(subgrade, bean, isChild);
//                    break;
                case ViewValue.RICHTEXT:
                    baseView = new RichTextView(subgrade, bean, isChild);
                    break;
                case ViewValue.ROUNDPROGRESS:
                    baseView = new CircleProgressBarView(subgrade, bean, isChild);
                    break;
                case ViewValue.TAGS:
                    baseView = new TagView(subgrade, bean, isChild);
                    break;
                case ViewValue.STARLEVEL:
                    baseView = new RatingBarView(subgrade, bean, isChild);
                    break;
                case ViewValue.RADIOVIEW:
                    baseView = new RadioView(subgrade, bean, isChild);
                    break;
                default:
                    if (baseView == null) {
                        baseView = new DefaultView(subgrade, bean, isChild);
                    }
                    break;
            }
            if (!isChild) {
                subgrade.getDesigner().widgets.put(baseView.cid, baseView);
            }
            if (baseView.getRawView() == null) {
                LogUtils.e(baseView.cid);
            } else {
                vg.addView(baseView.getRawView());
            }
            if (views != null) {
                views.add(baseView);
            }
        }
    }

    /**
     * 非列表子组件，参数见上个方法
     *
     * @param list
     * @param subgrade
     * @param vg
     * @param views
     */
    @SuppressLint("CheckResult")
    public static void createViews(List<BaseLayoutBean> list, Subgrade subgrade,
                                   final ViewGroup vg, List<BaseWidget> views) {
        createViews(list, subgrade, vg, views, false, null);
    }

    @SuppressLint("CheckResult")
    public static void assemble(final String json, final Subgrade sub) {
        if (TextUtils.isEmpty(json)) {
            return;
        }

        final String img = Optional.ofNullable(ParseJsonUtil.getStringByKey(json, "bgImg")).orElse("");
        final String bgMode = ParseJsonUtil.getStringByKey(json, "bgMode");

        ImageView bg = sub.getSurfaceView("BG");
        sub.findViewById(R.id.page).setBackgroundColor(MUtil.getRealColor(ParseJsonUtil.getStringByKey(json, "bgColor")));
        if (!img.isEmpty()) {
            try {
                if ("tile".equals(bgMode)) {
                    Observable.just(new Object())
                            .subscribeOn(Schedulers.io())
                            .observeOn(Schedulers.newThread())
                            .map(baseLayoutBean -> Glide.with(sub.getCarrier())
                                    .asBitmap()
                                    .load(img)
                                    .submit()
                                    .get()
                            )
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(bitmap -> {
                                bg.setImageBitmap(
                                        createRepeater(bg.getWidth(),
                                                bg.getHeight(), bitmap)
                                );
                            });
                } else {
                    Glide.with(sub.getCarrier())
                            .load(img)
                            .into(bg);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        List<BaseLayoutBean> nav = getContentData(json, JsonValue.NAVBAR);
        ViewGroup headerView = sub.getSurfaceView("NAV");
        if (nav == null || nav.isEmpty()) {
        } else {
            List<BaseWidget> views = new ArrayList<>();
            ModuleViewFactory.createViews(nav, sub, headerView, views);
        }

        List<BaseLayoutBean> list = getContentData(json, JsonValue.CONTENT);
        String fixedStr = ParseJsonUtil.getStringByKey(json, "fixed");
        FixedBean fixed = ParseJsonUtil.getBean(fixedStr, FixedBean.class);
        if (fixed != null) {
            String leftW = ParseJsonUtil.getStringByKey(json, "leftW"),
                    rightW = ParseJsonUtil.getStringByKey(json, "rightW"),
                    topH = ParseJsonUtil.getStringByKey(json, "topH"),
                    botH = ParseJsonUtil.getStringByKey(json, "botH");

            fixed.setLeftW(TextUtils.isEmpty(leftW) ? 0 : Integer.parseInt(leftW));
            fixed.setRightW(TextUtils.isEmpty(rightW) ? 0 : Integer.parseInt(rightW));
            fixed.setTopH(TextUtils.isEmpty(topH) ? 0 : Integer.parseInt(topH));
            fixed.setBotH(TextUtils.isEmpty(botH) ? 0 : Integer.parseInt(botH));

            loadFixed(fixed, sub);
        }

        //全浮动布局
        String floatJson = ParseJsonUtil.getStringByKey(json, "float");
        if (!TextUtils.isEmpty(floatJson)) {
            List<BaseLayoutBean> floats = ParseJsonUtil.getBeanList(floatJson, BaseLayoutBean.class);
            if (floats != null) {
                ModuleViewFactory.createViews(floats, sub,
                        sub.getSurfaceView("FLOAT"), null);
            }
        }


        final FrameLayout contentView = sub.getSurfaceView("COMMON");
        switch (sub.getDesigner().pageType) {
            case "list": {
                //列表页
                //列表页仅有两个子节点，列表头和列表，筛选出列表，其余组件作为列表头
                List<BaseLayoutBean> listview = new ArrayList<>();
                List<BaseLayoutBean> content = new ArrayList<>();
                int list_index = -1;

                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).getType().equals(ViewValue.LIST)
                            || list.get(i).getType().equals(ViewValue.WATERFALL)
                            || list.get(i).getType().equals(ViewValue.MUITSTYLELIST)) {

                        JSONObject commonObj = JSON.parseObject(list.get(i).getCommon().toString());
                        JSONArray commonArr = commonObj.getJSONArray("common");
                        if (list_index == -1 && TerminalUtil.isAndroid(commonArr)) {
                            listview.add(list.get(i));
                            list_index = i;
                        }
                    } else {
                        content.add(list.get(i));
                    }
                }

                List<BaseWidget> views = new ArrayList<>();
                if (list_index == -1) {
                    //列表页不放列表？那就当普通页处理吧
                    ModuleViewFactory.createViews(content, sub, contentView, views, false, null);
                } else {
                    //创建列表
                    ModuleViewFactory.createViews(listview, sub, contentView, views, false, null);
                    if (views.size() == 0) {
                        //创建列表出现了问题
                        return;
                    }
                    BaseWidget recyclerViewBaseView = views.get(0);
                    //创建列表头
                    FrameLayout listHeaderView = new FrameLayout(sub.getCarrier());
                    FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams
                            (FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
                    listHeaderView.setLayoutParams(lp);
                    List<BaseWidget> headerViews = new ArrayList<>();
                    ModuleViewFactory.createViews(content, sub, listHeaderView, headerViews, false, null);
//                    try {
//                        ((BaseQuickAdapter)
//                                ((MRecyclerView) recyclerViewBaseView.view).getAdapter())
//                                .addHeaderView(listHeaderView);
//                    } catch (Exception e) {
//                    }
                }
                break;
            }
            default: {
                //默认普通页
                List<BaseWidget> views = new ArrayList<>();
                ModuleViewFactory.createViews(list, sub, contentView, views, false, null);
                break;
            }
        }

    }

    private static void loadFixed(FixedBean fixed, Subgrade sub) {
        //左边浮动布局
        if (fixed.getLeft() != null) {
            FrameLayout left = sub.getSurfaceView("L");
            ViewGroup.LayoutParams leftParams = left.getLayoutParams();
            leftParams.width = fixed.getLeftW();
            left.setLayoutParams(leftParams);
            if (fixed.getLeft().size() != 0) {
                ModuleViewFactory.createViews(fixed.getLeft(), sub, left, null);
            }
        }
        //右边浮动布局
        if (fixed.getRight() != null) {
            FrameLayout right = sub.getSurfaceView("R");
            ViewGroup.LayoutParams rightParams = right.getLayoutParams();
            rightParams.width = fixed.getRightW();
            right.setLayoutParams(rightParams);
            if (fixed.getRight().size() != 0) {
                ModuleViewFactory.createViews(fixed.getRight(), sub, right, null);
            }
        }
        //上边浮动布局
        if (fixed.getTop() != null) {
            FrameLayout top = sub.getSurfaceView("T");
            ViewGroup.LayoutParams topParams = top.getLayoutParams();
            topParams.height = fixed.getTopH();
            top.setLayoutParams(topParams);
            if (fixed.getTop().size() != 0) {
                ModuleViewFactory.createViews(fixed.getTop(), sub, top, null);
            }
        }
        //下边浮动布局
        if (fixed.getBottom() != null) {
            FrameLayout bottom = sub.getSurfaceView("B");
            ViewGroup.LayoutParams botParams = bottom.getLayoutParams();
            botParams.height = fixed.getBotH();
            bottom.setLayoutParams(botParams);
            if (fixed.getBottom().size() != 0) {
                ModuleViewFactory.createViews(fixed.getBottom(), sub, bottom, null);
            }
        }
    }

    private static List<BaseLayoutBean> getContentData(String json, String key) {
        String content = ParseJsonUtil.getStringByKey(json, key);
        if (TextUtils.isEmpty(content)) {
            return null;
        } else {
            return ParseJsonUtil.getBeanList(content, BaseLayoutBean.class);
        }
    }

    public static Bitmap createRepeater(int width, int height, Bitmap src) {

        int count = (width + src.getWidth() - 1) / src.getWidth();
        int count1 = (height + src.getHeight() - 1) / src.getHeight();
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        for (int hidx = 0; hidx < count1; ++hidx) {
            for (int idx = 0; idx < count; ++idx) {
                canvas.drawBitmap(src, idx * src.getWidth(), hidx * src.getHeight(), null);
            }
        }
        return bitmap;
    }
}
