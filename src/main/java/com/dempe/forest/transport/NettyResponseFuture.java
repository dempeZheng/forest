package com.dempe.forest.transport;

import com.dempe.forest.client.Promise;
import com.dempe.forest.codec.Message;
import io.netty.channel.Channel;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/12/1
 * Time: 11:21
 * To change this template use File | Settings | File Templates.
 */
public class NettyResponseFuture<T>  {
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
