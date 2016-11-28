package com.dempe.forest.core.invoker;

import com.dempe.forest.codec.Message;
import com.google.common.collect.Lists;
import com.sun.corba.se.impl.interceptors.InterceptorInvoker;

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

    private List<InterceptorInvoker> interceptorInvokers = Lists.newArrayList();

    public Object invoke() throws InvocationTargetException, IllegalAccessException {
        //TODO message转换为args[]
        Object[] args = new Object[]{};
        return actionMethod.rateLimiterInvoker(args);
    }


    public Message getMessage() {
        return message;
    }
}
