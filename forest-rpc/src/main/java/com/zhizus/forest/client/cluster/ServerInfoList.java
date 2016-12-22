package com.zhizus.forest.client.cluster;

import com.google.common.collect.Lists;
import com.zhizus.forest.common.MetaInfo;
import com.zhizus.forest.common.ServerInfo;
import com.zhizus.forest.common.registry.AbstractServiceDiscovery;
import com.zhizus.forest.common.registry.AbstractServiceEventListener;
import org.apache.curator.x.discovery.ServiceInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Created by Dempe on 2016/12/20.
 */
public class ServerInfoList<T> extends AbstractServiceEventListener {
    private final static Logger LOGGER = LoggerFactory.getLogger(ServerInfo.class);

    private FailoverCheckingStrategy failoverCheckingStrategy;
    private String serviceName;
    private AbstractServiceDiscovery discovery;

    private volatile List<ServerInfo<T>> serverInfoList = Lists.newArrayList();

    public ServerInfoList(FailoverCheckingStrategy failoverCheckingStrategy, String serviceName, AbstractServiceDiscovery discovery) {
        this.failoverCheckingStrategy = failoverCheckingStrategy;
        this.serviceName = serviceName;
        this.discovery = discovery;
        discovery.subscribe(serviceName, this);
        init();
    }

    public List<ServerInfo<T>> getAvailableServerList() {
        List<ServerInfo<T>> returnList = Lists.newArrayList();
        if (serverInfoList == null || serverInfoList.size() == 0) {
            //从服务发现节点拉取最新的数据
            init();
            try {
                TimeUnit.MICROSECONDS.sleep(100);
            } catch (InterruptedException e) {
                LOGGER.error(e.getMessage(), e);
            }
        }
        Set failed = failoverCheckingStrategy.getFailed();
        for (ServerInfo<T> serverInfo : serverInfoList) {
            if (!failed.contains(serverInfo)) {
                returnList.add(serverInfo);
            }
        }
        return returnList;
    }


    public void init() {
        try {
            Collection<ServiceInstance<MetaInfo>> serviceInstances = discovery.queryForInstances(serviceName);
            initServerInfoByServiceInstance(serviceInstances);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    protected void initServerInfoByServiceInstance(Collection<ServiceInstance<MetaInfo>> serviceInstances) {
        List<ServerInfo<T>> list = Lists.newArrayList();
        for (ServiceInstance<MetaInfo> serviceInstance : serviceInstances) {
            ServerInfo<T> serverInfo = new ServerInfo<>(serviceInstance);
            list.add(serverInfo);
        }
        Collections.shuffle(list);
        this.serverInfoList = list;
    }

    public void fail(T object) {
        failoverCheckingStrategy.fail(object);
    }


    @Override
    public void onRegister(ServiceInstance serviceInstance) {
        init();
    }

    @Override
    public void onRemove(ServiceInstance serviceInstance) {
        init();
    }
}
