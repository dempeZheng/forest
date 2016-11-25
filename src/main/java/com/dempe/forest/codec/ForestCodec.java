package com.dempe.forest.codec;

import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/11/25
 * Time: 18:10
 * To change this template use File | Settings | File Templates.
 */
public class ForestCodec implements Codec<Message> {

    @Override
    public MessageToByteEncoder<Message> getEncoder() {
        return new ForestEncoder();
    }

    @Override
    public ByteToMessageDecoder getDecoder() {
        return new ForestDecoder();
    }
}
