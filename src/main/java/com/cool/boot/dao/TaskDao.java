package com.cool.boot.dao;






import com.cool.boot.entity.Task;

import java.util.List;


/**
 * 定时任务的dao
 */
public interface TaskDao {

    List<Task> findTaskAll();

    void saveTask(Task task);

    void updateTask(Task task);

    void deleteTask(Task task);

}
