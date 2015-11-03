package com.yy.ent.srv.core;

import com.yy.ent.mvc.interceptor.KettyInterceptor;
import com.yy.ent.srv.exception.JServerException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2015/10/15
 * Time: 16:19
 * To change this template use File | Settings | File Templates.
 */
public class ActionMethod {


    private Object target;

    private Method method;

    private List<KettyInterceptor> interceptorList = new ArrayList<KettyInterceptor>();

    public ActionMethod() {
    }

    /**
     * @param target
     * @param method
     */
    public ActionMethod(Object target, Method method) {
        this.target = target;
        this.method = method;
    }

    public Object call(Object... args) throws JServerException {
        try {
            return method.invoke(target, args);
        } catch (IllegalAccessException e) {
            throw new JServerException("MessageAction method exception", e);
        } catch (IllegalArgumentException e) {
            throw new JServerException("MessageAction method exception", e);
        } catch (InvocationTargetException e) {
            throw new JServerException("MessageAction method exception", e);
        }
    }

    public void setTarget(Object target) {
        this.target = target;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public Object getTarget() {
        return target;
    }

    public Method getMethod() {
        return method;
    }

    public List<KettyInterceptor> getInterceptorList() {
        return interceptorList;
    }

    public void addInterceptor(KettyInterceptor interceptor) {
        interceptorList.add(interceptor);
    }

    public String toString() {
        return "MessageActionMethod [target=" + target.getClass().getSimpleName() + ", method=" + method.getName()
                + "]";
    }
}
