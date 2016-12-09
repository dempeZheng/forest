package com.zhizus.forest;

import com.zhizus.forest.common.config.ServiceExportConfig;
import com.zhizus.forest.transport.NettyServerNew;
import org.aeonbits.owner.ConfigFactory;

/**
 * Created by Dempe on 2016/12/9.
 */
public class ForestServerFactory {

    private ServerConfig serverConfig;

    public ForestServerFactory(ServerConfig serverConfig) {
        this.serverConfig = serverConfig;
    }

    public NettyServerNew createServer(ForestRouter router, ServiceExportConfig config) throws InterruptedException {
        NettyServerNew nettyServerNew = new NettyServerNew(router, ConfigFactory.create(ServerConfig.class), config.getPort());
        return nettyServerNew;
    }


}
