package com.cool.boot.design.strategy;

import com.cool.boot.design.strategy.member.AdvancedMemberStrategy;

import java.util.HashMap;

/**
 * 用户
 */
public class Client {

    public static void main(String[] args) {

        System.out.println(new Price(new AdvancedMemberStrategy()).quote(300));

    }
}
