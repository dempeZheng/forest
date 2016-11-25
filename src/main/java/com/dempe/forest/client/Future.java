package com.dempe.forest.client;

import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/3/9
 * Time: 11:01
 * To change this template use File | Settings | File Templates.
 */
public interface Future<T> {

    T await() throws Exception;

    T await(long amount, TimeUnit unit) throws Exception;

}
