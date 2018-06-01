package com.cool.boot.design.strategy.member;

import com.cool.boot.design.strategy.MemberStrategy;

/**
 * @author Vincent
 * 中级会员折扣
 */
public class IntermediateMemberStrategy implements MemberStrategy {

    @Override
    public double calcPrice(double price) {

        return price * 0.8;
    }
}
