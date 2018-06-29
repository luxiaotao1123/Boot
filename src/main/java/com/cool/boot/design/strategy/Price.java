package com.cool.boot.design.strategy;

/**
 * 价格类
 */
public class Price {

    private MemberStrategy memberStrategy;

    Price(MemberStrategy memberStrategy) {

        this.memberStrategy = memberStrategy;
    }

    public double quote(double price) {

        return memberStrategy.calcPrice(price);
    }
}
