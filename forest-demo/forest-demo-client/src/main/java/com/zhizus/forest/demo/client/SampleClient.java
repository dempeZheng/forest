package com.zhizus.forest.demo.client;

import com.zhizus.forest.Forest;
import com.zhizus.forest.common.CompressType;
import com.zhizus.forest.common.SerializeType;
import com.zhizus.forest.common.config.MethodConfig;
import com.zhizus.forest.common.config.ServiceConfig;
import com.zhizus.forest.demo.api.SampleService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SampleClient {

    public static void main(String[] args) throws InterruptedException {
        serviceTest();
        serviceSpringTest();
//        benchmarkTest();

    }

    public static void serviceTest() throws InterruptedException {
        SampleService sampleService = Forest.from(SampleService.class, ServiceConfig.Builder.newBuilder()
                .withMethodConfig("say", MethodConfig.Builder.newBuilder()
                        .withCompressType(CompressType.compressNo)
                        .withSerializeType(SerializeType.fastjson)
                        .build())
                .withMethodConfig("echo", MethodConfig.Builder.newBuilder()
                        .withCompressType(CompressType.compressNo)
                        .build())
                .build());

        String hello = sampleService.say("hello");
        System.out.println(hello);
    }

    public static void serviceSpringTest() {
        ApplicationContext context = new ClassPathXmlApplicationContext(new String[]{"application-client.xml"});
        SampleService bean = (SampleService) context.getBean("sampleServiceProxy");
        String test = bean.say("hello");
        System.out.println(test);
    }

    public static void benchmarkTest() throws InterruptedException {
        final SampleService sampleService = Forest.from(SampleService.class);
        ExecutorService executorService = Executors.newCachedThreadPool();
        for (int i = 0; i < 20; i++) {
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    for (int i = 0; i < 1000000; i++) {
                        String say = sampleService.echo("hello");
                        if (i % 10000 == 0) {
                            System.out.println(say);
                        }
                    }
                }
            });
        }
    }


}
