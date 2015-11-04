package com.dempe.ketty.codec;

import com.dempe.ketty.protocol.KettyResponse;
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
public class KettyRespEncoder extends MessageToByteEncoder<KettyResponse> {

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, KettyResponse resp, ByteBuf byteBuf) throws Exception {

        resp.encode(byteBuf);
    }
}
