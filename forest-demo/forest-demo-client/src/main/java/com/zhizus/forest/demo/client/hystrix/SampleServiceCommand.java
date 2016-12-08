package com.zhizus.forest.demo.client.hystrix;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.zhizus.forest.demo.api.SampleService;

import javax.annotation.Resource;

/**
 * Created by Dempe on 2016/12/7.
 */
//@Component
public class SampleServiceCommand {

    @Resource(name = "sampleServiceProxy")
    SampleService remoteServiceRef;


    @HystrixCommand(groupKey = "ExampleGroup", commandKey = "HelloWorld", threadPoolKey = "HelloWorldPool", fallbackMethod = "sayFallback")
    public String say(String str) {
        String say = remoteServiceRef.say(str);
        System.out.println("say:" + say);
        str.toString();
        return say;
    }

    public String sayFallback(String str) {
        return "sayFallBack:" + str;
    }
}
