package com.cool.boot.listener;

import com.alibaba.fastjson.JSONObject;
import com.cool.boot.entity.Response;
import com.cool.boot.message.DemoSubscriber;
import com.cool.boot.message.TransferData;

import com.cool.boot.service.TaskServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Auth vincent
 */
@Component("demoSubscriber")
public class DemoListener implements DemoSubscriber {

    private Logger logger = LoggerFactory.getLogger(DemoListener.class);

    @Autowired
    private TaskServer taskServer;

    @Override
    public void subscribe(TransferData transferData) {
        if (transferData == null || transferData.getData() == null) {
            logger.warn("接受监听计算费用的jms消息参数为空");
            return;
        }

        logger.info("受监听计算费用的jms消息={}", transferData.getData());
        String taskName = (String) JSONObject.parseObject(transferData.getData(), Response.class).get(Response.MSG);

        String[] params = taskName.split(":");

        System.out.println("==================================" + params[1] + "======================================");

        //删除持久层job
        taskServer.autoDelete(taskName);

    }

    public static void main(String[] args) {
    }
}
