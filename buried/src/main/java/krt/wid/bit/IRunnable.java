package krt.wid.bit;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lzy.okgo.OkGo;

import java.io.IOException;

import krt.wid.bit.net.Result;
import okhttp3.Response;

import static krt.wid.bit.Buried.http_url;

/**
 * @author: MaGua
 * @create_on:2021/9/7 9:56
 * @description
 */
public class IRunnable implements Runnable {

    private String data;

    public IRunnable(String data){
        this.data = data;
    }

    @Override
    public void run() {
        try {
            Response result = OkGo.<Result>post(http_url)
                    .retryCount(0)
                    .upJson(data)
                    .execute();
            JSONObject jsData = JSON.parseObject(result.body().string());
            if(jsData.getInteger("code")!=200){
                QuickExecutor.getInstance().execute(this);
            }
        } catch (IOException e) {
            e.printStackTrace();
            QuickExecutor.getInstance().execute(this);
        }

    }
}
