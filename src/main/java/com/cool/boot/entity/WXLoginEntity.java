package com.cool.boot.entity;

import lombok.Data;

@Data
public class WXLoginEntity {

    private String errcode;
    private String errmsg;
    private String access_token;
    private String expires_in;
    private String refresh_token;
    private String openid;
    private String scope;
    private String unionid;
    private String nickname;
    private String sex;
    private String province;
    private String city;
    private String country;
    private String headimgurl;
    private String privilege;
}
