package com.zhizus.forest.support.spring;

import com.google.common.base.Strings;
import com.google.common.collect.Sets;
import com.zhizus.forest.ForestRouter;
import com.zhizus.forest.support.MetricInterceptor;
import com.zhizus.forest.common.config.ServerConfig;
import com.zhizus.forest.client.proxy.processor.AnnotationProcessorsProvider;
import com.zhizus.forest.client.proxy.processor.IAnnotationProcessor;
import com.zhizus.forest.client.proxy.processor.ServiceExportProcessor;
import com.zhizus.forest.common.MetaInfo;
import com.zhizus.forest.common.annotation.ServiceExport;
import com.zhizus.forest.common.config.ServiceExportConfig;
import com.zhizus.forest.common.registry.AbstractServiceDiscovery;
import com.zhizus.forest.common.util.NetUtils;
import com.zhizus.forest.transport.ForestServerFactory;
import com.zhizus.forest.transport.NettyServer;
import org.apache.curator.x.discovery.ServiceInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.lang.reflect.Method;
import java.util.Set;

/**
 * Created by Dempe on 2016/12/9.
 */
public class ForestServerBean implements ApplicationContextAware, InitializingBean {

    private final static Logger LOGGER = LoggerFactory.getLogger(ForestServerBean.class);

    AnnotationProcessorsProvider processors = AnnotationProcessorsProvider.DEFAULT;

    private ClassPathXmlApplicationContext context;

    private ForestServerFactory factory;

    private AbstractServiceDiscovery registry;

    private Set<String> serviceProviderKey = Sets.newHashSet();

    public void init() {
        for (String beanName : context.getBeanNamesForAnnotation(ServiceExport.class)) {
            Object bean = context.getBean(beanName);
            // init config
            ServiceExportConfig config = new ServiceExportConfig();
            for (Method method : bean.getClass().getMethods()) {
                for (IAnnotationProcessor processor : processors.getProcessors()) {
                    processor.process(bean.getClass(), method, config);
                }
            }
            // init router
            ForestRouter router = new ForestRouter(context);
            router.init(bean, config);
            String serviceName = config.getServiceName();
            int port = config.getPort();
            // 同一个端口只注册一个服务
            String serviceKey = serviceName + "|" + port;
            if (Strings.isNullOrEmpty(serviceName) || port == 0 || serviceProviderKey.contains(serviceKey)) {
                continue;
            }
            serviceProviderKey.add(serviceKey);
            try {
                NettyServer server = factory.createServer(router, config);
                server.start();
                // 注册服务
                ServiceInstance<MetaInfo> serviceInstance = ServiceInstance.<MetaInfo>builder()
                        .name(serviceName)
                        .address(NetUtils.getLocalAddress().getHostAddress())
                        .port(port)
                        .build();
                if (registry == null) {
                    registry = AbstractServiceDiscovery.DEFAULT_DISCOVERY;
                }
                registry.registerService(serviceInstance);
            } catch (Exception e) {
                LOGGER.error(e.getMessage(), e);
            }

        }
    }

    public void register() {
        processors.register(new ServiceExportProcessor());
    }


    public AbstractServiceDiscovery getRegistry() {
        return registry;
    }

    public void setRegistry(AbstractServiceDiscovery registry) {
        this.registry = registry;
    }

    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        this.context = (ClassPathXmlApplicationContext) context;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        registerServerConfig();
        registerMetricsInterceptor();
        factory = new ForestServerFactory(context.getBean(ServerConfig.class));
        register();
        init();
    }

    public void registerServerConfig() {
        DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) context.getBeanFactory();
        GenericBeanDefinition serverConfig = new GenericBeanDefinition();
        //参数参考上面配置文件里面
        serverConfig.setBeanClass(ServerConfig.class);
        //注册
        beanFactory.registerBeanDefinition("serverConfig", serverConfig);
    }

    public void registerMetricsInterceptor() {
        DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) context.getBeanFactory();
        GenericBeanDefinition metricInterceptor = new GenericBeanDefinition();
        //参数参考上面配置文件里面
        metricInterceptor.setBeanClass(MetricInterceptor.class);
        //注册
        beanFactory.registerBeanDefinition("metricInterceptor", metricInterceptor);
    }
}
