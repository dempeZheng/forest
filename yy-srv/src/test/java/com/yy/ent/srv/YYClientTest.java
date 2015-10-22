package com.yy.ent.srv;


import com.yy.ent.client.ClientSender;
import com.yy.ent.client.YYClient;
import com.yy.ent.common.MetricThread;
import com.yy.ent.protocol.json.Request;
import org.junit.Test;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2015/10/20
 * Time: 13:12
 * To change this template use File | Settings | File Templates.
 */
public class YYClientTest {

    public static void main(String[] args) throws InterruptedException {
        YYClient client = new YYClient();
        client.connect("localhost", 8888);


        for (int i = 0; i < 100; i++) {
            Request req = new Request();
            req.setId(10);
            HashMap<String, String> params = new HashMap<String, String>();
            params.put("name", "demo");
            req.setParams(params);
            req.setUri("/simpleAction/test");
            client.send(req);
            TimeUnit.SECONDS.sleep(1);
        }


    }

    @Test
    public void testClientSender() {
        ClientSender sender = new ClientSender();
        MetricThread metric = new MetricThread("test");
        sender.connect("localhost", 8888);
        for (int i = 0; i < 100000000; i++) {
            metric.increment();
            Request req = new Request();
            HashMap<String, String> params = new HashMap<String, String>();
            params.put("name", "demo");
            req.setParams(params);
            req.setUri("/simpleAction/test");
            sender.sendAndWait(req);
        }

    }
}
