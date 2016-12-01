package com.dempe.forest.example;

import com.dempe.forest.HttpForestServer;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/11/29
 * Time: 17:56
 * To change this template use File | Settings | File Templates.
 */
public class Main {

    private final static Logger LOGGER = LoggerFactory.getLogger(HttpForestServer.class);
    final static Map<Long, Long> map = Maps.newConcurrentMap();
    static ExecutorService executorService = Executors.newCachedThreadPool();
    static AtomicLong msgId = new AtomicLong(0);

    public static void main(String[] args) {

        for (int i = 0; i < 1000000000; i++) {
            final long id = msgId.incrementAndGet();
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    map.put(id, id);
                    Long aLong = map.get(id);
                    if (aLong == null) {
                        System.out.println("____________bug_______________________");
                    }

                }
            });

        }

    }
}
