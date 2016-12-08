package com.zhizus.forest.registry;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.curator.x.discovery.ServiceInstance;

import java.util.List;
import java.util.Map;

/**
 * Created by Dempe on 2016/12/8.
 */
public abstract class AbstractServiceDiscovery<T> implements IServiceDiscovery<T> {

    private Map<String, List<AbstractServiceEventListener<T>>> listenerForNameMap = Maps.newConcurrentMap();

    @Override
    public void subscribe(String serviceName, AbstractServiceEventListener<T> listener) {
        List<AbstractServiceEventListener<T>> listenerList = listenerForNameMap.get(serviceName);
        if (listenerList == null) {
            listenerList = Lists.newCopyOnWriteArrayList();
            listenerForNameMap.put(serviceName, listenerList);
        }
        listenerList.add(listener);
    }


    @Override
    public void unSubscribe(String serviceName, AbstractServiceEventListener<T> listener) {
        List<AbstractServiceEventListener<T>> listeners = listenerForNameMap.get(serviceName);
        if (listeners == null) {
            return;
        }
        listeners.remove(listener);
    }


    @Override
    public void unSubscribe(AbstractServiceEventListener<T> listener) {

    }

    @Override
    public void subscribe(AbstractServiceEventListener<T> listener) {

    }

    @Override
    public void updateService(ServiceInstance<T> service) throws Exception {

    }

    @Override
    public ServiceInstance<T> queryForInstance(String name, String id) throws Exception {
        return null;
    }


    protected void notify(String serviceName, ServiceInstance<T> serviceInstanceOld, ServiceInstance<T> serviceInstanceNew, IServiceEventListener.ServiceEvent event) {
        List<AbstractServiceEventListener<T>> listenerList = listenerForNameMap.get(serviceName);
        if (listenerList == null) {
            return;
        }
        for (AbstractServiceEventListener<T> listener : listenerList) {
            listener.onFresh(serviceInstanceOld, serviceInstanceNew, event);
        }
    }
}
