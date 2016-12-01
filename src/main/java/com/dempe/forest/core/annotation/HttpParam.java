package com.dempe.forest.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用户http协议参数对应
 * User: Dempe
 * Date: 2015/7/17
 * Time: 18:56
 * To change this template use File | Settings | File Templates.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface HttpParam {
    String value() default "";
}
