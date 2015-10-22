package com.yy.ent.action;

import com.yy.ent.ClientMonitorTest;
import com.yy.ent.mvc.ioc.BeanFactory;
import com.yy.ent.protocol.json.Request;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2015/10/22
 * Time: 10:54
 * To change this template use File | Settings | File Templates.
 */
public class SimpleActionTest extends ClientMonitorTest {

    @Override
    public void init() {
    }

    @Test
    public void cherryTest() {
        Object bean = BeanFactory.getBean(SimpleAction.class.getName());
        System.out.println(bean);
    }

    @Test
    public void getUserByUidTest() throws InterruptedException {
        Request request = new Request();
        request.setUri("/simpleAction/getUserByUid");
        Map<String, String> params = new HashMap<String, String>();
        params.put("uid", "12345677");
        request.setParams(params);
        int i = 0;
        while (true) {
            i++;
            String s = clientSender.sendAndWait(request);
            if (i % 10000 == 0) {
                TimeUnit.MICROSECONDS.sleep(10);
                System.out.println(s);
            }

        }
    }
}
