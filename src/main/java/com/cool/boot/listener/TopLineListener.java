package com.cool.boot.listener;

import com.cool.boot.tcp.OnlineServerHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import static com.cool.boot.config.RabbitMqConfig.*;

@Slf4j
@Component
public class TopLineListener {


    @RabbitHandler
    @RabbitListener(queues = TOPLINE_QUEUE)
    public void onMessage(String msg) {

        try {

            Thread.sleep(1000);
            OnlineServerHandler.topLineBroadCast(msg);
        } catch (InterruptedException e) {

            log.error("top line throw error");
        }

    }


}
