package quickstart.client;

import com.dempe.forest.Forest;
import com.dempe.forest.config.MethodConfig;
import com.dempe.forest.config.ServiceConfig;
import com.dempe.forest.core.CompressType;
import com.dempe.forest.core.SerializeType;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import quickstart.api.SampleService;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SampleClient {

    public static void main(String[] args) {
        serviceTest();
        serviceSpringTest();
//        benchmarkTest();

    }

    public static void serviceTest() {
        SampleService sampleService = Forest.from(SampleService.class, ServiceConfig.Builder.newBuilder()
                .withMethodConfig("say", MethodConfig.Builder.newBuilder()
                        .withCompressType(CompressType.gizp)
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

    public static void benchmarkTest() {
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
