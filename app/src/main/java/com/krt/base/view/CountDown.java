package com.krt.base.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.os.CountDownTimer;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.krt.base.util.MUtil;
import com.krt.lego.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * author: MaGua
 * create on:2020/11/27 15:49
 * description
 */
public class CountDown extends LinearLayout {

    private Context context;
    private boolean hidZeroDay;
    private String separator, fontSize;
    private String timestamp;
    private long overTime = 0;

    private CountDownTimer countDownTimer;

    private TextView countD, countH, countM, countS, suffixD, suffixH, suffixM, suffixS;

    public CountDown(Context context) {
        super(context);
        this.context = context;
        initView();
    }

    private void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.m_layout_countdown, this, true);
        countD = findViewById(R.id.countdown_day);
        countH = findViewById(R.id.countdown_hour);
        countM = findViewById(R.id.countdown_min);
        countS = findViewById(R.id.countdown_seconds);
        suffixD = findViewById(R.id.suffix_day);
        suffixH = findViewById(R.id.suffix_hour);
        suffixM = findViewById(R.id.suffix_min);
        suffixS = findViewById(R.id.suffix_seconds);
    }

    public void setSeparator(String separator, int separatorSize, int separatorColor) {
        if (separator.equals("zh")) {
            suffixD.setText("天");
            suffixH.setText("时");
            suffixM.setText("分");
            suffixS.setText("秒");
        } else {
            suffixD.setText(":");
            suffixH.setText(":");
            suffixM.setText(":");
            suffixS.setVisibility(View.GONE);
        }

        suffixD.setTextSize(TypedValue.COMPLEX_UNIT_PX, MUtil.getRealValue(separatorSize));
        suffixH.setTextSize(TypedValue.COMPLEX_UNIT_PX, MUtil.getRealValue(separatorSize));
        suffixM.setTextSize(TypedValue.COMPLEX_UNIT_PX, MUtil.getRealValue(separatorSize));
        suffixS.setTextSize(TypedValue.COMPLEX_UNIT_PX, MUtil.getRealValue(separatorSize));

        suffixD.setTextColor(separatorColor);
        suffixH.setTextColor(separatorColor);
        suffixM.setTextColor(separatorColor);
        suffixS.setTextColor(separatorColor);
    }

    public void setTimeStamp(String timestamp) {
        this.timestamp = timestamp;
        try {
            overTime = dateToStamp(timestamp);
        } catch (Exception e) {

        }
    }

    public void setIsShowBorder(boolean isShowBorder, String borderColor, String bgColor) {
        if (isShowBorder) {
            GradientDrawable drawable = MUtil.getBgDrawable(bgColor,
                    GradientDrawable.RECTANGLE, 6, 1,
                    borderColor);
            countD.setBackgroundDrawable(drawable);
            countH.setBackgroundDrawable(drawable);
            countM.setBackgroundDrawable(drawable);
            countS.setBackgroundDrawable(drawable);
        }
    }

    public void setTextStyle(int textSize, int color) {
        countD.setTextSize(TypedValue.COMPLEX_UNIT_PX, MUtil.getRealValue(textSize));
        countH.setTextSize(TypedValue.COMPLEX_UNIT_PX, MUtil.getRealValue(textSize));
        countM.setTextSize(TypedValue.COMPLEX_UNIT_PX, MUtil.getRealValue(textSize));
        countS.setTextSize(TypedValue.COMPLEX_UNIT_PX, MUtil.getRealValue(textSize));
        countD.setTextColor(color);
        countH.setTextColor(color);
        countM.setTextColor(color);
        countS.setTextColor(color);
    }

    public void isShow(boolean showDay, boolean showHours, boolean showMin, boolean showSeconds) {
        countD.setVisibility(showDay ? View.VISIBLE : View.GONE);
        suffixD.setVisibility(showDay ? View.VISIBLE : View.GONE);
        countH.setVisibility(showHours ? View.VISIBLE : View.GONE);
        suffixH.setVisibility(showDay ? View.VISIBLE : View.GONE);
        countM.setVisibility(showMin ? View.VISIBLE : View.GONE);
        suffixM.setVisibility(showMin ? View.VISIBLE : View.GONE);
        countS.setVisibility(showSeconds ? View.VISIBLE : View.GONE);
        suffixS.setVisibility(showSeconds ? View.VISIBLE : View.GONE);
    }

    public CountDown setHidZeroDay(boolean hidZeroDay) {
        this.hidZeroDay = hidZeroDay;
        return this;
    }

    public long dateToStamp(String s) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = simpleDateFormat.parse(s);
        long ts = date.getTime();
        return ts;
    }

    public void start() {
        long time = Math.abs(System.currentTimeMillis() - overTime);
        countDownTimer = new CountDownTimer(time, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (!((Activity) context).isFinishing()) {
                    long day = millisUntilFinished / (1000 * 24 * 60 * 60);
                    long hour = (millisUntilFinished - day * (1000 * 24 * 60 * 60)) / (1000 * 60 * 60);
                    long minute = (millisUntilFinished - day * (1000 * 24 * 60 * 60) - hour * (1000 * 60 * 60)) / (1000 * 60);
                    long second = (millisUntilFinished - day * (1000 * 24 * 60 * 60) - hour * (1000 * 60 * 60) - minute * (1000 * 60)) / 1000;
                    try {
                        if (day == 0 && hidZeroDay) {
                            countD.setVisibility(View.GONE);
                            suffixD.setVisibility(View.GONE);
                        }
                        countD.setText(String.valueOf(day).length() == 1 ? "0" + day : day + "");
                        countH.setText(String.valueOf(hour).length() == 1 ? "0" + hour : hour + "");
                        countM.setText(String.valueOf(minute).length() == 1 ? "0" + minute : minute + "");
                        countS.setText(String.valueOf(second).length() == 1 ? "0" + second : second + "");
                    } catch (Exception e) {
                    }
                }
            }

            /**
             *倒计时结束后调用的
             */
            @Override
            public void onFinish() {

            }

        };
        countDownTimer.start();

    }


}
