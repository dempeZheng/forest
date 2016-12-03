package com.dempe.forest.core.annotation;

import com.dempe.forest.core.CompressType;
import com.dempe.forest.core.SerializeType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 服务提供
 * User: Dempe
 * Date: 2016/11/28
 * Time: 19:08
 * To change this template use File | Settings | File Templates.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface MethodProvider {

    String methodName() default "";

    SerializeType serializeType() default SerializeType.kyro;

    CompressType compressType() default CompressType.compressNo;

    long timeout() default 5000; // 客户端超时时间

}

