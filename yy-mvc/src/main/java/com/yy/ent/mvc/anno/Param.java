package com.yy.ent.mvc.anno;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2015/7/17
 * Time: 18:56
 * To change this template use File | Settings | File Templates.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Param {
    String value() default "";
}
