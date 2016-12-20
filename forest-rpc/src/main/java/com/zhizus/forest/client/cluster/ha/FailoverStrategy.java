package com.zhizus.forest.client.cluster.ha;

import com.google.common.collect.Lists;
import com.zhizus.forest.client.cluster.ILoadBalance;
import com.zhizus.forest.common.ServerInfo;
import com.zhizus.forest.common.codec.Message;
import com.zhizus.forest.common.exception.ForestFrameworkException;
import com.zhizus.forest.common.util.ExceptionUtil;
import com.zhizus.forest.transport.NettyClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by Dempe on 2016/12/7.
 */
public class FailoverStrategy<T> extends AbstractHAStrategy<T> {

    private final static Logger LOGGER = LoggerFactory.getLogger(FailoverStrategy.class);


    protected ThreadLocal<List<ServerInfo<NettyClient>>> referersHolder = new ThreadLocal<List<ServerInfo<NettyClient>>>() {
        @Override
        protected java.util.List<ServerInfo<NettyClient>> initialValue() {
            return Lists.newArrayList();
        }
    };

    protected List<ServerInfo<NettyClient>> selectReferers(ILoadBalance<NettyClient> loadBalance) {
        List<ServerInfo<NettyClient>> refererList = referersHolder.get();
        refererList.clear();
        return refererList;
    }

    @Override
    public Object call(Message message, ILoadBalance loadBalance) throws Exception {
        List<ServerInfo<NettyClient>> referers = selectReferers(loadBalance);
        if (referers.isEmpty()) {
            throw new ForestFrameworkException(String.format("FailoverHaStrategy No refererList for  loadbalance:%s", loadBalance));
        }
        // TODO: 2016/12/7
        int tryCount = 3;
        // 如果有问题，则设置为不重试
        if (tryCount < 0) {
            tryCount = 0;
        }

        for (int i = 0; i <= tryCount; i++) {
            ServerInfo<NettyClient> refer = referers.get(i % referers.size());
            try {
                // TODO: 2016/12/7
//                request.setRetries(i);
                return  call(refer,message);
            } catch (Exception e) {
                // 对于业务异常，直接抛出
                if (ExceptionUtil.isBizException(e)) {
                    throw e;
                } else if (i >= tryCount) {
                    throw e;
                }
                LOGGER.warn("FailoverHaStrategy Call false for error={}", e.getMessage());
            }
        }

        throw new ForestFrameworkException("FailoverHaStrategy.call should not come here!");
    }
}
