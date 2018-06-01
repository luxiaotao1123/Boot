package com.cool.boot.design.strategy.member;

import com.cool.boot.design.strategy.MemberStrategy;

/**
 * @author Vincent
 * 高级会员折扣
 */
public class AdvancedMemberStrategy implements MemberStrategy {

    @Override
    public double calcPrice(double price) {

        return price * 0.5;
    }
}
