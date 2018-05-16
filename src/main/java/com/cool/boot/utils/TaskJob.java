package com.cool.boot.utils;


import com.cool.boot.entity.Task;
import com.cool.boot.enums.RabbitTypeEnum;
import com.cool.boot.enums.TaskTypeEnum;
import com.cool.boot.message.RabbitMqPublishImpl;
import com.cool.boot.message.TransferData;
import org.apache.commons.lang3.StringUtils;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * timerTask的具体实现子类
 * @Auth Vincent
 */
@Component
public class TaskJob implements Job {

    private Logger logger = LoggerFactory.getLogger(TaskJob.class);

    @Autowired
    private RabbitMqPublishImpl rabbitMqPublish;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

        Collection<Object> values = jobExecutionContext.getMergedJobDataMap().values();
        Task task;

        for (Object obj : values.toArray()) {
            task = (Task) obj;
            if (StringUtils.isEmpty(task.getParams())) {
                continue;
            }
            logger.info("开始执行定时任务{}", task);
            TransferData transferData = new TransferData();
            transferData.setData(task.getParams());
            String params = task.getParams();
            if (params.contains(TaskTypeEnum.DEMO_KEY.getValue())){
                transferData.setRabbitTypeEnum(RabbitTypeEnum.DEMO);
            }
            rabbitMqPublish.publish(transferData);

            logger.info("结束执行定时任务{}", task);
        }
    }


}
