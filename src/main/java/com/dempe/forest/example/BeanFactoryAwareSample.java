package com.dempe.forest.example;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/11/29
 * Time: 10:16
 * To change this template use File | Settings | File Templates.
 */
@Component
public class BeanFactoryAwareSample implements BeanFactoryAware, ApplicationListener<ContextRefreshedEvent>  {

    private DefaultListableBeanFactory beanFactory;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = (DefaultListableBeanFactory) beanFactory;
    }


    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        System.out.println("beanFactory>>>>>>>>>>>>>>"+beanFactory);
    }
}
