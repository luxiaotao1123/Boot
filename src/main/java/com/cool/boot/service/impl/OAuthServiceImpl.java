package com.cool.boot.service.impl;

import com.cool.boot.entity.Response;
import com.cool.boot.enums.HttpStatusEnum;
import com.cool.boot.service.OAuthService;
import com.cool.boot.utils.JwtToken;
import com.cool.boot.utils.OkHttpUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static com.cool.boot.utils.JwtToken.*;

/**
 * @author Vincent
 */
@Slf4j
@Service("userServiceImpl")
@Transactional(rollbackFor = Exception.class)
public class OAuthServiceImpl implements OAuthService {

    private static String LINK = "-";
    private static String CODE = "code";
    private static String AUTHORIZATION_CODE = "authorization_code";
    private static String REFRESH_TOKEN = "refresh_token";
    private static String OAUTH_CODE = "oAuth:code:%s";
    private static String OAUTH_KEY = "oAuth:key:%s";
    private static String OAUTH_ACCESS_TOKEN = "oAuth:access_token:%s";
    private static String OAUTH_REFRESH_TOKEN = "oAuth:refresh_token:%s";

    @Resource
    private RedisTemplate<String, String> oAuthCache;

    /**
     * 获取授权码code
     *
     * @param responseType responseType
     * @param clientId     clientId
     * @param redirectUrl  redirectUrl
     * @param scope        scope
     * @param state        state
     * @return 200/成功
     */
    @Override
    public Response authConnect(String responseType, String clientId, String redirectUrl, String scope, String state) {

        if (responseType == null || StringUtils.isEmpty(responseType)) {
            return Response.error(HttpStatusEnum.EMPTY_PARAMS.getCode(), "授权码response_type不能为空");
        } else if (!CODE.equals(responseType)) {
            return Response.error("response_type只能为code");
        } else if (clientId == null || StringUtils.isEmpty(clientId)) {
            return Response.error(HttpStatusEnum.EMPTY_PARAMS.getCode(), "client_id不能为空");
        }//todo redirectUrl合法判断

        Map<String, Object> res = new HashMap<>(16);

        String resCode = UUID.randomUUID().toString().split(LINK)[0];
        res.put("code", resCode);

        String cacheKey = String.format(OAUTH_CODE, clientId);
        oAuthCache.opsForValue().set(cacheKey, resCode);
        oAuthCache.expire(cacheKey, 10, TimeUnit.MINUTES);

        if (state != null) {
            res.put("state", state);
        }

        if (redirectUrl != null) {

            StringBuilder getUrl = new StringBuilder(redirectUrl);
            getUrl.append("?code=");
            getUrl.append(resCode);
            if (state != null) {
                res.put("state", state);

                getUrl.append("&state=");
                getUrl.append(state);
            }

            OkHttpUtils.toGet(getUrl.toString());

        }

        log.info("clientId={}用户获取code={}成功", clientId, resCode);
        return Response.ok(res);
    }


    /**
     * 获取access_token
     *
     * @param clientId     clientId
     * @param clientSecret clientSecret
     * @param code         code
     * @param grantType    grantType
     * @return 200/成功
     */
    @Override
    public Response getAccessToken(String clientId, String clientSecret, String code, String grantType) {

        if (clientId == null || StringUtils.isEmpty(clientId)) {
            return Response.error(HttpStatusEnum.EMPTY_PARAMS.getCode(), "client_id不能为空");
        } else if (clientSecret == null || StringUtils.isEmpty(clientSecret)) {
            return Response.error(HttpStatusEnum.EMPTY_PARAMS.getCode(), "client_secret不能为空");
        } else if (code == null || StringUtils.isEmpty(code)) {
            return Response.error(HttpStatusEnum.EMPTY_PARAMS.getCode(), "授权码code不能为空");
        } else if (grantType == null || !grantType.equals(AUTHORIZATION_CODE)) {
            return Response.error("grant_type参数错误");
        }

        if (!code.equals(oAuthCache.opsForValue().get(String.format(OAUTH_CODE, clientId)))) {
            return Response.error("授权码code参数错误");
        }

        String oAuthKey = UUID.randomUUID().toString().split(LINK)[0];
        String oAuthKeyCacheKey = String.format(OAUTH_KEY, clientId);
        oAuthCache.opsForValue().set(oAuthKeyCacheKey, oAuthKey);

        //todo 验证client_id和secret的匹配

        String refreshToken = JwtToken.enToken(clientSecret, oAuthKey);
        String refreshTokenKey = String.format(OAUTH_REFRESH_TOKEN, clientId);
        oAuthCache.opsForValue().set(refreshTokenKey, refreshToken);
        oAuthCache.expire(refreshToken, REFRESH_TOKEN_EXPIRE_TIME, TimeUnit.DAYS);


        String accessToken = JwtToken.enToken(refreshToken, oAuthKey);
        String accessTokenKey = String.format(OAUTH_ACCESS_TOKEN, clientId);
        oAuthCache.opsForValue().set(accessTokenKey, accessToken);
        oAuthCache.expire(accessTokenKey, ACCESS_TOKEN_EXPIRE_TIME, TimeUnit.HOURS);

        Map<String, Object> res = new HashMap<>();

        res.put("access_token", accessToken);
        res.put("expires_in", ACCESS_TOKEN_EXPIRE_TIME * 60 * 60);
        res.put("refresh_token", refreshToken);
        //res.put("scope",null);


        log.info("clientId={}用户获取accessToken成功", clientId);
        return Response.ok(res);
    }


    /**
     * 使用refresh_token刷新access_token
     *
     * @param clientId     clientId
     * @param refreshToken refreshToken
     * @param grantType    grantType
     * @return 200/成功
     */
    @Override
    public Response refreshToken(String clientId, String refreshToken, String grantType) {

        if (clientId == null || StringUtils.isEmpty(clientId)) {
            return Response.error(HttpStatusEnum.EMPTY_PARAMS.getCode(), "client_id不能为空");
        } else if (refreshToken == null || StringUtils.isEmpty(refreshToken)) {
            return Response.error(HttpStatusEnum.EMPTY_PARAMS.getCode(), "refresh_token不能为空");
        } else if (grantType == null || !grantType.equals(REFRESH_TOKEN)) {
            return Response.error("grant_type参数错误");
        }

        if (!refreshToken.equals(oAuthCache.opsForValue().get(String.format(OAUTH_REFRESH_TOKEN, clientId)))) {
            return Response.error("refresh_token参数错误");
        }

        String oAuthKey = UUID.randomUUID().toString().split(LINK)[0];
        String oAuthKeyCacheKey = String.format(OAUTH_KEY, clientId);
        oAuthCache.opsForValue().set(oAuthKeyCacheKey, oAuthKey);

        String newRefreshToken = JwtToken.enToken(clientId, oAuthKey);
        String refreshTokenKey = String.format(OAUTH_REFRESH_TOKEN, clientId);
        oAuthCache.opsForValue().set(refreshTokenKey, newRefreshToken);
        oAuthCache.expire(refreshTokenKey, REFRESH_TOKEN_EXPIRE_TIME, TimeUnit.DAYS);

        String accessToken = JwtToken.enToken(newRefreshToken, oAuthKey);
        String accessTokenKey = String.format(OAUTH_ACCESS_TOKEN, clientId);
        oAuthCache.opsForValue().set(accessTokenKey, accessToken);
        oAuthCache.expire(accessTokenKey, ACCESS_TOKEN_EXPIRE_TIME, TimeUnit.HOURS);

        Map<String, Object> res = new HashMap<>();
        res.put("access_token", accessToken);
        res.put("expires_in", ACCESS_TOKEN_EXPIRE_TIME * 60 * 60);
        res.put("refresh_token", newRefreshToken);
//        res.put("scope",null);

        log.info("clientId={}用户成功刷新accessToken", clientId);
        return Response.ok(res);
    }

}
