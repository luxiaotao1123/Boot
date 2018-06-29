package com.cool.boot.listener;


import com.alibaba.fastjson.JSONObject;

import com.cool.boot.entity.Task;
import com.cool.boot.message.TaskSubscriber;
import com.cool.boot.message.TransferData;
import com.cool.boot.utils.TaskBus;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * timTask的监听类
 *
 * @Auth Vincent
 */
@Component("taskSubscriber")
public class TaskListener implements TaskSubscriber {

    private Logger logger = LoggerFactory.getLogger(TaskListener.class);

    @Autowired
    private TaskBus taskBus;

    @Override
    public void subscribe(TransferData transferData) {
        try {

            if (transferData == null || transferData.getData() == null) {
                logger.warn("接受监听定时任务的jms消息参数为空");
                return;
            }


            logger.info("接受监听定时任务的jms消息={}", transferData.getData());


            Task task = JSONObject.parseObject(transferData.getData(), Task.class);


            if (task == null || task.getScheduleOperaEnum() == null ||
                    StringUtils.isEmpty(task.getTaskName()) || StringUtils.isEmpty(task.getExecuteTime())) {
                logger.warn("接受监听定时任务的timeTask消息参数为空");
                return;
            }


            taskBus.execute(task);

        } catch (Exception e) {
            logger.warn("监听定时任务的jms消息{}出错", transferData.getData(), e);
        }
    }
}
