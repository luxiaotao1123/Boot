package com.cool.boot.service;

import com.cool.boot.entity.Response;

/**
 * @author Vincent
 */
public interface UserService {

    Response login();

    Response authConnect(String code, String clientId, String redirectUrl, String scope, String state);

    Response getAccessToken(String clientId,String clientSecret,String code,String grantType);
}
