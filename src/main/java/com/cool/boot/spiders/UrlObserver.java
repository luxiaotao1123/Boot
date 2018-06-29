package com.cool.boot.spiders;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static com.cool.boot.spiders.UrlSubject.urlReq;


@Slf4j
@Component("urlObserver")
public class UrlObserver implements Observer {

    @Override
    public boolean work() {

        boolean res = Boolean.FALSE;

        String url = urlReq.poll();

        if (url == null) {

            return res;
        }

        log.warn("将要爬取url={}数据", url);

        //todo

        return res;
    }

}
