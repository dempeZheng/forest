package com.zhizus.forest.client.cluster.lb;

import com.zhizus.forest.client.cluster.FailoverCheckingStrategy;
import com.zhizus.forest.common.ServerInfo;
import com.zhizus.forest.common.codec.Message;
import com.zhizus.forest.common.codec.Request;
import com.zhizus.forest.common.registry.AbstractServiceDiscovery;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Dempe on 2016/12/22.
 */
public class HashLoadBalance<T> extends AbstractLoadBalance<T> {

    public HashLoadBalance(FailoverCheckingStrategy failoverCheckingStrategy, String serviceName, AbstractServiceDiscovery discovery) {
        super(failoverCheckingStrategy, serviceName, discovery);
    }

    @Override
    public ServerInfo<T> select(Message message) {
        List<ServerInfo<T>> availableServerList = getAvailableServerList();
        return availableServerList.get(getHash((Request) message.getContent()) % availableServerList.size());
    }

    private int getHash(Request request) {
        int hashcode;
        if (request.getArgs() == null || request.getArgs().length == 0) {
            hashcode = request.hashCode();
        } else {
            hashcode = Arrays.hashCode(request.getArgs());
        }
        return 0x7fffffff & hashcode;
    }
}
