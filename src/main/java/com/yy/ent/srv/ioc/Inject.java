package com.yy.ent.srv.ioc;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2015/10/19
 * Time: 19:50
 * To change this template use File | Settings | File Templates.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Inject {

    /**
     * 注入实例标识
     */
    String id() default "";

    /**
     * 是否为单实模式  默认为true,目前只支持单例*
     */
    boolean isSingle() default true;

    /**
     * 实例名称  如果注入类型的实例不唯一，需要用名称标示(全路径[com.yy.etn..])*
     */
    Class<?> instance() default Object.class;
}