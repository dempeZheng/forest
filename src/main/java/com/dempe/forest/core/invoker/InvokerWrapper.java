package com.dempe.forest.core.invoker;

import com.dempe.forest.codec.Message;
import com.dempe.forest.codec.serialize.FastJsonSerialization;
import com.dempe.forest.codec.serialize.KryoSerialization;
import com.dempe.forest.codec.serialize.Serialization;
import com.dempe.forest.core.annotation.Action;
import com.google.common.collect.Lists;
import com.sun.corba.se.impl.interceptors.InterceptorInvoker;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/11/25
 * Time: 17:09
 * To change this template use File | Settings | File Templates.
 */
public class InvokerWrapper {

    private ActionMethod actionMethod;

    private Message message;

    public InvokerWrapper(ActionMethod actionMethod, Message message) {
        this.actionMethod = actionMethod;
        this.message = message;
    }

    public Object invoke() throws InvocationTargetException, IllegalAccessException, ClassNotFoundException, IOException {
        byte[] payload = message.getPayload();
        Serialization serialization = new FastJsonSerialization();
        String[] parameterNames = MethodParam.getParameterNames(actionMethod.getMethod());
        Object[] args = new Object[parameterNames.length];
        for (int i = 0; i < args.length; i++) {
            Class<?> aClass = ReflectUtil.forName(parameterNames[i]);
            Object param = serialization.deserialize(payload, aClass);
            args[i] = param;
        }
        return actionMethod.rateLimiterInvoker(args);
    }


    public Message getMessage() {
        return message;
    }
}
