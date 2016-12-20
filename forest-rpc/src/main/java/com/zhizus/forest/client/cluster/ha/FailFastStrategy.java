package com.zhizus.forest.client.cluster.ha;

import com.zhizus.forest.client.cluster.ILoadBalance;
import com.zhizus.forest.common.ServerInfo;
import com.zhizus.forest.common.codec.Message;

/**
 * Created by Dempe on 2016/12/7.
 */
public class FailFastStrategy<T> extends AbstractHAStrategy<T> {

    @Override
    public Object call(Message message, ILoadBalance<T> loadBalance) throws Exception {
        ServerInfo select = loadBalance.select();
        return call(select, message);
    }
}

