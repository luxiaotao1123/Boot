package com.cool.boot.utils;


import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.net.URLEncoder;


public class WXLoginUtils {

    //appId appSecret
    private static final String AppID = "wx4a300c4010f4be69";
    private static final String AppSecret = "66fb35add2f7242f24102a9380115abb";

    //getCodeApi url
    private static final String GET_CODE_URL = "https://open.weixin.qq.com/connect/qrconnect";

    //getAccess_token url
    private static final String GET_TOKEN_URL = "https://api.weixin.qq.com/sns/oauth2/access_token";

    //refreshToken url
    private static final String REFRESH_TOKEN_URL = "https://api.weixin.qq.com/sns/oauth2/refresh_token";

    //checkToken url
    private static final String AVAILABLE_TOKEN_URL = "https://api.weixin.qq.com/sns/auth";

    //getUserInfo url
    private static final String GET_USERINFO_URL = "https://api.weixin.qq.com/sns/userinfo";

    //getCodeApi url
    public static String getCodeApi(String redirectUri) {
        String res = "";
        try {
            res = URLEncoder.encode(redirectUri);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return GET_CODE_URL + "?appid=" + AppID + "&redirect_uri=" + res + "&response_type=code&scope=snsapi_login&state=2014#wechat_redirect";
    }

    //getAccess_token url
    public static String getTokenApi(String code) {
        return GET_TOKEN_URL + "?appid=" + AppID + "&secret=" + AppSecret + "&code=" + code + "&grant_type=authorization_code";
    }

    //refreshToken url
    public static String refreshTokenApi(String refreshToken) {
        return REFRESH_TOKEN_URL + "?appid=" + AppID + "&grant_type=refresh_token&refresh_token=" + refreshToken;
    }

    //checkToken url
    public static String checkTokenApi(String accessToken, String openId) {
        return AVAILABLE_TOKEN_URL + "?access_token=" + accessToken + "&openid=" + openId;
    }

    //getUserInfo url
    public static String getUserInfoApi(String accessToken, String openId) {
        return GET_USERINFO_URL + "?access_token=" + accessToken + "&openid=" + openId;
    }

    public static String toGet(String uri) {
        try {
            Request request = new Request.Builder().url(uri).build();
            Response response = new OkHttpClient.Builder().build().newCall(request).execute();
            return response.isSuccessful() ? response.body().string() : null;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        System.out.println(toGet(getCodeApi("meiheyoupin.com")));
        toGet(getCodeApi("meiheyoupin.com"));       //拿到code
//        toGet(getTokenApi("code"));                  //通过code拿到access_token
//        toGet(refreshTokenApi("refreshToken"));              //通过refresh_token刷新access_token
//        toGet(checkTokenApi("accessToken", "openId"));       //通过access_token和openid检查access_token是否还有效
//        toGet(getUserInfoApi("accessToken", "openId"));      //通过access_token和openid拿到用户信息，头像url保存起来
    }
}
