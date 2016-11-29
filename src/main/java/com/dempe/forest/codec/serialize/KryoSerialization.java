package com.dempe.forest.codec.serialize;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/11/29
 * Time: 11:47
 * To change this template use File | Settings | File Templates.
 */
public class KryoSerialization implements Serialization {

    @Override
    public byte[] serialize(Object obj) throws IOException {
        return new byte[0];
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clz) throws IOException {
        return null;
    }
}
