package com.example.user.base;

import lombok.Data;

@Data
public class ResultJson<T> {
    private int code = 0;
    private String msg;
    private T data;

    public static ResultJson success(Object object) {
        ResultJson result = new ResultJson();
        result.setCode(200);
        result.setMsg("成功");
        result.setData(object);
        return result;
    }

    public static ResultJson error(String msg) {
        ResultJson result = new ResultJson();
        result.setCode(500);
        result.setMsg(msg);
        return result;
    }

    public static ResultJson noAccess(String msg) {
        ResultJson result = new ResultJson();
        result.setCode(403);
        if(msg.length()==0) msg = "没有权限";
        result.setMsg(msg);
        return result;
    }
}
