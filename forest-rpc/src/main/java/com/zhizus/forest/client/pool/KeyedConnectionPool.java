package com.zhizus.forest.client.pool;

import com.zhizus.forest.client.Connection;
import com.zhizus.forest.common.ServerInfo;
import com.zhizus.forest.transport.NettyClient;
import org.apache.commons.pool2.KeyedPooledObjectFactory;
import org.apache.commons.pool2.impl.GenericKeyedObjectPool;
import org.apache.commons.pool2.impl.GenericKeyedObjectPoolConfig;

/**
 * Created by Dempe on 2016/12/20.
 */
public class KeyedConnectionPool extends GenericKeyedObjectPool<ServerInfo<NettyClient>, Connection> {
    public KeyedConnectionPool(KeyedPooledObjectFactory<ServerInfo<NettyClient>, Connection> factory) {
        super(factory);
    }

    public KeyedConnectionPool(KeyedPooledObjectFactory<ServerInfo<NettyClient>, Connection> factory, GenericKeyedObjectPoolConfig config) {
        super(factory, config);
    }
}
