package com.zhizus.forest.rpc.hystrix;

import com.google.common.collect.Lists;
import com.netflix.hystrix.HystrixCommandGroupKey;
import org.junit.Test;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Dempe on 2017/7/9 0009.
 */
//@RunWith(SpringJUnit4ClassRunner.class)
//@SpringApplicationConfiguration(classes = Application.class)
//@WebAppConfiguration
public class HystrixTest {

    @Test
    public void common_test() throws Exception {
        ExecutorService executorService = Executors.newCachedThreadPool();
        final AtomicInteger count = new AtomicInteger();
        ArrayList<Future<?> > futures = Lists.newArrayList();


        for (int i = 0; i < 1; i++) {
            Runnable test = new Runnable() {
                @Override
                public void run() {
                    try {
                        final Command command = new Command(HystrixCommandGroupKey.Factory.asKey("test"));
                        String run = command.queue().get();
                        if (count.incrementAndGet() % 1 == 0) {
                            System.out.println(run);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
            Future<?> submit = executorService.submit(test);
            futures.add(submit);
        }
        for (Future<?> future : futures) {
            future.get();
        }

    }
}
