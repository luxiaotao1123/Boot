package com.cool.boot.enums;

/**
 * Created by 97947 on 2017/7/22.
 */
public enum TaskTypeEnum {
    DEMO_KEY("demo"),;
    private String value;

    public String getValue() {
        return value;
    }

    TaskTypeEnum(String value) {
        this.value = value;
    }
}
