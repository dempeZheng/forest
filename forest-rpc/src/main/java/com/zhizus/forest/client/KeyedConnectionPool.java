package com.zhizus.forest.client;

import com.zhizus.forest.common.ServerInfo;
import com.zhizus.forest.transport.NettyClient;
import org.apache.commons.pool2.KeyedPooledObjectFactory;
import org.apache.commons.pool2.impl.GenericKeyedObjectPool;

/**
 * Created by Dempe on 2016/12/20.
 */
public class KeyedConnectionPool extends GenericKeyedObjectPool<ServerInfo<NettyClient>, Connection> {
    public KeyedConnectionPool(KeyedPooledObjectFactory<ServerInfo<NettyClient>, Connection> factory) {
        super(factory);
    }

}
