package com.dempe.forest.core.interceptor;


import com.dempe.forest.ForestContext;
import com.dempe.forest.support.ForestUtil;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 性能统计拦截器
 * User: Dempe
 * Date: 2016/12/1
 * Time: 18:25
 * To change this template use File | Settings | File Templates.
 */
public class MetricInterceptor extends AbstractInvokerInterceptor {

    private final static Logger LOGGER = LoggerFactory.getLogger(MetricInterceptor.class);

    private final static Map<String, Metric> metricsMap = Maps.newConcurrentMap();
    private final static String BEG_TIME = "begTime";

    ScheduledFuture<?> scheduledFuture = Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(new Runnable() {
        @Override
        public void run() {
            for (Map.Entry<String, Metric> stringMetricEntry : metricsMap.entrySet()) {
                Metric value = stringMetricEntry.getValue();
                LOGGER.info("group:{}, methodName:{}, current tps:{}, avgTime:{}, maxTime:{}, minTime:{} ",
                        value.getGroup(), stringMetricEntry.getKey(), value.getAndSet(), value.totalTime / 60, value.maxTime, value.minTime);
            }
        }
    }, 0, 1, TimeUnit.SECONDS);

    @Override
    public boolean beforeInvoke(Object target, Method method, Object... args) {
        ForestContext.putAttr(BEG_TIME, String.valueOf(System.currentTimeMillis()));
        return true;
    }


    @Override
    public boolean afterInvoke(Object target, Method method, Object result) {
        Long beginTime = Long.valueOf(ForestContext.getAttr(BEG_TIME));
        long exeTime = System.currentTimeMillis() - beginTime;
        String key = ForestContext.getHeader().getUri();
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
        metric.setGroup(ForestUtil.getGroup(method));
        metric.incrementAndGetTPS();
        metric.exeTime(exeTime);
        return true;
    }

    class Metric {
        private int minTime;
        private int maxTime;
        private int totalTime;
        private String group;
        private AtomicLong tps = new AtomicLong(0);

        public long incrementAndGetTPS() {
            return tps.incrementAndGet();
        }


        public long getAndSet() {
            totalTime = 0;
            return tps.getAndSet(0);
        }

        public synchronized void exeTime(long currentTime) {
            if (currentTime < minTime || minTime == 0) {
                minTime = (int) currentTime;
            }
            if (currentTime > maxTime) {
                maxTime = (int) currentTime;
            }
            totalTime += currentTime;
        }

        public String getGroup() {
            return group;
        }

        public void setGroup(String group) {
            this.group = group;
        }

        public void setTotalTime(int totalTime) {
            this.totalTime = totalTime;
        }
    }

}

