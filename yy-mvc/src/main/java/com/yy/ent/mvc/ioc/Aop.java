package com.yy.ent.mvc.ioc;

import java.lang.reflect.Method;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2015/10/19
 * Time: 19:55
 * To change this template use File | Settings | File Templates.
 */
public interface Aop {

    /**
     * 方法执行前，会执行的方法
     *
     * @return
     * @throws Exception
     */
    public void before(Method method, Object[] args) throws Exception;

    /**
     * 方法执行后，会执行的方法
     *
     * @return
     * @throws Exception
     */
    public void after(Method method, Object[] args) throws Exception;
}
