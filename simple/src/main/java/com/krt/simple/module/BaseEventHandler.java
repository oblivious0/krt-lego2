package com.krt.simple.module;

import android.content.Context;
import android.content.Intent;

import com.blankj.utilcode.util.LogUtils;
import com.krt.base.util.MToast;
import com.krt.base.util.ParseJsonUtil;
import com.krt.lego.oc.core.bean.ParamBean;
import com.krt.lego.oc.core.execute.EventHandler;
import com.krt.lego.oc.core.surface.Subgrade;
import com.krt.lego.oc.core.tools.VariableFilter;
import com.krt.simple.ui.ZxingActivity;
import com.krt.simple.web.WebActivity;

import java.util.HashMap;
import java.util.List;

/**
 * author: MaGua
 * create on:2021/3/25 10:22
 * description
 */
public class BaseEventHandler extends EventHandler {


    @Override
    protected void gotoLogin(Context context) {
        MToast.showToast(context, "此处跳转原生登录页");
    }

    @Override
    protected void onClickListener(Subgrade subgrade, String type, HashMap<String,String> objects) {
        switch (type) {
            case "qrcode":
                subgrade.getCarrier().startActivity(new Intent(subgrade.getCarrier(), ZxingActivity.class));
                break;
            default:
                MToast.showToast(subgrade.getCarrier(), "此处交由原生实现界面或执行其他操作！");
                break;
        }
    }

    @Override
    protected void onStartWebActivity(Subgrade subgrade, String url, HashMap<String,String> objects) {
        Intent intent = new Intent(subgrade.getCarrier(), WebActivity.class)
                .putExtra("url", url);
        subgrade.getCarrier().startActivity(intent);
    }

    @Override
    protected void onStartModuleActivity(Subgrade subgrade, String jsonName, HashMap<String,String> objects) {
        Intent intent = new Intent(subgrade.getCarrier(), ModuleActivity.class)
                .putExtra("name", jsonName)
                .putExtra("param", ParseJsonUtil.toJson(VariableFilter.convert(objects)));
        subgrade.getCarrier().startActivity(intent);
    }

    @Override
    protected void onCallPhone(Subgrade subgrade, String number) {
        MToast.showToast(subgrade.getCarrier(), "拨打电话：" + number);
    }

    @Override
    protected String disposeUrl(String url, List<String> development) {
        if (development != null) {
            if (development.size() != 0) {
                if (development.contains("2")) {
                    String urlStr = url.replace("#", "?iftest#");
                    LogUtils.e(urlStr);
                    return urlStr;
                }
            }
        }
        return url;
    }

    @Override
    protected void jumpWx(Subgrade subgrade, String key, String path) {
        MToast.showToast(subgrade.getCarrier(),"此处跳转小程序（因此APP未申请sdk权限无法跳转）");
    }
}
