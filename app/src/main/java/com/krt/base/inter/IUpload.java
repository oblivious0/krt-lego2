package com.krt.base.inter;

import java.io.File;
import java.util.List;

/**
 * @author Marcus
 * @package krt.wid.http.upload
 * @description
 * @time 2018/2/23
 */

public interface IUpload {
    //压缩图片的方法
    void compress();

    //上传图片
    void doUpload(List<File> files);
}
