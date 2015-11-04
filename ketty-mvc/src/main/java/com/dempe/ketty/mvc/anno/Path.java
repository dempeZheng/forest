package com.dempe.ketty.mvc.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 协议uri，默认为方法名
 * User: zhengdaxia
 * Date: 15/7/5
 * Time: 上午10:15
 * To change this template use File | Settings | File Templates.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Path {
    String value() default "";
}

