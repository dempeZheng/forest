package com.zhizus.forest.client.cluster;

import com.zhizus.forest.client.cluster.lb.AbstractLoadBalance;
import com.zhizus.forest.common.codec.Message;
import com.zhizus.forest.common.registry.AbstractServiceEventListener;
import com.zhizus.forest.common.registry.IServiceEventListener;

/**
 * Created by Dempe on 2016/12/7.
 */
public interface IHaStrategy<T> {

    Object call(Message message, AbstractLoadBalance<T> loadBalance) throws Exception;

    void clearPool(T key);
}
