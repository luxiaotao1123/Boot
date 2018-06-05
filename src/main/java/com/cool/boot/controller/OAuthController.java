package com.cool.boot.controller;

import com.cool.boot.entity.Response;
import com.cool.boot.service.OAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author Vincent
 * 2018-5-6
 */

@RestController
@RequestMapping("user")
public class OAuthController {

    @Autowired
    private OAuthService oAuthService;


    /**
     * 给予授权
     * @param responseType  常量"code"    true
     * @param clientId  用户id        true
     * @param redirectUrl   客户端服务回调域    false
     * @param scope     授权范围        false
     * @param state     客户端状态码      false
     * @return  200成功
     */
    @GetMapping("authconnect")
    public Response authConnect(@RequestParam(value = "response_type") String responseType,
                                @RequestParam(value = "client_id")String clientId,
                                @RequestParam(value = "redirect_url",required = false) String redirectUrl,
                                @RequestParam(value = "scope",required = false)String scope,
                                @RequestParam(value = "state",required = false)String state){

        return oAuthService.authConnect(responseType, clientId, redirectUrl, scope, state);

    }


    /**
     * 获取认证令牌
     * @param clientId  客户端ID       true
     * @param clientSecret  客户端密钥   true
     * @param code  授权接口返回的授权码code      true
     * @param grantType 常量"authorization_code"      true
     * @return  200成功
     */
    @GetMapping("access_token")
    public Response getAccessToken(@RequestParam(value = "client_id",required = false)String clientId,
                                   @RequestParam(value = "client_secret",required = false)String clientSecret,
                                   @RequestParam(value = "code",required = false)String code,
                                   @RequestParam(value = "grant_type",required = false)String grantType){

        return oAuthService.getAccessToken(clientId, clientSecret,code, grantType);
    }

    /**
     * 刷新access_token
     * @param clientId  客户端ID       true
     * @param refreshToken  true
     * @param grantType     常量"refresh_token"      true
     * @return  200成功
     */
    @GetMapping("refresh_token")
    public Response refreshToken(@RequestParam(value = "client_id",required = false)String clientId,
                                 @RequestParam(value = "refresh_token",required = false)String refreshToken,
                                 @RequestParam(value = "grant_type",required = false)String grantType){

        return oAuthService.refreshToken(clientId, refreshToken, grantType);
    }
}
