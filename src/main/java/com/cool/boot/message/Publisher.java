package com.cool.boot.message;

/**
 * 消息发布者
 *
 * @Auth Vincent
 */
public interface Publisher {
    boolean publish(TransferData transferData);
}
