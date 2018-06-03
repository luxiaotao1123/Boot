package com.cool.boot.config;


import com.alibaba.fastjson.JSONObject;
import com.cool.boot.entity.Response;
import com.cool.boot.enums.HttpStatusEnum;
import com.cool.boot.utils.Iptools;
import com.cool.boot.utils.JwtToken;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * @Auth vincent
 * 2018/4/9
 * app接口拦截器
 */
@Configuration
public class InterceptorConfig extends WebMvcConfigurerAdapter {

    private Logger logger = LoggerFactory.getLogger(InterceptorConfig.class);

    private final Map<String, MyInterceptor.AccessRecord> ipIntercept = new ConcurrentHashMap<>(1000);
    private final int ACCESS_FORBIDDEN_TIMES = 10;
    private final int ACCESS_INTERVAL_TIMES = 2000;
    private final static String TOKEN_CASH = "token:%s";

    @Resource
    private ValueOperations<String, String> tokenValueRedisTemplate;

    @Override
    public void addInterceptors(InterceptorRegistry registry){
        registry.addInterceptor(new MyInterceptor())
                .addPathPatterns("/**/auth");
    }

    private class MyInterceptor implements HandlerInterceptor {

        @Override
        public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object handler) throws Exception {
            HttpServletRequest request = WebUtils.toHttp(req);

            if (!isAccess(request)) {
                return false;
            }

            String token = request.getHeader("token");

            if (StringUtils.isEmpty(token)) {
                logger.warn("Ip={}没有token进行访问", Iptools.gainRealIp(request));
                request.setAttribute(Response.STATUS, HttpStatusEnum.TOKEN_ERROR.getCode());
                return false;
            }

            Integer id = JwtToken.parseToken(token);

            if (tokenValueRedisTemplate.size(String.format(TOKEN_CASH,id))==0 || !token.equals(tokenValueRedisTemplate.get(String.format(TOKEN_CASH,id)))){
                logger.warn("Ip={}的token验证失败", Iptools.gainRealIp(request));
                request.setAttribute(Response.STATUS, HttpStatusEnum.TOKEN_ERROR.getCode());
                return false;
            }

            if (id == null) {
                logger.warn("token={}解析id不正确", token);
                request.setAttribute(Response.STATUS, HttpStatusEnum.TOKEN_ERROR.getCode());
                return false;
            }


            //刷新token
            String refreshToken = JwtToken.toToken(id);
            res.addHeader(JwtToken.HEADER, refreshToken);

            tokenValueRedisTemplate.set(String.format(TOKEN_CASH,id),refreshToken);

            request.setAttribute(JwtToken.ID, id);

            return true;
        }


        @Override
        public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

        }

        @Override
        public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

        }

        private void writeForbid(HttpServletResponse response, Object tips) {


            response.setCharacterEncoding("utf-8");
            response.setContentType("application/json; charset=utf-8");

            try {
                try (PrintWriter printWriter = response.getWriter()) {

                    printWriter.write(JSONObject.toJSONString(tips));
                    printWriter.flush();
                }


            } catch (IOException e) {
                logger.warn("获取写禁止数据流异常", e);
            }

        }

        /**
         * 访问记录
         */
        private class AccessRecord {
            /**
             * 第一次访问时间
             */
            private long firstAccessTime;

            /**
             * 访问的次数
             */
            private int accessTimes;

            public long getFirstAccessTime() {
                return firstAccessTime;
            }

            public void setFirstAccessTime(long firstAccessTime) {
                this.firstAccessTime = firstAccessTime;
            }

            public int getAccessTimes() {
                return accessTimes;
            }

            public void setAccessTimes(int accessTimes) {
                this.accessTimes = accessTimes;
            }

            public void setAccessTimes(Integer accessTimes) {
                this.accessTimes = accessTimes;
            }

            @Override
            public String toString() {
                return "AccessRecord{" +
                        "firstAccessTime=" + firstAccessTime +
                        ", accessTimes=" + accessTimes +
                        '}';
            }
        }

        private boolean isAccess(HttpServletRequest res) {
            String ip = Iptools.gainRealIp(res);


            if (StringUtils.isEmpty(ip)) {
                logger.warn("空Ip地址正在进行访问......");
                res.setAttribute(Response.STATUS, HttpStatusEnum.TOKEN_ERROR.getCode());
                return false;

            } else {
                AccessRecord accessRecord = ipIntercept.get(ip);

                long currentTime = System.currentTimeMillis();

                if (accessRecord == null) {
                    accessRecord = new AccessRecord();
                    accessRecord.setAccessTimes(0);
                    accessRecord.setFirstAccessTime(currentTime);
                }
                long intervalTime = currentTime - accessRecord.getFirstAccessTime();

                if (intervalTime > ACCESS_INTERVAL_TIMES) {
                    accessRecord.setAccessTimes(0);
                    accessRecord.setFirstAccessTime(currentTime);
                }

                int count = accessRecord.getAccessTimes();

                accessRecord.setAccessTimes(++count);

                ipIntercept.put(ip, accessRecord);


                if (intervalTime < ACCESS_INTERVAL_TIMES && accessRecord.getAccessTimes() > ACCESS_FORBIDDEN_TIMES) {
                    logger.warn("ip{} 时间间隔 {} 毫秒 访问路径={}, {} 次 ,已拦截 ", ip, intervalTime, res.getRequestURI(), accessRecord.getAccessTimes());
                    res.setAttribute(Response.STATUS, HttpStatusEnum.TOKEN_ERROR.getCode());
                    return false;

                }


            }
            return true;
        }
    }
}
