package com.cool.boot.utils;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Auth Vincent
 */
public class TimeUtil {

    private static final String second = "秒";
    private static final String minute = "分钟";
    private static final String hour = "小时";
    public static final String TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * 入参时间距离当前时间的秒数
     * @param date
     * @return
     */
    public static Double timeDifferenceToSeconds(String date){
        Double duration;
        long start = 0L;
        try {
            start = new SimpleDateFormat(TIME_FORMAT).parse(date).getTime();
        }catch (Exception e){
        }
        duration =(double) ((new Date().getTime() - start)/1000);
        return duration;
    }

    /**
     * 秒变分
     * @param param
     * @return
     */
    public static StringBuilder secondToMinute(Double param, StringBuilder stringBuilder){
        StringBuilder res;
        if (stringBuilder != null){
            res = stringBuilder;
        }else {
            res = new StringBuilder();
        }
        if (param < 60){
            res.append(param.intValue());
            res.append(second);
        }else if (param >= 60 && param < 3600){
            Double minutes = new BigDecimal(param / 60.0).setScale(0, BigDecimal.ROUND_DOWN).doubleValue();
            res.append(minutes.intValue());
            res.append(minute);
            Double seconds = param % 60;
            if (!seconds.equals(0.0)){
                res.append(seconds.intValue());
                res.append(second);
            }
        }else {
            Double hours = new BigDecimal(param / 3600.0).setScale(0, BigDecimal.ROUND_DOWN).doubleValue();
            res.append(hours.intValue());
            res.append(hour);
            Double seconds = param % 3600;
            if (!seconds.equals(0.0)){
                return secondToMinute(seconds, res);
            }
        }
        return res;
    }

    public static StringBuilder secondToMinute(Double param){
        StringBuilder res = new StringBuilder();
        if (param < 60){
            res.append(param.intValue());
            res.append(second);
        }else if (param >= 60 && param < 3600){
            Double minutes = new BigDecimal(param / 60.0).setScale(0, BigDecimal.ROUND_DOWN).doubleValue();
            res.append(minutes.intValue());
            res.append(minute);
            Double seconds = param % 60;
            if (!seconds.equals(0.0)){
                res.append(seconds.intValue());
                res.append(second);
            }
        }else {
            Double hours = new BigDecimal(param / 3600.0).setScale(0, BigDecimal.ROUND_DOWN).doubleValue();
            res.append(hours.intValue());
            res.append(hour);
            Double seconds = param % 3600;
            if (!seconds.equals(0.0)){
                return secondToMinute(seconds, res);
            }
        }
        return res;
    }

    /**
     * 当前时间yyyy-MM-dd HH:mm
     */
    public static String currentTime(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String date = simpleDateFormat.format(new Date());
        return date;
    }
    /**
     * TimeUtil.currentTime()转换
     */
    public static Date changeStringTimeToDate(String time){
        //日期格式化
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date = null;
        try {
            date = simpleDateFormat.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 比较日期大小
     *
     * @param time1
     * @param time2
     * @return 0 相等  负数 time1 小于 time2  正数 time1大于time2
     */
    public static Integer compareTime(String time1 , String time2){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Long date1 = null;
        Long date2 = null;
        try {
            date1 = simpleDateFormat.parse(time1).getTime();
            date2 = simpleDateFormat.parse(time2).getTime();

        } catch (ParseException e) {
            e.printStackTrace();
        }
        int compare = date1.compareTo(date2);
        return compare;
    }
    /**
     * 今日yyyy-MM-dd
     */
    public static String today(){
        //日期格式化
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String date = simpleDateFormat.format(new Date());
        return date;
    }

    /**
     * 时间计算
     * @param hour 小时
     * @param minute 分钟
     * @return
     */
    public static String afterTime(String date ,Integer hour,Integer minute){
        //日期格式化
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        //计算日期
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(simpleDateFormat.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        calendar.add(Calendar.HOUR,hour);
        calendar.add(Calendar.MINUTE,minute);
        return simpleDateFormat.format(calendar.getTime());
    }

    public static String beforeTime(String date ,Integer hour,Integer minute){
        //日期格式化
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        //计算日期
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(simpleDateFormat.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        calendar.add(Calendar.HOUR,-hour);
        calendar.add(Calendar.MINUTE,-minute);
        return simpleDateFormat.format(calendar.getTime());
    }

    /***********************************************************qurtz Cron****************************************************************/

    /**
     * quertCron表达式
     * （当前时间往后{}个小时）
     * @param hour        时
     * @return
     */
    public static String qurtzCurrentLaterCron(Double hour){
        String currentTime = TimeUtil.currentTime();
        SimpDate simpDate = SimpDateFactory.endDate();
        String runTime;
        Map<String, String> cron = null;
        try {
            runTime = simpDate.endDate(currentTime, hour, 0);
            cron = simpDate.transformTime(runTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "0 "+cron.get("mm")+" "+cron.get("HH")+" "+cron.get("dd")+" "+cron.get("MM")+" ? ";
    }

    /**
     * quertCron表达式
     *  (当前时间往后{}几分钟）
     * @param minute      分
     * @return
     */
    public static String qurtzCurrentLaterCron(Integer minute){
        String currentTime = TimeUtil.currentTime();
        SimpDate simpDate = SimpDateFactory.endDate();
        String runTime;
        Map<String, String> cron = null;
        try {
            runTime = simpDate.endDate(currentTime, 0.0, minute);
            cron = simpDate.transformTime(runTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "0 "+cron.get("mm")+" "+cron.get("HH")+" "+cron.get("dd")+" "+cron.get("MM")+" ? ";
    }

    /**
     * quertCron表达式
     * （自定义时间往后{}个小时）
     * @return
     */
    public static String qurtzCustomLaterCron(String time, Double duration){
        SimpDate simpDate = SimpDateFactory.endDate();
        String executeTime;
        Map<String, String> cron = null;
        try {
            executeTime = simpDate.endDate(time,duration,0);
            cron = simpDate.transformTime(executeTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "0 "+cron.get("mm")+" "+cron.get("HH")+" "+cron.get("dd")+" "+cron.get("MM")+" ? ";
    }

    /**
     * quertCron表达式
     * （自定义时间往前{}个小时）
     * @return
     */
    public static String qurtzCustomFrontCron(String time, Double duration){
        SimpDate simpDate = SimpDateFactory.endDate();
        String executeTime;
        Map<String, String> cron = null;
        try {
            executeTime = simpDate.frontDate(time,duration,0);
            cron = simpDate.transformTime(executeTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "0 "+cron.get("mm")+" "+cron.get("HH")+" "+cron.get("dd")+" "+cron.get("MM")+" ? ";
    }

    /*********************************************************************************************************************************/


    public static int getAgeByBirth(Date birthday) {
        int age = 0;
        try {
            Calendar now = Calendar.getInstance();
            now.setTime(new Date());// 当前时间

            Calendar birth = Calendar.getInstance();
            birth.setTime(birthday);

            if (birth.after(now)) {//如果传入的时间，在当前时间的后面，返回0岁
                age = 0;
            } else {
                age = now.get(Calendar.YEAR) - birth.get(Calendar.YEAR);
                if (now.get(Calendar.DAY_OF_YEAR) > birth.get(Calendar.DAY_OF_YEAR)) {
                    age += 1;
                }
            }
            return age;
        } catch (Exception e) {//兼容性更强,异常后返回数据
           return 0;
        }
    }
    
    /**
     * 比较日期大小
     *
     * @param time1
     * @param time2
     * @return 0 相等  负数 time1 小于 time2  正数 time1大于time2
     */
    public static Integer compareTimeDay(String time1 , String time2){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Long date1 = null;
        Long date2 = null;
        try {
            date1 = simpleDateFormat.parse(time1).getTime();
            date2 = simpleDateFormat.parse(time2).getTime();

        } catch (ParseException e) {
            e.printStackTrace();
        }
        int compare = date1.compareTo(date2);
        return compare;
    }

    /**
     * qurtz时间表达式当前时间往后60秒
     * @return
     */
    public static String quartzCronCurrentAfterSeconds(Integer seconds){
        Calendar cal = Calendar.getInstance();
        Date now = new Date();
        cal.setTime(new Date(now.getTime() + (seconds * 1000)));

        int year = cal.get(Calendar.YEAR);//获取年份
        int month=cal.get(Calendar.MONTH)+1;//获取月份，0为1月
        int day=cal.get(Calendar.DATE);//获取日
        int hour=cal.get(Calendar.HOUR_OF_DAY);//小时
        int minute=cal.get(Calendar.MINUTE);//分
        int second=cal.get(Calendar.SECOND);//秒
        int WeekOfYear = cal.get(Calendar.DAY_OF_WEEK);//一周的第几天


        Map<String,String> map = new HashMap<>(16);
        map.put("yyyy", String.valueOf(year));
        map.put("MM", String.valueOf(month));
        map.put("dd", String.valueOf(day));
        map.put("HH", String.valueOf(hour));
        map.put("mm", String.valueOf(minute));
        map.put("ss", String.valueOf(second));

        return map.get("ss")+" "+map.get("mm")+" "+map.get("HH")+" "+map.get("dd")+" "+map.get("MM")+" ? ";
    }

}
