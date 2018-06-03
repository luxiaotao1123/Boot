package com.cool.boot.service.impl;

import com.cool.boot.entity.Response;
import com.cool.boot.enums.HttpStatusEnum;
import com.cool.boot.service.UserService;
import com.cool.boot.utils.WXLoginUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author Vincent
 */
@Service("userServiceImpl")
@Transactional(rollbackFor = Exception.class)
public class UserServiceImpl implements UserService {

    private static String CODE = "code";
    private static String AUTHORIZATION_CODE = "authorization_code";


    /**
     * 登录
     * @return
     */
    @Override
    public Response login() {
        return null;
    }



    @Override
    public Response authConnect(String code, String clientId, String redirectUrl, String scope, String state) {

        if (code == null || StringUtils.isEmpty(code)){
            return Response.error(HttpStatusEnum.EMTRY_PARAMS.getCode(),"授权码code不能为空");
        } else if (!CODE.equals(code)){
            return Response.error("授权码只能为code");
        } else if (clientId == null || StringUtils.isEmpty(clientId)){
            return Response.error(HttpStatusEnum.EMTRY_PARAMS.getCode(),"client_id不能为空");
        }//todo redirectUrl合法判断

        Map<String, Object> res = new HashMap<>(16);

        //todo 存入redis，生成指定jwt，有效时间设置
        String resCode = UUID.randomUUID().toString();
        res.put("code",resCode);

        if (state != null) {
            res.put("state", state);
        }

        if (redirectUrl != null){

            StringBuilder getUrl = new StringBuilder(redirectUrl);
            getUrl.append("?code=");
            getUrl.append(resCode);
            if (state != null){
                res.put("state",state);

                getUrl.append("&state=");
                getUrl.append(state);
            }

            WXLoginUtils.toGet( getUrl.toString());

        }

        return Response.ok(res);
    }



    @Override
    public Response getAccessToken(String clientId, String clientSecret, String code, String grantType) {

        if (clientId == null || StringUtils.isEmpty(clientId)){
            return Response.error(HttpStatusEnum.EMTRY_PARAMS.getCode(),"client_id不能为空");
        }else if (clientSecret == null || StringUtils.isEmpty(clientSecret)){
            return Response.error(HttpStatusEnum.EMTRY_PARAMS.getCode(),"client_secret不能为空");
        }else if (code == null || StringUtils.isEmpty(code)){
            return Response.error(HttpStatusEnum.EMTRY_PARAMS.getCode(),"授权码code不能为空");
        }else if (grantType == null || !grantType.equals(AUTHORIZATION_CODE)){
            return Response.error("grant_type参数错误");
        }

        //todo code判断（redis）

        //todo jwt加密生成accessToken 并且设置有效时间expires_in，单位为秒

        //todo refreshToken的生成

        Map<String, Object> res = new HashMap<>();

        res.put("access_token",null);
        res.put("expires_in",null);
        res.put("refresh_token",null);
        res.put("scope",null);

        return Response.ok(res);
    }

}
