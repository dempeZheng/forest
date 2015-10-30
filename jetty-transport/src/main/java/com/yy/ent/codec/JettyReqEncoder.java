package com.yy.ent.codec;

import com.yy.ent.protocol.JettyReq;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2015/10/26
 * Time: 20:03
 * To change this template use File | Settings | File Templates.
 */
public class JettyReqEncoder extends MessageToByteEncoder<JettyReq> {

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, JettyReq req, ByteBuf byteBuf) throws Exception {
        System.out.println("_________________encoder-----------------------");
        byte[] bytes = req.encoder();
        byteBuf.writeShort(bytes.length);
        byteBuf.writeBytes(bytes);
    }
}
