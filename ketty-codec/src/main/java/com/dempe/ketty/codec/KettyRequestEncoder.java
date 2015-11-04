package com.dempe.ketty.codec;

import com.dempe.ketty.protocol.KettyRequest;
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
public class KettyRequestEncoder extends MessageToByteEncoder<KettyRequest> {

    /**
     *
     * @param channelHandlerContext
     * @param req
     * @param byteBuf
     * @throws Exception
     */
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, KettyRequest req, ByteBuf byteBuf) throws Exception {

        //
        req.encoder(byteBuf);
    }
}
