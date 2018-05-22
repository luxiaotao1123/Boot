package com.cool.boot.design.singleton;

/**
 * @author Vincent
 * 懒汉式单例模式
 * 时间换空间，只有这个单例被调用时才会实例化
 */
public class LazySingleton {

    private volatile static LazySingleton instance = null;

    private LazySingleton(){
    }

    public static LazySingleton getInstance(){

        if (instance == null){

            synchronized (LazySingleton.class){
                return new LazySingleton();
            }

        }
        return instance;
    }

}
