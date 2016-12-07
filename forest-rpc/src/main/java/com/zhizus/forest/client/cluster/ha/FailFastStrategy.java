package com.zhizus.forest.client.cluster.ha;

import com.zhizus.forest.Referer;
import com.zhizus.forest.client.cluster.IHaStrategy;
import com.zhizus.forest.client.cluster.ILoadBalance;
import com.zhizus.forest.common.codec.Message;

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

