package com.dempe.forest.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * Created by Dempe on 2016/12/7.
 */
public class ForestEncoder extends MessageToByteEncoder<Message> {

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Message message, ByteBuf byteBuf) throws Exception {
        Header header = message.getHeader();
        byteBuf.writeShort(header.getMagic());
        byteBuf.writeByte(header.getVersion());
        byteBuf.writeByte(header.getExtend());
        byteBuf.writeLong(header.getMessageID());
        byteBuf.writeShort(header.getUri().length());
        byteBuf.writeBytes(header.getUri().getBytes());
        byteBuf.writeInt(message.getPayload().length);
        byteBuf.writeBytes(message.getPayload());
    }
}
