package com.dempe.forest.core.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * Created by Dempe on 2016/12/7.
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface ServiceProvider {

    String serviceName() default "";

    int port() default 0;

}
