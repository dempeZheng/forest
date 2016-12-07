package com.zhizus.forest.transport;

import com.zhizus.forest.client.Promise;
import com.zhizus.forest.common.codec.Message;
import io.netty.channel.Channel;

/**
 * Created by Dempe on 2016/12/7.
 */
public class NettyResponseFuture<T> {
    private long createTime;
    private long timeOut;
    private Message request;
    private Channel channel;
    private Promise<T> promise;

    public NettyResponseFuture(long createTime, long timeOut, Message request, Channel channel, Promise<T> promise) {
        this.createTime = createTime;
        this.timeOut = timeOut;
        this.request = request;
        this.channel = channel;
        this.promise = promise;
    }

    public long getTimeOut() {
        return timeOut;
    }

    public void setTimeOut(long timeOut) {
        this.timeOut = timeOut;
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

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public Promise<T> getPromise() {
        return promise;
    }

    public void setPromise(Promise<T> promise) {
        this.promise = promise;
    }
}
