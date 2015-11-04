package com.dempe.ketty.client;

import com.alibaba.fastjson.JSONObject;
import com.dempe.ketty.protocol.KettyRequest;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2015/11/4
 * Time: 16:20
 * To change this template use File | Settings | File Templates.
 */
public class KettyClientTest {
    public static void main(String[] args) {
        ClientSender sender = new ClientSender("localhost", 8888);
        KettyRequest request = new KettyRequest();
        request.setUri("/simpleAction/getUserByUid");
        JSONObject params = new JSONObject();
        params.put("uid", "1234567");
        request.setParameter(params);
        System.out.println("result : " + sender.sendAndWait(request));
    }
}
