package com.cool.boot.service;

import com.cool.boot.entity.Response;

/**
 * @author Vincent
 */
public interface OAuthService {

    Response authConnect(String responseType, String clientId, String redirectUrl, String scope, String state);

    Response getAccessToken(String clientId, String clientSecret, String code, String grantType);

    Response refreshToken(String clientId, String refreshToken, String grantType);
}
