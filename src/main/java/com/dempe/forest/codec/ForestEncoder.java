package com.dempe.forest.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/11/25
 * Time: 16:34
 * To change this template use File | Settings | File Templates.
 */
public class ForestEncoder extends MessageToByteEncoder<Message> {


    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Message message, ByteBuf byteBuf) throws Exception {
        Header header = message.getHeader();
        byte[] encode = null;
        byteBuf.writeBytes(encode);
    }
}
