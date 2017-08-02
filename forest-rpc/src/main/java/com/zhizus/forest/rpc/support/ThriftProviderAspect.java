package com.zhizus.forest.rpc.support;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.google.common.util.concurrent.RateLimiter;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.aspectj.MethodInvocationProceedingJoinPoint;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * Created by dempezheng on 2017/7/6.
 */
@Aspect
@Component
public class ThriftProviderAspect {
    private static final Logger logger = LoggerFactory.getLogger(ThriftProviderAspect.class);

    private final static Map<Method, MethodWrapper> thriftMethodWrapperMap = Maps.newConcurrentMap();

    @Pointcut("@annotation(com.zhizus.forest.rpc.support.ThriftMethodProvider)")
    public void thriftProviderMethodPointcut() {
    }

    @Around("thriftProviderMethodPointcut()")
    public Object interceptor(ProceedingJoinPoint pjp) throws Throwable {
        long beginTime = System.currentTimeMillis();
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();
        String methodName = method.getName();
        MethodWrapper methodWrapper = putIfAbsent(method, methodName, (MethodInvocationProceedingJoinPoint) pjp);
        RateLimiter rateLimiter = methodWrapper.getRateLimiter();
        logInfo(methodWrapper, "[req]: methodName:{}, req:{}", methodName, JSONObject.toJSON(pjp.getArgs()));
        if (rateLimiter != null) {
            logger.info("rate limit ing");
            rateLimiter.acquire();//限流
        }
        Object result = pjp.proceed();
        logInfo(methodWrapper, "[rsp]: methodName:{},takeTime:{}, rsp:{}", methodName, System.currentTimeMillis() - beginTime, result);
        return result;
    }


    private void logInfo(MethodWrapper methodWrapper, String format, Object... args) {
        if (methodWrapper.isAccessLogOn()) {
            logger.info(format, args);
        }
    }


    private MethodWrapper putIfAbsent(Method method, String methodName, MethodInvocationProceedingJoinPoint pjp) throws NoSuchMethodException {
        MethodWrapper methodWrapper = thriftMethodWrapperMap.get(method);
        if (methodWrapper == null) {
            Method targetMethod = pjp.getTarget().getClass().getMethod(methodName, method.getParameterTypes());
            ThriftMethodProvider provider = targetMethod.getAnnotation(ThriftMethodProvider.class);
            methodWrapper = new MethodWrapper()
                    .setMethod(method)
                    .setMethodName(methodName)
                    .setReturnType(method.getReturnType())
                    .setAccessLogOn(provider.accessLogOn());

            int rate = provider.rate();
            if (rate > 0) {
                methodWrapper.setRateLimiter(RateLimiter.create(provider.rate()));
            }
            thriftMethodWrapperMap.putIfAbsent(method, methodWrapper);
        }
        return methodWrapper;
    }


}
