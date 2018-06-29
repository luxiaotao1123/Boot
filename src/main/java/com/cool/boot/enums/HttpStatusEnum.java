package com.cool.boot.enums;

/**
 * @Auth Vincent
 */
public enum HttpStatusEnum {

    SUCCESS(200, "请求成功"),
    ERROR(500, "服务器异常"),
    TOKEN_ERROR(401, "token无效"),
    EMPTY_PARAMS(402, "参数为空"),;

    private Integer code;
    private String value;

    HttpStatusEnum(Integer code, String value) {
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
