package com.dempe.forest.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 速率限制注解
 * 默认1000每秒
 * User: Dempe
 * Date: 2015/11/6
 * Time: 15:10
 * To change this template use File | Settings | File Templates.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Rate {
    int value() default 1000;
}
