package com.krt.lego.oc.imp.custom.bottombar;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.krt.Lego;
import com.krt.base.util.MGlideUtil;
import com.krt.base.util.MUtil;
import com.krt.base.util.ParseJsonUtil;
import com.krt.lego.Constants;
import com.krt.lego.R;
import com.krt.lego.config.LegoConfig;
import com.krt.lego.oc.core.bean.BottomBean;
import com.krt.lego.oc.core.bean.SkinIconBean;
import com.krt.lego.oc.core.important.SkinManager;
import com.krt.lego.oc.util.CropUtil;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.CommonPagerTitleView;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;


/**
 * author:Marcus
 * create on:2019/3/26 11:02
 * description
 */
public class BottomLayout extends LinearLayout {

    private Context context;

    private SViewPager viewPager;

    private MagicIndicator indicator;

    private BottomAdapter bottomAdapter;

    private List<BottomBean.CommonBean.LinksBean> list, allList;

    private List<Fragment> fragments = new ArrayList<>();

    private BottomBean bottomBean;

    private String name;

    private int selectPos;

    private int unPageCount;

    CommonNavigator commonNavigator;

    private InstanceFragmentImp imp;

    public BottomLayout(Context context, String name,
                        InstanceFragmentImp instanceFragmentImp) {
        this(context, null, name, instanceFragmentImp);
    }

    public BottomLayout(Context context, @Nullable AttributeSet attrs, String name,
                        InstanceFragmentImp instanceFragmentImp) {
        super(context, attrs);
        this.context = context;
        this.setOrientation(VERTICAL);
        this.name = name;
        this.imp = instanceFragmentImp;
        init();
    }

    /**
     * 初始化视图
     */
    private void init() {
        File resFile = new File(Constants.jsonPath + Lego.getVersionInfo().getAppInfo().getStartPageId() + ".json");
        if (!resFile.exists()) {
            Log.w(Lego.TAG, "app底部菜单不存在:" + resFile.getAbsolutePath());
            return;
        }
        String json = MUtil.getJson(resFile);
        if (TextUtils.isEmpty(json)) {
            Log.w(Lego.TAG, "app底部菜单配置有误");
            return;
        }
        bottomBean = ParseJsonUtil.getBean(json, BottomBean.class);
        allList = bottomBean.getCommon().getLinks();

        try {
            initFragment();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        addViewPager();
        addIndicator();

        initIndicator();
    }


    /**
     * 初始化fragment
     */
    private void initFragment() throws InstantiationException, IllegalAccessException {

        /**
         * 过滤一遍属于本端的菜单
         */
        list = new ArrayList<>();
        for (int i = 0; i < allList.size(); i++) {
            if (allList.get(i).getTerminal() == null) {
//                list.add(allList.get(i));
                continue;
            }
            if (allList.get(i).getTerminal().size() == 0) {
//                list.add(allList.get(i));
                continue;
            }

            if (allList.get(i).isIfOpenPage()) {
                unPageCount++;
            }

            if (allList.get(i).getTerminal().contains(String.valueOf(Lego.getLegoInfo().terminal))) {
                list.add(allList.get(i));
            }
        }

        /**
         * 开始添加fragment
         */
        if (fragments.isEmpty()) {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).isIfOpenPage()) {
                    continue;
                }

                if (list.get(i).isIfSelect()) selectPos = i;
                Fragment fragment = null;
                //源生界面2
                if (list.get(i).isIfModulePage()) {
//                    Class.forName()
                    try {
                        fragment = LegoConfig.getFragmentClazz().newInstance().setJsonFile(list.get(i).getPageId());
                    } catch (Exception e) {
                        e.printStackTrace();
                        fragment  = imp.instanceFragment(i, list.get(i).getPageId());
                    }

//                    fragment = LegoConfig.getFragmentClazz().newInstance().setJsonFile(list.get(i).getPageId());
                } else {
                    if (list.get(i).isIfH5()) {
                        fragment = (Fragment) LegoConfig.getWebFragmentClazz().newInstance().setUrl(list.get(i).getUrl());
                    } else {
                        fragment = imp.instanceFragment(i, list.get(i).getUrl());
                    }
                }
                fragments.add(fragment);
            }
        }
    }

    /**
     * 添加viewpager
     */
    private void addViewPager() {
        viewPager = new SViewPager(context);
        viewPager.setId(View.generateViewId());
        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, 0, 1);
        viewPager.setLayoutParams(lp);
        viewPager.setCanScroll(false);
        viewPager.setOffscreenPageLimit(1);
        viewPager.setBackgroundColor(Color.BLUE);
        addView(viewPager);
    }

    /**
     * 添加指示器
     */
    private void addIndicator() {
        indicator = new MagicIndicator(context);
        LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        indicator.setLayoutParams(lp);
        addView(indicator);
    }

    /**
     * 初始化指示器
     */
    private void initIndicator() {
        commonNavigator = new CommonNavigator(context);
        commonNavigator.setAdjustMode(true);
        bottomAdapter = new BottomAdapter();
        commonNavigator.setAdapter(bottomAdapter);
        indicator.setNavigator(commonNavigator);
        viewPager.setAdapter(new PagerAdapter(((FragmentActivity) context).getSupportFragmentManager()));
    }

    public void setCurrentPage(int index) {
        viewPager.setCurrentItem(index);
    }

    public List<? extends Fragment> getFragments() {
        return fragments;
    }

    private class BottomAdapter extends CommonNavigatorAdapter {

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public IPagerTitleView getTitleView(final Context context, final int index) {
            CommonPagerTitleView commonPagerTitleView = new CommonPagerTitleView(context);
            View customLayout;
            final ImageView titleImg;
            final TextView titleText;
//            if (list.get(index).getType().equals("2")) {
//                customLayout = LayoutInflater.from(context).inflate(R.layout.layout_bottom2, null);
//                titleImg = customLayout.findViewById(R.id.img);
//                titleText = null;
//            } else {
                customLayout = LayoutInflater.from(context).inflate(R.layout.layout_bottom, null);
                titleImg = customLayout.findViewById(R.id.img);
                titleText = customLayout.findViewById(R.id.title);
                titleText.setText(list.get(index).getLabel());
//            }
            MGlideUtil.load(context, list.get(index).getOriginImg(), titleImg);
            commonPagerTitleView.setContentView(customLayout);
            commonPagerTitleView.setOnPagerTitleChangeListener(new CommonPagerTitleView.OnPagerTitleChangeListener() {

                @Override
                public void onSelected(int index, int totalCount) {
                    if (list.get(index).isSkinIcon()) {
                        SkinIconBean ico = SkinManager.getPosition(list.get(index).getSelectSkin());
                        CropUtil.getInstance().cropImg(context, SkinManager.defaultFile,
                                ico.getFileName(), bitmap -> titleImg.setImageBitmap(bitmap));

                    } else {
                        MGlideUtil.load(context, list.get(index).getSelectImgUrl(), titleImg);
                    }
                    if (titleText != null)
                        titleText.setTextColor(MUtil.getRealColor(bottomBean.getCommon().getHighlightTextColor()));
                }

                @Override
                public void onDeselected(int index, int totalCount) {
                    if (list.get(index).isSkinIcon()) {
                        SkinIconBean ico = SkinManager.getPosition(list.get(index).getOriginSkin());
                        CropUtil.getInstance().cropImg(context, SkinManager.defaultFile,
                                ico.getFileName(), bitmap -> titleImg.setImageBitmap(bitmap));
                    } else {
                        MGlideUtil.load(context, list.get(index).getImgUrl(), titleImg);
                    }

                    if (titleText != null)
                        titleText.setTextColor(MUtil.getRealColor(bottomBean.getCommon().getOriginTextColor()));
                }

                @Override
                public void onLeave(int index, int totalCount, float leavePercent, boolean leftToRight) {
                    titleImg.setScaleX(1.5f + (0.8f - 1.3f) * leavePercent);
                    titleImg.setScaleY(1.5f + (0.8f - 1.3f) * leavePercent);
                }

                @Override
                public void onEnter(int index, int totalCount, float enterPercent, boolean leftToRight) {
                    titleImg.setScaleX(0.7f + (1.3f - 0.9f) * enterPercent);
                    titleImg.setScaleY(0.7f + (1.3f - 0.9f) * enterPercent);
                }
            });

            commonPagerTitleView.setOnClickListener(v -> {
                int i = index;
                if (list.get(index).isIfOpenPage()) {
                    //跳转
                    context.startActivity(
                            new Intent(context, LegoConfig.getActivityClazz())
                                    .putExtra("name", list.get(index).getPageId()));
                    return;
                }

                while (list.get(i).isIfOpenPage() && i != 0) {
                    i--;
                }
                viewPager.setCurrentItem(i);
                indicator.onPageSelected(i);
                selectPos = i;
            });
            return commonPagerTitleView;
        }

        @Override
        public IPagerIndicator getIndicator(Context context) {
            return null;
        }
    }

    private class PagerAdapter extends FragmentStatePagerAdapter {

        public PagerAdapter(FragmentManager fm) {
            super(fm);
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

    public Fragment getCurrentFragment() {
        return fragments.get(viewPager.getCurrentItem());
    }

    public interface InstanceFragmentImp {
        /**
         * @param idx
         * @param url
         * @return
         */
        Fragment instanceFragment(int idx, String url);
    }

    public void onResume() {

        for (int i = 0; i < fragments.size(); i++) {
            if (i != viewPager.getCurrentItem())
                commonNavigator.onDeselected(i, fragments.size());
        }
    }

}
