package com.cool.boot.enums;

/**
 * @Auth Vincent
 */
public enum TCPStatusEnum {

    SUCCESS(200, "请求成功"),
    ERROR(500, "服务器异常"),
    LACK_DIAMONDS(380, "钻石不足,请充值"),
    NO_DIAMONDS(381, "钻石不足，通话已中断"),;

    private Integer code;
    private String value;

    TCPStatusEnum(Integer code, String value) {
        this.code = code;
        this.value = value;
    }

    public Integer getCode() {
        return code;
    }


    public String getValue() {
        return value;
    }

}
