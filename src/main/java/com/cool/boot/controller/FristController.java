package com.cool.boot.controller;


import com.cool.boot.entity.Response;
import com.cool.boot.service.FirstService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.cool.boot.config.RabbitMqConfig.TOPLINE_EXCHANGE;
import static com.cool.boot.config.RabbitMqConfig.TOPLINE_ROUTINGKEY;

@RestController
public class FristController {


    @Autowired
    private FirstService firstService;

    @Autowired
    private RabbitTemplate rabbitPush;

    @GetMapping("hello")
    public Response hello(){
        return firstService.hello();
    }

    @GetMapping("test")
    public boolean test(){

        for (int i = 0 ; i<10; i++){
            rabbitPush.convertAndSend(TOPLINE_EXCHANGE,TOPLINE_ROUTINGKEY,"hello luxiaotao"+i);
        }

        return true;

    }


}
