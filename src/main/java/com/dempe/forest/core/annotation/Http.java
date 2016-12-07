package com.dempe.forest.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Dempe on 2016/12/7.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Http {

    enum HttpMethod {
        DELETE,
        GET,
        POST,
        PATCH,
        PUT
    }

    HttpMethod method();

    String uri() default "";

    Header[] headers() default {};

    @interface Header {

        String name();

        String value();
    }
}
