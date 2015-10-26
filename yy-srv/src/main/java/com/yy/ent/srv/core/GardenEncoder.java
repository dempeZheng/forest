package com.yy.ent.srv.core;

import com.yy.ent.commons.protopack.base.Packet;
import com.yy.ent.protocol.GardenReq;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.MessageToByteEncoder;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2015/10/26
 * Time: 20:03
 * To change this template use File | Settings | File Templates.
 */
public class GardenEncoder extends MessageToByteEncoder<GardenReq> {

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, GardenReq req, ByteBuf byteBuf) throws Exception {
        byteBuf.writeBytes(req.encoder());
    }
}
