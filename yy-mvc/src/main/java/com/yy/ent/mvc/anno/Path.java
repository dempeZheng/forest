package com.yy.ent.mvc.anno;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created with IntelliJ IDEA.
 * User: zhengdaxia
 * Date: 15/7/5
 * Time: 上午10:15
 * To change this template use File | Settings | File Templates.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Path {
    String value() default "";
}

