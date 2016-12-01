package com.dempe.forest.transport;

import com.dempe.forest.client.Callback;
import com.dempe.forest.client.Future;
import com.dempe.forest.codec.Message;
import io.netty.channel.Channel;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/12/1
 * Time: 11:21
 * To change this template use File | Settings | File Templates.
 */
public class NettyResponseFuture<T> implements Callback<T>, Future<T> {
    private long sendTime;
    private Message request;
    private T response;
    Throwable error;
    private final CountDownLatch latch = new CountDownLatch(1);

    private Channel channel;

    public NettyResponseFuture( long sendTime, Message request, Channel channel) {
        this.sendTime = sendTime;
        this.request = request;
        this.channel = channel;
    }

    public Message getRequest() {
        return request;
    }

    public void setRequest(Message request) {
        this.request = request;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public long getSendTime() {
        return sendTime;
    }

    public void setSendTime(long sendTime) {
        this.sendTime = sendTime;
    }

    @Override
    public void onReceive(T response) {
        synchronized (this) {
            this.response = response;
            latch.countDown();
        }
    }

    @Override
    public T await() throws Exception {
        latch.await();
        return get();
    }

    @Override
    public T await(long amount, TimeUnit unit) throws Exception {
        if (latch.await(amount, unit)) {
            return get();
        } else {
            throw new TimeoutException();
        }
    }
    private T get() throws Exception {
        Throwable e = error;
        if (e != null) {
            if (e instanceof RuntimeException) {
                throw (RuntimeException) e;
            } else if (e instanceof Exception) {
                throw (Exception) e;
            } else if (e instanceof Error) {
                throw (Error) e;
            } else {
                // don'M expect to hit this case.
                throw new RuntimeException(e);
            }
        }
        return response;
    }
}
