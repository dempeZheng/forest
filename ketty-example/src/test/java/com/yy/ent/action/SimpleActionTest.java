package com.yy.ent.action;

import com.alibaba.fastjson.JSONObject;
import com.yy.ent.ClientMonitorTest;
import com.yy.ent.client.ClientSender;
import com.yy.ent.protocol.KettyRequest;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2015/10/22
 * Time: 10:54
 * To change this template use File | Settings | File Templates.
 */
public class SimpleActionTest extends ClientMonitorTest {

    private final static Logger LOGGER = LoggerFactory.getLogger(SimpleActionTest.class);

    @Override
    public void init() {
    }

    @Test
    public void getUserByUidTest() throws InterruptedException {
        for (int i = 0; i < 1; i++) {
            KettyRequest request = new KettyRequest();
            request.setUri("/simpleAction/getUserByUid");
            JSONObject params = new JSONObject();
            params.put("uid", "12345677");
            request.setParameter(params);
            String s = clientSender.sendAndWait(request);
            System.out.println("===>"+s);
            //clientSender.sendOnly(request);

        }

    }

    @Test
    public void ClientPoolTest() throws Exception {


        final AtomicInteger i = new AtomicInteger(0);
        ExecutorService executorService = Executors.newFixedThreadPool(8);
        while (true) {
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    i.incrementAndGet();
                    ClientSender sender = null;
                    try {
                        KettyRequest request = new KettyRequest();
                        request.setUri("/simpleAction/getUserByUid");
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("uid", "12345677");
                        //request.setParams(params);
                        sender = pool.borrowObject();
                        String s = sender.sendAndWait(request);
                        String s2 = clientSender.sendAndWait(request);
                        if (i.get() % 10000 == 0) {
                            TimeUnit.MICROSECONDS.sleep(10);
                            System.out.println("----" + i.get());
                            System.out.println(s);
                        }
                        pool.returnObject(sender);
                    } catch (Exception e) {
                        LOGGER.error(e.getMessage(), e);
                    }

                }
            });


        }
    }
}
