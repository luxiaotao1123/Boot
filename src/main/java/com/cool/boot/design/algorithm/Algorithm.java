package com.cool.boot.design.algorithm;

import com.cool.boot.exception.CoolException;
import com.cool.boot.utils.TimeUtil;
import lombok.extern.slf4j.Slf4j;

import java.sql.Time;
import java.util.Arrays;

/**
 * 算法
 * @author Vincent
 */
@Slf4j
public class Algorithm {

    private final static int NO_EXIST = -1;


    /**
     * 二分查找法（有序数组）
     * @param arg   数组
     * @param params    参数key
     * @return  下标
     */
    public static int binarySearch(int[] arg , int ...params){
        if (arg == null || arg.length < 1){
            return NO_EXIST;
        }

        int key = params[0];
        int frontIndex = 0;
        int backIndex = arg.length - 1;

        if (params.length > 1){
            frontIndex = params[1];
            backIndex = params[2];
        }

        int midIndex = (backIndex + frontIndex) >>> 1;
        int midVal = arg[midIndex];

        if (midVal > key)
            return binarySearch(arg, key, frontIndex , midIndex - 1);
        else if (midVal < key)
            return binarySearch(arg, key, midIndex + 1, backIndex);
        else if (midVal == key)
            return midIndex;

        return NO_EXIST;
    }


    /**
     * 冒泡排序
     * @param arg   入参
     * @param asc   不传true升序，false为降序
     * @return  排序后
     */
    public static int[] bubbleSort(int[] arg, boolean ...asc){
        if (arg == null || arg.length < 1){
            log.error("BubbleSort can not find args");
            throw new CoolException("BubbleSort can not find args");
        }

        for (int i = 0; i < arg.length - 1 ;i++){
            int temp;
            for (int j = 0; j <arg.length - i - 1; j++){
                if (asc[0] == Boolean.TRUE){
                    if (arg[j] > arg[j + 1]){
                        temp = arg[j];
                        arg[j] = arg[j + 1];
                        arg[j + 1] = temp;
                    }
                } else {
                    if (arg[j] < arg[j + 1]){
                        temp = arg[j];
                        arg[j] = arg[j + 1];
                        arg[j + 1] = temp;
                    }
                }
            }
        }

        return arg;
    }


}
