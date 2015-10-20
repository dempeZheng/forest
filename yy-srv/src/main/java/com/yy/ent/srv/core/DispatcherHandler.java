package com.yy.ent.srv.core;

import com.alibaba.fastjson.JSONObject;
import com.yy.ent.srv.action.SimpleAction;
import com.yy.ent.srv.exception.JServerException;
import com.yy.ent.srv.method.ActionMethod;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created with IntelliJ IDEA.
 * User: zhengdaxia
 * Date: 15/10/17
 * Time: 上午10:33
 * To change this template use File | Settings | File Templates.
 */
public class DispatcherHandler extends ChannelHandlerAdapter {

    private ServerContext context;

    public DispatcherHandler(ServerContext context) {
        this.context = context;
    }


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("===========>>>" + msg);
        JSONObject json = JSONObject.parseObject((String) msg);
        Long id = json.getLong("id");
        JSONObject params = json.getJSONObject("params");
        context.setRequestContext(params);
        String uri = json.getString("uri");
        System.out.println(uri);

        dispatcher(uri);

//        String uri = message.getHeader().getUri();
//        if (StringUtils.isNotBlank(uri)) {
//            dispatcher(uri, ctx, message);
//        }

        super.channelRead(ctx, msg);
    }

    private void dispatcher(String uri) throws JServerException {
        ActionMethod actionMethod = context.get(uri);
        actionMethod.call();


    }


}
