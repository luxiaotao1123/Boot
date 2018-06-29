package com.cool.boot.spiders;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Slf4j
@Component("urlSubject")
public class UrlSubject extends Subject {

    public final static BlockingQueue<String> urlReq = new LinkedBlockingQueue<>();

    static {


    }

    public boolean addUrl(String url) {

        boolean res = Boolean.FALSE;

        if (!urlReq.offer(url)) {
            return res;
        }

        if (!this.urlNotify()) {
            return res;
        }


        return res;
    }

//    public static void main(String[] args) {
//        OkHttpUtils.toGet("test.52woo.com:9203/merchants/propelling/users/load/admin")
//
//    }

}
