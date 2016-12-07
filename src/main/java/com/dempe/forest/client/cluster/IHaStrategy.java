package com.dempe.forest.client.cluster;

import com.dempe.forest.codec.Message;

/**
 * Created by Dempe on 2016/12/7.
 */
public interface IHaStrategy<T> {

    Object call(Message message, ILoadBalance<T> loadBalance) throws Exception;
}
