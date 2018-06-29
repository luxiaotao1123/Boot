package com.cool.boot.dao.impl;


import com.cool.boot.dao.TaskDao;
import com.cool.boot.entity.Task;
import com.cool.boot.mapper.TaskMapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * 任务时刻dao
 */
@Repository
public class TaskDaoImpl implements TaskDao {

    @Autowired
    private TaskMapper taskMapper;

    private Logger logger = LoggerFactory.getLogger(TaskDaoImpl.class);

    @Override
    public List<Task> findTaskAll() {
        logger.info("开始查询所有的任务的列表");
        return taskMapper.findTaskAll();
    }

    @Override
    public void saveTask(Task task) {
        if (task == null || StringUtils.isEmpty(task.getTaskName())) {
            logger.info("开始保存任务列表参数为空");
            return;
        }
        logger.info("开始保存任务列表={}", task.getTaskName());
        taskMapper.saveTask(task);
    }

    @Override
    public void updateTask(Task task) {
        if (task == null || StringUtils.isEmpty(task.getTaskName())) {
            logger.info("开始修改任务列表参数为空");
            return;
        }
        logger.info("开始修改任务列表={}", task.getTaskName());
        taskMapper.updateTask(task);
    }


    @Override
    public void deleteTask(Task task) {
        if (task == null || StringUtils.isEmpty(task.getTaskName())) {
            logger.info("开始删除任务列表参数为空");
            return;
        }
        logger.info("开始删除任务列表={}", task.getTaskName());
        task.setDeleted(Boolean.TRUE);
        taskMapper.updateTask(task);
    }
}
