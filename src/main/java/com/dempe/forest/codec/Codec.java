package com.dempe.forest.codec;

import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/11/25
 * Time: 16:39
 * To change this template use File | Settings | File Templates.
 */
public interface Codec<I> {

    MessageToByteEncoder<I> getEncoder();

    ByteToMessageDecoder getDecoder();
}
