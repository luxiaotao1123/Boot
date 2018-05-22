package com.cool.boot.design.singleton;

/**
 * @author Vincent
 * 类级别的内部类（内部类上加static）单例模式
 * 即实现了饿汉的线程同步，又实现了懒汉的调用时才创建
 */
public class Singleton {

    private Singleton(){

    }

    private static class SingletonHolder{

        private static Singleton instance = new Singleton();
    }

    public static Singleton getInstance(){

        return SingletonHolder.instance;
    }
}
