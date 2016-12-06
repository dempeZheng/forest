package com.dempe.forest.core.annotation;

import java.lang.annotation.*;

/**
 * User: Dempe
 * Date: 2016/11/28
 * Time: 19:04
 * To change this template use File | Settings | File Templates.
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
//@Component
public @interface ServiceExport {
}
