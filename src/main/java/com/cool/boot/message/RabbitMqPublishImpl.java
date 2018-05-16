package com.cool.boot.message;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * rabbitMq的推送实现类
 * @Auth Vincent
 */
@Component("rabbitMqPublish")
public class RabbitMqPublishImpl implements Publisher {

    private Logger logger = LoggerFactory.getLogger(RabbitMqPublishImpl.class);

    @Resource
    private RabbitTemplate taskRabbitTemplate;

    @Resource
    private RabbitTemplate demoRabbitTemplate;

    @Override
    public boolean publish(TransferData transferData) {

        if (transferData == null || transferData.getData() == null || transferData.getData().isEmpty()) {
            logger.warn("开始发送推送消息,参数为空");
            return false;
        }

        try {
            logger.info("开始发送推送消息={}", transferData.getData());

            switch (transferData.getRabbitTypeEnum()) {
                case TASK:
                    taskRabbitTemplate.convertAndSend(transferData.getData());
                    break;
                case DEMO:
                    demoRabbitTemplate.convertAndSend(transferData.getData());
                    break;
                default:
                    logger.warn("开始发送推送消息,传递的操作类型不对", transferData.getData());
                    return false;
            }


            logger.info("结束发送推送消息={}", transferData.getData());
            return true;
        } catch (Exception e) {
            logger.warn("发送推送消息={}失败", transferData.getData(), e);

        }
        return false;
    }


}
