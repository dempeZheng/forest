package com.zhizus.forest.client;

/**
 * Created by Dempe on 2016/12/7.
 */
public interface Callback<T> {

    void onReceive(T message);
}
