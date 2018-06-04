package com.cool.boot.controller;

import com.cool.boot.entity.Response;
import com.cool.boot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author Vincent
 * 2018-5-6
 */

@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("login")
    public Response login(){
        return userService.login();
    }

    /**
     * 给予授权
     * @param code  常量"code"
     * @param clientId  用户id
     * @param redirectUrl   客户端服务回调域
     * @param scope     授权范围
     * @param state     客户端状态码
     * @return  200成功
     */
    @GetMapping("authconnect")
    public Response authConnect(String code,
                                @RequestParam(value = "client_id")String clientId,
                                @RequestParam(value = "redirect_url",required = false) String redirectUrl,
                                @RequestParam(value = "scope",required = false)String scope,
                                @RequestParam(value = "state",required = false)String state){

        return userService.authConnect(code, clientId, redirectUrl, scope, state);

    }


    /**
     * 获取认证令牌
     * @param clientId  客户端ID
     * @param clientSecret  客户端密钥
     * @param code  授权接口返回的授权码code
     * @param grantType 常量"authorization_code"
     * @return  200成功
     */
    @GetMapping("access_token")
    public Response getAccessToken(@RequestParam(value = "client_id",required = false)String clientId,
                                   @RequestParam(value = "client_secret",required = false)String clientSecret,
                                   @RequestParam(value = "code",required = false)String code,
                                   @RequestParam(value = "grant_type",required = false)String grantType){

        return userService.getAccessToken(clientId, clientSecret,code, grantType);
    }

}
