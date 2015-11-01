package com.yy.ent.codec;

import com.alibaba.fastjson.JSONObject;
import com.yy.ent.pack.Pack;
import com.yy.ent.protocol.JettyRequest;
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
public class JettyRequestEncoder extends MessageToByteEncoder<JettyRequest> {

    /**
     * TODO  根据协议自动自动编码
     * @param channelHandlerContext
     * @param req
     * @param byteBuf
     * @throws Exception
     */
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, JettyRequest req, ByteBuf byteBuf) throws Exception {

        req.encoder(byteBuf);
    }
}
