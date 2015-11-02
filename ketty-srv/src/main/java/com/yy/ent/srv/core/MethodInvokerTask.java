package com.yy.ent.srv.core;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yy.ent.common.utils.LocalVariableTableParameterNameDiscoverer;
import com.yy.ent.mvc.anno.Param;
import com.yy.ent.protocol.KettyRequest;
import com.yy.ent.protocol.KettyResponse;
import com.yy.ent.srv.exception.JServerException;
import com.yy.ent.srv.exception.ModelConvertJsonException;
import io.netty.channel.ChannelHandlerContext;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

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
        String[] parameterNames = new LocalVariableTableParameterNameDiscoverer().getParameterNames(method);
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        for (Annotation[] parameterAnnotation : parameterAnnotations) {
            for (int i = 0; i < parameterAnnotation.length; i++) {
                if (parameterAnnotation[i].annotationType() == Param.class) {
                    String value = ((Param) parameterAnnotation[i]).value();
                    if (StringUtils.isNotBlank(value)) {
                        parameterNames[i] = value;
                    }
                }
            }
        }
        Object[] paramTarget = null;
        if (parameterNames != null) {
            paramTarget = new Object[parameterNames.length];
            Type[] type = method.getGenericParameterTypes();
            for (int i = 0; i < parameterNames.length; i++) {
                if (Integer.class == type[i] || StringUtils.equals(type[i].toString(), "int")) {
                    paramTarget[i] = params.getInteger(parameterNames[i]);
                } else if (String.class == type[i]) {
                    paramTarget[i] = params.getString(parameterNames[i]);
                } else if (Boolean.class == type[i] || StringUtils.equals(type[i].toString(), "boolean")) {
                    paramTarget[i] = params.getBoolean(parameterNames[i]);
                } else if (Long.class == type[i] || StringUtils.equals(type[i].toString(), "long")) {
                    paramTarget[i] = params.getLong(parameterNames[i]);
                } else if (Short.class == type[i] || StringUtils.equals(type[i].toString(), "short")) {
                    paramTarget[i] = params.getShort(parameterNames[i]);
                } else if (Double.class == type[i] || StringUtils.equals(type[i].toString(), "double")) {
                    paramTarget[i] = params.getDouble(parameterNames[i]);
                } else if (Float.class == type[i] || StringUtils.equals(type[i].toString(), "float")) {
                    paramTarget[i] = params.getFloat(parameterNames[i]);
                }

            }
        }
        return actionMethod.call(paramTarget);
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

    public void doInvoke(){
        KettyResponse response = null;
        try {
            int id = req.getMsgId();
            JSONObject params = req.getParameter();
            String uri = req.getUri();
            LOGGER.info("dispatcher id:{}, uri:{}", id, uri);
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
        return new KettyResponse(id, toJSONString(result));
    }

    /**
     * 将对象转换成JSONString
     *
     * @param result
     * @return
     * @throws ModelConvertJsonException
     */
    private String toJSONString(Object result) throws ModelConvertJsonException {
        if (result instanceof String) {
            return result.toString();
        }
        String data = null;
        try {
            data = JSON.toJSONString(result);
        } catch (Exception e) {
            LOGGER.error("model convert 2 json err:{} parse json error", result);
            throw new ModelConvertJsonException("model convert 2 json err");
        }
        return data;
    }
}
