package com.cool.boot.annotation;

import java.lang.annotation.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


public class AnnotationTest {

    public static void main(String[] args) throws NoSuchFieldException, NoSuchMethodException, IllegalAccessException {
        AnnotationTest test = new AnnotationTest();

        System.out.println(AnnotationCool.returnClassAn());

        System.out.println(AnnotationCool.returnFieldAn());

        System.out.println(AnnotationCool.returnMehtodAn());

        test.checkBug();
    }

    public void checkBug() throws IllegalAccessException {
        NoBug noBug = new NoBug();
        for (Method method : noBug.getClass().getDeclaredMethods()){

            if (method.isAnnotationPresent(Cool.class)){

                method.setAccessible(true);
                try {
                    method.invoke(noBug, null);
                } catch (InvocationTargetException e) {
                    System.out.println(e.getCause().getClass().getName());
                }
            }
        }

    }

    @Cool
    static class AnnotationCool{
        @Boot(name = "luixiaotao")
        private String str;
        @Boot(name = "coolMethod")
        private static void meth(){};

        static String returnClassAn(){
            //判断是否此类是否由此注解
            boolean isAnnotation = AnnotationCool.class.isAnnotationPresent(Cool.class);
            if (isAnnotation){
                //如果有，则获取此注解对象
                Annotation annotation = AnnotationCool.class.getAnnotation(Cool.class);
                return ((Cool) annotation).value();
            }
            return null;
        }

        static String returnFieldAn() throws NoSuchFieldException {
            //获取类中得成员变量
            Field str = AnnotationCool.class.getDeclaredField("str");
            //当改字段未private时，默认为false，设置为true才能通过反射得到
            str.setAccessible(true);
            //判断是否此字段是否由此注解
            boolean isAnnotation = str.isAnnotationPresent(Boot.class);
            if (isAnnotation){
                //如果有，则获取此注解对象
                Annotation annotation = str.getAnnotation(Boot.class);
                return ((Boot) annotation).name();
            }
            return null;
        }

        static String returnMehtodAn() throws NoSuchMethodException {
            Method method = AnnotationCool.class.getDeclaredMethod("meth");
            boolean usAnnotation = method.isAnnotationPresent(Boot.class);
            if (usAnnotation){
                Annotation annotation = method.getAnnotation(Boot.class);
                return ((Boot) annotation).name();
            }
            return null;
        }
    }


    class NoBug{
        @Cool
        public void suanShu(){
            System.out.println("1234567890");
        }
        @Cool
        public void jiafa(){
            System.out.println("1+1="+1+1);
        }
        @Cool
        public void jiefa(){
            System.out.println("1-1="+(1-1));
        }
        @Cool
        public void chengfa(){
            System.out.println("3 x 5="+ 3*5);
        }
        @Cool
        public void chufa(){
            System.out.println("6 / 0="+ 6 / 0);
        }
    }



}
