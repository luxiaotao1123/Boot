package com.cool.boot.exception;

import com.cool.boot.enums.HttpStatusEnum;

/**
 * @Auth Vincent
 */

public class CoolException extends RuntimeException{

    private static final long serialVersionUID = 1L;

    private String msg;
    private int code = HttpStatusEnum.ERROR.getCode();

    public CoolException(String msg) {
        super(msg);
        this.msg = msg;
    }

    public CoolException(String msg, Throwable e) {
        super(msg, e);
        this.msg = msg;
    }

    public CoolException(String msg, int code) {
        super(msg);
        this.msg = msg;
        this.code = code;
    }

    public CoolException(String msg, int code, Throwable e) {
        super(msg, e);
        this.msg = msg;
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

}
