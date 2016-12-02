package com.dempe.forest.core.handler;

import com.dempe.forest.client.Connection;
import com.dempe.forest.codec.Message;
import com.dempe.forest.codec.Response;
import com.dempe.forest.codec.compress.Compress;
import com.dempe.forest.codec.serialize.Serialization;
import com.dempe.forest.core.CompressType;
import com.dempe.forest.core.SerializeType;
import com.dempe.forest.core.exception.ForestErrorMsgConstant;
import com.dempe.forest.transport.NettyResponseFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/11/30
 * Time: 14:30
 * To change this template use File | Settings | File Templates.
 */
public class ClientHandler extends SimpleChannelInboundHandler<Message> {

    private final static Logger LOGGER = LoggerFactory.getLogger(ChannelHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Message message) throws Exception {
        NettyResponseFuture responseFuture = Connection.callbackMap.remove(message.getHeader().getMessageID());
        Response response = null;
        byte[] payload = message.getPayload();
        Byte extend = message.getHeader().getExtend();
        Serialization serialization = SerializeType.getSerializationByExtend(extend);
        Compress compress = CompressType.getCompressTypeByValueByExtend(extend);
        try {
            payload = compress.unCompress(payload);
            response = serialization.deserialize(payload, Response.class);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            response = new Response();
            response.setForestErrorMsg(ForestErrorMsgConstant.FRAMEWORK_DECODE_ERROR);
        } finally {
            responseFuture.getPromise().onReceive(response);
        }


    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }


}

