package com.zhizus.forest.common.annotation;


import com.zhizus.forest.common.CompressType;
import com.zhizus.forest.common.Constants;
import com.zhizus.forest.common.SerializeType;

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

