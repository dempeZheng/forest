package com.yy.ent.srv.ketty;

import com.alibaba.fastjson.JSONObject;
import com.yy.ent.protocol.KettyRequest;
import com.yy.ent.protocol.KettyResponse;
import com.yy.ent.srv.core.ActionMethod;
import com.yy.ent.srv.core.ActionTake;
import com.yy.ent.srv.exception.ModelConvertJsonException;
import com.yy.ent.srv.uitl.MethodInvoker;
import com.yy.ent.srv.uitl.MethodParam;
import com.yy.ent.srv.uitl.ResCode;
import com.yy.ent.srv.uitl.ResultConcert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2015/11/4
 * Time: 10:17
 * To change this template use File | Settings | File Templates.
 */
public class KettyActionTack implements ActionTake<KettyResponse, KettyRequest> {

    private final static Logger LOGGER = LoggerFactory.getLogger(KettyActionTack.class);

    private KettyServerContext context;

    public KettyActionTack(KettyServerContext context) {
        this.context = context;
    }


    public KettyResponse act(KettyRequest request) throws InvocationTargetException, IllegalAccessException, ModelConvertJsonException {
        int id = request.getMsgId();
        JSONObject params = request.getParameter();
        String uri = request.getUri();
        LOGGER.debug("dispatcher id:{}, uri:{}", id, uri);

        ActionMethod actionMethod = context.tackAction(uri);
        if (actionMethod == null) {
            LOGGER.warn("[dispatcher]:not find uri {}", uri);
            return null;
        }
        Method method = actionMethod.getMethod();
        String[] parameterNames = MethodParam.getParameterNames(method);
        Object[] parameterValues = MethodParam.getParameterValues(parameterNames, method, params);
        Object result = MethodInvoker.interceptorInvoker(actionMethod, parameterValues);
        if (result == null) {
            // 当action method 返回是void的时候，不返回任何消息
            LOGGER.debug("actionMethod:{} return void.", actionMethod);

            return new KettyResponse(id, ResCode.NO_PERMISSION.getResCode(), ResCode.NO_PERMISSION.getMsg());
        }
        return new KettyResponse(id, ResultConcert.toJSONString(result));
    }


}
