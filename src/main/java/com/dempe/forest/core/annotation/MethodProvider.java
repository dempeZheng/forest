package com.dempe.forest.core.annotation;

import com.dempe.forest.Constants;
import com.dempe.forest.core.CompressType;
import com.dempe.forest.core.SerializeType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Dempe on 2016/12/7.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface MethodProvider {

    String methodName() default "";

    SerializeType serializeType() default SerializeType.kyro;

    CompressType compressType() default CompressType.compressNo;

    int timeout() default Constants.DEFAULT_TIMEOUT; // 客户端超时时间

}

