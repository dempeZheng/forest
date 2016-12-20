package com.zhizus.forest.client.cluster.lb;

import com.zhizus.forest.client.FailoverCheckingStrategy;
import com.zhizus.forest.common.ServerInfo;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Dempe on 2016/12/7.
 */
public class RandomLoadBalance<T> extends AbstractLoadBalance<T> {

    public RandomLoadBalance(FailoverCheckingStrategy failoverCheckingStrategy) {
        super(failoverCheckingStrategy);
    }

    @Override
    public ServerInfo<T> select() {
        List<ServerInfo<T>> availableServerList = getAvailableServerList();
        int idx = (int) (ThreadLocalRandom.current().nextDouble() * availableServerList.size());
        for (int i = 0; i < availableServerList.size(); i++) {
            ServerInfo<T> serverInfo = availableServerList.get((i + idx) % availableServerList.size());
            return serverInfo;
        }
        return null;
    }
}
