package com.zhizus.forest.registry;

import org.apache.curator.x.discovery.ServiceInstance;

/**
 * Created by Dempe on 2016/12/8.
 */
public interface IServiceEventListener<T> {

    void onRegister(ServiceInstance<T> serviceInstance);

    void onRemove(ServiceInstance<T> serviceInstance);

    void onUpdate(ServiceInstance<T> serviceInstanceOld, ServiceInstance<T> serviceInstanceNew);

    enum ServiceEvent {
        ON_REGISTER,
        ON_UPDATE,
        ON_REMOVE
    }

}
