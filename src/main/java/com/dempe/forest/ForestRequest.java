package com.dempe.forest;

import rx.Observable;

import java.util.concurrent.Future;

/**
 * Created by Dempe on 2016/12/7.
 */
public interface ForestRequest<T> {

    T execute();

    Future<T> queue();

    Observable<T> observe();

    Observable<T> toObservable();

    RequestWithMetaData<T> withMetadata();


}
