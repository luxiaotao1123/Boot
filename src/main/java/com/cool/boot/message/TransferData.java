package com.cool.boot.message;

import com.cool.boot.enums.RabbitTypeEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;


import java.io.Serializable;

/**
 * 传输的数据格式
 *
 * @Auth Vincent
 */
public class TransferData implements Serializable {
    private static final long serialVersionUID = 2838267042452729324L;


    @JsonIgnore
    private RabbitTypeEnum rabbitTypeEnum;

    /**
     * 数据
     */
    private String data;


    public TransferData() {

    }

    private final String START_SING = "\"";

    public String getData() {
        if (data.startsWith(START_SING)) {
            data = data.substring(1, data.length() - 1).replaceAll("\\\\", "");
        }
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public RabbitTypeEnum getRabbitTypeEnum() {
        return rabbitTypeEnum;
    }

    public void setRabbitTypeEnum(RabbitTypeEnum rabbitTypeEnum) {
        this.rabbitTypeEnum = rabbitTypeEnum;
    }


    @Override
    public String toString() {
        return "TransferData{" +
                "rabbitTypeEnum=" + rabbitTypeEnum +
                ", data=" + data +
                '}';
    }
}
