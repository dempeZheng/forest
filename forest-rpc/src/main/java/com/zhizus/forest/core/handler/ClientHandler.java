package com.zhizus.forest.core.handler;

import com.zhizus.forest.client.Connection;
import com.zhizus.forest.common.codec.Message;
import com.zhizus.forest.common.codec.Response;
import com.zhizus.forest.common.codec.compress.Compress;
import com.zhizus.forest.common.codec.serialize.Serialization;
import com.zhizus.forest.common.CompressType;
import com.zhizus.forest.common.SerializeType;
import com.zhizus.forest.common.exception.ForestErrorMsgConstant;
import com.zhizus.forest.transport.NettyResponseFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * Created by Dempe on 2016/12/7.
 */
public class ClientHandler extends SimpleChannelInboundHandler<Message> {

    private final static Logger LOGGER = LoggerFactory.getLogger(ChannelHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Message message) throws Exception {
        NettyResponseFuture responseFuture = Connection.callbackMap.remove(message.getHeader().getMessageID());
        if (responseFuture == null) {
            // 服务端响应超时，NettyResponseFuture已经被回收，理论上应该将callbackMap的超时回收时间大于客户端设置的服务超时时间
            return;
        }
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

