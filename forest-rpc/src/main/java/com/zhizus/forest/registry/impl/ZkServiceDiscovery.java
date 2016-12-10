package com.zhizus.forest.registry.impl;

import com.zhizus.forest.common.Constants;
import com.zhizus.forest.common.InstanceDetails;
import com.zhizus.forest.registry.AbstractServiceDiscovery;
import com.zhizus.forest.registry.IServiceEventListener;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;
import org.apache.curator.framework.recipes.cache.TreeCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.curator.x.discovery.ServiceInstance;
import org.apache.curator.x.discovery.details.InstanceSerializer;
import org.apache.curator.x.discovery.details.JsonInstanceSerializer;
import org.springframework.beans.factory.InitializingBean;

import java.util.Collection;

/**
 * Created by Dempe on 2016/12/8.
 */
public class ZkServiceDiscovery extends AbstractServiceDiscovery<InstanceDetails> implements InitializingBean, TreeCacheListener {

    private final static InstanceSerializer serializer = new JsonInstanceSerializer<>(InstanceDetails.class);

    private ServiceDiscovery<InstanceDetails> serviceDiscovery;

    private String connStr = "localhost:2181";

    @Override
    public void registerService(ServiceInstance service) throws Exception {
        serviceDiscovery.registerService(service);
    }

    @Override
    public void updateService(ServiceInstance service) throws Exception {
        serviceDiscovery.unregisterService(service);
    }

    @Override
    public void unregisterService(ServiceInstance service) throws Exception {
        serviceDiscovery.unregisterService(service);
    }

    @Override
    public Collection<String> queryForNames() throws Exception {
        return serviceDiscovery.queryForNames();
    }

    @Override
    public Collection<ServiceInstance<InstanceDetails>> queryForInstances(String name) throws Exception {
        return serviceDiscovery.queryForInstances(name);
    }

    @Override
    public ServiceInstance<InstanceDetails> queryForInstance(String name, String id) throws Exception {
        return serviceDiscovery.queryForInstance(name, id);
    }

    @Override
    public void childEvent(CuratorFramework client, TreeCacheEvent event) throws Exception {
        ChildData data = event.getData();
        ServiceInstance serviceInstance = serializer.deserialize(data.getData());
        switch (event.getType()) {
            case NODE_ADDED: {
                notify(serviceInstance, IServiceEventListener.ServiceEvent.ON_REGISTER);
                break;
            }
            case NODE_UPDATED: {
                notify(serviceInstance, IServiceEventListener.ServiceEvent.ON_UPDATE);
                break;
            }
            case NODE_REMOVED: {
                notify(serviceInstance, IServiceEventListener.ServiceEvent.ON_REMOVE);
                break;
            }
            default:
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        CuratorFramework client = CuratorFrameworkFactory.newClient(connStr, new ExponentialBackoffRetry(1000, 3));
        client.start();
        serviceDiscovery = ServiceDiscoveryBuilder.builder(InstanceDetails.class)
                .client(client)
                .basePath(Constants.BASE_PATH)
                .serializer(serializer)
                .build();
    }

    public String getConnStr() {
        return connStr;
    }

    public void setConnStr(String connStr) {
        this.connStr = connStr;
    }
}
