package com.zhizus.forest.client.cluster.lb;

import com.zhizus.forest.client.FailoverCheckingStrategy;
import com.zhizus.forest.client.ServerInfoList;
import com.zhizus.forest.client.cluster.ILoadBalance;
import com.zhizus.forest.common.ServerInfo;

/**
 * Created by Dempe on 2016/12/7.
 */
public class AbstractLoadBalance<T> extends ServerInfoList implements ILoadBalance<T> {


    public AbstractLoadBalance(FailoverCheckingStrategy failoverCheckingStrategy) {
        super(failoverCheckingStrategy);
    }

    @Override
    public ServerInfo<T> select() {
        return null;
    }

    @Override
    public ServerInfo<T> selectByKey() {
        return null;
    }
}
