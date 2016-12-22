package com.zhizus.forest.client.cluster.lb;

import com.zhizus.forest.client.cluster.FailoverCheckingStrategy;
import com.zhizus.forest.client.cluster.ServerInfoList;
import com.zhizus.forest.client.cluster.ILoadBalance;
import com.zhizus.forest.common.ServerInfo;
import com.zhizus.forest.common.registry.AbstractServiceDiscovery;

/**
 * Created by Dempe on 2016/12/7.
 */
public class AbstractLoadBalance<T> extends ServerInfoList implements ILoadBalance<T> {

    public AbstractLoadBalance(FailoverCheckingStrategy failoverCheckingStrategy, String serviceName, AbstractServiceDiscovery discovery) {
        super(failoverCheckingStrategy, serviceName, discovery);
    }

    @Override
    public ServerInfo<T> select() {
        return null;
    }

    @Override
    public ServerInfo<T> selectByKey() {
        return null;
    }
}
