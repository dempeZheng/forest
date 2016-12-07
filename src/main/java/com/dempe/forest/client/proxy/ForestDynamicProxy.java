package com.dempe.forest.client.proxy;

import com.dempe.forest.ClientConfig;
import com.dempe.forest.client.ChannelPool;
import com.dempe.forest.client.proxy.processor.*;
import com.dempe.forest.codec.Header;
import com.dempe.forest.codec.Message;
import com.dempe.forest.codec.Response;
import com.dempe.forest.codec.compress.Compress;
import com.dempe.forest.codec.serialize.Serialization;
import com.dempe.forest.config.MethodConfig;
import com.dempe.forest.config.ServiceConfig;
import com.dempe.forest.core.CompressType;
import com.dempe.forest.core.SerializeType;
import com.dempe.forest.core.annotation.MethodProvider;
import com.dempe.forest.core.annotation.ServiceProvider;
import com.dempe.forest.support.ForestUtil;
import com.dempe.forest.transport.NettyClient;
import com.dempe.forest.transport.NettyResponseFuture;
import com.google.common.base.Strings;
import org.aeonbits.owner.ConfigFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;


public class ForestDynamicProxy implements InvocationHandler {

    private final static Logger LOGGER = LoggerFactory.getLogger(ForestDynamicProxy.class);

    private ServiceConfig config;

    private final static AtomicLong id = new AtomicLong(0);

    private String serviceName;

    public ForestDynamicProxy(ServiceConfig serviceConfig, Class<?> interfaceClass) {
        this(interfaceClass);
        for (Map.Entry<String, MethodConfig> methodConfigEntry : serviceConfig.getMethodConfigMap().entrySet()) {
            MethodConfig methodConfigFromAnnotation = config.getMethodConfig(methodConfigEntry.getKey());
            if (methodConfigFromAnnotation == null) {
                LOGGER.warn("methodName is not exist. err methodName:{},serviceName:{}", methodConfigEntry.getKey(), serviceName);
                continue;
            }
            MethodConfig methodConfig = methodConfigEntry.getValue();
            methodConfig.setMethodName(methodConfigFromAnnotation.getMethodName());
            methodConfig.setServiceName(methodConfigFromAnnotation.getServiceName());
            config.registerMethodConfig(methodConfigEntry.getKey(), methodConfig);
        }
        config.setServiceName(serviceName);
    }

    public ForestDynamicProxy(Class<?> interfaceClass) {
        config = ServiceConfig.Builder.newBuilder().build();
        AnnotationProcessorsProvider processors = AnnotationProcessorsProvider.DEFAULT;
        registerAnnotationProcessors(processors);
        ServiceProvider serviceProvider = interfaceClass.getAnnotation(ServiceProvider.class);
        this.serviceName = Strings.isNullOrEmpty(serviceProvider.serviceName()) ? interfaceClass.getSimpleName() : serviceProvider.serviceName();
        // 加载注解配置作为默认配置
        for (Method method : interfaceClass.getMethods()) {
            for (AnnotationProcessor processor : processors.getProcessors()) {
                processor.process(serviceName, method, config);
            }
        }
    }

    public static void registerAnnotationProcessors(AnnotationProcessorsProvider processors) {
        processors.register(new HttpAnnotationProcessor());
        processors.register(new HystrixAnnotationProcessor());
        processors.register(new MethodProviderAnnotationProcessor());
    }

    public static <T> T newInstance(Class<T> clazz, ServiceConfig serviceConfig) {
        return (T) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class[]{clazz}, new ForestDynamicProxy(serviceConfig, clazz));
    }

    public static <T> T newInstance(Class<T> clazz) {
        return (T) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class[]{clazz}, new ForestDynamicProxy(clazz));
    }


    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        MethodProvider methodProvider = method.getAnnotation(MethodProvider.class);
        if (methodProvider == null) {
            LOGGER.info("method:{} cannot find methodProvider.", method.getName());
            return null;
        }
        String methodName = Strings.isNullOrEmpty(methodProvider.methodName()) ? method.getName() : methodProvider.methodName();
        MethodConfig methodConfig = config.getMethodConfig(methodName);
        if (methodConfig == null) {
            LOGGER.info("serviceName:{},methodName is not found", serviceName, method.getName());
            return null;
        }
        Compress compress = CompressType.getCompressTypeByValueByExtend(methodConfig.getCompressType().getValue());
        Serialization serialization = SerializeType.getSerializationByExtend(methodConfig.getSerializeType().getValue());
        byte[] serialize = serialization.serialize(args);
        Message message = new Message(Header.HeaderMaker.newMaker()
                .loadWithMethodConfig(methodConfig)
                .withMessageId(id.incrementAndGet())
                .withUri(ForestUtil.buildUri(serviceName, methodName)).make(),
                compress.compress(serialize));
        // // TODO: 2016/12/7
        NettyResponseFuture<Response> responseFuture = new ChannelPool(new NettyClient(ConfigFactory.create(ClientConfig.class))).write(message, methodConfig.getTimeout());
        return responseFuture.getPromise().await().getResult();
    }
}
