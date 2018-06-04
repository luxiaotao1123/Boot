package com.cool.boot.worm;

import java.util.Date;

public class test {

    private static String myAdd(String arg){

        String res = "";
        for (int i = 0 ; i < 20000 ; i++){

            res += arg.intern();
        }
        return res;
    }

    private static String myAppend(String arg){

        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0 ; i < 20000 ; i++){

            stringBuilder.append(arg);
        }
        return stringBuilder.toString();
    }

    public static void main(String[] args) {
        long before = new Date().getTime();
        System.out.println(myAdd(new String("s")));;
        long after = new Date().getTime();
        System.out.println("花费：" + (after - before) + "ms");
    }
}
