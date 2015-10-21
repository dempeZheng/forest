package com.yy.ent.client;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created with IntelliJ IDEA.
 * User: lamfire
 * Date: 14-2-17
 * Time: 下午6:04
 * To change this template use File | Settings | File Templates.
 */
public class ReplyWaitQueue {
    private static Map<Long, ReplyFuture> waits = new ConcurrentHashMap<Long, ReplyFuture>();

    public void add(ReplyFuture future) {
        waits.put(future.getMessageId(), future);
    }

    public ReplyFuture get(int messageId) {
        return waits.get(messageId);
    }

    public ReplyFuture take(long messageId) {
        return waits.remove(messageId);
    }

    public ReplyFuture remove(long messageId) {
        return waits.remove(messageId);
    }
}
