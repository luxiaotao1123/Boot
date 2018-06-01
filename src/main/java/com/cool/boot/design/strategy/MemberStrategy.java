package com.cool.boot.design.strategy;

/**
 * @author Vincent
 * 折扣策略接口
 */
public interface MemberStrategy {

    double calcPrice(double price);

}
