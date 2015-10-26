package com.yy.ent.srv.core;

import com.yy.ent.protocol.GardenReq;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2015/10/26
 * Time: 20:08
 * To change this template use File | Settings | File Templates.
 */
public class GardenDecoder extends ByteToMessageDecoder{

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {

    }
}
