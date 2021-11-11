package krt.wid.bit;

import android.annotation.SuppressLint;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.EncodeUtils;
import com.blankj.utilcode.util.EncryptUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.AbsCallback;
import com.lzy.okgo.model.Response;

import java.util.Optional;
import java.util.UUID;

import krt.wid.bit.net.Collector;
import krt.wid.bit.net.Result;
import krt.wid.bit.net.callback.JsonCallback;

/**
 * @author: MaGua
 * @create_on:2021/8/26 9:28
 * @description
 */
public class Buried {

    private static int level = 0;
//    static final String http_url = "https://dev.krtservice.com/krt-data/api/dataopt/v1/actLog/save";
    static final String http_url = "https://prod.krtservice.com/krt-data/api/dataopt/v1/actLog/saveEncrypt";
    private static String uuid = "";
    private static Collector collector;

    private static String getUUID() {
        UUID uuid1 = UUID.randomUUID();
        uuid = uuid1.toString().replace("-", "");
        return uuid;
    }

    public static boolean initialize(Collector val) {
        collector = val;
        return collector != null;
    }

    @SuppressLint("NewApi")
    public static void bury(String dataJson) {
        if (collector == null) return;
        Point point = new Point();
        JSONObject jsonObject = Optional.ofNullable(JSONObject.parseObject(dataJson))
                .orElse(new JSONObject());

        point.setActCode(Optional.ofNullable(jsonObject.getString("actCode")).orElse(""));
        point.setThemeCode(Optional.ofNullable(jsonObject.getString("themeCode")).orElse(""));
        point.setItemCode(Optional.ofNullable(jsonObject.getString("itemCode")).orElse(""));
        point.setItemName(Optional.ofNullable(jsonObject.getString("itemName")).orElse(""));
        point.setPageCode(Optional.ofNullable(jsonObject.getString("pageCode")).orElse(""));
        point.setItemType(Optional.ofNullable(jsonObject.getString("itemType")).orElse(""));
        point.setActJson(Optional.ofNullable(jsonObject.getString("actJson")).orElse(""));
        point.setActTime(Optional.ofNullable(jsonObject.getString("actTime")).orElse(TimeUtils.getNowString()));
        point.setTakeKrtNo(Optional.ofNullable(jsonObject.getString("takeKrtNo")).orElse(""));
        point.setTimes(Optional.ofNullable(jsonObject.getString("times")).orElse(""));
        point.setTranLevel(Optional.ofNullable(jsonObject.getInteger("tranLevel")).orElse(0));
        point.setStayTime(Math.abs(Optional.ofNullable(jsonObject.getInteger("stayTime")).orElse(0)));
        point.setTranPuuid(Optional.ofNullable(jsonObject.getString("tranPuuid")).orElse(""));
        point.setTranRootUuid(Optional.ofNullable(jsonObject.getString("tranRootUuid")).orElse(""));
        point.setTranSource(Optional.ofNullable(jsonObject.getString("tranSource")).orElse(""));
        point.setTranUrl(Optional.ofNullable(jsonObject.getString("tranUrl")).orElse(""));
        point.setTranUuid(Optional.ofNullable(jsonObject.getString("tranUuid")).orElse(""));

        point.setAddrLat(LocationUtils.lat);
        point.setAddrLng(LocationUtils.lng);

        point.setRootUuid(Optional.ofNullable(collector.getAppUUID()).orElse(""));
        point.setTag(Optional.ofNullable(collector.getKrtTag()).orElse(""));
        point.setPuuid(Optional.ofNullable(uuid).orElse(""));
        point.setUuid(Optional.ofNullable(getUUID()).orElse(""));
        point.setLevel(level++);

        point.setKrtNo(Optional.ofNullable(collector.getKrtNo()).orElse(""));
        point.setShareKrtNo(Optional.ofNullable(jsonObject.getString("shareKrtNo")).orElse(""));

        JSONObject param = (JSONObject) JSONObject.toJSON(point);

        go(param.toJSONString());
    }


    private static void go(final String js) {
        String key1 = EncryptUtil.getAes16LenK();
        String val1 = EncryptUtil.encryptAES2HexString(js,key1);
        String val2 = EncryptUtil.encryptRSAHexString(key1);

        JSONObject object = new JSONObject();
        object.put("data", val1);
        object.put("key", val2);



        OkGo.<Result>post(http_url)
                .upJson(object.toJSONString())
                .retryCount(0)
                .execute(new JsonCallback<Result>() {

                    boolean isSuccess = false;

                    @Override
                    public void onSuc(Response<Result> response) {
                        isSuccess = response.isSuccessful();
                    }

                    @Override
                    public void onError(Response<Result> response) {
                        super.onError(response);
                        isSuccess = false;
                    }

                    @Override
                    public void onFinish() {
                        if (!isSuccess) {
                            QuickExecutor.getInstance().execute(new IRunnable(object.toJSONString()));
                        }
                    }
                });
    }
}
