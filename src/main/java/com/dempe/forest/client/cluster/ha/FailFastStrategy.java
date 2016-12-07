package com.dempe.forest.client.cluster.ha;

import com.dempe.forest.Referer;
import com.dempe.forest.client.cluster.IHaStrategy;
import com.dempe.forest.client.cluster.ILoadBalance;
import com.dempe.forest.codec.Message;

/**
 * Created by Dempe on 2016/12/7.
 */
public class FailFastStrategy<T> implements IHaStrategy<T> {

    @Override
    public Object call(Message message, ILoadBalance<T> loadBalance) throws Exception {
        Referer<T> referer = loadBalance.select();
        return referer.call(message);
    }
}

