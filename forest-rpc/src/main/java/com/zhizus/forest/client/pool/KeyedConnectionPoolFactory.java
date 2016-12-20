package com.zhizus.forest.client.pool;

import com.zhizus.forest.client.Connection;
import com.zhizus.forest.common.ServerInfo;
import com.zhizus.forest.transport.NettyClient;
import io.netty.channel.ChannelFuture;
import org.apache.commons.pool2.KeyedPooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Dempe on 2016/12/20.
 */
public class KeyedConnectionPoolFactory implements KeyedPooledObjectFactory<ServerInfo<NettyClient>, Connection> {

    private final static Logger LOGGER = LoggerFactory.getLogger(KeyedConnectionPoolFactory.class);

    @Override
    public PooledObject<Connection> makeObject(ServerInfo<NettyClient> key) throws Exception {
        NettyClient client = key.getClient();
        if (client == null) {
            client = new NettyClient(key);
        }
        ChannelFuture future = client.connect();
        Connection connection = new Connection();
        // Wait until the connection is made successfully.
        future.awaitUninterruptibly();
        if (!future.isSuccess()) {
            LOGGER.error("connection is not connected", future.cause());
        } else {
            connection.setIsConnected(true);
        }
        connection.setFuture(future);
        return new DefaultPooledObject<>(connection);
    }

    @Override
    public void destroyObject(ServerInfo<NettyClient> key, PooledObject<Connection> p) throws Exception {
        p.getObject().close();
    }

    @Override
    public boolean validateObject(ServerInfo<NettyClient> key, PooledObject<Connection> p) {
        return p.getObject().isConnected() && p.getObject().getFuture().channel().isOpen()
                && p.getObject().getFuture().channel().isActive();
    }

    @Override
    public void activateObject(ServerInfo<NettyClient> key, PooledObject<Connection> p) throws Exception {

    }

    @Override
    public void passivateObject(ServerInfo<NettyClient> key, PooledObject<Connection> p) throws Exception {

    }
}
