package com.dempe.forest.codec.serialize;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/11/29
 * Time: 11:46
 * To change this template use File | Settings | File Templates.
 */
public interface Serialization {

    byte[] serialize(Object obj) throws IOException;

    <T> T deserialize(byte[] bytes, Class<T> clz) throws IOException, ClassNotFoundException;
}

