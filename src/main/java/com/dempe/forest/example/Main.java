package com.dempe.forest.example;

import com.dempe.forest.core.annotation.Action;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/11/29
 * Time: 9:43
 * To change this template use File | Settings | File Templates.
 */
public class Main {

    public static void main(String[] args) {
       ApplicationContext context = new ClassPathXmlApplicationContext(new String[]{"application.xml"});
        SampleAction bean = context.getBean(SampleAction.class);
        String[] beanNamesForAnnotation = context.getBeanNamesForAnnotation(Action.class);
        for (String s : beanNamesForAnnotation) {
            System.out.println(s);
        }
        bean.hello();

    }
}
