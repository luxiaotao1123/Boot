package com.cool.boot.controller;


import com.cool.boot.entity.Response;
import com.cool.boot.service.FirstService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FristController {


    @Autowired
    private FirstService firstService;

    @GetMapping("hello")
    public Response hello(){
        return firstService.hello();
    }

}
