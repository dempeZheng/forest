package com.zhizus.forest.client.cluster;

import com.zhizus.forest.common.codec.Message;

/**
 * Created by Dempe on 2016/12/7.
 */
public interface IHaStrategy<T> {

    Object call(Message message, ILoadBalance<T> loadBalance) throws Exception;
}
