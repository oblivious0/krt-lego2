package krt.wid.bit.net;

public class Result<T> {
    public String msg;

    public int code;

    public T data;

    public boolean isSuccess() {
        return code == 200;
    }

}
