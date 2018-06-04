package com.cool.boot.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 系统参数
 * @author Vincent
 */
@Component("sysParams")
public class SysParams {

    /*@Value("${wx.appid}")
    public String WX_APPID;*/

    @Value("${wx.secret}")
    public String WX_SECRET;

    @Value("${qq.appid}")
    public String QQ_APPID;

    @Value("${qq.secret}")
    public String QQ_SECRET;

    @Value("${wx.mini.appid}")
    public String WX_MINI_APPID;

    @Value("${wx.secret}")
    public String WX_MINI_SECRET;
}
