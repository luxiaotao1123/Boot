package com.cool.boot.design.strategy.member;

import com.cool.boot.design.strategy.MemberStrategy;

/**
 * @author Vincent
 * 初级会员折扣
 */
public class PrimaryMemberStrategy implements MemberStrategy {

    @Override
    public double calcPrice(double price) {

        return price;
    }
}
