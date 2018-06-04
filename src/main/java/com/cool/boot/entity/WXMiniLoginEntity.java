package com.cool.boot.entity;

import lombok.Data;

@Data
public class WXMiniLoginEntity {

    private String openid;
    private String session_key;
    private String unionid;


}
