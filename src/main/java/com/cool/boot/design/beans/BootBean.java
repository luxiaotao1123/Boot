package com.cool.boot.design.beans;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import sun.misc.Launcher;

@Lazy
@Component
public class BootBean {

    static {
        System.out.println("=========================boot1=======================");
    }

    {
        System.out.println("=========================boot2=======================");
    }

    public BootBean(){
        System.out.println(System.getProperty("java.ext.dirs"));
        System.out.println("=========================boot3=======================");
    }

}
