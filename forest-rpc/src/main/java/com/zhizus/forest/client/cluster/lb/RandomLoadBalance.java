package com.zhizus.forest.client.cluster.lb;

import com.zhizus.forest.Referer;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Dempe on 2016/12/7.
 */
public class RandomLoadBalance<T> extends AbstractLoadBalance<T> {

    @Override
    public Referer<T> doSelect() {
        List<Referer<T>> refererList = getRefererList();
        int idx = (int) (ThreadLocalRandom.current().nextDouble() * refererList.size());
        for (int i = 0; i < refererList.size(); i++) {
            Referer<T> ref = refererList.get((i + idx) % refererList.size());
            if (ref.isAvailable()) {
                return ref;
            }
        }
        return null;
    }

    @Override
    public void doSelectToHolder(List<Referer<T>> refersHolder) {
        List<Referer<T>> refererList = getRefererList();
        int idx = (int) (ThreadLocalRandom.current().nextDouble() * refererList.size());
        for (int i = 0; i < refererList.size(); i++) {
            Referer<T> referer = refererList.get((i + idx) % refererList.size());
            if (referer.isAvailable()) {
                refersHolder.add(refererList.get((i + idx) % refererList.size()));
            }
        }
    }

}
