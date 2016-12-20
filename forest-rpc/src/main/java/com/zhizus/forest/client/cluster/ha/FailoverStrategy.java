package com.zhizus.forest.client.cluster.ha;

import com.zhizus.forest.client.cluster.lb.AbstractLoadBalance;
import com.zhizus.forest.common.ServerInfo;
import com.zhizus.forest.common.codec.Message;
import com.zhizus.forest.transport.NettyClient;

/**
 * Created by Dempe on 2016/12/7.
 */
public class FailoverStrategy extends AbstractHAStrategy {

    @Override
    public Object call(Message message, AbstractLoadBalance<ServerInfo<NettyClient>> loadBalance) throws Exception {
        return null;
    }
}
