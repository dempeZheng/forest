package com.dempe.forest.core.annotation;

import com.dempe.forest.core.CompressType;
import com.dempe.forest.core.SerializeType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/11/28
 * Time: 19:08
 * To change this template use File | Settings | File Templates.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Export {

    String uri() default "";

    long timeOut() default 5000;

    SerializeType serializeType() default SerializeType.kyro;

    CompressType compressType() default CompressType.compressNo;
}

