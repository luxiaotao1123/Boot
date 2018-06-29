package com.cool.boot.mapper;


import com.cool.boot.entity.Task;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 订单信息的mapper层
 *
 * @Auth Vincent
 */
@Mapper
@Repository
public interface TaskMapper {


    /**
     * 查找所有的任务列表
     *
     * @return
     */
    List<Task> findTaskAll();


    /**
     * 保存任务
     *
     * @param task
     */
    void saveTask(Task task);


    /**
     * 修改任务
     *
     * @param task
     */
    void updateTask(Task task);


}
