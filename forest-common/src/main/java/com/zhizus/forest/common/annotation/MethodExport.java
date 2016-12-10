package com.zhizus.forest.common.annotation;


import com.zhizus.forest.common.Constants;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Dempe on 2016/12/7.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface MethodExport {

    String group() default Constants.DEF_GROUP; // 业务组别，不同的group在不同的线程池
}
