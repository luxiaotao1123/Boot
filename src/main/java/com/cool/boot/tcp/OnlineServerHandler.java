package com.cool.boot.tcp;

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
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import static com.cool.boot.entity.TCPMsgRequest.HEART_TYPE;
import static com.cool.boot.entity.TCPMsgRequest.MESSAGE_TYPE;


/**
 * TCP长连接Handler
 *
 * @Auth Vincent
 */
@Slf4j
@Component
public class OnlineServerHandler extends SimpleChannelInboundHandler<String> {

    private static Map<Integer, ChannelCache> onlineUsers;

    public static Map<Integer, String> LRU;

    static {

        onlineUsers = new ConcurrentHashMap<>();
        LRU = new LRULinkedHashMap<>(20);
    }

    /*
        连接初始化
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        ctx.fireChannelActive();
    }

    /*
        消息格式:   token:msg
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String request) {

        Channel channel = ctx.channel();

        TCPMsgRequest req = JSON.parseObject(request, TCPMsgRequest.class);

        String params[] = req.getMsg().split(":");

        Integer userId = JwtToken.parseToken(params[0]);

        if (!onlineUsers.containsKey(userId)) {

            log.warn("TCP客户端{}已建立连接", userId);

            channel.closeFuture().addListener((ChannelFutureListener) future -> {
                onlineUsers.remove(userId);
                LRU.remove(userId);
            });

            onlineUsers.put(userId, new ChannelCache(channel, heartTask(ctx, channel, userId)));
            LRU.put(userId, TimeUtil.currentTime());
        }

        switch (req.getType()) {

            case HEART_TYPE:

                ChannelCache channelCache = onlineUsers.get(userId);

                channelCache.getScheduledFuture().cancel(Boolean.TRUE);
                channelCache.setScheduledFuture(heartTask(ctx, channel, userId));
                ctx.channel().writeAndFlush(req);

                break;
            case MESSAGE_TYPE:

                //// TODO: 2018\4\28 0028 业务逻辑

                break;
            default:
                break;

        }
    }


    /*
        正常关闭连接
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) {

        ctx.fireChannelInactive();
    }


    /*
        异常关闭连接
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {

        if (null != ctx) {
            log.warn("TCP客户端{}异常退出", ctx.channel().remoteAddress());
            ctx.close();
        }
        if (null != cause) cause.printStackTrace();

    }

    private ScheduledFuture heartTask(ChannelHandlerContext ctx, Channel channel, Integer userId) {

        return ctx.executor().schedule(() -> {

            log.warn("TCP客户端{}因长时间未发送心跳包，已断开连接", userId);
            channel.close();
        }, 20, TimeUnit.SECONDS);
    }

    /*
        视频通话钻石不足提醒
     */
    public static boolean sendVideoLackDiamonds(Integer userId) {

        Channel channel = onlineUsers.get(userId).getChannel();
        if (channel == null) {
            log.warn("TCP长连接{}传输异常，连接已断开", userId);
            return false;
        }
        channel.writeAndFlush(JSON.toJSONString(TCPMsgResponse.error(TCPStatusEnum.LACK_DIAMONDS.getCode(), TCPStatusEnum.LACK_DIAMONDS.getValue())));
        return true;
    }

    /*
        视频通话钻石不足强制中断
     */
    public static boolean sendVideoNoDiamonds(Integer userId) {

        Channel channel = onlineUsers.get(userId).getChannel();
        if (channel == null) {
            log.warn("TCP长连接{}传输异常，连接已断开", userId);
            return false;
        }
        channel.writeAndFlush(JSON.toJSONString(TCPMsgResponse.error(TCPStatusEnum.NO_DIAMONDS.getCode(), TCPStatusEnum.NO_DIAMONDS.getValue())));
        return true;
    }

    /*
        广播
     */
    public static boolean topLineBroadCast(String msg) {

        Boolean res = false;
        try {

            for (Map.Entry<Integer, ChannelCache> arg : onlineUsers.entrySet()) {
                arg.getValue().getChannel().writeAndFlush(msg);
            }
            res = true;
        } catch (Exception ignore) {

            log.error("broadCast throw error");
        }
        return res;
    }

}
