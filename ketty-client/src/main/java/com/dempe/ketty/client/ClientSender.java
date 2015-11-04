package com.dempe.ketty.client;


import com.dempe.ketty.protocol.KettyRequest;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2015/10/21
 * Time: 17:13
 * To change this template use File | Settings | File Templates.
 */
public class ClientSender extends KettyClient {

    private static AtomicInteger idMaker = new AtomicInteger(0);

    private ReplyWaitQueue replyQueue = new ReplyWaitQueue();

    public ClientSender(String host, int port) {
        super(host, port);
    }


    public void sendOnly(KettyRequest request) {
        int id = idMaker.incrementAndGet();
        request.setMsgId(id);
        send(request);

    }

    public String sendAndWait(KettyRequest request) {
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

    public String sendAndWait(KettyRequest request, long timeout) {
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
