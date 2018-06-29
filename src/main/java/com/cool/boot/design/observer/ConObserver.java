package com.cool.boot.design.observer;

public class ConObserver implements Observer {


    @Override
    public void update(String msg) {

        System.out.println(msg);
    }
}
