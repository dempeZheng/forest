package com.zhizus.forest.client.cluster.ha;

import com.google.common.collect.Lists;
import com.zhizus.forest.Referer;
import com.zhizus.forest.client.cluster.IHaStrategy;
import com.zhizus.forest.client.cluster.ILoadBalance;
import com.zhizus.forest.common.codec.Message;
import com.zhizus.forest.common.exception.ForestFrameworkException;
import com.zhizus.forest.common.util.ExceptionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by Dempe on 2016/12/7.
 */
public class FailoverStrategy<T> implements IHaStrategy<T> {

    private final static Logger LOGGER = LoggerFactory.getLogger(FailoverStrategy.class);


    protected ThreadLocal<List<Referer<T>>> referersHolder = new ThreadLocal<List<Referer<T>>>() {
        @Override
        protected java.util.List<Referer<T>> initialValue() {
            return Lists.newArrayList();
        }
    };

    @Override
    public Object call(Message message, ILoadBalance<T> loadBalance) throws Exception {

        List<Referer<T>> referers = selectReferers(loadBalance);
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
            Referer<T> refer = referers.get(i % referers.size());
            try {
                // TODO: 2016/12/7
//                request.setRetries(i);
                return refer.call(message);
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

    protected List<Referer<T>> selectReferers(ILoadBalance<T> loadBalance) {
        List<Referer<T>> refererList = referersHolder.get();
        refererList.clear();
        loadBalance.selectToHolder(refererList);
        return refererList;
    }
}
