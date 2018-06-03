package com.cool.boot.spiders;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public abstract class Subject {

    private final static List<Observer> observers = new ArrayList<>();


    static {

        Observer urlObserver = new UrlObserver();

    }

    public static void register(Observer observer){
        observers.add(observer);
    }

    public static void logout(Observer observer){
        observers.remove(observer);
    }

    boolean urlNotify(){

        boolean res = Boolean.FALSE;

        for (Observer observer : observers){

            if (observer.work()){
                res = Boolean.TRUE;
            }

        }

        return res;
    }


}
