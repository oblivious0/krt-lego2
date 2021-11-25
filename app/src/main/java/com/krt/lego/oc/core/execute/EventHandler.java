package com.krt.lego.oc.core.execute;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.CloneUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.krt.base.util.MToast;
import com.krt.base.xshare.ShareInfo;
import com.krt.base.xshare.ShareUtil;
import com.krt.lego.AppLibManager;
import com.krt.lego.R;
import com.krt.lego.config.LegoGlobal;
import com.krt.lego.oc.core.bean.EventBean;
import com.krt.lego.oc.core.bean.ParamBean;
import com.krt.lego.oc.core.surface.BaseWidget;
import com.krt.lego.oc.core.surface.Subgrade;
import com.krt.lego.oc.core.tools.BroadCastMessageWarp;
import com.krt.lego.oc.core.tools.EventMessageWarp;
import com.krt.lego.oc.core.tools.VariableFilter;
import com.krt.lego.oc.util.ParamUtil;
import com.krt.lego.oc.util.TerminalUtil;
import com.youth.banner.Banner;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;


/**
 * @author hyj
 * @time 2020/7/17 10:55
 * @class describe
 */
public abstract class EventHandler {

    public void onViewClick(BaseWidget baseView, List<EventBean> eventBeans) {
        for (final EventBean eventBean : eventBeans) {
//            EventBean eventBean1 = CloneUtils.deepClone(eventBean, EventBean.class);
//            List<ParamBean> paramBeans = new ArrayList<>();
//            //判断参数集是否为空，不为空将遍历参数集，把传出值替换为实际数据；
//            if (eventBean.getParams() != null) {
//                paramBeans = (VariableFilter.filter(baseView.getDependent(), eventBean.getParams()));
//            }
//            eventBean1.setParams(paramBeans);
            action(baseView.getDependent(), eventBean,null);
        }
    }

    public void onDataListClick(BaseWidget baseView, List<EventBean> eventBeans, int position) {
        Object json = new Object();

        if (baseView.getRawView() instanceof RecyclerView) {
            RecyclerView recyclerView = (RecyclerView) baseView.getRawView();

            if (baseView.type.equals("waterfall") && position == 0) {
                if (!TextUtils.isEmpty(Optional.ofNullable
                        (baseView.getAttr("leftSpaceId").toString()).orElse(""))) {
                    return;
                }
            }

            try {
                BaseQuickAdapter<Object, BaseViewHolder> adapter =
                        (BaseQuickAdapter<Object, BaseViewHolder>) recyclerView.getAdapter();
                json = adapter.getData().get(position);
            } catch (Exception e) {
                return;
            }
        } else if (baseView.getRawView() instanceof Banner) {
            Banner banner = (Banner) baseView.getRawView();
            try {
                json = banner.getAdapter().getData(position);
            } catch (Exception e) {
                return;
            }
        }


        for (EventBean eventBean : eventBeans) {
//            EventBean eventBean1 = CloneUtils.deepClone(eventBean, EventBean.class);
//            if (eventBean1.isUrlFromApi()) {
//                // data%krt_Array%krt_linkUrl
//                String[] stringKey = eventBean1.getUrl().split("%krt_");
//                String url = PropertyBindTool.getProperty(stringKey, json);
//                eventBean1.setUrl(url);
//            }
//
//            List<ParamBean> paramBeans = ParamUtil.filterParam
//                    (baseView.getDependent(), eventBean.getParams(), json);
//            //判断参数集是否为空，不为空将遍历参数集，把传出值替换为实际数据；
//            eventBean1.setParams(paramBeans);
            action(baseView.getDependent(), eventBean, json);
        }
    }

    private void action(Subgrade subgrade, EventBean eventBean, Object json) {

        if (eventBean.isNeedLogin() && !LegoGlobal.isUserLogin()) {
            //处于需要登录的情况下切未登录的状态返回
            gotoLogin(subgrade.getCarrier());
            return;
        }

        if ("tobedeveloped".equals(eventBean.getUrl())) {
            MToast.showToast(subgrade.getCarrier(), "敬请期待！");
            return;
        }

        HashMap<String, String> obj =  VariableFilter.filterParam
                (subgrade, eventBean.getParams(), json);
        execute(subgrade, eventBean, obj);
    }

    /**
     * 事件分类执行
     *
     * @param subgrade
     */
    public void execute(Subgrade subgrade, EventBean eventBean, HashMap<String, String> obj) {

        if (eventBean.isIfToMp()) {
            jumpWx(subgrade, eventBean.getAppId(), eventBean.getUrl());
            return;
        }

        //非安卓执行
        if (TerminalUtil.isNonAndroid(eventBean.getTerminal())) {
            return;
        }

        if (eventBean.isIfAppsetNav()) {

        }

        try {
            switch (eventBean.getType()) {
                case EventMessageWarp.NAVIGATOR:
                    navigator(subgrade, eventBean, obj);
                    break;
                case EventMessageWarp.SEND_AJAX:
                    sendAjax(subgrade, eventBean);
                    break;
                case EventMessageWarp.SEND_BROAD_CAST:
                    sendEventBus(subgrade, eventBean);
                    break;
                case EventMessageWarp.SHARE_PAGE:
//                    sharePage();
                    break;
                case EventMessageWarp.STATE_CHANGE:
                    stateChange(subgrade, eventBean);
                    break;
                case EventMessageWarp.PHONE_CALL:
                    onCallPhone(subgrade, eventBean.getNumber());
                    break;
                case EventMessageWarp.TOAST:
                    toast(subgrade.getCarrier(), eventBean.getText());
                    break;
                case EventMessageWarp.MODAL:
                    modal(subgrade, eventBean);
                    break;
                case EventMessageWarp.POPUP:
                    openPopup(subgrade, eventBean);
                    break;
                case EventMessageWarp.POPUP_CLOSE:
                    closePopup(subgrade, eventBean);
                    break;
                case EventMessageWarp.VARI_MODIFY:
                    variModify(subgrade, eventBean);
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 改变页面变量
     *
     * @param subgrade
     * @param eventBean
     */
    private void variModify(Subgrade subgrade, EventBean eventBean) {
        switch (eventBean.getSource()) {
            case "widget":
                String cid = eventBean.getTarget().split("%krt%")[1];
                BaseWidget view = (BaseWidget) subgrade.getDesigner().widgets.get(cid);
                switch (eventBean.getAttr()) {
//                    case "text":
////                        subgrade.getAdaptor().getContainer("element").put(eventBean.getVariName(),view.view.getText)
//                        break;
//                    case "width":
//                        subgrade.getAdaptor().getContainer("element").put(eventBean.getVariName(),
//                                view.view.getWidth() + "");
//                        break;
//                    case "height":
//                        subgrade.getAdaptor().getContainer("element").put(eventBean.getVariName(),
//                                view.view.getHeight() + "");
//                        break;
                    default:
                }
                break;
            default:
        }
    }

    /**
     * 关闭弹窗
     *
     * @param subgrade
     * @param eventBean
     */
    private void closePopup(Subgrade subgrade, EventBean eventBean) {
//        if (subgrade.getAdaptor().getDialogs().containsKey(eventBean.getPopId())) {
//            subgrade.getAdaptor().getDialogs().get(eventBean.getPopId()).close();
//        }
    }

    /**
     * 打开弹窗
     *
     * @param subgrade
     * @param eventBean
     */
    private void openPopup(Subgrade subgrade, EventBean eventBean) {
//        if (subgrade.getAdaptor().getDialogs().containsKey(eventBean.getCid())) {
//            subgrade.getAdaptor().getDialogs().get(eventBean.getCid()).showAtLocation();
//        } else {
//            BasePopupWindow basePopupWindow = BasePopupWindow.create(subgrade, eventBean.getPageId());
//            basePopupWindow.setOutsideTouchable(true);
//            basePopupWindow.maskClose = eventBean.isMaskClose();
//            subgrade.getAdaptor().getDialogs().put(eventBean.getCid(), basePopupWindow);
//            basePopupWindow.showAtLocation();
//        }
    }

    /**
     * 系统弹窗提示
     *
     * @param subgrade
     * @param eventBean
     */
    private void modal(Subgrade subgrade, EventBean eventBean) {
        try {
            new AlertDialog.Builder(subgrade.getCarrier())
                    .setCancelable(eventBean.isShowCancel())
                    .setTitle(eventBean.getTitle())
                    .setMessage(eventBean.getContent())
                    .setNegativeButton("确定", (dialogInterface, i) -> {
                        if (eventBean.getConfirmEv() != null) {
                            for (String cid : eventBean.getConfirmEv()) {
                                action(subgrade, subgrade.getDesigner().orders.get(cid),null);
                            }
                        }
                        dialogInterface.dismiss();
                    })
                    .create()
                    .show();
        } catch (NullPointerException e) {

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 短消息提示
     *
     * @param context
     * @param string
     */
    private void toast(Context context, String string) {
        MToast.showToast(context, string);
    }

    /**
     * 改变成员属性
     *
     * @param subgrade
     * @param eventBean
     */
    private void stateChange(Subgrade subgrade, EventBean eventBean) {
//        for (ActionBean actionBean : eventBean.getList()) {
//            if (TextUtils.isEmpty(actionBean.getTarget())) {
//
//                for (String cell : actionBean.getTargets()) {
//                    String[] cell1 = cell.split("%krt_");
//                    String[] val = cell1[cell1.length - 1].split("%krt%");
//                    if (actionBean.getType().equals("attr")) {
//                        for (ActionBean.Attr attr : actionBean.getAttrList()) {
//                            BaseWidget baseView = ((BaseWidget) subgrade.getDesigner().widgets.get(val[1]));
//                            if (baseView != null)
//                                if (attr.isVari()) {
//                                    String tar = subgrade.getDesigner().variables.get(attr.getTarget()).toString();
//                                    baseView.bindData(val[1], attr.getAttr().split("_")[1], tar);
//                                } else {
//                                    baseView.bindData(val[1], attr.getAttr().split("_")[1], attr.getTarget());
//                                }
//                        }
//                    } else {
//                        setAttr(actionBean.getType(), subgrade, val[1]);
//                    }
//                }
//            } else {
//                String[] cell = actionBean.getTarget().split("%krt_");
//                String[] val = cell[cell.length - 1].split("%krt%");
//                setAttr(actionBean.getType(), subgrade, val[1]);
//
//            }
//        }
    }

    private void setAttr(String type, Subgrade subgrade, String targetCid) {
//        switch (type) {
//            case "attr":
//                for (ActionBean.Attr attr : actionBean.getAttrList()) {
//                    BaseView baseView = ((BaseView) subgrade.getAdaptor().getContainer("view").get(targetCid));
//                    if (baseView != null)
//                        baseView.bindData(targetCid, attr.getAttr().split("_")[1], attr.getTarget());
//                }
//                break;
//            case "hide":
//                ((BaseView) subgrade.getAdaptor().getContainer("view").get(targetCid)).view.setVisibility(View.GONE);
//                break;
//            case "show":
//                ((BaseView) subgrade.getAdaptor().getContainer("view").get(targetCid)).view.setVisibility(View.VISIBLE);
//                break;
//            case "clear":
//                BaseView baseView = ((BaseView) subgrade.getAdaptor().getContainer("view").get(targetCid));
//                if (baseView != null) {
//                    View view1 = baseView.view;
//                    if (view1 instanceof MRecyclerView) {
//                        ((BaseQuickAdapter) ((MRecyclerView) view1).getAdapter()).getData().clear();
//                    } else if (view1 instanceof Banner) {
//                        ((Banner) view1).getAdapter().setDatas(new ArrayList());
//                    }
//                }
//                break;
//
//            default:
//                break;
//        }
    }

    /**
     * 发送广播
     *
     * @param subgrade
     * @param eventBean
     */
    private void sendEventBus(Subgrade subgrade, EventBean eventBean) {
        if (eventBean.getParams() != null) {
            HashMap<String, String> param = new HashMap<>(eventBean.getParams().size());
            for (ParamBean bean : eventBean.getParams()) {
                param.put(bean.getKey(), bean.getVal());
            }
            BroadCastMessageWarp warp = new BroadCastMessageWarp(eventBean.getName(), param);
            EventBus.getDefault().postSticky(warp);
        }
    }

    /**
     * 发送ajax
     * ae5a0405c8078fd
     *
     * @param subgrade
     * @param eventBean
     */
    private void sendAjax(Subgrade subgrade, EventBean eventBean) {
//        for (String ids : eventBean.getAjaxIds()) {
//            String[] cell = ids.split("%krt_");
//            Subgrade ctx = subgrade;
//            for (String cellStr : cell) {
//                String[] splitStr = cellStr.split("%krt%");
////                if ("page".equals(splitStr[0])) {
////                    if (splitStr[1].equals(subgrade.getPageId())) {
////                        ctx = subgrade;
////                    } else {
////                        ctx = AppLibManager.getPageContext(splitStr[1]);
////                    }
////                } else
//                if ("ajax".equals(splitStr[0])) {
//                    Object obj = ctx.getAdaptor().getContainer("ajax").get(splitStr[1]);
//                    if (obj != null) {
//                        AjaxBean ajaxBean = (AjaxBean) obj;
//                        AjaxFactory.execute(ajaxBean, subgrade);
//                    }
//
//                } else if ("list".equals(splitStr[0])) {
//                    try {
//                        MRecyclerView view = (MRecyclerView) ((BaseView) ctx.getAdaptor().getContainer("view").get(splitStr[1])).view;
//                        view.resetPage();
//                    } catch (Exception e) {
//                    }
//                }
//            }
//        }
    }

    /**
     * 跳转事件
     *
     * @param subgrade
     * @param eventBean
     */
    private void navigator(Subgrade subgrade, EventBean eventBean, HashMap params) {
        if (!TextUtils.isEmpty(eventBean.getNaviType()) && eventBean.getNaviType().equals("navigateBack")) {
            ((Activity) subgrade.getCarrier()).finish();
            return;
        }

        if (eventBean.isIfOuterChain()) {
            String url = disposeUrl(eventBean.getUrl(), eventBean.getDevelopment());
            onStartWebActivity(subgrade, url, params);
        } else if (eventBean.isIfModulePage() && !eventBean.isIfOuterChain()) {
            onStartModuleActivity(subgrade, eventBean.getPageId(), params);
        } else {
            onClickListener(subgrade, eventBean.getUrl(), params);
        }
    }

    private void share(Subgrade subgrade, EventBean eventBean) {
        String val = "";
        switch (eventBean.getSource()) {
            case "props":
            case "variable":
                val = subgrade.getDesigner().variables.get(eventBean.getUrl());
                break;
            case "static":
                val = eventBean.getUrl();
                break;
            case "storage":
                val = AppLibManager.getStorageVal(eventBean.getUrl(), subgrade.getCarrier());
                break;
            default:
                break;
        }

        HashMap<String, String> paramBeans = VariableFilter.filterParam(subgrade, eventBean.getParams(), null);
        sharePage(subgrade, val, eventBean.getImage(), paramBeans);


    }

    /**
     * 实现分享事件
     */
    protected void sharePage(Subgrade subgrade, String url, String icon, HashMap objects) {
        ShareInfo info = new ShareInfo();
        info.setDescription("");
        info.setTitle(subgrade.getCarrier().getString(R.string.app_name));
        info.setUrl(url);
        info.setImgUrl(icon);
        JSONObject jsonObject = new JSONObject();
        //itemType: 'click',itemCode: '2',itemName: '通用分享',
        jsonObject.put("itemType", "share");
        jsonObject.put("itemCode", 2);
        jsonObject.put("itemName", "通用分享");
        jsonObject.put("tranSource", "");
        jsonObject.put("tranUrl", url);
        ShareUtil shareUtil = new ShareUtil(subgrade.getCarrier(), info, jsonObject.toJSONString());
        shareUtil.init();
    }

    protected abstract void gotoLogin(Context context);

    protected abstract void onClickListener(Subgrade subgrade, String type, HashMap<String,String> objects);

    protected abstract void onStartWebActivity(Subgrade subgrade, String url, HashMap<String,String> objects);

    protected abstract void onStartModuleActivity(Subgrade subgrade, String jsonName, HashMap<String,String> objects);

    protected abstract void onCallPhone(Subgrade subgrade, String number);

    protected abstract String disposeUrl(String url, List<String> development);

    protected abstract void jumpWx(Subgrade subgrade, String key, String path);
}
