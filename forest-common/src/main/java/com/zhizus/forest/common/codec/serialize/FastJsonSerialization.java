package com.zhizus.forest.common.codec.serialize;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.SerializeWriter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.zhizus.forest.common.codec.Request;

import java.io.IOException;

/**
 * Created by Dempe on 2016/12/7.
 */
public class FastJsonSerialization implements Serialization {

    @Override
    public byte[] serialize(Object data) throws IOException {
        SerializeWriter out = new SerializeWriter();
        JSONSerializer serializer = new JSONSerializer(out);
        serializer.config(SerializerFeature.WriteEnumUsingToString, true);
        serializer.config(SerializerFeature.WriteClassName, true);
        serializer.write(data);
        return out.toBytes(null);
    }

    @Override
    public <T> T deserialize(byte[] data, Class<T> clz) throws IOException {
        return JSON.parseObject(new String(data), clz);
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Request req = new Request();
        req.setServiceName("test");
        req.setMethodName("hell");
        req.setArgs(new Object[]{"test", 1});
        Serialization serialization = new FastJsonSerialization();
        byte[] serialize = serialization.serialize(req);
        Request deserialize = serialization.deserialize(serialize, Request.class);
        System.out.println(deserialize);


    }
}
