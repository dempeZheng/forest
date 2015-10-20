package com.yy.ent.srv;


import com.yy.ent.client.YYClient;
import com.yy.ent.protocol.json.Request;

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
        YYClient client = new YYClient("localhost", 8888);
        client.connect("localhost", 8888);


        for (int i = 0; i < 100; i++) {
            Request req = new Request();
            req.setId(10);
            req.setParams(new HashMap<String, String>());
            req.setUri("/simpleAction/test");
            client.send(req.toJsonString());
            TimeUnit.SECONDS.sleep(1);
        }


    }
}
