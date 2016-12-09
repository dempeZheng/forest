package com.zhizus.forest.common.annotation;

import com.zhizus.forest.common.Constants;
import com.zhizus.forest.common.HaStrategyType;
import com.zhizus.forest.common.LoadBalanceType;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * Created by Dempe on 2016/12/7.
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface ServiceProvider {

    String serviceName() default "";

    HaStrategyType haStrategyType() default HaStrategyType.FAIL_FAST;

    LoadBalanceType loadBalanceType() default LoadBalanceType.RANDOM;

    String hashKey() default "";// 仅当使用hash策略时候使用

    int connectionTimeout() default Constants.CONNECTION_TIMEOUT;


}
