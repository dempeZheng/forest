package com.zhizus.forest;

import com.zhizus.forest.client.ChannelPool;
import com.zhizus.forest.common.Constants;
import com.zhizus.forest.common.codec.Message;
import com.zhizus.forest.common.codec.Response;
import com.zhizus.forest.common.exception.ForestFrameworkException;
import com.zhizus.forest.transport.NettyClient;
import com.zhizus.forest.transport.NettyResponseFuture;
import org.apache.curator.x.discovery.ServiceInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.IOException;

/**
 * Created by Dempe on 2016/12/7.
 */
public class Referer<T> implements Closeable {

    private final static Logger LOGGER = LoggerFactory.getLogger(Referer.class);

    private ChannelPool channelPool;

    private ServiceInstance instance;

    public Referer(ClientConfig clientConfig) throws InterruptedException {
        channelPool = new ChannelPool(new NettyClient(clientConfig));
    }

    public Referer(ServiceInstance instance) throws InterruptedException {
        this.instance = instance;
        channelPool = new ChannelPool(new NettyClient(instance));
    }

    public boolean isAvailable() {
        return true;
    }

    public Object call(Message message) throws Exception {
        if (!isAvailable()) {
            throw new ForestFrameworkException("client is not available now");
        }
        NettyResponseFuture<Response> responseFuture = channelPool.write(message, Constants.DEFAULT_TIMEOUT);
        return responseFuture.getPromise().await().getResult();
    }

    public void close() {
        if (channelPool != null) {
            try {
                channelPool.close();
            } catch (IOException e) {
                LOGGER.error(e.getMessage(), e);
            }
        }
    }

    public ServiceInstance getInstance() {
        return instance;
    }

    public void setInstance(ServiceInstance instance) {
        this.instance = instance;
    }
}
