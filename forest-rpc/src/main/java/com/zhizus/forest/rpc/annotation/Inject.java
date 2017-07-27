package com.zhizus.forest.rpc.annotation;

import java.lang.annotation.*;

/**
 * Created by Dempe on 2017/7/27 0027.
 */
@Target({ElementType.CONSTRUCTOR, ElementType.FIELD, ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Inject {
    boolean required() default true;

    String value() default "";
}
