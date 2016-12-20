package com.zhizus.forest.client;

import com.google.common.collect.Lists;
import com.zhizus.forest.common.InstanceDetails;
import com.zhizus.forest.common.ServerInfo;
import com.zhizus.forest.registry.AbstractServiceEventListener;
import org.apache.curator.x.discovery.ServiceInstance;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * Created by Dempe on 2016/12/20.
 */
public class ServerInfoList<T> extends AbstractServiceEventListener {

    private FailoverCheckingStrategy failoverCheckingStrategy;

    private volatile List<ServerInfo<T>> serverInfoList = Lists.newArrayList();

    public ServerInfoList(FailoverCheckingStrategy failoverCheckingStrategy) {
        this.failoverCheckingStrategy = failoverCheckingStrategy;
    }

    public List<ServerInfo<T>> getAvailableServerList() {
        List<ServerInfo<T>> returnList = Lists.newArrayList();
        Set failed = failoverCheckingStrategy.getFailed();
        for (ServerInfo<T> serverInfo : serverInfoList) {
            if (!failed.contains(serverInfo)) {
                returnList.add(serverInfo);
            }
        }
        return returnList;
    }

    public void initServerInfoByServiceInstance(Collection<ServiceInstance<InstanceDetails>> serviceInstances) {
        List<ServerInfo<T>> list = Lists.newArrayList();
        for (ServiceInstance<InstanceDetails> serviceInstance : serviceInstances) {
            ServerInfo<T> serverInfo = new ServerInfo<>(serviceInstance);
            list.add(serverInfo);
        }
        this.serverInfoList = list;
    }


    @Override
    public void onRegister(ServiceInstance serviceInstance) {

    }

    @Override
    public void onRemove(ServiceInstance serviceInstance) {

    }
}
