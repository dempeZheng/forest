package com.yy.ent.srv.core;

import com.alibaba.fastjson.JSONObject;
import com.yy.ent.protocol.KettyRequest;
import com.yy.ent.protocol.KettyResponse;
import com.yy.ent.srv.exception.JServerException;
import com.yy.ent.srv.exception.ModelConvertJsonException;
import com.yy.ent.srv.interceptor.KettyInterceptor;
import com.yy.ent.srv.uitl.MethodParam;
import com.yy.ent.srv.uitl.ResultConcert;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2015/10/21
 * Time: 10:59
 * To change this template use File | Settings | File Templates.
 */
public class MethodInvokerTask implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodInvokerTask.class);

    private ServerContext serverContext;

    public MethodInvokerTask(ServerContext serverContext) {
        this.serverContext = serverContext;
    }

    /**
     * @param actionMethod
     * @param params
     * @return
     * @throws JServerException
     */
    public Object invoke(ActionMethod actionMethod, JSONObject params) throws JServerException {
        Method method = actionMethod.getMethod();

        List<KettyInterceptor> interceptorList = actionMethod.getInterceptorList();
        Iterator<KettyInterceptor> interceptorIterator = interceptorList.iterator();

        String[] parameterNames = MethodParam.getParameterNames(method);
        Object[] parameterValues = MethodParam.getParameterValues(parameterNames, method, params);

        boolean flag = true;
        while (interceptorIterator.hasNext() && flag) {
            KettyInterceptor interceptor = interceptorIterator.next();
            flag = interceptor.before(serverContext);
        }
        if (!flag) {
            return null;
        }
        // 拦截器前
        Object result = actionMethod.call(parameterValues);
        interceptorIterator = interceptorList.iterator();
        // 拦截器后
        while (interceptorIterator.hasNext() && flag) {
            KettyInterceptor interceptor = interceptorIterator.next();
            flag = interceptor.after(serverContext, result);
        }
        return result;
    }

    @Override
    public void run() {
        KettyResponse response = null;
        try {
            KettyRequest req = (KettyRequest) serverContext.getRequest();
            ChannelHandlerContext ctx = serverContext.getCtx();
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
        return new KettyResponse(id, ResultConcert.toJSONString(result));
    }


}
