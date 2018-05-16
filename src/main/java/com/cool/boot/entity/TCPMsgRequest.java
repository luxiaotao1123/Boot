package com.cool.boot.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @Auth Vincent
 */
@Getter
@Setter
@ToString
public class TCPMsgRequest {

    public static final int HEART_TYPE = 1;
    public static final int MESSAGE_TYPE = 2;

    private int type;

    private Integer msgLen;

    /*
        消息格式:   token:uid:msg
     */
    private String msg;

}
