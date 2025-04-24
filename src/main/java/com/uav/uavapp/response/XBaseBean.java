package com.uav.uavapp.response;

import java.io.Serializable;

/**
 * Description : XBaseBean 实体类的基类
 */
public class XBaseBean<T> implements Serializable {

    private int code;
    private String msg;
    private T data;
    private String md5;
    private boolean succ;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public boolean isSucc() {
        return succ;
    }

    public void setSucc(boolean succ) {
        this.succ = succ;
    }
}
