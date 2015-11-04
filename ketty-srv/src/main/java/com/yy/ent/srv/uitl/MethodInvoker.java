package com.yy.ent.srv.uitl;

import com.yy.ent.srv.core.ActionMethod;
import com.yy.ent.srv.interceptor.KettyInterceptor;

import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2015/11/4
 * Time: 10:06
 * To change this template use File | Settings | File Templates.
 */
public class MethodInvoker {


    public static Object interceptorInvoker(ActionMethod actionMethod, Object[] parameterValues)
            throws InvocationTargetException, IllegalAccessException {
        List<KettyInterceptor> interceptorList = actionMethod.getInterceptorList();
        Iterator<KettyInterceptor> interceptorIterator = interceptorList.iterator();
        boolean flag = true;
        while (interceptorIterator.hasNext() && flag) {
            KettyInterceptor interceptor = interceptorIterator.next();
            flag = interceptor.before();
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
            flag = interceptor.after();
        }
        return result;
    }
}
