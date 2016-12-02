package com.dempe.forest.core.annotation;

import com.dempe.forest.Constants;
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

    String uri() default ""; // 路由uri

    SerializeType serializeType() default SerializeType.kyro;

    CompressType compressType() default CompressType.compressNo;

    long timeOut() default 5000; // 客户端超时时间

    String group() default Constants.DEF_GROUP; // 业务组别，不同的group在不同的线程池
}

