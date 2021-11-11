package com.krt.lego.oc.core.bean;

import java.util.List;

/**
 * author:Marcus
 * create on:2019/3/26 19:21
 * description
 */
public class BottomBean {


    /**
     * type : tabbar
     * name : app菜单
     * cid : 7w4lb2vib2
     * common : {"isHidden":false,"name":"","tag":"","labelsOnly":false,"iconsOnly":false,"zIndex":1000,"bgColor":"rgb(247, 247, 248)","links":[{"badge":"0","badgeColor":"#ff3b30","label":"首页","originTextColor":"#666666","highlightTextColor":"#2196f3","IfSelect":true,"imgUrl":"./img/home.png","SelectImgUrl":"./img/home-active.png","skinName":"/krt-module/file/image/20190416/14b24b1d0d0c470aab2b2a4ac93037f3.png","originSkin":"50_50_37_846","selectSkin":"50_50_38_759","pageId":"qi2rcnt41t","shangeType":"strange","skinIcon":true},{"badge":"0","badgeColor":"#ff3b30","highlightTextColor":"#2196f3","label":"旅游圈","originTextColor":"#666666","IfSelect":false,"imgUrl":"./img/nianka-active.png","SelectImgUrl":"./img/nianka.png","skinName":"icon.png","originSkin":"50_50_334_845","selectSkin":"50_50_336_758","pageId":"20kzxobghyu","shangeType":"strange","skinIcon":true},{"badge":"0","badgeColor":"#ff3b30","label":"年卡","originTextColor":"#666666","highlightTextColor":"#2196f3","IfSelect":false,"imgUrl":"./img/home.png","SelectImgUrl":"./img/home-active.png","skinName":"icon.png","originSkin":"50_50_185_845","selectSkin":"50_50_187_758","pageId":"","shangeType":"strange","skinIcon":true},{"badge":"0","badgeColor":"#ff3b30","label":"好友","originTextColor":"#666666","highlightTextColor":"#2196f3","IfSelect":false,"imgUrl":"./img/home.png","SelectImgUrl":"./img/home-active.png","skinName":"icon.png","originSkin":"50_50_484_845","selectSkin":"50_50_485_758","pageId":"","shangeType":"strange","skinIcon":true},{"badge":"0","badgeColor":"#ff3b30","label":"我的","originTextColor":"#666666","highlightTextColor":"#2196f3","IfSelect":false,"imgUrl":"./img/home.png","SelectImgUrl":"./img/home-active.png","skinName":"icon.png","originSkin":"50_50_634_845","selectSkin":"50_50_637_758","pageId":"","shangeType":"strange","skinIcon":true}],"height":56}
     * style : {}
     */

    private String type;
    private String name;
    private String cid;
    private CommonBean common;
    private Object style;
    private boolean noAjax;
    private boolean waitToCopy;

    public boolean isNoAjax() {
        return noAjax;
    }

    public void setNoAjax(boolean noAjax) {
        this.noAjax = noAjax;
    }

    public boolean isWaitToCopy() {
        return waitToCopy;
    }

    public void setWaitToCopy(boolean waitToCopy) {
        this.waitToCopy = waitToCopy;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public CommonBean getCommon() {
        return common;
    }

    public void setCommon(CommonBean common) {
        this.common = common;
    }

    public Object getStyle() {
        return style;
    }

    public void setStyle(Object style) {
        this.style = style;
    }

    public static class CommonBean {
        /**
         * isHidden : false
         * name :
         * tag :
         * labelsOnly : false
         * iconsOnly : false
         * zIndex : 1000
         * bgColor : rgb(247, 247, 248)
         * links : [{"badge":"0","badgeColor":"#ff3b30","label":"首页","originTextColor":"#666666","highlightTextColor":"#2196f3","IfSelect":true,"imgUrl":"./img/home.png","SelectImgUrl":"./img/home-active.png","skinName":"/krt-module/file/image/20190416/14b24b1d0d0c470aab2b2a4ac93037f3.png","originSkin":"50_50_37_846","selectSkin":"50_50_38_759","pageId":"qi2rcnt41t","shangeType":"strange","skinIcon":true},{"badge":"0","badgeColor":"#ff3b30","highlightTextColor":"#2196f3","label":"旅游圈","originTextColor":"#666666","IfSelect":false,"imgUrl":"./img/nianka-active.png","SelectImgUrl":"./img/nianka.png","skinName":"icon.png","originSkin":"50_50_334_845","selectSkin":"50_50_336_758","pageId":"20kzxobghyu","shangeType":"strange","skinIcon":true},{"badge":"0","badgeColor":"#ff3b30","label":"年卡","originTextColor":"#666666","highlightTextColor":"#2196f3","IfSelect":false,"imgUrl":"./img/home.png","SelectImgUrl":"./img/home-active.png","skinName":"icon.png","originSkin":"50_50_185_845","selectSkin":"50_50_187_758","pageId":"","shangeType":"strange","skinIcon":true},{"badge":"0","badgeColor":"#ff3b30","label":"好友","originTextColor":"#666666","highlightTextColor":"#2196f3","IfSelect":false,"imgUrl":"./img/home.png","SelectImgUrl":"./img/home-active.png","skinName":"icon.png","originSkin":"50_50_484_845","selectSkin":"50_50_485_758","pageId":"","shangeType":"strange","skinIcon":true},{"badge":"0","badgeColor":"#ff3b30","label":"我的","originTextColor":"#666666","highlightTextColor":"#2196f3","IfSelect":false,"imgUrl":"./img/home.png","SelectImgUrl":"./img/home-active.png","skinName":"icon.png","originSkin":"50_50_634_845","selectSkin":"50_50_637_758","pageId":"","shangeType":"strange","skinIcon":true}]
         * height : 56
         */

        private boolean isHidden;
        private String name;
        private String tag;
        private boolean labelsOnly;
        private boolean iconsOnly;
        private int zIndex;
        private String bgColor;
        private int height;
        private List<LinksBean> links;
        private String skinName;
        private String originTextColor;
        private String highlightTextColor;

        public boolean isIsHidden() {
            return isHidden;
        }

        public void setIsHidden(boolean isHidden) {
            this.isHidden = isHidden;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getTag() {
            return tag;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }

        public boolean isLabelsOnly() {
            return labelsOnly;
        }

        public void setLabelsOnly(boolean labelsOnly) {
            this.labelsOnly = labelsOnly;
        }

        public boolean isIconsOnly() {
            return iconsOnly;
        }

        public void setIconsOnly(boolean iconsOnly) {
            this.iconsOnly = iconsOnly;
        }

        public int getZIndex() {
            return zIndex;
        }

        public void setZIndex(int zIndex) {
            this.zIndex = zIndex;
        }

        public String getBgColor() {
            return bgColor;
        }

        public void setBgColor(String bgColor) {
            this.bgColor = bgColor;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        public List<LinksBean> getLinks() {
            return links;
        }

        public void setLinks(List<LinksBean> links) {
            this.links = links;
        }

        public String getSkinName() {
            return skinName;
        }

        public void setSkinName(String skinName) {
            this.skinName = skinName;
        }

        public String getOriginTextColor() {
            return originTextColor;
        }

        public void setOriginTextColor(String originTextColor) {
            this.originTextColor = originTextColor;
        }

        public String getHighlightTextColor() {
            return highlightTextColor;
        }

        public void setHighlightTextColor(String highlightTextColor) {
            this.highlightTextColor = highlightTextColor;
        }

        public static class LinksBean {
            /**
             * badge : 0
             * badgeColor : #ff3b30
             * label : 首页
             * originTextColor : #666666
             * highlightTextColor : #2196f3
             * IfSelect : true
             * imgUrl : ./img/home.png
             * SelectImgUrl : ./img/home-active.png
             * skinName : /krt-module/file/image/20190416/14b24b1d0d0c470aab2b2a4ac93037f3.png
             * originSkin : 50_50_37_846
             * selectSkin : 50_50_38_759
             * pageId : qi2rcnt41t
             * shangeType : strange
             * skinIcon : true
             *
             * "ifModulePage": false,
             *           "url": "mine"
             */

            private String badge;
            private String badgeColor;
            private String label;
            private String originTextColor;
            private String highlightTextColor;
            private boolean IfSelect;
            private String imgUrl;
            private String SelectImgUrl;
            private String skinName;
            private String originSkin;
            private String selectSkin;
            private String pageId;
            private String shangeType;
            private boolean skinIcon;
            private String originImg;
            private String selectImg;
            private boolean ifH5;
            private boolean ifOpenPage;
            private String type = "1";

            private boolean ifModulePage;
            private String url;
            private String text;

            public String getText() {
                return text;
            }

            public void setText(String text) {
                this.text = text;
            }

            public boolean isIfOpenPage() {
                return ifOpenPage;
            }

            public void setIfOpenPage(boolean ifOpenPage) {
                this.ifOpenPage = ifOpenPage;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public boolean isIfH5() {
                return ifH5;
            }

            public void setIfH5(boolean ifH5) {
                this.ifH5 = ifH5;
            }

            private List<String> terminal;

            public List<String> getTerminal() {
                return terminal;
            }

            public void setTerminal(List<String> terminal) {
                this.terminal = terminal;
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

            public String getBadge() {
                return badge;
            }

            public void setBadge(String badge) {
                this.badge = badge;
            }

            public String getBadgeColor() {
                return badgeColor;
            }

            public void setBadgeColor(String badgeColor) {
                this.badgeColor = badgeColor;
            }

            public String getLabel() {
                return label;
            }

            public void setLabel(String label) {
                this.label = label;
            }

            public String getOriginTextColor() {
                return originTextColor;
            }

            public void setOriginTextColor(String originTextColor) {
                this.originTextColor = originTextColor;
            }

            public String getHighlightTextColor() {
                return highlightTextColor;
            }

            public void setHighlightTextColor(String highlightTextColor) {
                this.highlightTextColor = highlightTextColor;
            }

            public boolean isIfSelect() {
                return IfSelect;
            }

            public void setIfSelect(boolean IfSelect) {
                this.IfSelect = IfSelect;
            }

            public String getImgUrl() {
                return imgUrl;
            }

            public void setImgUrl(String imgUrl) {
                this.imgUrl = imgUrl;
            }

            public String getSelectImgUrl() {
                return SelectImgUrl;
            }

            public void setSelectImgUrl(String SelectImgUrl) {
                this.SelectImgUrl = SelectImgUrl;
            }

            public String getSkinName() {
                return skinName;
            }

            public void setSkinName(String skinName) {
                this.skinName = skinName;
            }

            public String getOriginSkin() {
                return originSkin;
            }

            public void setOriginSkin(String originSkin) {
                this.originSkin = originSkin;
            }

            public String getSelectSkin() {
                return selectSkin;
            }

            public void setSelectSkin(String selectSkin) {
                this.selectSkin = selectSkin;
            }

            public String getPageId() {
                return pageId;
            }

            public void setPageId(String pageId) {
                this.pageId = pageId;
            }

            public String getShangeType() {
                return shangeType;
            }

            public void setShangeType(String shangeType) {
                this.shangeType = shangeType;
            }

            public boolean isSkinIcon() {
                return skinIcon;
            }

            public void setSkinIcon(boolean skinIcon) {
                this.skinIcon = skinIcon;
            }

            public String getOriginImg() {
                return originImg;
            }

            public void setOriginImg(String originImg) {
                this.originImg = originImg;
            }

            public String getSelectImg() {
                return selectImg;
            }

            public void setSelectImg(String selectImg) {
                this.selectImg = selectImg;
            }
        }
    }
}
