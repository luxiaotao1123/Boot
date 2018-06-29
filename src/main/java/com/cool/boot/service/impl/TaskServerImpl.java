package com.cool.boot.service.impl;


import com.cool.boot.dao.TaskDao;
import com.cool.boot.entity.Task;

import com.cool.boot.service.TaskServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 任务时刻的serviceImpl
 */
@Transactional
@Service
public class TaskServerImpl implements TaskServer {

    private Logger logger = LoggerFactory.getLogger(TaskServerImpl.class);

    @Autowired
    private TaskDao taskDao;

    @Override
    public List<Task> findTaskAll() {
        return taskDao.findTaskAll();
    }

    @Override
    public void saveTask(Task task) {
        taskDao.saveTask(task);
    }

    @Override
    public void updateTask(Task task) {
        taskDao.updateTask(task);
    }


    @Override
    public void deleteTask(Task task) {
        taskDao.deleteTask(task);
    }

    @Override
    public void autoDelete(String taskName) {
        Task task = new Task();
        task.setTaskName(taskName);
        task.setDeleted(Boolean.TRUE);
        taskDao.deleteTask(task);
        logger.warn("定时任务taskName = {}已从持久层清除！", taskName);
    }


}
