package com.zhizus.forest;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.google.common.util.concurrent.RateLimiter;
import com.zhizus.forest.common.annotation.Interceptor;
import com.zhizus.forest.common.annotation.MethodExport;
import com.zhizus.forest.common.annotation.Rate;
import com.zhizus.forest.common.config.ServiceExportConfig;
import com.zhizus.forest.common.util.ForestUtil;
import com.zhizus.forest.core.ActionMethod;
import com.zhizus.forest.core.interceptor.InvokerInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * Created by Dempe on 2016/12/9.
 */
public class ForestRouter implements IRouter {

    private final static Logger LOGGER = LoggerFactory.getLogger(ForestRouter.class);

    private Map<String, ActionMethod> routerMapping = Maps.newConcurrentMap();

    private ApplicationContext context;

    public ForestRouter(ApplicationContext context) {
        this.context = context;
    }

    public void init(Object bean, ServiceExportConfig config) {
        for (Method method : bean.getClass().getMethods()) {
            MethodExport methodExport = method.getAnnotation(MethodExport.class);
            if (methodExport == null) {
                continue;
            }
            if (Strings.isNullOrEmpty(config.getServiceName()) || Strings.isNullOrEmpty(config.getMethodName(method))) {
                LOGGER.warn("methodName or service is null.methodName:{},serviceName:{}", config.getMethodName(method), config.getServiceName());
            }
            String uri = ForestUtil.buildUri(config.getServiceName(), config.getMethodName(method));
            ActionMethod actionMethod = new ActionMethod(bean, method);
            routerMapping.put(uri, actionMethod);

            initInterceptor(actionMethod, context);
            initRate(actionMethod);
        }
    }

    public void initRate(ActionMethod actionMethod) {
        Rate rate = actionMethod.getMethod().getAnnotation(Rate.class);
        if (rate != null) {
            int value = rate.value();
            if (value > 0) {
                actionMethod.setRateLimiter(RateLimiter.create(value));
            } else {
                LOGGER.warn("Rate value < 0 !");
            }
        }
    }

    public void initInterceptor(ActionMethod actionMethod, ApplicationContext context) {

        Interceptor interceptor = actionMethod.getMethod().getAnnotation(Interceptor.class);
        if (interceptor != null) {
            String id = interceptor.value();
            if (Strings.isNullOrEmpty(id) && context != null) {
                Class<?> clazz = interceptor.clazz();
                if (clazz == Object.class) {
                    LOGGER.warn("Interceptor id is empty !");
                } else {
                    Object bean = context.getBean(clazz);
                    if (bean != null && bean instanceof InvokerInterceptor) {
                        actionMethod.addInterceptorList((InvokerInterceptor) bean);
                    }
                }

            } else {
                for (String beanId : id.split(",")) {
                    Object bean = context.getBean(beanId);
                    if (bean != null && bean instanceof InvokerInterceptor) {
                        InvokerInterceptor invokerInterceptor = (InvokerInterceptor) bean;
                        actionMethod.addInterceptorList(invokerInterceptor);
                    }
                }
            }
        }
    }

    @Override
    public ActionMethod router(String uri) {
        return routerMapping.get(uri);
    }

}
