package com.cool.boot.design.observer;

public class ConSubject extends Subject {

    private String msg;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public void change(String msg){

       this.notifyObservers(msg);

    }
}
