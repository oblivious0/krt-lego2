package com.krt.lego.oc.core.bean;

import java.util.List;

/**
 * @author hyj
 * @time 2020/8/19 17:02
 * @class describe
 */
public class EventBean {
    /**
     * {
     * "type": "navigator",
     * "ifOuterChain": false,
     * "ifModulePage": false,
     * "url": "",
     * "pageId": "",
     * "urlforH5": "/views/index/searchPage",
     * "params": [],
     * "naviType": "navigateTo"
     * }
     */

    /**
     * 跳转类型  navigator：界面跳转
     */
    private String type;
    /**
     * 是否跳转链接地址
     */
    private boolean ifOuterChain;
    /**
     * 是否跳转模块化  （如果ifOuterChain与ifModulePage均为false，且type为navigator时，跳转至原生界面）
     */
    private boolean ifModulePage;
    /**
     * 如果为原生界面时，url为跳转标识，否则为跳转的链接地址
     */
    private String url;
    /**
     * 如果为模块化时，pageId为json文件名
     */
    private String pageId;

    /**
     * 小程序用，app暂未用到
     */
    private String urlforH5;

    /**
     * 跳转至原生界面时对应的传参
     */
    private List<ParamBean> params;

    private String naviType;
    /**
     * 小程序用
     */
    private boolean urlFromApi;
    private boolean ifAppsetNav;
    private int backDelta;

    private boolean needLogin;
    private String cid;
    private List<String> ajaxIds;

    private String target;
    private String name;
    private String number;

    private List<String> terminal;
    private List<String> development;
    private String text;
    private List<String> confirmEv;
    private boolean showCancel;
    private String title;
    private String content;

    private boolean maskClose;
    private String popId;
    private String variName;
    private String source;
    private String attr;

    private boolean ifToMp;
    private String appId;

    /**
     * "actCode": "A2021082000001D",
     *             "themeCode": "ATH20210819001Y",
     *             "itemCode": "001001",
     *             "itemName": "生成短链",
     *             "withLoc": true,
     *             "shareLockCycle": 1,
     *
     */

    private String actCode;
    private String themeCode;
    private String itemCode;
    private String itemName;
    private boolean withLoc;
    private int shareLockCycle;

    private String image;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getActCode() {
        return actCode;
    }

    public void setActCode(String actCode) {
        this.actCode = actCode;
    }

    public String getThemeCode() {
        return themeCode;
    }

    public void setThemeCode(String themeCode) {
        this.themeCode = themeCode;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public boolean isWithLoc() {
        return withLoc;
    }

    public void setWithLoc(boolean withLoc) {
        this.withLoc = withLoc;
    }

    public int getShareLockCycle() {
        return shareLockCycle;
    }

    public void setShareLockCycle(int shareLockCycle) {
        this.shareLockCycle = shareLockCycle;
    }

    public boolean isIfToMp() {
        return ifToMp;
    }

    public void setIfToMp(boolean ifToMp) {
        this.ifToMp = ifToMp;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getVariName() {
        return variName;
    }

    public void setVariName(String variName) {
        this.variName = variName;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getAttr() {
        return attr;
    }

    public void setAttr(String attr) {
        this.attr = attr;
    }

    public String getPopId() {
        return popId;
    }

    public void setPopId(String popId) {
        this.popId = popId;
    }

    public boolean isMaskClose() {
        return maskClose;
    }

    public void setMaskClose(boolean maskClose) {
        this.maskClose = maskClose;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<String> getConfirmEv() {
        return confirmEv;
    }

    public void setConfirmEv(List<String> confirmEv) {
        this.confirmEv = confirmEv;
    }

    public boolean isShowCancel() {
        return showCancel;
    }

    public void setShowCancel(boolean showCancel) {
        this.showCancel = showCancel;
    }

    public List<String> getDevelopment() {
        return development;
    }

    public void setDevelopment(List<String> development) {
        this.development = development;
    }

    public List<String> getTerminal() {
        return terminal;
    }

    public void setTerminal(List<String> terminal) {
        this.terminal = terminal;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getAjaxIds() {
        return ajaxIds;
    }

    public void setAjaxIds(List<String> ajaxIds) {
        this.ajaxIds = ajaxIds;
    }

    private List<ActionBean> list;

    public List<ActionBean> getList() {
        return list;
    }

    public void setList(List<ActionBean> list) {
        this.list = list;
    }

    public boolean isNeedLogin() {
        return needLogin;
    }

    public void setNeedLogin(boolean needLogin) {
        this.needLogin = needLogin;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public boolean isUrlFromApi() {
        return urlFromApi;
    }

    public void setUrlFromApi(boolean urlFromApi) {
        this.urlFromApi = urlFromApi;
    }

    public boolean isIfAppsetNav() {
        return ifAppsetNav;
    }

    public void setIfAppsetNav(boolean ifAppsetNav) {
        this.ifAppsetNav = ifAppsetNav;
    }

    public int getBackDelta() {
        return backDelta;
    }

    public void setBackDelta(int backDelta) {
        this.backDelta = backDelta;
    }

    public List<ParamBean> getParams() {
        return params;
    }

    public void setParams(List<ParamBean> params) {
        this.params = params;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isIfOuterChain() {
        return ifOuterChain;
    }

    public void setIfOuterChain(boolean ifOuterChain) {
        this.ifOuterChain = ifOuterChain;
    }

    public boolean isIfModulePage() {
        return ifModulePage;
    }

    public void setIfModulePage(boolean ifModulePage) {
        this.ifModulePage = ifModulePage;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPageId() {
        return pageId;
    }

    public void setPageId(String pageId) {
        this.pageId = pageId;
    }

    public String getUrlforH5() {
        return urlforH5;
    }

    public void setUrlforH5(String urlforH5) {
        this.urlforH5 = urlforH5;
    }

    public String getNaviType() {
        return naviType;
    }

    public void setNaviType(String naviType) {
        this.naviType = naviType;
    }


}