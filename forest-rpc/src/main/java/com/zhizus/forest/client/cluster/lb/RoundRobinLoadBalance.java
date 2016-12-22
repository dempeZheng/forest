package com.zhizus.forest.client.cluster.lb;

import com.zhizus.forest.client.cluster.FailoverCheckingStrategy;
import com.zhizus.forest.common.ServerInfo;
import com.zhizus.forest.common.codec.Message;
import com.zhizus.forest.common.registry.AbstractServiceDiscovery;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Dempe on 2016/12/22.
 */
public class RoundRobinLoadBalance<T> extends AbstractLoadBalance<T> {

    private AtomicInteger idx = new AtomicInteger(0);

    public RoundRobinLoadBalance(FailoverCheckingStrategy failoverCheckingStrategy, String serviceName, AbstractServiceDiscovery discovery) {
        super(failoverCheckingStrategy, serviceName, discovery);
    }

    @Override
    public ServerInfo<T> select(Message message) {
        List<ServerInfo<T>> availableServerList = getAvailableServerList();
        return availableServerList.get(idx.incrementAndGet() % availableServerList.size());
    }
}
