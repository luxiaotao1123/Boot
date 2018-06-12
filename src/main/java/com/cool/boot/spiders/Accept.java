package com.cool.boot.spiders;


import com.cool.boot.entity.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author Vincent
 */
@RestController
@RequestMapping("v1/spider")
public class Accept {

    @Autowired
    private UrlSubject urlSubject;

    @GetMapping("vin")
    public Response One(String url){

        return !urlSubject.addUrl(url) ? Response.error() : Response.ok();
    }
}
