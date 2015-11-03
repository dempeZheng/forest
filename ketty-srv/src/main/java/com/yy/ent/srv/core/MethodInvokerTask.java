package com.yy.ent.srv.core;

import com.alibaba.fastjson.JSONObject;
import com.yy.ent.protocol.KettyRequest;
import com.yy.ent.protocol.KettyResponse;
import com.yy.ent.srv.exception.JServerException;
import com.yy.ent.srv.exception.ModelConvertJsonException;
import com.yy.ent.srv.uitl.MethodParam;
import com.yy.ent.srv.uitl.ResultConcert;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2015/10/21
 * Time: 10:59
 * To change this template use File | Settings | File Templates.
 */
public class MethodInvokerTask implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodInvokerTask.class);

    private ChannelHandlerContext ctx;
    private ServerContext serverContext;
    private KettyRequest req;

    public MethodInvokerTask(ChannelHandlerContext ctx, ServerContext serverContext, KettyRequest req) {
        this.ctx = ctx;
        this.serverContext = serverContext;
        this.req = req;
    }

    /**
     * 反射调用方法，根据方法参数自动注入value
     *
     * @param actionMethod
     * @param params
     * @return
     * @throws JServerException
     */
    public Object invoke(ActionMethod actionMethod, JSONObject params) throws JServerException {
        Method method = actionMethod.getMethod();
        String[] parameterNames = MethodParam.getParameterNames(method);

        Object[] parameterValues = MethodParam.getParameterValues(parameterNames, method, params);
        return actionMethod.call(parameterValues);
    }

    @Override
    public void run() {
        KettyResponse response = null;
        try {
            int id = req.getMsgId();
            JSONObject params = req.getParameter();
            String uri = req.getUri();
            LOGGER.debug("dispatcher id:{}, uri:{}", id, uri);
            response = dispatcher(uri, id, params);
            if (response != null) {
//            // 写入的时候已经release msg 无需显示的释放
                ctx.writeAndFlush(response);
            }
        } catch (ModelConvertJsonException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (JServerException e) {
            LOGGER.error(e.getMessage(), e);
        }


    }

    private KettyResponse dispatcher(String uri, Integer id, JSONObject requestParams) throws ModelConvertJsonException, JServerException {
        ActionMethod actionMethod = serverContext.get(uri);
        if (actionMethod == null) {
            LOGGER.warn("[dispatcher]:not find uri {}", uri);
        }
        Object result = invoke(actionMethod, requestParams);
        if (result == null) {
            // 当action method 返回是void的时候，不返回任何消息
            LOGGER.debug("actionMethod:{} return void.", actionMethod);
            return null;
        }

        if (id == null) {
            LOGGER.warn("request msg id is null,uri:{},params:{}", uri, requestParams);
        }
        return new KettyResponse(id, ResultConcert.toJSONString(result));
    }


}
