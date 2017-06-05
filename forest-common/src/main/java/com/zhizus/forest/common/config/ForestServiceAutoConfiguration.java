package com.zhizus.forest.common.config;

import com.zhizus.forest.common.annotation.ForestService;
import org.springframework.context.annotation.Configuration;

/**
 * Created by dempezheng on 2017/6/5.
 */
@Configuration
@ConditionalOnClass(ForestService.class)
@ConditionalOnWebApplication
public class ForestServiceAutoConfiguration {
}
