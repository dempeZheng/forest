package com.zhizus.forest.registry.impl;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.zhizus.forest.registry.AbstractServiceDiscovery;
import com.zhizus.forest.registry.AbstractServiceEventListener;
import com.zhizus.forest.registry.IServiceEventListener;
import org.apache.commons.lang3.StringUtils;
import org.apache.curator.x.discovery.ServiceInstance;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Dempe on 2016/12/8.
 */
public class LocalServiceDiscovery<T> extends AbstractServiceDiscovery<T> {

    private Map<String, ServiceInstance<T>> serviceMap = Maps.newConcurrentMap();

    private Map<String, List<AbstractServiceEventListener<T>>> listenerForNameMap = Maps.newConcurrentMap();

    @Override
    public void registerService(ServiceInstance<T> service) throws Exception {
        serviceMap.put(service.getId(), service);
        notify(service.getName(), null, service, IServiceEventListener.ServiceEvent.ON_REGISTER);

    }

    @Override
    public void updateService(ServiceInstance<T> service) throws Exception {
        ServiceInstance<T> serviceInstanceOld = serviceMap.get(service.getId());
        serviceMap.put(service.getId(), service);
        notify(service.getName(), serviceInstanceOld, service, IServiceEventListener.ServiceEvent.ON_UPDATE);
    }

    @Override
    public void unregisterService(ServiceInstance<T> service) throws Exception {
        serviceMap.remove(service.getId());
        notify(service.getName(), service, null, IServiceEventListener.ServiceEvent.ON_REMOVE);
    }


    @Override
    public Collection<String> queryForNames() throws Exception {
        Set<String> names = Sets.newHashSet();
        for (Map.Entry<String, ServiceInstance<T>> serviceInstanceEntry : serviceMap.entrySet()) {
            names.add(serviceInstanceEntry.getValue().getName());
        }
        return names;
    }

    @Override
    public Collection<ServiceInstance<T>> queryForInstances(String name) throws Exception {
        Set<ServiceInstance<T>> serviceInstances = Sets.newHashSet();
        for (Map.Entry<String, ServiceInstance<T>> serviceInstanceEntry : serviceMap.entrySet()) {
            if (StringUtils.equals(serviceInstanceEntry.getValue().getName(), name)) {
                serviceInstances.add(serviceInstanceEntry.getValue());
            }
        }
        return serviceInstances;
    }

    @Override
    public ServiceInstance<T> queryForInstance(String name, String id) throws Exception {
        return serviceMap.get(id);
    }


}
