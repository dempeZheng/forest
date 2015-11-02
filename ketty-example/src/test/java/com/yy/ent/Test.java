package com.yy.ent;

import com.alibaba.fastjson.JSONObject;
import com.yy.ent.client.ClientSender;
import com.yy.ent.protocol.KettyRequest;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2015/10/22
 * Time: 11:37
 * To change this template use File | Settings | File Templates.
 */
public class Test {

    public static ClientSender clientSender = new ClientSender("localhost", 8888);

    public static void main(String[] args) throws Exception {
        KettyRequest request = new KettyRequest();
        request.setUri("/simpleAction/getUserByUid");
        JSONObject params = new JSONObject();
        params.put("uid", "12345677");
        request.setParameter(params);
        String result = clientSender.sendAndWait(request);
        System.out.println("result : " + result);
    }
}
