package com.zhizus.forest.rpc.configuration;

import com.zhizus.forest.rpc.annotation.ThriftService;
import org.apache.thrift.TProcessor;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocolFactory;
import org.apache.thrift.server.TServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.target.SingletonTargetSource;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.embedded.RegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import java.lang.reflect.Constructor;


@Configuration
@ConditionalOnClass(ThriftService.class)
@ConditionalOnWebApplication
public class ThriftAutoConfiguration {

    public interface ThriftConfigurer {
        void configureProxyFactory(ProxyFactory proxyFactory, String path, String type);
    }

    @Bean
    @ConditionalOnMissingBean(ThriftConfigurer.class)
    ThriftConfigurer thriftConfigurer() {
        return new DefaultThriftConfigurer();
    }

    @Bean
    @ConditionalOnMissingBean(TProtocolFactory.class)
    TProtocolFactory thriftProtocolFactory() {
        return new TBinaryProtocol.Factory();
    }

    public static class DefaultThriftConfigurer implements ThriftConfigurer {

        public void configureProxyFactory(ProxyFactory proxyFactory, String path, String type) {
            proxyFactory.setOptimize(true);
        }
    }

    @Configuration
    @AutoConfigureAfter({ThriftService.class})
    public static class Registrar extends RegistrationBean implements ApplicationContextAware {
        private final static Logger logger = LoggerFactory.getLogger(Registrar.class);

        private static final String TYPE_THRIFT = "thrift";


        ApplicationContext applicationContext;

        @Autowired
        TProtocolFactory protocolFactory;

        @Autowired
        ThriftConfigurer thriftConfigurer;

        @Override

        public void onStartup(ServletContext servletContext) throws ServletException {
            addThrift(servletContext);
        }


        /**
         * @param servletContext
         */
        private void addThrift(ServletContext servletContext) {
            for (String beanName : applicationContext.getBeanNamesForAnnotation(ThriftService.class)) {
                ThriftService annotation = applicationContext.findAnnotationOnBean(beanName, ThriftService.class);
                try {
                    registerThriftHandler(servletContext, annotation.value(), applicationContext.getBean(beanName));
                } catch (BeansException | ClassNotFoundException
                        | NoSuchMethodException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }

        protected void registerThriftHandler(ServletContext servletContext, String[] urls, Object handler) throws ClassNotFoundException, NoSuchMethodException {
            Class<?>[] handlerInterfaces = handler.getClass().getInterfaces();

            Class<?> ifaceClass = null;
            Class<TProcessor> processorClass = null;
            Class<?> serviceClass = null;

            for (Class<?> handlerInterfaceClass : handlerInterfaces) {
                if (!handlerInterfaceClass.getName().endsWith("$Iface")) {
                    continue;
                }

                serviceClass = handlerInterfaceClass.getDeclaringClass();

                if (serviceClass == null) {
                    continue;
                }

                for (Class<?> innerClass : serviceClass.getDeclaredClasses()) {
                    if (!innerClass.getName().endsWith("$Processor")) {
                        continue;
                    }

                    if (!TProcessor.class.isAssignableFrom(innerClass)) {
                        continue;
                    }

                    if (ifaceClass != null) {
                        throw new IllegalStateException("Multiple Thrift Ifaces defined on handler");
                    }

                    ifaceClass = handlerInterfaceClass;
                    processorClass = (Class<TProcessor>) innerClass;
                    break;
                }
            }

            if (ifaceClass == null) {
                throw new IllegalStateException("No Thrift Ifaces found on handler");
            }

            handler = wrapHandler(ifaceClass, handler, urls[0], TYPE_THRIFT);

            Constructor<TProcessor> processorConstructor = processorClass.getConstructor(ifaceClass);

            TProcessor processor = BeanUtils.instantiateClass(processorConstructor, handler);

            TServlet servlet = getServlet(processor, protocolFactory);

            String servletBeanName = ifaceClass.getDeclaringClass().getSimpleName() + "Servlet";

            ServletRegistration.Dynamic registration = servletContext.addServlet(servletBeanName, servlet);

            if (urls != null && urls.length > 0) {
                logger.info("register mapping: url:{},servletName:{}", urls, servletBeanName);
                registration.addMapping(urls);
            } else {
                registration.addMapping("/" + serviceClass.getSimpleName());
                logger.info("register mapping: url:{},servletName:{}", serviceClass.getSimpleName(), servletBeanName);
            }
        }

        private Object wrapHandler(Class<?> ifaceClass, Object handler,
                                   String path, String type) {
            ProxyFactory proxyFactory = new ProxyFactory(ifaceClass, new SingletonTargetSource(handler));

            thriftConfigurer.configureProxyFactory(proxyFactory, path, type);
            //TODO remove from here?
            proxyFactory.setFrozen(true);
            return proxyFactory.getProxy();
        }

        protected TServlet getServlet(TProcessor processor, TProtocolFactory protocolFactory) {
            return new TServlet(processor, protocolFactory);
        }

        @Override
        public void setApplicationContext(ApplicationContext applicationContext)
                throws BeansException {
            this.applicationContext = applicationContext;
        }
    }


}
