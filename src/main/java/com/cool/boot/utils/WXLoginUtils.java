package com.cool.boot.utils;


import com.alibaba.fastjson.JSON;
import com.cool.boot.entity.WXLoginEntity;

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
        StringBuilder url = new StringBuilder(GET_CODE_URL);
        String res = "";
        try {
            res = URLEncoder.encode(redirectUri);
        } catch (Exception ignore) {
        }
        url.append("?appid=");
        url.append(AppID);
        url.append("&redirect_uri=");
        url.append(res);
        url.append("&response_type=code&scope=snsapi_login&state=2014#wechat_redirect");
        return url.toString();
    }

    //getAccess_token url
    private static String getTokenApi(String code) {
        return GET_TOKEN_URL + "?appid=" +
                AppID +
                "&secret=" +
                AppSecret +
                "&code=" +
                code +
                "&grant_type=authorization_code";
    }

    //refreshToken url
    private static String refreshTokenApi(String refreshToken) {
        return REFRESH_TOKEN_URL + "?appid=" + AppID + "&grant_type=refresh_token&refresh_token=" + refreshToken;
    }

    //checkToken url
    private static String checkTokenApi(String accessToken, String openId) {
        return AVAILABLE_TOKEN_URL + "?access_token=" + accessToken + "&openid=" + openId;
    }

    //getUserInfo url
    private static String getUserInfoApi(String accessToken, String openId) {
        return GET_USERINFO_URL + "?access_token=" + accessToken + "&openid=" + openId;
    }

    /*
        微信第三方登录接口业务
      */
    public static WXLoginEntity getUserInfo(String code) {

        //通过code拿到 access_token , openid 等
        String firstJson = OkHttpUtils.toGet(WXLoginUtils.getTokenApi(code));
        WXLoginEntity firstData = JSON.parseObject(firstJson, WXLoginEntity.class);

        if (firstData != null){
            //通过 access_token , openid 检验 access_token 是否有效
            String secondJson = OkHttpUtils.toGet(WXLoginUtils.checkTokenApi(firstData.getAccess_token(), firstData.getOpenid()));
            WXLoginEntity secondData = JSON.parseObject(secondJson, WXLoginEntity.class);

            if (secondData != null){

                if (secondData.getErrmsg().equals("ok")) {
                    //通过access_token和openid拿到用户信息，头像url保存起来
                    String thirdJson = OkHttpUtils.toGet(WXLoginUtils.getUserInfoApi(firstData.getAccess_token(),firstData.getOpenid()));
                    WXLoginEntity thirdData = JSON.parseObject(thirdJson,WXLoginEntity.class);

                    if (thirdData != null){
                        return thirdData;
                    }

                }else {
                    String refreshJson = OkHttpUtils.toGet(WXLoginUtils.refreshTokenApi(firstData.getRefresh_token()));
                    WXLoginEntity refreshData = JSON.parseObject(refreshJson,WXLoginEntity.class);

                    if (refreshData != null){

                        String resJson = OkHttpUtils.toGet(WXLoginUtils.getUserInfoApi(firstData.getAccess_token(),firstData.getOpenid()));
                        WXLoginEntity resData = JSON.parseObject(resJson,WXLoginEntity.class);

                        if (resData != null){
                            return resData;
                        }
                    }
                }
            }
        }

        return null;
    }

}
