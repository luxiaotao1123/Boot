package com.cool.boot.enums;

/**
 * @author Vincent
 */
public enum  ExecutorsEnum {

    COOL_EXECUTORS("coolExecutors",0),
    ;

    private String value;
    private Integer code;

    ExecutorsEnum(String value, Integer code){
        this.value = value;
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public Integer getCode() {
        return code;
    }

    public static ExecutorsEnum getExecutorsEnum(String param){

        ExecutorsEnum[] values = ExecutorsEnum.values();

        for (ExecutorsEnum value : values){
            if (param.equals(value.getValue())){

                return value;
            }
        }

        return null;
    }
}
