package com.zhizus.forest.rpc.annotation;

import java.lang.annotation.*;

/**
 * Created by dempezheng on 2017/7/6.
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ThriftMethodProvider {

    //默认不限流
    int rate() default -1;

    // 开启日志
    boolean accessLogOn() default true;

}
