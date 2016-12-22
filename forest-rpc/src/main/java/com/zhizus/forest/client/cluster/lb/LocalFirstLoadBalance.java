package com.zhizus.forest.client.cluster.lb;

import com.google.common.collect.Lists;
import com.zhizus.forest.client.cluster.FailoverCheckingStrategy;
import com.zhizus.forest.common.ServerInfo;
import com.zhizus.forest.common.codec.Message;
import com.zhizus.forest.common.registry.AbstractServiceDiscovery;
import com.zhizus.forest.common.util.NetUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Dempe on 2016/12/22.
 */
public class LocalFirstLoadBalance<T> extends RandomLoadBalance<T> {

    private final static Logger LOGGER = LoggerFactory.getLogger(LocalFirstLoadBalance.class);

    public LocalFirstLoadBalance(FailoverCheckingStrategy failoverCheckingStrategy, String serviceName, AbstractServiceDiscovery discovery) {
        super(failoverCheckingStrategy, serviceName, discovery);
    }

    @Override
    public ServerInfo<T> select(Message message) {
        String hostAddress = NetUtils.getLocalAddress().getHostAddress();
        long localIP = ipToLong(hostAddress);
        List<ServerInfo<T>> serverInfos = filterLocalServerInfo(localIP);
        if (serverInfos.isEmpty()) {
            return super.select(message);
        }
        int index = ThreadLocalRandom.current().nextInt(serverInfos.size());
        return serverInfos.get(index % serverInfos.size());
    }

    private List<ServerInfo<T>> filterLocalServerInfo(long localIP) {
        List<ServerInfo<T>> localServers = Lists.newArrayList();
        if (localIP == 0) {
            return localServers;
        }
        List<ServerInfo<T>> availableServerList = getAvailableServerList();
        for (ServerInfo<T> serverInfo : availableServerList) {
            long ip = ipToLong(serverInfo.getHost());
            if (ip == localIP) {
                localServers.add(serverInfo);
            }
        }
        return localServers;
    }


    public static long ipToLong(final String addr) {
        final String[] addressBytes = addr.split("\\.");
        int length = addressBytes.length;
        if (length < 3) {
            return 0;
        }
        long ip = 0;
        try {
            for (int i = 0; i < 4; i++) {
                ip <<= 8;
                ip |= Integer.parseInt(addressBytes[i]);
            }
        } catch (Exception e) {
            LOGGER.warn("Warn ipToInt addr is wrong: addr=" + addr);
        }

        return ip;
    }
}
