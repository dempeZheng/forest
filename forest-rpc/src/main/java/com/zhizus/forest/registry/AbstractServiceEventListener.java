package com.zhizus.forest.registry;

import org.apache.curator.x.discovery.ServiceInstance;

/**
 * Created by Dempe on 2016/12/8.
 */
public abstract class AbstractServiceEventListener<T> implements IServiceEventListener<T> {

    public void onFresh(ServiceInstance<T> serviceInstanceOld, ServiceInstance<T> serviceInstanceNew, ServiceEvent event) {
        switch (event) {
            case ON_REGISTER:
                onRegister(serviceInstanceNew);
                break;
            case ON_UPDATE:
                onUpdate(serviceInstanceOld, serviceInstanceNew);
                break;
            case ON_REMOVE:
                onRemove(serviceInstanceOld);
                break;
        }
    }
}
