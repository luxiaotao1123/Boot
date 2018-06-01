package com.cool.boot.design.observer;

public class Client {


    public static void main(String[] args) {

        Observer observer1 = new ConObserver();
        Observer observer2 = new ConObserver();

        Subject subject = new ConSubject();

        subject.register(observer1);
        subject.register(observer2);

        subject.logout(observer2);

        subject.change("Hello world");

        System.out.println(Integer.MIN_VALUE);

    }
}
