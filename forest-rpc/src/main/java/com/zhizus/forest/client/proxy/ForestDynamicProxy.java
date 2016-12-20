package com.zhizus.forest.client.proxy;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.zhizus.forest.client.cluster.FailoverCheckingStrategy;
import com.zhizus.forest.client.cluster.IHaStrategy;
import com.zhizus.forest.client.cluster.ha.FailFastStrategy;
import com.zhizus.forest.client.cluster.ha.FailoverStrategy;
import com.zhizus.forest.client.cluster.lb.AbstractLoadBalance;
import com.zhizus.forest.client.cluster.lb.RandomLoadBalance;
import com.zhizus.forest.client.proxy.processor.*;
import com.zhizus.forest.common.ServerInfo;
import com.zhizus.forest.common.annotation.MethodProvider;
import com.zhizus.forest.common.annotation.ServiceProvider;
import com.zhizus.forest.common.codec.Header;
import com.zhizus.forest.common.codec.Message;
import com.zhizus.forest.common.codec.Request;
import com.zhizus.forest.common.config.MethodConfig;
import com.zhizus.forest.common.config.ServiceProviderConfig;
import com.zhizus.forest.common.exception.ForestFrameworkException;
import com.zhizus.forest.registry.AbstractServiceDiscovery;
import com.zhizus.forest.registry.impl.LocalServiceDiscovery;
import com.zhizus.forest.transport.NettyClient;
import org.apache.curator.x.discovery.ServiceInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by Dempe on 2016/12/7.
 */
public class ForestDynamicProxy implements InvocationHandler {

    private final static Logger LOGGER = LoggerFactory.getLogger(ForestDynamicProxy.class);

    private ServiceProviderConfig config;

    private final static AtomicLong id = new AtomicLong(0);

    private String serviceName;

    private FailoverCheckingStrategy<ServerInfo<NettyClient>> failoverCheckingStrategy;

    private AbstractServiceDiscovery<ServiceInstance> discovery;

    private IHaStrategy<ServerInfo<NettyClient>> haStrategy;
    private AbstractLoadBalance<ServerInfo<NettyClient>> loadBalance;


    public Map<Method, Header> headerMapCache = Maps.newConcurrentMap();

    public ForestDynamicProxy(ServiceProviderConfig serviceProviderConfig, Class<?> interfaceClass,
                              AbstractServiceDiscovery registry, FailoverCheckingStrategy failoverCheckingStrategy) throws Exception {
        this(interfaceClass, registry, failoverCheckingStrategy);
        // 覆盖service的配置
        config.setLoadBalanceType(serviceProviderConfig.getLoadBalanceType());
        config.setHaStrategyType(serviceProviderConfig.getHaStrategyType());
        config.setConnectionTimeout(serviceProviderConfig.getConnectionTimeout());

        // 覆盖method的配置
        for (Map.Entry<String, MethodConfig> methodConfigEntry : serviceProviderConfig.getMethodConfigMap().entrySet()) {
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
        switch (config.getHaStrategyType()) {
            case FAIL_FAST:
                haStrategy = new FailFastStrategy();
                break;
            case FAIL_OVER:
                haStrategy = new FailoverStrategy();
                break;
        }
        switch (config.getLoadBalanceType()) {
            case RANDOM:
                loadBalance = new RandomLoadBalance<>(failoverCheckingStrategy, serviceName, discovery);
                break;
            // TODO: 2016/12/20
        }

    }

    protected ForestDynamicProxy(Class<?> interfaceClass, AbstractServiceDiscovery discovery, FailoverCheckingStrategy failoverCheckingStrategy) throws Exception {
        this.failoverCheckingStrategy = failoverCheckingStrategy;
        AnnotationProcessorsProvider processors = AnnotationProcessorsProvider.DEFAULT;
        registerAnnotationProcessors(processors);
        ServiceProvider serviceProvider = interfaceClass.getAnnotation(ServiceProvider.class);
        if (serviceProvider == null) {
            throw new ForestFrameworkException("interfaceClass " + interfaceClass + "ServiceProvider is null");
        }
        config = ServiceProviderConfig.Builder.newBuilder()
                .withConnectionTimeout(serviceProvider.connectionTimeout())
                .withHaStrategyType(serviceProvider.haStrategyType())
                .withLoadBalanceType(serviceProvider.loadBalanceType())
                .build();
        this.serviceName = Strings.isNullOrEmpty(serviceProvider.serviceName()) ? interfaceClass.getSimpleName() : serviceProvider.serviceName();
        // 加载注解配置作为默认配置
        for (Method method : interfaceClass.getMethods()) {
            for (IAnnotationProcessor processor : processors.getProcessors()) {
                processor.process(serviceName, method, config);
            }
        }
        config.setServiceName(serviceName);
        if (discovery == null) {
            discovery = AbstractServiceDiscovery.DEFAULT_DISCOVERY;
        }
        this.discovery = discovery;
        if (discovery instanceof LocalServiceDiscovery) {
            discovery.registerLocal(config.getServiceName(), ((LocalServiceDiscovery) discovery).getAddress());
        }


    }

    public static void registerAnnotationProcessors(AnnotationProcessorsProvider processors) {
        processors.register(new HttpAnnotationProcessor());
        processors.register(new HystrixAnnotationProcessor());
        processors.register(new MethodProviderAnnotationProcessor());
    }

    public static <T> T newInstance(Class<T> clazz, ServiceProviderConfig serviceProviderConfig,
                                    AbstractServiceDiscovery registry, FailoverCheckingStrategy strategy) throws Exception {
        return (T) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class[]{clazz},
                new ForestDynamicProxy(serviceProviderConfig, clazz, registry, strategy));
    }

    public static <T> T newInstance(Class<T> clazz, AbstractServiceDiscovery registry, FailoverCheckingStrategy strategy) throws Exception {
        return (T) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
                new Class[]{clazz}, new ForestDynamicProxy(clazz, registry, strategy));
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
        Header header = headerMapCache.get(method);
        if (header == null) {
            header = Header.HeaderMaker.newMaker()
                    .loadWithMethodConfig(methodConfig)
                    .withMessageId(id.incrementAndGet()).make();

        }
        Message message = new Message(header,
                new Request(serviceName, methodName, args));
        return haStrategy.call(message, loadBalance);

    }
}
