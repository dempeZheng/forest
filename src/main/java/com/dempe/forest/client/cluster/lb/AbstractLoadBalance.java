package com.dempe.forest.client.cluster.lb;

import com.dempe.forest.Referer;
import com.dempe.forest.client.cluster.ILoadBalance;
import com.dempe.forest.core.exception.ForestServiceException;

import java.util.List;

/**
 * Created by Dempe on 2016/12/7.
 */
public abstract class AbstractLoadBalance<T> implements ILoadBalance<T> {

    private List<Referer<T>> refererList;

    @Override
    public Referer<T> select() {
        List<Referer<T>> refererList = this.refererList;

        Referer<T> ref = null;
        if (refererList.size() > 1) {
            ref = doSelect();

        } else if (refererList.size() == 1) {
            ref = refererList.get(0).isAvailable() ? refererList.get(0) : null;
        }

        if (ref != null) {
            return ref;
        }
        throw new ForestServiceException(this.getClass().getSimpleName() + " No available referers for call");
    }


    public abstract Referer<T> doSelect();

    public abstract void doSelectToHolder(List<Referer<T>> refersHolder);

    @Override
    public void selectToHolder(List<Referer<T>> refersHolder) {
        List<Referer<T>> refererList = this.refererList;

        if (refererList == null) {
            throw new ForestServiceException(this.getClass().getSimpleName() + " No available referers for call : referers_size= 0 ");
        }

        if (refererList.size() > 1) {
            doSelectToHolder(refersHolder);

        } else if (refererList.size() == 1 && refererList.get(0).isAvailable()) {
            refersHolder.add(refererList.get(0));
        }
        if (refersHolder.isEmpty()) {
            throw new ForestServiceException(this.getClass().getSimpleName() + " No available referers for call : referers_size="
                    + refererList.size());
        }
    }

    @Override
    public Referer<T> selectByKey() {
        return null;
    }

    @Override
    public void onFresh(List<Referer<T>> refererList) {
        this.refererList = refererList;
    }

    public List<Referer<T>> getRefererList() {
        return refererList;
    }

    public void setRefererList(List<Referer<T>> refererList) {
        this.refererList = refererList;
    }
}
