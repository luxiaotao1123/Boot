package com.cool.boot.service;


import com.cool.boot.entity.Task;

import java.util.List;

/**
 * 定时任务的server
 */
public interface TaskServer {

    List<Task> findTaskAll();

    void saveTask(Task task);

    void updateTask(Task task);

    void deleteTask(Task task);

    void autoDelete(String taskName);
}
