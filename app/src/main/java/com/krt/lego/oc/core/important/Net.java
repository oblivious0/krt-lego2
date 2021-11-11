package com.krt.lego.oc.core.important;

import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * author: MaGua
 * create on:2021/2/27 15:39
 * description
 */
public class Net {

    public static final HashMap<String, JSONObject> requestBodys = new HashMap<>();

    static List<String> prod = new ArrayList<>();
    static List<String> dev = new ArrayList<>();
    static int defaultIndex = 0;


    public static final String Base_Module = "https://www.krtservice.com/krt-module/apis/module/getLastVersionInfo";
    public static final String jsonUrl = "https://version.krtservice.com/";

    public static int sucCode = 200;

}
