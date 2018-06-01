package com.cool.boot.tcp.test;

import com.alibaba.fastjson.JSON;
import com.cool.boot.entity.ChannelCache;
import com.cool.boot.entity.TCPMsgRequest;
import com.cool.boot.entity.TCPMsgResponse;
import com.cool.boot.enums.TCPStatusEnum;
import com.cool.boot.utils.JwtToken;
import com.cool.boot.utils.LRULinkedHashMap;
import com.cool.boot.utils.TimeUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.concurrent.ScheduledFuture;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import static com.cool.boot.entity.TCPMsgRequest.HEART_TYPE;
import static com.cool.boot.entity.TCPMsgRequest.MESSAGE_TYPE;


/**
 * TCP长连接Handler
 * @Auth Vincent
 */
@Slf4j
@Component
public class OnlineServerHandlerTwo extends SimpleChannelInboundHandler<String> {

    private static Map<String, Channel> test = new ConcurrentHashMap<>();

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        ctx.fireChannelActive();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String request) {
        Channel channel = ctx.channel();
        System.out.println("=====================================================");
        System.out.println("==================="+request+"========================");
        System.out.println("=====================================================");
        String uuid = UUID.randomUUID().toString();
        if (!test.containsKey(uuid)){

            log.warn("TCP客户端{}已建立连接",uuid);

            channel.closeFuture().addListener((ChannelFutureListener) future -> {
                test.remove(uuid);
            });
            test.put(uuid, channel);
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        ctx.fireChannelInactive();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        if (null != ctx){
            log.warn("TCP客户端{}异常退出",ctx.channel().remoteAddress());
            ctx.close();
        }
        if(null != cause) cause.printStackTrace();
    }

    public static boolean topLine(String msg){
        try {
            for (Map.Entry<String, Channel> arg: test.entrySet()){
                arg.getValue().writeAndFlush(msg);
            }
            return true;
        }catch (Exception e){
            return false;
        }
    }

}
