package com.zhizus.forest.client.cluster;

import com.zhizus.forest.common.ServerInfo;
import com.zhizus.forest.common.codec.Message;

/**
 * Created by Dempe on 2016/12/6.
 */
public interface ILoadBalance<T> {

    ServerInfo<T> select(Message message);
}
