package com.dempe.forest.core.handler;

import com.dempe.forest.codec.Message;
import com.dempe.forest.codec.Response;
import com.dempe.forest.codec.compress.Compress;
import com.dempe.forest.codec.serialize.Serialization;
import com.dempe.forest.core.CompressType;
import com.dempe.forest.core.SerializeType;
import com.dempe.forest.transport.NettyResponseFuture;
import com.google.common.collect.Maps;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/11/30
 * Time: 14:30
 * To change this template use File | Settings | File Templates.
 */
public class ClientHandler extends SimpleChannelInboundHandler<Message> {


    private final static Map<Long, NettyResponseFuture<Response>> callbackMap = Maps.newConcurrentMap();

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Message message) throws Exception {
        byte[] payload = message.getPayload();
        Byte extend = message.getHeader().getExtend();
        Serialization serialization = SerializeType.getSerializationByExtend(extend);
        Compress compress = CompressType.getCompressTypeByValueByExtend(extend);
        payload = compress.unCompress(payload);
        Response response = serialization.deserialize(payload, Response.class);
        Long messageID = message.getHeader().getMessageID();
        NettyResponseFuture responseFuture = callbackMap.remove(messageID);
        responseFuture.onReceive(response);

    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }

    public NettyResponseFuture registerCallbackMap(Long messageId, NettyResponseFuture<Response> responseFuture) {
        return callbackMap.put(messageId, responseFuture);
    }

    public NettyResponseFuture removeCallbackMap(Long messageId) {
        return callbackMap.remove(messageId);
    }
}
