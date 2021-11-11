package com.krt.base.util;

import android.content.Context;

import com.krt.base.ui.TranslucentActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * author:Marcus
 * create on:2020/3/27 10:35
 * description
 */
public class MPickUtil {

    /**
     * 选择文件数量
     */
    private int maxCount = 1;

    public static final int PHOTO = 1;


    public static final int DOC = 2;
    /**
     * 支持检索zip压缩文件
     */
    private boolean supportZip;
    /**
     * 支持检索pdf文件
     */
    private boolean supportPdf;
    /**
     * 支持检索文档
     */
    private boolean supportDoc;

    private int type;


    private ArrayList<String> photoPaths;

    private ArrayList<String> docsPath;

    private PickListener listener;

    public ArrayList<String> getPhotoPaths() {
        return photoPaths;
    }

    public ArrayList<String> getDocsPath() {
        return docsPath;
    }

    public PickListener getListener() {
        return listener;
    }

    private static class SingleTonHoler {
        private static MPickUtil INSTANCE = new MPickUtil();
    }

    public static MPickUtil getInstance() {
        return SingleTonHoler.INSTANCE;
    }

    private MPickUtil() {
        photoPaths = new ArrayList<>();
        docsPath = new ArrayList<>();
    }

    public MPickUtil addSupportPdf(boolean supportPdf) {
        this.supportPdf = supportPdf;
        return this;
    }

    public MPickUtil addSupportZip(boolean supportZip) {
        this.supportZip = supportZip;
        return this;
    }

    public MPickUtil addSupportDoc(boolean supportDoc) {
        this.supportDoc = supportDoc;
        return this;
    }

    public boolean isSupportDoc() {
        return supportDoc;
    }

    public boolean isSupportPdf() {
        return supportPdf;
    }

    public boolean isSupportZip() {
        return supportZip;
    }

    public MPickUtil setType(int type) {
        this.type = type;
        return this;
    }

    public MPickUtil setMaxCount(int maxCount) {
        this.maxCount = maxCount;
        return this;
    }

    public void pick(final Context context, PickListener pickListener) {
        this.listener = pickListener;
        TranslucentActivity.start(context, maxCount, type);
    }

    public interface PickListener {
        void result(List<String> paths);
    }
}
