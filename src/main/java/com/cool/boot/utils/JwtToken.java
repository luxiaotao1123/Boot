package com.cool.boot.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * jwt的认证
 */
public class JwtToken {


    public static final int EXPIRE_TIME = 128;
    public static final int ACCESS_TOKEN_EXPIRE_TIME = 2;
    public static final int REFRESH_TOKEN_EXPIRE_TIME = 30;
    public static final String HEADER = "token";

    private static final String KEY = "com.cool.boot";
    public static final String ID = "id";
    private static final String ARG = "arg";
    public static final String CLIENT_ID = "client_id";
    public static final String SECRET = "secret";
    private static Logger logger = LoggerFactory.getLogger(JwtToken.class);

    /**
     * 生成token
     *
     * @param id 用户id
     * @return token
     */
    public static String toToken(Integer id) {
        id = Optional.ofNullable(id).orElse(-1);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR, EXPIRE_TIME);
        Map<String, Object> map = new HashMap<>();
        map.put(ID, id);
        return Jwts.
                builder().
                setClaims(map).
                setIssuedAt(new Date()).
                setExpiration(calendar.getTime()).
                signWith(SignatureAlgorithm.HS256, KEY).
                compact();
    }

    /**
     * 加密token
     *
     * @param arg arg
     * @return
     */
    public static String enToken(String arg, String key) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR, ACCESS_TOKEN_EXPIRE_TIME);
        Map<String, Object> map = new HashMap<>();
        map.put(ARG, arg);
        return Jwts.
                builder().
                setClaims(map).
                setIssuedAt(new Date()).
                setExpiration(calendar.getTime()).
                signWith(SignatureAlgorithm.HS256, key).
                compact();

    }


    /**
     * 解密token
     *
     * @param token token
     * @return id
     */
    public static Integer parseToken(String token) {
        if (token == null || token.length() == 0) {
            logger.warn("用户所传token参数为空");
            return null;
        }
        Claims body = null;

        try {
            body = Jwts.parser().setSigningKey(KEY).parseClaimsJws(token).getBody();
        } catch (Exception e) {
            logger.warn("用户所传token={},解析异常", token, e);
        }
        if (body == null || body.isEmpty()) {
            logger.warn("用户所传token={}解密失败", token);
            return null;
        }

        Object res = body.get(ID);

        if (res == null) {
            logger.warn("用户所传token={}中不包含{}", token, ID);
            return null;
        }

        if (new Date().after(body.getExpiration())) {
            logger.warn("用户{}的token={}已经失效", res, token);
            return null;
        }

        return (Integer) res;

    }


}
