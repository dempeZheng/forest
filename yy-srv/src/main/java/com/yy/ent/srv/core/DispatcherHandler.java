package com.yy.ent.srv.core;

import com.alibaba.fastjson.JSONObject;
import com.yy.ent.protocol.json.Response;
import com.yy.ent.srv.exception.JServerException;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created with IntelliJ IDEA.
 * User: zhengdaxia
 * Date: 15/10/17
 * Time: 上午10:33
 * To change this template use File | Settings | File Templates.
 */
public class DispatcherHandler extends ChannelHandlerAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(DispatcherHandler.class);

    private ServerContext context;

    public DispatcherHandler(ServerContext context) {
        this.context = context;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        JSONObject json = JSONObject.parseObject((String) msg);
        Long id = json.getLong("id");
        JSONObject params = json.getJSONObject("params");
        //context.setRequestContext(params);
        Response response = dispatcher(json.getString("uri"), id, params);
        if (response != null) {
            ctx.writeAndFlush(response.toJsonStr());
        }
        //super.channelRead(ctx, msg);
    }

    private Response dispatcher(String uri, Long id, JSONObject requestParams) throws JServerException {
        ActionMethod actionMethod = context.get(uri);
        if (actionMethod == null) {
            LOGGER.warn("[dispatcher]:not find uri {}", uri);
        }
        Object result = new MethodInvoker().invoke(actionMethod, requestParams);
        if (result == null) {
            // 当action method 返回是void的时候，不返回任何消息
            LOGGER.debug("actionMethod:{} return void.", actionMethod);
            return null;
        }
        // TODO
        LOGGER.info("result:{}", result.toString());
        if (id == null) {
            LOGGER.warn("request msg id is null,uri:{},params:{}", uri, requestParams);
        }
        return new Response(id, result.toString());
    }


}
