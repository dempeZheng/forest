package com.dempe.forest.example;


import com.dempe.forest.core.interceptor.InvokerInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/12/1
 * Time: 18:25
 * To change this template use File | Settings | File Templates.
 */
@Component
public class MetricInterceptor implements InvokerInterceptor {

    private final static Logger LOGGER = LoggerFactory.getLogger(MetricInterceptor.class);

    private final static AtomicLong count = new AtomicLong(0);
    ScheduledFuture<?> scheduledFuture = Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(new Runnable() {
        @Override
        public void run() {
            LOGGER.info("current count:{}", count.get());
            count.set(0);
        }
    }, 0, 1, TimeUnit.SECONDS);

    @Override
    public boolean before(Object... args) {
        return true;
    }

    @Override
    public boolean after(Object result) {
        count.incrementAndGet();
        return true;
    }
}
