package com.zhizus.forest.client.cluster;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.zhizus.forest.Referer;
import com.zhizus.forest.client.cluster.ha.FailFastStrategy;
import com.zhizus.forest.client.cluster.lb.AbstractLoadBalance;
import com.zhizus.forest.client.cluster.lb.RandomLoadBalance;
import com.zhizus.forest.common.codec.Message;
import com.zhizus.forest.common.exception.ForestFrameworkException;
import com.zhizus.forest.registry.AbstractServiceEventListener;
import org.apache.curator.x.discovery.ServiceInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by Dempe on 2016/12/7.
 */
public class ClusterProvider extends AbstractServiceEventListener {

    private final static Logger LOGGER = LoggerFactory.getLogger(ClusterProvider.class);

    private final static Map<String, List<Referer>> refererListMap = Maps.newConcurrentMap();

    private IHaStrategy haStrategy;

    private AbstractLoadBalance loadBalance;

    private List<Referer> refererList;

    private AtomicBoolean available = new AtomicBoolean(false);

    private Collection<ServiceInstance> serviceInstances;

    private String serviceName;

    public ClusterProvider(Collection<ServiceInstance> serviceInstances, String serviceName) {
        this.serviceInstances = serviceInstances;
        this.serviceName = serviceName;

    }

    public void init() throws InterruptedException {
        available.set(true);
        refererList = Lists.newArrayList();
        haStrategy = new FailFastStrategy<>();
        for (ServiceInstance serviceInstance : serviceInstances) {
            refererList.add(new Referer(serviceInstance));
        }
        refererListMap.put(serviceName, refererList);
        loadBalance = new RandomLoadBalance<>();
        loadBalance.setRefererList(refererList);

    }

    public Object call(Message message) throws Exception {
        return haStrategy.call(message, loadBalance);
    }

    protected boolean inRefererList(ServiceInstance instance, List<Referer> refererList) {
        for (Referer referer : refererList) {
            if (referer.getInstance().equals(instance)) {
                return true;
            }
        }
        return false;
    }


    @Override
    public void onRegister(ServiceInstance serviceInstance) {
        List<Referer> refererList = refererListMap.get(serviceInstance.getName());
        if (refererList == null) {
            refererList = Lists.newCopyOnWriteArrayList();
        }
        if (inRefererList(serviceInstance, refererList)) {
            return;
        }
        try {
            refererList.add(new Referer(serviceInstance));
        } catch (InterruptedException e) {
            LOGGER.error(e.getMessage(), e);
            throw new ForestFrameworkException("refer add err");
        }
        // 如果referList有更改，则通知loadBalance
        loadBalance.onFresh(refererList);


    }

    @Override
    public void onRemove(ServiceInstance serviceInstance) {
        List<Referer> refererList = refererListMap.get(serviceInstance.getName());
        if (refererList == null) {
            return;
        }
        boolean isInReferList = false;
        for (Referer referer : refererList) {
            if (referer.getInstance().equals(serviceInstance)) {
                refererList.remove(referer);
                isInReferList = true;
                referer.close();
            }
        }
        if (isInReferList) {
            loadBalance.onFresh(refererList);
        }


    }

}
