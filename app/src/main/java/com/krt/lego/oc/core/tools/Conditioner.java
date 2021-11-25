package com.krt.lego.oc.core.tools;

import com.blankj.utilcode.constant.TimeConstants;
import com.blankj.utilcode.util.TimeUtils;
import com.krt.lego.oc.core.bean.StateMentBean;
import com.krt.lego.oc.core.bean.StyleLinkBean;
import com.krt.lego.oc.core.surface.Subgrade;
import com.krt.lego.oc.util.ParamUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: MaGua
 * @create_on:2021/11/11 14:36
 * @description 条件判断工具
 */
public class Conditioner {
    /**
     * 判断 StyleLink
     *
     * @param bean
     * @param data
     * @return
     */
    public static boolean judge(StyleLinkBean bean, Object data, Subgrade subgrade) {
        for (StateMentBean stateMent : bean.getStateMent()) {
            if (!judgeStateMent(stateMent, data, subgrade)) {
                //多个stateMent关系是 且，当其中一项出现false中止判断并返回
                return false;
            }
        }
        return true;
    }

    /**
     * 判断对比类型
     *
     * @param stateMent
     * @param data
     * @return
     */
    public static boolean judgeStateMent(StateMentBean stateMent, Object data, Subgrade subgrade) {
        try {
            switch (stateMent.getValType()) {
                case "common":
                    return judgeCommon(stateMent, data, subgrade);
                case "date":
                    return judgeDate(stateMent, data, subgrade);
                default:
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private static boolean judgeDate(StateMentBean stateMent, Object data, Subgrade subgrade) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(stateMent.getDateFormat());
        List<String> fieldVal = new ArrayList<>();

        for (String field : stateMent.getFields()) {
//            sdf.format(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2012-10-15 00:00:00"/*你要转化的日期*/))
            String val = VariableFilter.filter(subgrade, field, stateMent.getSource(), data);
            if (val.contains("T")) {
                //判断是否为数据库时间格式

                String sdf = val.substring(0, 19);
                val = sdf.replace("T", " ");
                try {
                    val = simpleDateFormat.format(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(val));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } else {
                val = simpleDateFormat.format(val);
            }

            fieldVal.add(val);
        }

        if (stateMent.getMode().equals("1")) {
            //与所有val值判断，且关系
            for (String val : fieldVal) {
                for (String vals : stateMent.getVal()) {
                    if (exeDate(val, vals, stateMent.getType(), simpleDateFormat)) {
                        return true;
                    }
                }
            }

        } else if (stateMent.getMode().equals("2")) {
            //一一对应判断， 且关系
            if (fieldVal.size() == 0 || stateMent.getVal().size() == 0) {
                return false;
            }
            int maxSize = Math.abs(fieldVal.size() - stateMent.getVal().size());
            for (int i = 0; i < maxSize; i++) {
                if (!exeDate(stateMent.getVal().get(i), fieldVal.get(i), stateMent.getType(), simpleDateFormat)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    /**
     * 判断普通值
     *
     * @param stateMent
     * @param data
     * @return
     */
    private static boolean judgeCommon(StateMentBean stateMent, Object data, Subgrade subgrade) {
        List<String> fieldVal = new ArrayList<>();

        for (String field : stateMent.getFields()) {
            fieldVal.add(VariableFilter.filter(subgrade, field, stateMent.getSource(), data));
        }

        if (stateMent.getMode().equals("1")) {
            //与所有val值判断，或关系
            for (String val : fieldVal) {
                for (String vals : stateMent.getVal()) {
                    if (exeCommon(val, vals, stateMent.getType())) {
                        return true;
                    }
                }
            }

        } else if (stateMent.getMode().equals("2")) {
            //一一对应判断， 且关系
            if (fieldVal.size() == 0 || stateMent.getVal().size() == 0) {
                return false;
            }
//            int maxSize = Math.abs(fieldVal.size() - stateMent.getVal().size());
            int maxSize = 0;

            if (fieldVal.size() < stateMent.getVal().size())
                maxSize = fieldVal.size();
            else
                maxSize = stateMent.getVal().size();


            for (int i = 0; i < maxSize; i++) {
                if (!exeCommon(fieldVal.get(i), stateMent.getVal().get(i), stateMent.getType())) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    private static boolean exeCommon(String val1, String val2, String type) {
        try {
            switch (type) {
                case "=":
                    return val1.equals(val2);
                case "!=":
                    return !val1.equals(val2);
                case "<":
                    return Double.parseDouble(val1) < Double.parseDouble(val2);
                case ">":
                    return Double.parseDouble(val1) > Double.parseDouble(val2);
                case "<=":
                    return Double.parseDouble(val1) <= Double.parseDouble(val2);
                case ">=":
                    return Double.parseDouble(val1) >= Double.parseDouble(val2);
                default:
            }
        } catch (Exception e) {

        }
        return false;
    }

    private static boolean exeDate(String val1, String val2, String type, SimpleDateFormat dateF) {
        long result = TimeUtils.getTimeSpan(val1, val2, dateF, TimeConstants.MIN);
        switch (type) {
            case "=":
                return result == 0;
            case "!=":
                return result != 0;
            case "<":
                return result < 0;
            case ">":
                return result > 0;
            case "<=":
                return result <= 0;
            case ">=":
                return result >= 0;
            default:
        }
        return false;
    }

}
