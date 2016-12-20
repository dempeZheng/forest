package com.zhizus.forest.client.cluster.ha;

import com.zhizus.forest.client.Connection;
import com.zhizus.forest.client.KeyedConnectionPool;
import com.zhizus.forest.client.KeyedConnectionPoolFactory;
import com.zhizus.forest.client.cluster.IHaStrategy;
import com.zhizus.forest.client.cluster.lb.AbstractLoadBalance;
import com.zhizus.forest.common.Constants;
import com.zhizus.forest.common.ServerInfo;
import com.zhizus.forest.common.codec.Message;
import com.zhizus.forest.common.codec.Response;
import com.zhizus.forest.transport.NettyClient;
import com.zhizus.forest.transport.NettyResponseFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;


/**
 * Created by Dempe on 2016/12/20.
 */
public abstract class AbstractHAStrategy implements IHaStrategy<ServerInfo<NettyClient>> {

    private final static Logger LOGGER = LoggerFactory.getLogger(AbstractHAStrategy.class);

    private KeyedConnectionPool poolProvider = new KeyedConnectionPool(new KeyedConnectionPoolFactory());

    protected Object call(ServerInfo<NettyClient> key, Message message, AbstractLoadBalance<ServerInfo<NettyClient>> loadBalance) {
        Object result;
        try {
            Connection connection = poolProvider.borrowObject(key);
            NettyResponseFuture<Response> future = connection.write(message, Constants.DEFAULT_TIMEOUT);
            result = future.getPromise().await(Constants.DEFAULT_TIMEOUT, TimeUnit.MILLISECONDS).getResult();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            // 失败 上报数据，根据策略切换
            loadBalance.fail(key);
            return null;

        }

        return result;
    }
}
