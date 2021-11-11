package com.krt.simple.ui;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.krt.Lego;
import com.krt.base.util.MPermissions;
import com.krt.base.util.MToast;
import com.krt.base.util.ParseJsonUtil;
import com.krt.simple.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private final int REQUEST_CODE = 10001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ((TextView) findViewById(R.id.txt_version)).setText("解析器版本 V" + Lego.getLegoInfo().compiler_version
                + "." + Lego.getLegoInfo().terminal_version);
        findViewById(R.id.scan).setOnClickListener(this);
//        MUpdate.newBuilder(this)
//                .setUrl("https://www.krtimg.com/file/json/version-APP-000074.json")
//                .build();

        //
    }

    @Override
    public void onClick(View view) {
        MPermissions.getInstance().request(this, new String[]{Manifest.permission.CAMERA}
                , value -> {
                    if (value) {
                        Intent intent = new Intent(MainActivity.this, ZxingActivity.class);
                        startActivityForResult(intent, REQUEST_CODE);
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_CODE) {
            if (data != null) {
                String result = data.getStringExtra("data");
                if (TextUtils.isEmpty(result)) {
                    Toast.makeText(this, "扫描结果为空", Toast.LENGTH_LONG).show();
                } else {
                    String tag = ParseJsonUtil.getStringByKey(result, "tag"),
                            ver = ParseJsonUtil.getStringByKey(result, "version");

                    if (TextUtils.isEmpty(tag) || TextUtils.isEmpty(ver)) {
                        MToast.showToast(MainActivity.this, "项目信息有误");
                    } else {

                        long timestamp = Long.parseLong(ParseJsonUtil.getStringByKey(result, "timestamp"));
                        long s = (System.currentTimeMillis() - timestamp) / (1000 * 60);
                        if (s > 10) {
                            MToast.showToast(this, "此二维码信息已超时");
                            return;
                        } else {
                            startActivity(new Intent(MainActivity.this, InterProActivity.class)
                                    .putExtra("krtCode", tag)
                                    .putExtra("krtVer", ver));
                        }
                    }
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}
