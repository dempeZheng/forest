package com.yy.ent.client;


import com.yy.ent.protocol.GardenReq;
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

    private static AtomicLong idMaker = new AtomicLong(0);

    private  ReplyWaitQueue replyQueue = new ReplyWaitQueue();

    public ClientSender(String host, int port) {
        super(host, port);
    }


    public void sendOnly(GardenReq request) {
        long id = idMaker.incrementAndGet();
        request.setId(id);
        send(request);

    }

    public String sendAndWait(GardenReq request) {
        long id = idMaker.incrementAndGet();
        request.setId(id);
        try {
            ReplyFuture future = new ReplyFuture(id);
            replyQueue.add(future);
            send(request);
            return future.getReply();
        } finally {
            replyQueue.remove(id);
        }

    }

    public String sendAndWait(GardenReq request, long timeout) {
        long id = idMaker.incrementAndGet();
        request.setId(id);
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
