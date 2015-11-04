package com.dempe.ketty.mvc.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 业务控制层注解
 * User: zhengdaxia
 * Date: 15/7/5
 * Time: 上午10:28
 * To change this template use File | Settings | File Templates.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Action {

    String value() default "";

}
