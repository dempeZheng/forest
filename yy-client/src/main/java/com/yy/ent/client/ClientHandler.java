package com.yy.ent.client;

import com.alibaba.fastjson.JSONObject;
import com.yy.ent.protocol.json.Response;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2015/10/21
 * Time: 11:41
 * To change this template use File | Settings | File Templates.
 */
public class ClientHandler extends ChannelHandlerAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClientHandler.class);
    private ReplyWaitQueue replyQueue = new ReplyWaitQueue();


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        JSONObject json = JSONObject.parseObject((String) msg);
        Long id = json.getLong("id");
        Response response = new Response(id, json.getString("data"));
        ReplyFuture future = replyQueue.take(id);
        future.onReceivedReply(response);
        LOGGER.info("result = {}", json);
    }


}
