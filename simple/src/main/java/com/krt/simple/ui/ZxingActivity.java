package com.krt.simple.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.FragmentActivity;

import com.google.zxing.ResultPoint;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.krt.simple.R;

import java.util.List;


/**
 * author: MaGua
 * create on:2020/11/10 9:06
 * description
 */
public class ZxingActivity extends FragmentActivity {

    private DecoratedBarcodeView bv_barcode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);

        bv_barcode = findViewById(R.id.bv_barcode);
        bv_barcode.decodeContinuous(barcodeCallback);
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        bv_barcode.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        bv_barcode.pause();
    }

    private BarcodeCallback barcodeCallback = new BarcodeCallback() {
        @Override
        public void barcodeResult(BarcodeResult result) {
            bv_barcode.pause();
//            LogUtils.e(result.getText());
//            if (result.getText().trim().startsWith("https://luokeng.ly3618.com/yja/1?")) {
//                String orderId = result.getText().replace("https://luokeng.ly3618.com/yja/1?", "");
//                AlertDialog alertDialog = new AlertDialog.Builder(ZxingActivity.this)
//                        .setMessage("是否要核销" + orderId + "订单").create();
//                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "确认核销", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        dialogInterface.dismiss();
//                        OkGo.<Result>post("https://ly3618.com/jx/api/order/v1/order/validationOrderByPhone?orderId=" + orderId)
//                                .headers("Authorization", "bearer " + App.token)
//                                .execute(new MCallBack<Result>(ZxingActivity.this) {
//                                    @Override
//                                    public void onSuc(Response<Result> response) {
//                                        MToast.showToast(ZxingActivity.this, response.body().msg);
//                                    }
//
//                                    @Override
//                                    public void onFinish() {
//                                        super.onFinish();
//                                        finish();
//                                    }
//                                });
//                    }
//                });
//                alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "取消", (dialogInterface, i) -> {
//                    dialogInterface.dismiss();
//                    bv_barcode.resume();
//                });
//
//                alertDialog.show();
//                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorPrimaryDark));
//                alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorPrimaryDark));
//
//            } else {
//                MToast.showToast(ZxingActivity.this, "这不是商品核销二维码！");
//                bv_barcode.resume();
//            }
            setResult(RESULT_OK, new Intent().putExtra("data", result.getText()));
            finish();

        }

        @Override
        public void possibleResultPoints(List<ResultPoint> resultPoints) {

        }
    };
}
