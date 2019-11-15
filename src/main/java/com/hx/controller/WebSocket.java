package com.hx.controller;/*
 *//*
 *@作者:张立恒
 *@时间:2019/11/14 11:12
 *@功能:
 */


import com.alibaba.fastjson.JSONObject;
import com.hx.dao.InboxMapper;
import com.hx.dao.OutboxMapper;
import com.hx.dao.ReturnReceiptMapper;
import com.hx.dao.SendReceiptMapper;
import com.hx.modle.WebModel;
import org.apache.log4j.Logger;

import javax.annotation.PostConstruct;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
@ServerEndpoint("/webSocket")
public class WebSocket {
    private static Logger logger=Logger.getLogger( WebSocket.class );
    private static int onlineCount = 0;
    private static Map<String, WebSocket> clients = new ConcurrentHashMap<String, WebSocket>();
    private Session session;
    public static List<WebModel> webModels=new ArrayList<>(  );
    public static List<WebModel> sendModels=new ArrayList<>(  );
    public static List<WebModel> inboxModels=new ArrayList<>(  );
    public static List<WebModel> outboxModels=new ArrayList<>(  );
    public static int webModelCount=0;
    public static int inboxCount=0;
    public static int outboxCount=0;
    public static int sendCount=0;
    @OnOpen
    public void onOpen(Session session)throws IOException{
        this.session = session;
        addOnlineCount();
    }

    @OnClose
    public void onClose()throws IOException{
        subOnlineCount();
    }
    @OnError
    public void OnError(Session session, Throwable error){
        logger.error( error );
    }
    @OnMessage
    public void onMessage(String message, Session session)throws Exception{
        for(;;){
            if(webModelCount==1){
                String msg=JSONObject.toJSONStringWithDateFormat( webModels,"yyyy-MM-dd HH:mm:ss" );
                sendMessageAll(msg);
                webModelCount=0;
                webModels=new ArrayList<>(  );
            }
            if(inboxCount==1){
                String msg=JSONObject.toJSONStringWithDateFormat( inboxModels,"yyyy-MM-dd HH:mm:ss" );
                sendMessageAll(msg);
                inboxCount=0;
                inboxModels=new ArrayList<>(  );
            }
            if(outboxCount==1){
                String msg=JSONObject.toJSONStringWithDateFormat( outboxModels,"yyyy-MM-dd HH:mm:ss" );
                sendMessageAll(msg);
                outboxCount=0;
                outboxModels=new ArrayList<>(  );
            }
            if(sendCount==1){
                String msg=JSONObject.toJSONStringWithDateFormat( sendModels,"yyyy-MM-dd HH:mm:ss" );
                sendMessageAll(msg);
                sendCount=0;
                sendModels=new ArrayList<>(  );
            }
        }

    }
    public void sendMessageAll(String message) throws IOException {
        synchronized (session) {   //加锁
            session.getAsyncRemote().sendText(message);
        }
    }

    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        WebSocket.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        WebSocket.onlineCount--;
    }

    public static synchronized Map<String, WebSocket> getClients() {
        return clients;
    }

}
