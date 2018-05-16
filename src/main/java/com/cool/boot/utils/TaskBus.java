package com.cool.boot.utils;

import com.cool.boot.entity.Task;
import com.cool.boot.service.TaskServer;
import org.apache.commons.lang3.StringUtils;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.spi.JobFactory;
import org.quartz.spi.TriggerFiredBundle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.List;

import static com.cool.boot.enums.ScheduleOperaEnum.*;

/**
 * 定时任务的总工具类
 */
@Component
public class TaskBus {

    private Logger logger = LoggerFactory.getLogger(TaskBus.class);
    private String JOB_DETAIL_GROUP = "job_detail_group";
    private String JOB_TRIGGER_GROUP = "job_trigger_group";

    @Autowired
    private TaskServer taskServer;

    @Autowired
    private TaskJob taskJob;


    protected static Boolean IS_RESET_OVER = false;


    private Scheduler schedule;

    private JobDetail jobDetail;
    private JobKey jobKey;

    {

        try {
            SchedulerFactory factory = new StdSchedulerFactory();
            schedule = factory.getScheduler();

            /**
             * 初始化设置timeTask中的注入类
             */
            schedule.setJobFactory(new JobFactory() {

                @Override
                public Job newJob(TriggerFiredBundle bundle, Scheduler scheduler) throws SchedulerException {

                    if (bundle.getJobDetail().getJobClass().equals(TaskJob.class)) {
                        return taskJob;
                    }
                    try {
                        return bundle.getJobDetail().getJobClass().newInstance();
                    } catch (InstantiationException e) {

                    } catch (IllegalAccessException e) {

                    }
                    return null;
                }
            });
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化定时任务列表
     *
     * @param jobName 定时任务的组名称
     */
    public void initSchedule(String jobName) {
        if (StringUtils.isEmpty(jobName)) {
            return;
        }

        try {
            jobKey = new JobKey(jobName, JOB_DETAIL_GROUP);
            jobDetail = schedule.getJobDetail(jobKey);
            if (jobDetail == null) {
                jobDetail = JobBuilder.newJob(TaskJob.class).withIdentity(jobName, JOB_DETAIL_GROUP).build();
            }
        } catch (SchedulerException e) {

        }
    }

    /**
     * 初始化定时任务
     */
    @PostConstruct
    private void initTask() {
        logger.info("开始初始化定时任务...");
        try {

            List<Task> taskAll = taskServer.findTaskAll();

            if (taskAll == null || taskAll.isEmpty()) {
                logger.info("未检测到需要添加的定时任务");
                IS_RESET_OVER = true;
                return;
            }

            for (Task task : taskAll) {
                addTask(task);
            }

            schedule.start();
            IS_RESET_OVER = true;
            logger.info("初始化定时任务success...");



        } catch (Exception e) {
            logger.warn("初始化定时任务失败", e);
            throw new RuntimeException(e);
        }


    }

    /**
     * 添加定时任务
     *
     * @param task
     */
    private void addTask(Task task) {

        logger.info("开始添加定时任务{}...", task);
        try {
            //检测是否已经存在
            if (schedule.checkExists(new JobKey(task.getTaskName(), JOB_DETAIL_GROUP))) {
                logger.info("已经添加过定时任务{}...", task);
                return;
            }
            initSchedule(task.getTaskName());

            jobDetail.getJobDataMap().put(task.getTaskName(), task);
            String cronExpress = task.getExecuteTime();
            CronScheduleBuilder cronBuild = CronScheduleBuilder.cronSchedule(cronExpress);
            Trigger trigger = TriggerBuilder.newTrigger().withIdentity(task.getTaskName(), JOB_TRIGGER_GROUP).withSchedule(cronBuild).build();
            schedule.scheduleJob(jobDetail, trigger);
            taskServer.saveTask(task);
            //判断是否需要重启
            if (!schedule.isStarted()) {
                logger.info("启动定时任务success");
                schedule.start();
            }

            logger.info("添加定时任务{}success...", task);
        } catch (Exception e) {
            logger.warn("添加定时任务{},失败", task, e);
            throw new RuntimeException(e);
        }


    }


    /**
     * 删除定时任务
     *
     * @param task
     */
    private void deleteTask(Task task) {

        logger.info("开始删除定时任务{}...", task);
        try {

            jobKey = new JobKey(task.getTaskName(), JOB_DETAIL_GROUP);
            if (schedule.checkExists(jobKey)) {
                schedule.deleteJob(jobKey);
                taskServer.deleteTask(task);
                logger.info("删除定时任务{}success...", task);
            } else {
                logger.info("没有查询到定时任务{}...", task);
            }


        } catch (Exception e) {
            logger.warn("删除定时任务{},失败", task, e);
            throw new RuntimeException(e);
        }


    }

    /**
     * 更新定时任务
     *
     * @param task
     */
    private void updateTask(Task task) {

        logger.info("开始更新定时任务{}...", task);
        try {
            TriggerKey triggerKey = new TriggerKey(task.getTaskName(), JOB_TRIGGER_GROUP);
            if (schedule.checkExists(triggerKey)) {
                String cronExpress = task.getExecuteTime();
                CronScheduleBuilder cron = CronScheduleBuilder.cronSchedule(cronExpress);
                Trigger trigger = TriggerBuilder.newTrigger().withIdentity(triggerKey).withSchedule(cron).build();
                trigger.getJobDataMap().put(task.getTaskName(), task);
                schedule.rescheduleJob(triggerKey, trigger);
                taskServer.updateTask(task);
                logger.info("更新定时任务{}success...", task);
            } else {
                logger.info("没有查询到定时任务{}...", task);
            }


        } catch (Exception e) {
            logger.warn("更新定时任务{},失败", task, e);
            throw new RuntimeException(e);
        }


    }

    /**
     * 立即执行定时任务
     *
     * @param task
     */
    private void startNowTask(Task task) {

        logger.info("开始立即执行定时任务{}...", task);
        try {
            TriggerKey triggerKey = new TriggerKey(task.getTaskName(), JOB_TRIGGER_GROUP);
            String cronExpress = task.getExecuteTime();
            CronScheduleBuilder cron = CronScheduleBuilder.cronSchedule(cronExpress);
            Trigger trigger = TriggerBuilder.newTrigger().withIdentity(triggerKey).withSchedule(cron).startNow().build();
            trigger.getJobDataMap().put(task.getTaskName(), task);
            if (schedule.checkExists(triggerKey)) {
                schedule.rescheduleJob(triggerKey, trigger);
                taskServer.updateTask(task);
                logger.info("更新已经存在的并开始立即执行定时任务{}success...", task);
            } else {
                initSchedule(task.getTaskName());
                schedule.scheduleJob(jobDetail, trigger);
                taskServer.saveTask(task);
                logger.info("重新创建并开始立即执行定时任务{}success...", task);
            }

            //判断是否需要重启
            if (!schedule.isStarted()) {
                logger.info("立即启动定时任务success");
                schedule.start();
            }
        } catch (Exception e) {
            logger.warn("开始立即执行定时任务{},失败", task, e);
            throw new RuntimeException(e);
        }


    }


    /**
     * 执行定时任务
     *
     * @param task 定时任务
     */
    public void execute(Task task) {
        if (task == null) {
            return;
        }

        try {
            logger.info("开始处理定时任务{}", task);
            switch (task.getScheduleOperaEnum()) {
                case ADD_TASK:
                    addTask(task);
                    break;
                case UPDATE_TASK:
                    updateTask(task);
                    break;
                case DELETE_TASK:
                    deleteTask(task);
                    break;
                case START_NOW_TASK:
                    startNowTask(task);
                    break;
                default:
                    logger.warn("所传处理定时任务{},不符合规范", task);
                    return;

            }


            logger.info("结束处理定时任务{}", task);
        } catch (Exception e) {
            logger.warn("处理定时任务{}失败", task, e);
        }


    }

    @PreDestroy
    private void destory() {

        try {
            if (schedule != null && !schedule.isShutdown()) {
                schedule.shutdown();
                logger.info("销毁定时任务成功");
            }

        } catch (SchedulerException e) {
            logger.warn("销毁定时任务失败", e);
        }
    }


}
