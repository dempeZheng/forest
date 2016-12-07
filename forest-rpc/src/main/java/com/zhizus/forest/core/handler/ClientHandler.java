package com.zhizus.forest.core.handler;

import com.zhizus.forest.client.Connection;
import com.zhizus.forest.common.codec.Message;
import com.zhizus.forest.common.codec.Response;
import com.zhizus.forest.transport.NettyResponseFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Dempe on 2016/12/7.
 */
public class ClientHandler extends SimpleChannelInboundHandler<Message<Response>> {

    private final static Logger LOGGER = LoggerFactory.getLogger(ChannelHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Message<Response> message) throws Exception {
        NettyResponseFuture responseFuture = Connection.callbackMap.remove(message.getHeader().getMessageID());
        if (responseFuture == null) {
            // 服务端响应超时，NettyResponseFuture已经被回收，理论上应该将callbackMap的超时回收时间大于客户端设置的服务超时时间
            return;
        }
        Response response = message.getContent();
        responseFuture.getPromise().onReceive(response);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }


}

