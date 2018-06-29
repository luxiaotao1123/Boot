package com.cool.boot.design.singleton;

/**
 * @author Vincent
 * 饿汉式单例模式
 * 类被加载的时候，就已经实例化对象
 * 饿汉式是典型的空间换时间，当类装载的时候就会创建类的实例，不管你用不用，先创建出来，然后每次调用的时候，就不需要再判断，节省了运行时间
 */
public class EagerSingleton {

    private static EagerSingleton instance = new EagerSingleton();

    private EagerSingleton() {
    }

    public static EagerSingleton getInstance() {

        return instance;
    }

}
