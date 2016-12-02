package com.dempe.forest.example;


import com.dempe.forest.core.ForestUtil;
import com.dempe.forest.core.interceptor.InvokerInterceptor;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Map;
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

    private final static Map<String, Metric> metricsMap = Maps.newConcurrentMap();

    ScheduledFuture<?> scheduledFuture = Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(new Runnable() {
        @Override
        public void run() {
            for (Map.Entry<String, Metric> stringMetricEntry : metricsMap.entrySet()) {
                LOGGER.info("uri:{}, current tps:{} ", stringMetricEntry.getKey(), stringMetricEntry.getValue().getAndSet());
            }
        }
    }, 0, 1, TimeUnit.SECONDS);

    @Override
    public boolean before(Object target, Method method, Object... args) {
        return true;
    }

    @Override
    public boolean after(Object target, Method method, Object result) {
        String key = ForestUtil.buildUri(target, method);
        Metric metric = metricsMap.get(key);
        if (metric == null) {
            synchronized (this) {
                metric = metricsMap.get(key);
                if (metric == null) {
                    metric = new Metric();
                    metricsMap.put(key, metric);
                }
            }
        }
        metric.incrementAndGetTPS();
        return true;
    }

    class Metric {

        private AtomicLong tps = new AtomicLong(0);

        public long incrementAndGetTPS() {
            return tps.incrementAndGet();
        }

        public long getAndSet() {
            return tps.getAndSet(0);
        }
    }

}

