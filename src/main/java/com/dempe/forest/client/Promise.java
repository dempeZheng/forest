package com.dempe.forest.client;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/3/9
 * Time: 10:57
 * To change this template use File | Settings | File Templates.
 */
public class Promise<T> implements Callback<T>, Future<T> {

    private final CountDownLatch latch = new CountDownLatch(1);
    Throwable error;
    private T message;

    @Override
    public void onReceive(T message) {
        synchronized (this) {
            this.message = message;
            latch.countDown();
        }

    }


    public T await(long amount, TimeUnit unit) throws Exception {
        if (latch.await(amount, unit)) {
            return get();
        } else {
            throw new TimeoutException();
        }
    }

    public T await() throws Exception {
        latch.await();
        return get();
    }

    private T get() throws Exception {
        Throwable e = error;
        if (e != null) {
            if (e instanceof RuntimeException) {
                throw (RuntimeException) e;
            } else if (e instanceof Exception) {
                throw (Exception) e;
            } else if (e instanceof Error) {
                throw (Error) e;
            } else {
                // don'M expect to hit this case.
                throw new RuntimeException(e);
            }
        }
        return message;
    }


}