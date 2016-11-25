package com.dempe.forest.client;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/3/9
 * Time: 10:35
 * To change this template use File | Settings | File Templates.
 */
public interface Callback<T> {

    public void onReceive(T message);
}
