package com.zhizus.forest.demo.client.hystrix;

import com.zhizus.forest.demo.client.hystrix.SampleServiceCommand;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by Dempe on 2016/12/7.
 */
public class SpringSampleClient {

    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext(new String[]{"application-client.xml"});
        SampleServiceCommand bean = context.getBean(SampleServiceCommand.class);
        String test = bean.say(null);
        System.out.println(test);
    }

}
