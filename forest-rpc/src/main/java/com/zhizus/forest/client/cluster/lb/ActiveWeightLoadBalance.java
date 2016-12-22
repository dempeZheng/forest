package com.zhizus.forest.client.cluster.lb;

import com.zhizus.forest.client.cluster.FailoverCheckingStrategy;
import com.zhizus.forest.common.ServerInfo;
import com.zhizus.forest.common.codec.Message;
import com.zhizus.forest.common.registry.AbstractServiceDiscovery;

/**
 * 低并发优先
 * todo
 * Created by Dempe on 2016/12/22.
 */
public class ActiveWeightLoadBalance<T> extends AbstractLoadBalance<T> {

    public ActiveWeightLoadBalance(FailoverCheckingStrategy failoverCheckingStrategy, String serviceName, AbstractServiceDiscovery discovery) {
        super(failoverCheckingStrategy, serviceName, discovery);
    }

    @Override
    public ServerInfo<T> select(Message message) {
        return null;
    }
}
