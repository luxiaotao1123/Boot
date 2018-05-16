package com.cool.boot.entity;



import com.cool.boot.enums.HttpStatusEnum;

import java.util.HashMap;
import java.util.Map;

public class Response extends HashMap<String, Object>{

    public final static String CODE = "code";
    public final static String MSG = "msg";

    private static final long serialVersionUID = 1L;

    public Response() {
        put(CODE, HttpStatusEnum.SUCCESS.getCode());
        put(MSG,HttpStatusEnum.SUCCESS.getValue());
    }

    public static Response error() {
        return error(HttpStatusEnum.ERROR.getCode(), HttpStatusEnum.ERROR.getValue());
    }

    public static Response error(String msg) {
        return error(HttpStatusEnum.ERROR.getCode(), msg);
    }

    public static Response error(int code, String msg) {
        Response r = new Response();
        r.put(CODE, code);
        r.put(MSG, msg);
        return r;
    }

    public static Response ok(String msg) {
        Response r = new Response();
        r.put(MSG, msg);
        return r;
    }

    public static Response ok(Map<String, Object> map) {
        Response r = new Response();
        r.putAll(map);
        return r;
    }

    public static Response ok() {
        return new Response();
    }

    public Response put(String key, Object value) {
        super.put(key, value);
        return this;
    }
}
