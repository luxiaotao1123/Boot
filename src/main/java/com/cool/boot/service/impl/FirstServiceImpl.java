package com.cool.boot.service.impl;

import com.alibaba.fastjson.JSON;

import com.cool.boot.entity.Response;
import com.cool.boot.entity.Task;
import com.cool.boot.enums.RabbitTypeEnum;
import com.cool.boot.enums.ScheduleOperaEnum;
import com.cool.boot.message.RabbitMqPublishImpl;
import com.cool.boot.message.TransferData;
import com.cool.boot.service.FirstService;
import com.cool.boot.utils.TimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.UUID;

@Slf4j
@Service("firstServiceImpl")
@Transactional(rollbackFor = Exception.class)
public class FirstServiceImpl implements FirstService {

    @Resource
    private RabbitMqPublishImpl rabbitMqPublish;

    @Resource
    private ValueOperations<String, String> myValueOperations;

    @Override
    public Response hello() {
        /*Task task = new Task();
        task.setScheduleOperaEnum(ScheduleOperaEnum.ADD_TASK);
        String uuid = UUID.randomUUID().toString();
        task.setTaskName("demo:"+uuid);
        task.setExecuteTime(TimeUtil.qurtzCurrentLaterCron(1));
        task.setParams(JSON.toJSONString(Response.error("demo:"+uuid)));

        TransferData data = new TransferData();
        data.setData(JSON.toJSONString(task));
        data.setRabbitTypeEnum(RabbitTypeEnum.TASK);
        rabbitMqPublish.publish(data);
        myValueOperations.set("data","hello world");*/

        log.error("error" + new Date().toString());
        log.warn("warn" + new Date().toString());
        log.info("info" + new Date().toString());
        log.debug("debug" + new Date().toString());
        return Response.ok();
    }
}
