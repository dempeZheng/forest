package com.zhizus.forest.rpc.hystrix;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandProperties;

import java.util.concurrent.TimeUnit;

/**
 * Created by Dempe on 2017/7/9 0009.
 */
public class Command extends HystrixCommand<String> {
    protected Command(HystrixCommandGroupKey group) {
        super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("HelloWorldGroup"))
                .andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
                        .withExecutionIsolationSemaphoreMaxConcurrentRequests(100000)
                        .withExecutionIsolationStrategy(HystrixCommandProperties.ExecutionIsolationStrategy.SEMAPHORE)));

    }

    @Override
    protected String run() throws Exception {
        TimeUnit.MILLISECONDS.sleep(900);
        return "HystrixThread:" + Thread.currentThread().getName();
    }


    @Override
    protected String getFallback() {
        return super.getFallback();
    }
}
