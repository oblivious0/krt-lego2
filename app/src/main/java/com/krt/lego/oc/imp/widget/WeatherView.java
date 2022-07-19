package com.krt.lego.oc.imp.widget;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;

import androidx.fragment.app.FragmentActivity;

import com.krt.base.net.callback.JsonCallback;
import com.krt.base.util.MPermissions;
import com.krt.base.util.MToast;
import com.krt.base.util.ParseJsonUtil;
import com.krt.lego.oc.core.bean.BaseLayoutBean;
import com.krt.lego.oc.core.surface.BaseWidget;
import com.krt.lego.oc.core.surface.Subgrade;
import com.krt.lego.oc.core.tools.ModuleViewFactory;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: MaGua
 * @create_on:2021/11/10 10:28
 * @description 天气组件
 */
public class WeatherView extends BaseWidget<FrameLayout> {

    public List<BaseWidget> childViews;
    double lat;
    double lng;


    public WeatherView(Subgrade imp, BaseLayoutBean obj, boolean isListChild) {
        super(imp, obj, isListChild);
    }

    @Override
    protected void createView() {
        view = new FrameLayout(subgrade.getCarrier());
    }

    @Override
    protected void initView() {
        childViews = new ArrayList<>();
        view.setVisibility(getBooleanVal("isHidden") ? View.GONE : View.VISIBLE);
        if (bean.getChildren() != null && !bean.getChildren().isEmpty()) {
            ModuleViewFactory.createViews(bean.getChildren(), subgrade, view, childViews);
        }
    }

    @Override
    protected void bindInMainThread() {
        MPermissions.getInstance().request((FragmentActivity) subgrade.getCarrier(),
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_BACKGROUND_LOCATION}
                , value -> {
                    if (value) {
                        String serviceString = Context.LOCATION_SERVICE;
                        LocationManager locationManager =
                                (LocationManager) subgrade.getCarrier().getSystemService(serviceString);
                        String provider = LocationManager.GPS_PROVIDER;
                        @SuppressLint("MissingPermission")
                        Location location = locationManager.getLastKnownLocation(provider);
                        lat = location.getLatitude();//获取纬度
                        lng = location.getLongitude();//获取经度
                        showWeather("");
                    } else {
//                        MToast.showToast(subgrade.getSubgradeContext(), "请在设置配置GPS定位权限");
                    }
                });
    }

    public void showWeather(String code) {
        if (TextUtils.isEmpty(code)) {
            OkGo.get("https://restapi.amap.com/v3/geocode/regeo?key=4f360dc401a0b7245ade3616af366949&location=" + lng + "," + lat)
//                    .params("key", "2be33124f87ba1f30d7d7a9ea462ed56")
//                    .params("location", lat + "," + lng)
                    .execute(new JsonCallback<Object>() {
                        @Override
                        public void onSuc(Response<Object> response) {
                            String result = ParseJsonUtil.toJson(response.body());
                            if (!ParseJsonUtil.getStringByKey(result, "infocode").equals("10000")) {
                                MToast.showToast(subgrade.getCarrier(), "定位获取失败");
                                return;
                            }

                            String regeocode = ParseJsonUtil.getStringByKey(result, "regeocode");
                            String addressComponent = ParseJsonUtil.getStringByKey(regeocode, "addressComponent");
                            String adcode = ParseJsonUtil.getStringByKey(addressComponent, "adcode");
                            showWeather(adcode);

                        }
                    });
        } else {
            OkGo.get("https://restapi.amap.com/v3/weather/weatherInfo")
                    .params("city", code)
                    .params("key", "4f360dc401a0b7245ade3616af366949")
                    .execute(new JsonCallback<Object>() {
                        @Override
                        public void onSuc(Response<Object> response) {
                            String result = ParseJsonUtil.toJson(response.body());
                            if (!ParseJsonUtil.getStringByKey(result, "infocode").equals("10000")) {
                                MToast.showToast(subgrade.getCarrier(), "天气信息获取失败");
                                return;
                            }

                            String weather = ParseJsonUtil.toJson(
                                    ParseJsonUtil.getBeanList(
                                            ParseJsonUtil.getStringByKey(result, "lives"),
                                            Object.class).get(0));

                            bindData(getStringVal("cityNameId"), "city", ParseJsonUtil.getStringByKey(weather, "city"));
                            bindData(getStringVal("weatherTextId"), "weather", ParseJsonUtil.getStringByKey(weather, "weather"));
                            bindData(getStringVal("temperatureId"), "temperature", ParseJsonUtil.getStringByKey(weather, "temperature"));
                            bindData(getStringVal("iconId"), "weatherIco", ParseJsonUtil.getStringByKey(weather, "weather"));
                        }
                    });
        }

    }

    @Override
    public void bindData(String cid, String key, Object val) {
        if (childViews.size() != 0) {
            for (BaseWidget baseV : childViews) {
                switch (key) {
                    case "temperature":
                    case "city":
                    case "weather":
                        baseV.bindData(cid, "text", "temperature".equals(key) ? val + "°C" : val);
                        break;
                    case "weatherIco":
                        baseV.bindData(cid, "src", getWeatherIco(val.toString()));
                        break;
                    default:
                }
            }
        }
    }

    private String getWeatherIco(String weather) {
        switch (weather) {
            case "晴":
                return "http://www.krtimg.com/group1/M00/04/FD/rAA0Kl_z13KAI1J9AAAKkDQKnSg133.png";
            case "云":
                return "http://www.krtimg.com/group1/M00/05/28/rAA0KV_z13KAGszZAAAJTMx92NE395.png";
            case "晴云":
                return "http://www.krtimg.com/group1/M00/04/FD/rAA0Kl_z13KACyG3AAAKtdnwZi4352.png";
            case "阴":
                return "http://www.krtimg.com/group1/M00/04/FD/rAA0Kl_z12SAKjVwAAAKKhh3el4891.png";
            case "雨":
                return "http://www.krtimg.com/group1/M00/05/28/rAA0KV_z12SAQq0_AAAJ8CRIYq0166.png";
            case "雪":
                return "http://www.krtimg.com/group1/M00/05/28/rAA0KV_z12SAe77SAAAKsupO86c622.png";
        }

        return "";

    }
}
