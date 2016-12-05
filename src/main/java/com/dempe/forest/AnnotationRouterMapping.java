package com.dempe.forest;

import com.dempe.forest.core.ActionMethod;
import com.dempe.forest.core.MethodParam;
import com.dempe.forest.core.annotation.*;
import com.dempe.forest.core.interceptor.InvokerInterceptor;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.common.util.concurrent.RateLimiter;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Map;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/11/28
 * Time: 9:59
 * To change this template use File | Settings | File Templates.
 */
public class AnnotationRouterMapping {

    private final static Logger LOGGER = LoggerFactory.getLogger(AnnotationRouterMapping.class);

    private ApplicationContext context;


    private Map<String, ActionMethod> mapping = Maps.newConcurrentMap();

    public AnnotationRouterMapping(ApplicationContext context) {
        this.context = context;
        initMapping();
    }

    public ActionMethod getInvokerWrapperByURI(String uri) {
        return mapping.get(uri);
    }

    public void initMapping() {
        // 获取spring中Action注解的bean
        String[] beanNamesForAnnotation = context.getBeanNamesForAnnotation(ServiceExport.class);
        for (String actionBeanName : beanNamesForAnnotation) {
            Object actionBean = context.getBean(actionBeanName);
            Class<?>[] interfaces = actionBean.getClass().getInterfaces();
            for (Class<?> anInterface : interfaces) {
                ServiceProvider serviceProvider = anInterface.getAnnotation(ServiceProvider.class);
                if (serviceProvider == null) {
                    continue;
                }
                String serviceName = Strings.isNullOrEmpty(serviceProvider.serviceName())
                        ? anInterface.getSimpleName() : serviceProvider.serviceName();
                int port = serviceProvider.port();

                // todo 分离不同的服务
                if (port == 0) {
                    // 使用默认的服务
                }
                initMethodProvider(anInterface, actionBean, serviceName);

            }

        }

    }

    public void initMethodProvider(Class<?> anInterface, Object actionBean, String serviceName) {
        for (Method method : anInterface.getDeclaredMethods()) {
            MethodProvider refs = method.getAnnotation(MethodProvider.class);
            if (refs != null) {
                String methodName = String.valueOf(refs.methodName());
                if (StringUtils.isBlank(methodName)) {
                    methodName = method.getName();
                }
                String uri = ForestUtil.buildUri(serviceName, methodName);
                if (mapping.containsKey(uri)) {
                    LOGGER.warn("Method:{} declares duplicated methodName:{}, previous one will be overwritten", method, uri);
                }


                Method methodByInterfaceMethod = ForestUtil.findMethodByInterfaceMethod(method, actionBean.getClass());
                if (methodByInterfaceMethod == null) {
                    LOGGER.error("methodByInterfaceMethod is null");
                    continue;
                }
                MethodExport methodExport = methodByInterfaceMethod.getAnnotation(MethodExport.class);
                if (methodExport == null) {
                    LOGGER.info("{} {} Impl {} {} not export", anInterface.getName(), method.getName(),
                            actionBean.getClass().getName(), methodByInterfaceMethod.getName());
                    continue;
                }
                makeAccessible(methodByInterfaceMethod);

                ActionMethod actionMethod = new ActionMethod(actionBean, methodByInterfaceMethod);
                String[] parameterNames = MethodParam.getParameterNames(methodByInterfaceMethod);
                actionMethod.setArgsName(parameterNames);
                LOGGER.info("Register router mapping : {}, methodName : {}", serviceName, uri);

                // group

                actionMethod.setGroup(methodExport.group());

                // Interceptor
                Interceptor interceptor = methodByInterfaceMethod.getAnnotation(Interceptor.class);
                if (interceptor != null) {
                    String id = interceptor.value();
                    if (Strings.isNullOrEmpty(id)) {
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
                            InvokerInterceptor invokerInterceptor = (InvokerInterceptor) context.getBean(beanId);
                            if (invokerInterceptor == null) {
                                LOGGER.warn("interceptor for value:{} not exist in spring container", beanId);
                            }
                            actionMethod.addInterceptorList(invokerInterceptor);
                        }
                    }
                }

                // Rate
                Rate rate = methodByInterfaceMethod.getAnnotation(Rate.class);
                if (rate != null) {
                    int value = rate.value();
                    if (value > 0) {
                        actionMethod.setRateLimiter(RateLimiter.create(value));
                    } else {
                        LOGGER.warn("Rate value < 0 !");
                    }
                }


                mapping.put(uri, actionMethod);
            }
        }
    }

    public Set<String> listGroup() {
        Set<String> groupSet = Sets.newHashSet();
        for (Map.Entry<String, ActionMethod> actionMethodEntry : mapping.entrySet()) {
            groupSet.add(actionMethodEntry.getValue().getGroup());
        }
        return groupSet;
    }


    protected void makeAccessible(Method method) {
        if ((!Modifier.isPublic(method.getModifiers()) || !Modifier.isPublic(method.getDeclaringClass().getModifiers()))
                && !method.isAccessible()) {
            method.setAccessible(true);
        }
    }
}
