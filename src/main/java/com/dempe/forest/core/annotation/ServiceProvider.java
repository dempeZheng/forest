package com.dempe.forest.core.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * 发布服务
 * User: Dempe
 * Date: 2016/12/3 0003
 * Time: 下午 7:36
 * To change this template use File | Settings | File Templates.
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface ServiceProvider {

    String serviceName() default "";

    int port() default 0;

}
