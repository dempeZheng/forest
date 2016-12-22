package com.zhizus.forest.demo.server;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by Dempe on 2016/12/7.
 */
public class SampleServer {

    public static void main(String[] args) throws Exception {

        new ClassPathXmlApplicationContext(new String[]{"application.xml"});


    }


}
