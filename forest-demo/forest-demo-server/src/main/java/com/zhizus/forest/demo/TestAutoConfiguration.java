package com.zhizus.forest.demo;

import com.zhizus.forest.common.annotation.ForestService;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.beans.BeansException;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.web.servlet.RegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

/**
 * Created by dempezheng on 2017/6/5.
 */
@Configuration
@ConditionalOnClass(ForestService.class)
public class TestAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(ForestConfiguration.class)
    ForestConfiguration thriftConfigurer() {
        return new DefaultForestConfiguration();
    }


    public interface ForestConfiguration {
        void configureProxyFactory(ProxyFactory proxyFactory, String path, String type);
    }

    public static class DefaultForestConfiguration implements ForestConfiguration {

        public void configureProxyFactory(ProxyFactory proxyFactory, String path, String type) {
            proxyFactory.setOptimize(true);
        }
    }

    @Configuration
    @AutoConfigureAfter({ForestService.class})
    public static class Registrar extends RegistrationBean implements ApplicationContextAware {

        private static final String TYPE_THRIFT = "thrift";

        ApplicationContext applicationContext;


        @Override
        public void onStartup(ServletContext servletContext) throws ServletException {
            System.out.println("____________________________==================>>>>");
            System.out.println(servletContext);
        }


        @Override
        public void setApplicationContext(ApplicationContext applicationContext)
                throws BeansException {
            System.out.println("____________________________==================>>>>");

            this.applicationContext = applicationContext;
        }
    }

}
