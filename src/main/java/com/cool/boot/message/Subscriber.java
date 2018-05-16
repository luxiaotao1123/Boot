package com.cool.boot.message;

/**
 * 消息的订阅者
 * @Auth Vincent
 */
public interface Subscriber{

    void subscribe(TransferData transferData);

}
