package com.yy.ent.srv.method;

import com.yy.ent.srv.exception.JServerException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

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

    public String toString() {
        return "MessageActionMethod [target=" + target.getClass().getSimpleName() + ", method=" + method.getName()
                + "]";
    }
}
