package com.zhizus.forest.client;

import com.zhizus.forest.client.cluster.IHaStrategy;
import com.zhizus.forest.client.cluster.ha.FailFastStrategy;
import com.zhizus.forest.client.cluster.lb.AbstractLoadBalance;
import com.zhizus.forest.client.cluster.lb.RandomLoadBalance;
import com.zhizus.forest.common.codec.Message;
import com.zhizus.forest.registry.AbstractServiceDiscovery;
import com.zhizus.forest.registry.AbstractServiceEventListener;
import org.apache.curator.x.discovery.ServiceInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Dempe on 2016/12/20.
 */
public class ClusterPoolProvider<T> extends AbstractServiceEventListener {

    private final static Logger LOGGER = LoggerFactory.getLogger(ClusterPoolProvider.class);

    private IHaStrategy haStrategy;
    private AbstractLoadBalance<T> loadBalance;


    public ClusterPoolProvider(FailoverCheckingStrategy failoverCheckingStrategy, String serviceName, AbstractServiceDiscovery discovery) {
        haStrategy = new FailFastStrategy();
        loadBalance = new RandomLoadBalance<>(failoverCheckingStrategy, serviceName, discovery);
    }

    public Object call(Message message) throws Exception {
        return haStrategy.call(message, loadBalance);
    }


    @Override
    public void onRegister(ServiceInstance serviceInstance) {
        loadBalance.onRegister(serviceInstance);
    }

    @Override
    public void onRemove(ServiceInstance serviceInstance) {
        loadBalance.onRemove(serviceInstance);
    }


}
