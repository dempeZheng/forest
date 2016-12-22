package com.zhizus.forest.client.cluster.ha;

import com.zhizus.forest.client.Connection;
import com.zhizus.forest.client.pool.KeyedConnectionPool;
import com.zhizus.forest.client.pool.KeyedConnectionPoolFactory;
import com.zhizus.forest.client.cluster.IHaStrategy;
import com.zhizus.forest.client.cluster.lb.AbstractLoadBalance;
import com.zhizus.forest.common.Constants;
import com.zhizus.forest.common.ServerInfo;
import com.zhizus.forest.common.codec.Message;
import com.zhizus.forest.common.codec.Response;
import com.zhizus.forest.common.exception.ForestFrameworkException;
import com.zhizus.forest.transport.NettyClient;
import com.zhizus.forest.transport.NettyResponseFuture;
import org.apache.commons.pool2.impl.GenericKeyedObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;


/**
 * Created by Dempe on 2016/12/20.
 */
public abstract class AbstractHAStrategy implements IHaStrategy<ServerInfo<NettyClient>> {

    private final static Logger LOGGER = LoggerFactory.getLogger(AbstractHAStrategy.class);

    private KeyedConnectionPool poolProvider;

    public AbstractHAStrategy(GenericKeyedObjectPoolConfig config) {
        poolProvider = new KeyedConnectionPool(new KeyedConnectionPoolFactory(), config);
    }

    protected Object wrapCall(ServerInfo<NettyClient> key, Message message, AbstractLoadBalance<ServerInfo<NettyClient>> loadBalance) {
        Object result;
        Connection connection = null;
        if (key == null) {
            throw new ForestFrameworkException("cannot get available key for server");
        }
        try {
            connection = poolProvider.borrowObject(key);
            NettyResponseFuture<Response> future = connection.request(message, Constants.DEFAULT_TIMEOUT);
            result = future.getPromise().await(Constants.DEFAULT_TIMEOUT, TimeUnit.MILLISECONDS).getResult();
            poolProvider.returnObject(key, connection);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            // 失败 上报数据，根据策略切换
            loadBalance.fail(key);
            if (connection != null) {
                try {
                    poolProvider.invalidateObject(key, connection);
                } catch (Exception e1) {
                    LOGGER.error(e.getMessage(), e);
                }
            }
            return null;
        }
        return result;
    }
}
