package com.cool.boot.config;

import lombok.extern.slf4j.Slf4j;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Socket Server
 *
 * @author vincent
 * 2018/4/10
 */
@Slf4j
//@Component
@ServerEndpoint(value = "/socketServer/{id}")
public class WebSocketServer {

    private static int onlineCount = 0;
    private Session session;
    private static Map<String, Session> sessionPool = new ConcurrentHashMap<>(16);
    private static Map<String, String> sessionIds = new ConcurrentHashMap<>(16);

    /*
        Establish a connection
     */
    @OnOpen
    public void onOpen(Session session, @PathParam(value = "id") String id) {
        this.session = session;
        addOnlineCount();
        log.warn("id={}的用户已建立socket连接，当前总连接数为{}", id, getOnlineCount());
        sessionPool.put(id, session);
        sessionIds.put(session.getId(), id);
    }

    /*
        receive message
     */
    @OnMessage
    public void onMessage(String message) {
        log.warn("收到id={}用户的消息，内容为：{}", sessionIds.get(session.getId()), message);
        //todo
    }

    /*
        close connection
     */
    @OnClose
    public void onClose() {
        subOnlineCount();
        log.warn("id={}的用户已关闭socket连接", sessionIds.get(session.getId()));
        sessionPool.remove(sessionIds.get(session.getId()));
        sessionIds.remove(session.getId());
    }

    /*
        connection error
     */
    @OnError
    public void onError(Session session, Throwable e) {
        e.printStackTrace();
    }

    private static synchronized int getOnlineCount() {
        return onlineCount;
    }

    private static synchronized void addOnlineCount() {
        onlineCount++;
    }

    private static synchronized void subOnlineCount() {
        onlineCount--;
    }

}
