package com.zhizus.forest.transport;

import com.zhizus.forest.ForestRouter;
import com.zhizus.forest.common.config.ServerConfig;
import com.zhizus.forest.common.config.ServiceExportConfig;

/**
 * Created by Dempe on 2016/12/9.
 */
public class ForestServerFactory {

    private ServerConfig serverConfig;

    public ForestServerFactory(ServerConfig serverConfig) {
        this.serverConfig = serverConfig;
    }

    public ForestServer createServer(ForestRouter router, ServiceExportConfig config) throws InterruptedException {
        ForestServer forestServer = new ForestServer(router,serverConfig, config.getPort());
        return forestServer;
    }


}
