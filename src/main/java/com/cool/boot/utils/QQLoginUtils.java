package com.cool.boot.utils;

import com.cool.boot.entity.QQLoginEntity;

import java.net.URLEncoder;

public class QQLoginUtils {

    //appId appSecret
    private static final String AppID = "";
    private static final String AppSecret = "";

    //1.获取Code Url
    private static final String GET_CODE_URL = "https://graph.qq.com/oauth2.0/authorize";

    //2.通过code获取AccessToken Url
    private static final String GET_TOKEN_URL = "https://graph.qq.com/oauth2.0/token";

    //3.根据refreshToken刷新AccessToken有效期 url
    private static final String REFRESH_TOKEN_URL = "https://graph.qq.com/oauth2.0/token";

    //4.根据accessToken拿到openId url
    private static final String GET_OPENID_URL = "https://graph.qq.com/oauth2.0/me";

    //5.根据accessToken，oauthConsumerKey，openId拿到userInfo url
    private static final String GET_USERINFO_URL = "https://graph.qq.com/user/get_user_info";

    //1.获取Code API
    public static String getCodeApi(String redirectUri, String state) {
        String codeRes = "";
        try {
            codeRes = URLEncoder.encode(redirectUri);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return GET_CODE_URL + "?response_type=code&client_id=" + AppID + "&redirect_uri=" + codeRes + "&state=" + state + "&scope=get_user_info";
    }

    //2.通过code获取AccessToken API
    public static String getTokenApi(String code, String redirectUri) {
        String tokenRes = "";
        try {
            tokenRes = URLEncoder.encode(redirectUri);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return GET_TOKEN_URL + "?grant_type=authorization_code&client_id=" + AppID + "&client_secret=" + AppSecret + "&code=" + code + "&redirect_uri=" + tokenRes;
    }

    //3.根据refreshToken刷新AccessToken有效期 API
    public static String refreshTokenApi(String refreshToken) {
        return REFRESH_TOKEN_URL + "?grant_type=refresh_token&client_id=" + AppID + "&client_secret=" + AppSecret + "&refresh_token=" + refreshToken;
    }

    //4.根据accessToken拿到openId API
    public static String getOpenIdApi(String accessToken) {
        return GET_OPENID_URL + "?access_token=" + accessToken;
    }

    //5.根据accessToken，oauthConsumerKey，openId拿到userInfo API
    public static String getUserInfoApi(QQLoginEntity data) {
        return GET_USERINFO_URL + "?access_token=" + data.getAccess_token() + "&oauth_consumer_key=" + AppID + "&openid" + data.getOpenid();
    }


}
