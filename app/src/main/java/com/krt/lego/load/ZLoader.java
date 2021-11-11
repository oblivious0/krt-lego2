package com.krt.lego.load;

import android.annotation.SuppressLint;
import android.text.TextUtils;

import com.blankj.utilcode.util.EncryptUtils;
import com.blankj.utilcode.util.FileIOUtils;
import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.SPStaticUtils;
import com.krt.Lego;
import com.krt.base.net.Result;
import com.krt.base.net.callback.JsonCallback;
import com.krt.base.util.ParseJsonUtil;
import com.krt.lego.Constants;
import com.krt.lego.load.info.ApplicationVersionInfo;
import com.krt.lego.load.info.NetResult;
import com.krt.lego.oc.core.bean.MPageInfoBean;
import com.krt.lego.oc.core.important.Net;
import com.krt.lego.oc.core.important.NetDispatch;
import com.krt.lego.oc.core.important.SkinManager;
import com.krt.lego.oc.util.LegoUtil;
import com.krt.lego.oc.util.TerminalUtil;
import com.krt.lego.oc.variable.LegoValue;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.FileCallback;
import com.lzy.okgo.model.Response;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.WeakHashMap;

import io.reactivex.Observable;

import static com.krt.Lego.VERSION_BETA;
import static com.krt.Lego.VERSION_HISTORY;
import static com.krt.Lego.VERSION_OFFICIAL;
import static com.krt.Lego.getVersionInfo;

/**
 * author: MaGua
 * create on:2021/2/27 10:31
 * description 新*版本解析器
 */
public class ZLoader {

    //项目加载信息 Start *********************************

    /**
     * 项目编号
     */
    private String code = "";
    /**
     * 项目状态
     */
    private String paramIsPublish = "";
    /**
     * 项目版本
     */
    private String paramVersion = "";

    //项目加载信息 End  **********************************

    private LoaderListener loaderListener;

    /**
     * 下载信息 K：保存文件名 V：下载路径
     */
    private WeakHashMap<String, String> downUrl = new WeakHashMap<>();

    /**
     * 下载总进度（需要下载的文件总数量）
     */
    private int maxProgress = -1;
    /**
     * 当前下载进度（当前下载完成的文件数量）
     */
    private int currentProgress = -1;

    private ZLoader(Builder builder) {
        this.code = builder.code;
        this.paramIsPublish = builder.isPublish;
        this.paramVersion = builder.version;
        this.loaderListener = builder.listener;
    }

    /**
     * 检查版本
     * 根据版本决定版本信息的渠道
     */
    public void checkVer() {

        switch (paramIsPublish) {
            case VERSION_BETA:
                resovlerJson(Net.jsonUrl + code + "-Beta" + LegoValue.JSON);
                break;
            case VERSION_OFFICIAL:
                resovlerJson(Net.jsonUrl + code + LegoValue.JSON);
                break;
            default:
                //测试版，开发版，或者模块化预览器App（未指定isPublish参数）
                requestVersion();
                break;
        }
    }

    /**
     * 测试版/开发版以及模块化预览器 获得版本信息
     */
    private void requestVersion() {

        OkGo.<Result<NetResult>>get(Net.Base_Module)
                .params("terminalVersion", Lego.getLegoInfo().terminal_version)
                .params("terminalCode", Lego.getLegoInfo().terminal)
                .params("interpreterCode", Lego.getLegoInfo().compiler_version)
                .params("tag", code)
                .params("versionStatus", paramIsPublish)
                .params("version", paramVersion)
                .params("t", System.currentTimeMillis())
                .execute(new JsonCallback<Result<NetResult>>() {

                    boolean isComplete = false;
                    ApplicationVersionInfo info;

                    @Override
                    public void onSuc(Response<Result<NetResult>> response) {
                        Result<NetResult> result = response.body();
                        //可以通过baseConfig改变sucCode码，不建议使用isSuccess()
                        if (result.code == Net.sucCode) {
                            isComplete = true;
                            info = result.data.getLast_version();
                            //因为测试版、开发版和模块化预览器的使用情景，不提示任何更新;
                            //模块化预览器自行检测是否存在更新
                        }
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        if (isComplete) {
                            startAnalyse(info);
                        } else {
                            //如果是历史发布版，需要解析本地资源
                            if (paramIsPublish.equals(VERSION_HISTORY)) {
                                startAnalyse(fail());
                            }
                        }
                    }
                });
    }

    /**
     * 当用户未选择升级，则调用历史版本
     */
    public void getHistoryVersion() {
        paramIsPublish = VERSION_HISTORY;
        requestVersion();
    }

    /**
     * 正式APP获取版本信息
     *
     * @param jsonHttp
     */
    private void resovlerJson(String jsonHttp) {
        OkGo.<ApplicationVersionInfo>get(jsonHttp)
                .execute(new JsonCallback<ApplicationVersionInfo>() {
                    @Override
                    public void onSuc(Response<ApplicationVersionInfo> response) {
                        ApplicationVersionInfo result = response.body();
                        //先检测是否需要更新
                        switch (TerminalUtil.isAndroidUp(result.getInterpreterCode()
                                , result.getTerminal())) {
                            case Compel:
                                coerceUpdate();
                                break;
                            case General:
                                //即使模块化解析器无需更新，也可能出现App因业务需求改动需要更新的情况
                                update();
                                getHistoryVersion();
                                break;
                            case Unnecessary:
                                update();
                                startAnalyse(result);
                            default:

                                break;
                        }
                    }

                    @Override
                    public void onError(Response<ApplicationVersionInfo> response) {
                        super.onError(response);
                        startAnalyse(fail());
                    }
                });
    }

    /**
     * 开始解析
     * 步骤1：保存项目基本信息
     * 步骤2：判断皮肤是否需要更新
     * 步骤3：筛选需要更新的Json文件
     * 步骤4：筛选需要下载的资源文件
     *
     * @param info
     */
    @SuppressLint("CheckResult")
    private void startAnalyse(ApplicationVersionInfo info) {
        if (info == null) return;
        //保存项目信息
        if (!TextUtils.isEmpty(paramIsPublish)) {
            info.setStatus(paramIsPublish);
        }
        Lego.setVersionInfo(info);

        //保存API信息
        NetDispatch.initApi();
        assembleApi();

        //判断皮肤版本
        int currentVer = SPStaticUtils.getInt("skinVer", -1);
        if (currentVer != info.getSkin().getSkinVersion()) {
            //如果皮肤版本不正确，需要清除所有切图，重新下载皮肤
            if (LegoUtil.isDevStatic())
                downUrl.put(SkinManager.defaultFile, info.getSkin().getUrl());
            else
                downUrl.put(SkinManager.defaultFile, info.getSkin().getCdnUrl());

            FileUtils.deleteFilesInDir(Constants.iconPath);
        }

        SkinManager.load(info.getSkin());

        if (info.getStatus().equals(Lego.VERSION_ALPHA) || info.getStatus().equals(Lego.VERSION_DEVELOP)) {
            OkGo.<Result<List<MPageInfoBean>>>get("https://www.krtservice.com/krt-module/apis/module/getPageList")
                    .params("withConfig", 1)
                    .params("pageSize", 100)
                    .params("tag", info.getCode())
                    .params("currentPage", 0)
                    .params("t", System.currentTimeMillis())
                    .params("version", info.getVersion())
                    .execute(new JsonCallback<Result<List<MPageInfoBean>>>() {
                        @Override
                        public void onSuc(Response<Result<List<MPageInfoBean>>> response) {
                            if (response.body().code == 200) {
                                for (MPageInfoBean bean : response.body().data) {
                                    String filePath = Constants.jsonPath + bean.getPage_code() + LegoValue.JSON;
                                    if (FileUtils.isFileExists(filePath)) {
                                        //如果存在，对比MD5
                                        String md5 = bean.getPage_config_md5().toUpperCase();
                                        if (md5.equals(EncryptUtils.encryptMD5File2String
                                                (filePath).toUpperCase())) {
                                            continue;
                                        }
                                    }
                                    FileUtils.createFileByDeleteOldFile(filePath);
                                    FileIOUtils.writeFileFromString(filePath, bean.getPage_config());
                                }
                            }
                        }

                        @Override
                        public void onFinish() {
                            super.onFinish();
                            download();
                        }
                    });
        } else {
            for (ApplicationVersionInfo.PageListBean bean : info.getPageList()) {
                String jsonName = bean.getPageCode() + LegoValue.JSON;
                //判断是否有同名文件，没有直接下载
                if (FileUtils.isFileExists(Constants.jsonPath + jsonName)) {
                    //如果存在，对比MD5，如果不匹配需要更新
                    String md5 = bean.getPageConfigMd5().toUpperCase();
                    if (md5.equals(EncryptUtils.encryptMD5File2String
                            (Constants.jsonPath + jsonName).toUpperCase())) {
                        continue;
                    }
                }
                downUrl.put(jsonName, bean.getFileUrl());
            }

            for (ApplicationVersionInfo.ResListBean bean : info.getResList()) {
                if (!FileUtils.isFileExists(Constants.imgPath + bean.getFileName())) {
                    downUrl.put(bean.getFileName(), bean.getImageUrl());
                }
            }
            download();
        }

        String versionJson = ParseJsonUtil.toJson(info);
        if (FileUtils.isFileExists(Constants.defPath + "version.json")) {
            String md5 = EncryptUtils.encryptMD5File2String(Constants.defPath + "version.json");
            if (md5.equals(EncryptUtils.encryptMD5ToString(versionJson))) return;
        }

        saveVersion(versionJson);

    }

    private void saveVersion(String info) {

        if (!FileUtils.createOrExistsDir(Constants.defPath)) {
            return;
        }

        FileOutputStream outputStream = null;
        try {
            File file = new File(Constants.defPath + "version.json");
            FileUtils.createFileByDeleteOldFile(file);
            outputStream = new FileOutputStream(file);//形参里面可追加true参数，表示在原有文件末尾追加信息
            outputStream.write(info.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @SuppressLint("CheckResult")
    private void download() {
        maxProgress = downUrl.size();
        currentProgress = 0;

        if (maxProgress == 0) {
            finish(getVersionInfo().getAppInfo().getStartType(),
                    getVersionInfo().getAppInfo().getStartPageId());
            return;
        }

        //遍历map循环下载
        Observable.fromIterable(downUrl.keySet())
                .subscribe(s -> {

                    String path = Constants.imgPath;
                    if (s.contains(LegoValue.JSON))
                        path = Constants.jsonPath;

                    OkGo.<File>get(downUrl.get(s))
                            .execute(new FileCallback(path, s) {
                                @Override
                                public void onSuccess(Response<File> response) {

                                }

                                @Override
                                public void onFinish() {
                                    super.onFinish();
                                    currentProgress++;
                                    progressBack();
                                    if (currentProgress == maxProgress) {
                                        finish(getVersionInfo().getAppInfo().getStartType(),
                                                getVersionInfo().getAppInfo().getStartPageId());
                                    }
                                }
                            });
                });

    }

    /**
     * Builder模式
     */
    public static Builder newBuilder() {
        return new Builder();
    }

    public final static class Builder {

        private Builder() {
        }

        private String code = "";
        private String isPublish = "";
        private String version = "";
        private LoaderListener listener;

        public Builder setCode(String code) {
            this.code = code;
            return this;
        }

        public Builder setIsPublish(String isPublish) {
            this.isPublish = isPublish;
            return this;
        }

        public Builder setVersion(String version) {
            this.version = version;
            return this;
        }

        public Builder setLoaderListener(LoaderListener listener) {
            this.listener = listener;
            return this;
        }

        public ZLoader build() {
            return new ZLoader(this);
        }

    }

    public interface LoaderListener {
        /**
         * 需要强制更新
         */
        void coerceUpdate();

        /**
         * 需要更新
         * 执行到这后需要手动调用执行下一步步骤
         */
        void update();

        /**
         * API地址加载完成
         */
        void assembleApi();

        /**
         * 下载进度回馈
         *
         * @param max
         * @param current
         */
        void progressBack(int max, int current);

        /**
         * 加载完成
         *
         * @param type 启动页类型
         * @param cid  启动页cid
         */
        void finish(String type, String cid);

        /**
         * 信息加载失败
         */
        ApplicationVersionInfo fail();
    }

    private void coerceUpdate() {
        if (loaderListener != null) loaderListener.coerceUpdate();
    }

    private void update() {
        if (loaderListener != null) loaderListener.update();
    }

    private void assembleApi() {
        if (loaderListener != null) loaderListener.assembleApi();
    }

    private void progressBack() {
        if (loaderListener != null) loaderListener.progressBack(maxProgress, currentProgress);
    }

    private void finish(String type, String cid) {
        if (loaderListener != null) loaderListener.finish(type, cid);
    }

    private ApplicationVersionInfo fail() {
        if (loaderListener != null) return loaderListener.fail();
        return null;
    }

    public void setLoaderListener(LoaderListener loaderListener) {
        this.loaderListener = loaderListener;
    }

}
