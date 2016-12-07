package com.dempe.forest;

import com.netflix.hystrix.HystrixInvokableInfo;

/**
 * Created by Dempe on 2016/12/7.
 */
public abstract class ForestResponse<T> {

    abstract T content();

    abstract HystrixInvokableInfo<?> getHystrixInfo();
}
