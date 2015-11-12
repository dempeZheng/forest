package com.dempe.ketty;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.msgpack.jackson.dataformat.MessagePackFactory;

import java.io.IOException;
import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2015/11/12
 * Time: 17:20
 * To change this template use File | Settings | File Templates.
 */
public class MsgPackTest {
    public static void main(String[] args) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper(new MessagePackFactory());
        HashMap<String, Integer> map = new HashMap<String, Integer>();
        map.put("test", 1);
        byte[] bytes = objectMapper.writeValueAsBytes(map);
        HashMap<String, Integer> readMap = objectMapper.readValue(bytes, HashMap.class);
        System.out.println(readMap.get("test"));


        JSONObject jsonObject = new JSONObject();
        jsonObject.put("demo",222);
        byte[] jsonBytes = objectMapper.writeValueAsBytes(jsonObject);
        JSONObject readJSON = objectMapper.readValue(jsonBytes, JSONObject.class);
        System.out.println(readJSON);


    }
}
