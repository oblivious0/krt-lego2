package com.krt.base.ui;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.krt.base.util.MPermissions;
import com.krt.base.util.MPickUtil;
import com.krt.lego.R;

import java.util.ArrayList;

import droidninja.filepicker.FilePickerBuilder;
import droidninja.filepicker.FilePickerConst;
import droidninja.filepicker.models.sort.SortingTypes;


/**
 * author:Marcus
 * create on:2020/3/27 10:26
 * description
 */
public class TranslucentActivity extends FragmentActivity {
    private int maxCount = 1;

    private final int IMG_CODE = 10010;

    private final int FILE_CODE = 10011;

    private String[] zips = {".zip", ".rar"};

    private String[] pdfs = {".pdf"};

    private ArrayList<String> photoPaths;

    private ArrayList<String> docPaths;

    private FilePickerBuilder builder;
    //选择图片你的类型 1是图片 2是视频
    private int type = 1;

    public static void start(Context context, int maxCount, int type) {
        Intent intent = new Intent(context, TranslucentActivity.class);
        intent.putExtra("maxcount", maxCount);
        intent.putExtra("type", type);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        maxCount = getIntent().getIntExtra("maxcount", 1);
        type = getIntent().getIntExtra("type", 1);
        docPaths = MPickUtil.getInstance().getDocsPath();
        photoPaths = MPickUtil.getInstance().getPhotoPaths();
        MPermissions.getInstance().request(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, new MPermissions.PermissionListener() {
            @Override
            public void callBack(boolean value) {
                if (value) {
                    initFileBuilder();
                } else {
                    finish();
                }
            }
        });
    }

    private void initFileBuilder() {
        builder = FilePickerBuilder.getInstance()
                .setMaxCount(maxCount)
                .setSelectedFiles(type == 1 ? photoPaths : docPaths)
                .setActivityTheme(R.style.LibAppTheme)
                .withOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        if (type == 1) {
            builder.pickPhoto(this, IMG_CODE);
        } else {
            if (MPickUtil.getInstance().isSupportPdf()) {
                builder.addFileSupport("PDF", pdfs, R.drawable.pdf_blue);
            }
            if (MPickUtil.getInstance().isSupportZip()) {
                builder.addFileSupport("ZIP", zips);
            }
            builder.enableDocSupport(MPickUtil.getInstance().isSupportDoc())
                    .enableSelectAll(true)
                    .sortDocumentsBy(SortingTypes.name)
                    .pickFile(this, FILE_CODE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && data != null) {
            switch (requestCode) {
                case IMG_CODE:
                    if (!photoPaths.isEmpty()) photoPaths.clear();
                    photoPaths.addAll(data.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_MEDIA));
                    if (MPickUtil.getInstance().getListener() != null) {
                        MPickUtil.getInstance().getListener().result(photoPaths);
                    }
                    finish();
                    break;
                case FILE_CODE:
                    if (!docPaths.isEmpty()) docPaths.clear();
                    docPaths.addAll(data.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_DOCS));
                    if (MPickUtil.getInstance().getListener() != null) {
                        MPickUtil.getInstance().getListener().result(docPaths);
                    }
                    finish();
                    break;
                default:
                    break;
            }
        } else {
            finish();
        }
    }
}
