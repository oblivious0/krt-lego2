package com.krt.lego.oc.util;

import com.krt.Lego;
import com.krt.lego.load.info.ApplicationVersionInfo.TerminalBean;

import java.util.List;

/**
 * author: MaGua
 * create on:2021/3/2 15:10
 * description
 */
public class TerminalUtil {

    /**
     * 判断解析器是否需要升级
     *
     * @param interpreterCode
     * @param terminalList
     * @return
     */
    public static TerminalVersion isAndroidUp(String interpreterCode,
                                              List<TerminalBean> terminalList) {
        //当前解析器版本小于编辑器版本，需要强制更新
        if (Integer.parseInt(interpreterCode) > Lego.getLegoInfo().compiler_version) {
            return TerminalVersion.Compel;
        }
        for (TerminalBean bean : terminalList) {
            if (bean.getTerminalCode().equals(
                    String.valueOf(Lego.getLegoInfo().terminal))) {
                //当前端版本小于对应端版本，则提示更新
                if (Lego.getLegoInfo().terminal_version >= bean.getTerminalVersion()) {
                    return TerminalVersion.Unnecessary;
                }
            }
        }

        return TerminalVersion.General;
    }

    public static boolean isNonAndroid(List<String> terminalBeanList) {
        if (terminalBeanList == null || terminalBeanList.size() == 0) return false;
        for (String cid : terminalBeanList) {
            if (cid.equals(String.valueOf(Lego.getLegoInfo().terminal))) {
                //当前端版本小于对应端版本，则提示更新
                return false;
            }
        }
        return true;
    }

    public static boolean isNoAndroid(List<String> terminalBeanList){
        if (terminalBeanList == null || terminalBeanList.size() == 0) return false;
        for (String cid : terminalBeanList) {
            if (cid.equals(String.valueOf(Lego.getLegoInfo().terminal))) {
                //当前端版本小于对应端版本，则提示更新
                return false;
            }
        }
        return true;
    }

}
