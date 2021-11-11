package com.krt.base.net;
import com.krt.Lego;

public class Result<T> {
    public String msg;

    public int code;

    public T data;

    public boolean isSuccess() {
        return code == Lego.getBaseConfig().getServerSuccessCode();
    }

}
