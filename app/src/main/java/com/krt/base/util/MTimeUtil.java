package com.krt.base.util;

import android.text.format.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * @author Marcus
 * @package krt.wid.util
 * @description 时间工具
 * @time 2019/1/3
 */
public class MTimeUtil {
    /**
     * 获取当前的系统时间
     *
     * @return
     */
    public static String getTime() {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        return sf.format(new Date());
    }

    /**
     * 将date转化为yyyy-mm-dd
     *
     * @return
     */
    public static String getDate(Date date) {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        return sf.format(date);
    }

    /**
     * 转化yyyy-mm-dd 为时间戳
     *
     * @param s
     * @return
     */
    public static long dateToStamp(String s) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = simpleDateFormat.parse(s);
            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * 获取当前系统时间前一天的时间，时间格式为yyyy-mm-dd
     */
    public static String getBeforeDay() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        return sf.format(calendar.getTime());
    }

    /**
     * 获取当前系统时间后一天的时间，时间格式为yyyy-mm-dd
     */
    public static String getAfterDay() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 1);
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        return sf.format(calendar.getTime());
    }

    /**
     * 获取当前系统时间前一个月的时间，时间格式为yyyy-mm-dd
     */
    public static String getBeforeMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -1);
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        return sf.format(calendar.getTime());
    }

    /**
     * 获取当前系统时间后一个月的时间，时间格式为yyyy-mm-dd
     */
    public static String getAfterMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, 1);
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        return sf.format(calendar.getTime());
    }

    /**
     * 判断当前时间为周几
     */
    public static String getWeek() {
        final Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        String mWay = String.valueOf(c.get(Calendar.DAY_OF_WEEK));
        if ("1".equals(mWay)) {
            mWay = "日";
        } else if ("2".equals(mWay)) {
            mWay = "一";
        } else if ("3".equals(mWay)) {
            mWay = "二";
        } else if ("4".equals(mWay)) {
            mWay = "三";
        } else if ("5".equals(mWay)) {
            mWay = "四";
        } else if ("6".equals(mWay)) {
            mWay = "五";
        } else if ("7".equals(mWay)) {
            mWay = "六";
        }
        return "周" + mWay;
    }

    /**
     * 微信朋友圈动态的发送时间显示方式
     *
     * @param when
     * @return
     */
    public static String format(long when) {
        String timeResult = null;
        long nowTime = System.currentTimeMillis();
        int passTime = (int) Math.floor((nowTime - when) / 1000);//发送的时间和当前时间的间隔  单位：秒
        if (DateUtils.isToday(when)) {
            if (passTime < 60) {
                timeResult = "1分钟前";
            } else if (passTime > 60 && passTime < 3600) {
                timeResult = passTime / 60 + "分钟前";
            } else if (passTime >= 3600 && passTime < 86400) {
                timeResult = (int) Math.floor(passTime / (60 * 60)) + "小时前";
            }
        } else {
            if (passTime < 86400) {
                timeResult = "昨天";
            } else {
                timeResult = (int) Math.ceil((passTime / 86400)) + 1 + "天前";
            }
        }

        return timeResult;
    }

    /**
     * 获取二个时间的时间差值
     *
     * @param beginDate
     * @param endDate
     * @return
     */
    public static int getDiffBetweenDay(Date beginDate, Date endDate) {
        Calendar beginCalendar = Calendar.getInstance();
        beginCalendar.setTime(beginDate);
        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTime(endDate);
        long beginTime = beginCalendar.getTime().getTime();
        long endTime = endCalendar.getTime().getTime();
        int betweenDays = (int) ((endTime - beginTime) / (1000 * 60 * 60 * 24));

        endCalendar.add(Calendar.DAY_OF_MONTH, -betweenDays);
        endCalendar.add(Calendar.DAY_OF_MONTH, -1);
        if (beginCalendar.get(Calendar.DAY_OF_MONTH) == endCalendar.get(Calendar.DAY_OF_MONTH)) {
            //相等说明确实跨天了
            return betweenDays + 1;
        } else {
            //不相等说明确实未跨天
            return betweenDays + 0;
        }
    }

    public static Date getDate(String time) {
        try {
            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
            return sf.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

}
