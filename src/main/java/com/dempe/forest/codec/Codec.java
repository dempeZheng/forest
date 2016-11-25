package com.dempe.forest.codec;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/11/25
 * Time: 16:39
 * To change this template use File | Settings | File Templates.
 */
public interface Codec {

    byte[] encode(Object message);

    Object decode(byte[] buffer);
}
