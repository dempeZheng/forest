package com.yy.ent.client;


import com.yy.ent.protocol.json.Request;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2015/10/21
 * Time: 17:13
 * To change this template use File | Settings | File Templates.
 */
public class ClientSender extends YYClient {

    private AtomicLong idMaker = new AtomicLong(0);

    private ReplyWaitQueue replyQueue = new ReplyWaitQueue();


    public void sendOnly(String msg) {
        channel.writeAndFlush(msg);

    }

    public String sendAndWait(Request request) {
        long id = idMaker.incrementAndGet();
        request.setId(id);
        try {
            ReplyFuture future = new ReplyFuture(id);
            replyQueue.add(future);
            channel.writeAndFlush(request.toJsonString());
            String result = future.getReply();
            return result;
        } finally {
            replyQueue.remove(id);
        }

    }

    public String sendAndWait(String msg, long timeout) {
        long id = idMaker.incrementAndGet();
        try {
            ReplyFuture future = new ReplyFuture(id);
            replyQueue.add(future);
            future.setReadTimeoutMillis(timeout);
            channel.writeAndFlush(msg);
            String result = future.getReply();
            return result;
        } finally {
            replyQueue.remove(id);
        }

    }
}
