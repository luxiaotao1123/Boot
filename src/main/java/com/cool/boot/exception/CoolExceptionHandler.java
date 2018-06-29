package com.cool.boot.exception;


import com.cool.boot.entity.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @Auth Vincent
 */
@RestControllerAdvice
public class CoolExceptionHandler {
    private Logger logger = LoggerFactory.getLogger(CoolExceptionHandler.class);

    /**
     * 自定义异常
     */
    @ExceptionHandler(CoolException.class)
    public Response handleRRException(CoolException e) {
        Response r = new Response();
        r.put("code", e.getCode());
        r.put("msg", e.getMessage());

        return r;
    }

    @ExceptionHandler(DuplicateKeyException.class)
    public Response handleDuplicateKeyException(DuplicateKeyException e) {
        logger.error(e.getMessage(), e);
        return Response.error("数据库中已存在该记录");
    }

//    @ExceptionHandler(AuthorizationException.class)
//    public Response handleAuthorizationException(AuthorizationException e){
//        logger.error(e.getMessage(), e);
//        return Response.error("没有权限，请联系管理员授权");
//    }

    @ExceptionHandler(Exception.class)
    public Response handleException(Exception e) {
        logger.error(e.getMessage(), e);
        return Response.error();
    }
}
