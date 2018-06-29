package com.cool.boot.design.observer;

import java.util.ArrayList;
import java.util.List;

public abstract class Subject {

    private final static List<Observer> observers = new ArrayList<>();

    public void register(Observer observer) {

        observers.add(observer);
    }

    public void logout(Observer observer) {

        observers.remove(observer);
    }

    void notifyObservers(String msg) {

        for (Observer observer : observers) {

            observer.update(msg);
        }

    }

    public void change(String msg) {

    }
}
