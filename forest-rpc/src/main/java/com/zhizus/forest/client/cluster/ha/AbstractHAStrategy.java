package com.zhizus.forest.client.cluster.ha;

import com.zhizus.forest.client.Connection;
import com.zhizus.forest.client.cluster.IHaStrategy;
import com.zhizus.forest.client.KeyedConnectionPool;
import com.zhizus.forest.client.KeyedConnectionPoolFactory;
import com.zhizus.forest.common.Constants;
import com.zhizus.forest.common.ServerInfo;
import com.zhizus.forest.common.codec.Message;
import com.zhizus.forest.common.codec.Response;
import com.zhizus.forest.transport.NettyClient;
import com.zhizus.forest.transport.NettyResponseFuture;

import java.util.concurrent.TimeUnit;


/**
 * Created by Dempe on 2016/12/20.
 */
public abstract class AbstractHAStrategy<T> implements IHaStrategy<T> {

    private KeyedConnectionPool poolProvider = new KeyedConnectionPool(new KeyedConnectionPoolFactory());

    protected Object call(ServerInfo<NettyClient> key, Message message) throws Exception {
        Connection connection = poolProvider.borrowObject(key);
        NettyResponseFuture<Response> future = connection.write(message, Constants.DEFAULT_TIMEOUT);
        return future.getPromise().await(Constants.DEFAULT_TIMEOUT, TimeUnit.MILLISECONDS).getResult();
    }
}
