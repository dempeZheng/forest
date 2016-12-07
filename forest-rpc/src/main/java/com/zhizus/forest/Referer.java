package com.zhizus.forest;

import com.zhizus.forest.client.ChannelPool;
import com.zhizus.forest.common.Constants;
import com.zhizus.forest.common.codec.Message;
import com.zhizus.forest.common.codec.Response;
import com.zhizus.forest.common.exception.ForestFrameworkException;
import com.zhizus.forest.transport.NettyClient;
import com.zhizus.forest.transport.NettyResponseFuture;

/**
 * Created by Dempe on 2016/12/7.
 */
public class Referer<T> {

    private ChannelPool channelPool;

    public Referer(ClientConfig clientConfig) throws InterruptedException {
        channelPool = new ChannelPool(new NettyClient(clientConfig));

    }

    public boolean isAvailable() {
        return true;
    }

    public Object call(Message message) throws Exception {
        if (!isAvailable()) {
            throw new ForestFrameworkException("client is not available now");
        }
        NettyResponseFuture<Response> responseFuture = channelPool.write(message, Constants.DEFAULT_TIMEOUT);
        return  responseFuture.getPromise().await().getResult();

    }
}
