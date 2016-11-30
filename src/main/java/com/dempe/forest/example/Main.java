package com.dempe.forest.example;

import com.dempe.forest.codec.Response;
import com.dempe.forest.codec.serialize.KryoSerialization;
import com.dempe.forest.codec.serialize.Serialization;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/11/29
 * Time: 17:56
 * To change this template use File | Settings | File Templates.
 */
public class Main {

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        for (int i = 0; i < 10000; i++) {
            Response response = new Response();
            response.setResult("hello");
            Serialization serialization =new KryoSerialization();
            byte[] serialize = serialization.serialize(response);
            Response deserialize = serialization.deserialize(serialize, Response.class);
            System.out.println(deserialize);
        }

    }


}

