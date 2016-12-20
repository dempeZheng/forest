package com.zhizus.forest.client.cluster.ha;

import com.zhizus.forest.client.cluster.lb.AbstractLoadBalance;
import com.zhizus.forest.common.ServerInfo;
import com.zhizus.forest.common.codec.Message;
import com.zhizus.forest.transport.NettyClient;
import org.apache.commons.pool2.impl.GenericKeyedObjectPoolConfig;

/**
 * Created by Dempe on 2016/12/7.
 */
public class FailFastStrategy extends AbstractHAStrategy {


    public FailFastStrategy(GenericKeyedObjectPoolConfig config) {
        super(config);
    }

    @Override
    public Object call(Message message, AbstractLoadBalance<ServerInfo<NettyClient>> loadBalance) throws Exception {
        ServerInfo select = loadBalance.select();
        return call(select, message, loadBalance);
    }
}

