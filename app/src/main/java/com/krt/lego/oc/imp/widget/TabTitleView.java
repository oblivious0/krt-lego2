package com.krt.lego.oc.imp.widget;

import android.content.Context;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.krt.Lego;
import com.krt.base.ui.TestFragment;
import com.krt.base.util.MGlideUtil;
import com.krt.base.util.MUtil;
import com.krt.base.view.ISimplePagerTitleView;
import com.krt.lego.config.LegoConfig;
import com.krt.lego.oc.core.bean.BaseLayoutBean;
import com.krt.lego.oc.core.surface.BaseWidget;
import com.krt.lego.oc.core.surface.Subgrade;
import com.krt.lego.oc.core.tools.FragmentFactory;
import com.krt.lego.oc.imp.custom.bottombar.SViewPager;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.UIUtil;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.CommonPagerTitleView;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author: MaGua
 * @create_on:2021/11/9 11:33
 * @description
 */
public class TabTitleView extends BaseWidget<LinearLayout> {

    private List<String> mTitles;
    private MagicIndicator magicIndicator;
    private SViewPager viewPager;

    public TabTitleView(Subgrade imp, BaseLayoutBean obj, boolean isListChild) {
        super(imp, obj, isListChild);
    }

    @Override
    protected void createView() {
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
                getIntVal("width"), getIntVal("height")
        );
        lp.setMargins(getIntVal("x"), getIntVal("y"), 0, 0);
        view = new LinearLayout(subgrade.getCarrier());
        view.setOrientation(LinearLayout.VERTICAL);
        view.setLayoutParams(lp);
    }

    @Override
    protected void initView() {
        LinearLayout.LayoutParams indicatorlp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                getIntVal("tabHeight") + getIntVal("indicatorHeight"));
        magicIndicator = new MagicIndicator(subgrade.getCarrier());
        if (getBooleanVal("isCenter")) {
            indicatorlp.gravity = Gravity.CENTER_HORIZONTAL;
        }
        magicIndicator.setLayoutParams(indicatorlp);
        viewPager = new SViewPager(subgrade.getCarrier());
        LinearLayout.LayoutParams viewpagerlp = new LinearLayout.LayoutParams
                (LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        viewPager.setCanScroll(false);
        viewPager.setId(View.generateViewId());
        viewPager.setOffscreenPageLimit(1);
        viewPager.setLayoutParams(viewpagerlp);
        PagerAdapter adapter = new PagerAdapter(((AppCompatActivity) subgrade.getCarrier()).getSupportFragmentManager());
        mTitles = new ArrayList<>();

        JSONArray links = JSONObject.parseArray(getStringVal("links"));

        for (int i = 0; i < links.size(); i++) {
            JSONObject JS = JSON.parseObject(links.get(i).toString());
            try {
                FragmentFactory factory = LegoConfig.getFragmentFactory().newInstance();
                Fragment fragment = null;
                switch (Optional.ofNullable(JS.getString("type")).orElse("")) {
                    case "module":
                        adapter.addFragment(factory.manufactureModule(
                                Optional.ofNullable(JS.getString("pageId")).orElse("")));
                        break;
                    case "origin":
                        adapter.addFragment(factory.manufactureOrigin(
                                Optional.ofNullable(JS.getString("url")).orElse("")));
                        break;
                    case "webView":
                        adapter.addFragment(factory.manufactureWeb(
                                Optional.ofNullable(JS.getString("url")).orElse("")));
                        break;
                    default:
                        adapter.addFragment(new TestFragment());
                }
                mTitles.add(JS.getString("text"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        CommonNavigator commonNavigator = new CommonNavigator(subgrade.getCarrier());
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return mTitles.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int i) {
                if (TextUtils.isEmpty(getStringVal("indicatorImg"))) {
                    ISimplePagerTitleView simplePagerTitleView = new ISimplePagerTitleView(context);
                    simplePagerTitleView.setPadding(0, 0, 0, 0);
                    if (!getBooleanVal("autoWidth"))
                        simplePagerTitleView.setWidth(getIntVal("tabItemWidth"));
                    simplePagerTitleView.setText(mTitles.get(i));
                    simplePagerTitleView.setFontSize(MUtil.getRealValue(getIntVal("textFont")));
                    simplePagerTitleView.setSelectedFontSize(MUtil.getRealValue(getIntVal("selectTextFont")));
                    simplePagerTitleView.setGravity(Gravity.CENTER);
                    simplePagerTitleView.setNormalColor(MUtil.getRealColor(getStringVal("textColor")));
                    simplePagerTitleView.setSelectedColor(MUtil.getRealColor(getStringVal("selectTextColor")));
                    simplePagerTitleView.setOnClickListener(v -> {
                        viewPager.setCurrentItem(i);
                    });
                    return simplePagerTitleView;
                } else {
                    return showIndicatorImg(i);
                }
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                if (TextUtils.isEmpty(getStringVal("indicatorImg"))) {
                    LinePagerIndicator indicator = new LinePagerIndicator(context);
                    indicator.setMode(LinePagerIndicator.MODE_WRAP_CONTENT);
                    indicator.setLineHeight(getIntVal("indicatorHeight"));
                    indicator.setLineWidth(UIUtil.dip2px(context, 50));
                    indicator.setRoundRadius(UIUtil.dip2px(context, 3));
                    indicator.setStartInterpolator(new AccelerateInterpolator());
                    indicator.setEndInterpolator(new DecelerateInterpolator(2.0f));
                    indicator.setColors(MUtil.getRealColor(getStringVal("indicatorColor")));
                    return indicator;
                } else {
                    return null;
                }
            }
        });
        commonNavigator.setAdjustMode(getBooleanVal("autoWidth"));
        magicIndicator.setNavigator(commonNavigator);
        viewPager.setAdapter(adapter);
        view.addView(magicIndicator);
        view.addView(viewPager);
        ViewPagerHelper.bind(magicIndicator, viewPager);
    }

    public CommonPagerTitleView showIndicatorImg(final int i) {
        CommonPagerTitleView commonPagerTitleView = new CommonPagerTitleView(subgrade.getCarrier());
        LinearLayout customLayout = new LinearLayout(subgrade.getCarrier());
        customLayout.setWeightSum(1);
        customLayout.setOrientation(LinearLayout.VERTICAL);
        customLayout.setGravity(Gravity.CENTER_HORIZONTAL);
        FrameLayout.LayoutParams lp1 = new FrameLayout.LayoutParams
                (ViewGroup.LayoutParams.WRAP_CONTENT, getIntVal("tabHeight") + getIntVal("indicatorHeight"));
        commonPagerTitleView.setLayoutParams(lp1);
        customLayout.setLayoutParams(new FrameLayout.LayoutParams
                (getIntVal("tabItemWidth"), getIntVal("tabHeight")));
        customLayout.setPadding(40, 0, 40, 0);
        final TextView name = new TextView(subgrade.getCarrier());
        name.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        customLayout.addView(name);
        final ImageView line = new ImageView(subgrade.getCarrier());
        customLayout.addView(line);

        name.setText(mTitles.get(i));
        name.setTextSize(TypedValue.COMPLEX_UNIT_PX, getIntVal("textFont"));
        line.setScaleType(ImageView.ScaleType.FIT_XY);
        MGlideUtil.load(subgrade.getCarrier(), getStringVal("indicatorImg"), line);
        commonPagerTitleView.setContentView(customLayout);
        commonPagerTitleView.setOnPagerTitleChangeListener(new CommonPagerTitleView.OnPagerTitleChangeListener() {
            @Override
            public void onSelected(int index, int totalCount) {

            }

            @Override
            public void onDeselected(int index, int totalCount) {

            }

            @Override
            public void onLeave(int index, int totalCount, float leavePercent, boolean leftToRight) {
                name.setTextColor(MUtil.getRealColor(getStringVal("textColor")));
                name.setTextSize(TypedValue.COMPLEX_UNIT_PX, getIntVal("textFont"));
                line.setVisibility(View.INVISIBLE);

            }

            @Override
            public void onEnter(int index, int totalCount, float enterPercent, boolean leftToRight) {
                name.setTextColor(MUtil.getRealColor(getStringVal("selectTextColor")));
                name.setTextSize(TypedValue.COMPLEX_UNIT_PX, getIntVal("selectTextFont"));
                line.setVisibility(View.VISIBLE);

            }
        });
        commonPagerTitleView.setOnClickListener(v -> viewPager.setCurrentItem(i));


        return commonPagerTitleView;
    }

    private class PagerAdapter extends FragmentStatePagerAdapter {

        private List<Fragment> fragments = new ArrayList<>();

        public PagerAdapter(FragmentManager fm) {
            super(fm, BEHAVIOR_SET_USER_VISIBLE_HINT);
        }

        public void addFragment(Fragment fragment) {
            fragments.add(fragment);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }


    }
}
