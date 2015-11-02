package com.yy.ent.client;


import com.yy.ent.protocol.JettyRequest;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2015/10/21
 * Time: 17:13
 * To change this template use File | Settings | File Templates.
 */
public class ClientSender extends YYClient {

    private static AtomicInteger idMaker = new AtomicInteger(0);

    private  ReplyWaitQueue replyQueue = new ReplyWaitQueue();

    public ClientSender(String host, int port) {
        super(host, port);
    }


    public void sendOnly(JettyRequest request) {
        int id = idMaker.incrementAndGet();
        request.setMsgId(id);
        send(request);

    }

    public String sendAndWait(JettyRequest request) {
        int id = idMaker.incrementAndGet();
        request.setMsgId(id);
        try {
            ReplyFuture future = new ReplyFuture(id);
            replyQueue.add(future);
            send(request);
            return future.getReply();
        } finally {
            replyQueue.remove(id);
        }

    }

    public String sendAndWait(JettyRequest request, long timeout) {
        int id = idMaker.incrementAndGet();
        request.setMsgId(id);
        try {
            ReplyFuture future = new ReplyFuture(id);
            replyQueue.add(future);
            future.setReadTimeoutMillis(timeout);
            send(request);
            return future.getReply();
        } finally {
            replyQueue.remove(id);
        }

    }
}
