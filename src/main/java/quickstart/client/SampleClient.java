package quickstart.client;

import com.dempe.forest.Constants;
import com.dempe.forest.MethodProviderConf;
import com.dempe.forest.client.proxy.RpcProxy;
import com.dempe.forest.core.CompressType;
import com.dempe.forest.core.SerializeType;
import quickstart.api.SampleService;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/12/3 0003
 * Time: 下午 5:27
 * To change this template use File | Settings | File Templates.
 */
public class SampleClient {

    public static void main(String[] args) throws InterruptedException {
        sampleServiceTest();
//        benchmarkTest();
    }

    public static void sampleServiceTest() throws InterruptedException {
        MethodProviderConf methodProviderConf = MethodProviderConf.makeMethodProviderConf()
                .setCompressType(CompressType.gizp)
                .setSerializeType(SerializeType.fastjson)
                .setTimeout(Constants.DEFAULT_TIMEOUT);
        SampleService sampleService = new RpcProxy()
                // set proxy say 方法配置(如果不配置，则默认使用接口注解的配置,如果都没有配置，则使用默认配置)
                .setMethodProviderConfig("say", methodProviderConf)
                .setMethodProviderConfig("say2", methodProviderConf)
                .setMethodProviderConfig("echo", MethodProviderConf.makeMethodProviderConf()
                        .setSerializeType(SerializeType.hession2))
                .getProxy(SampleService.class);
        String world = sampleService.say("world");
        System.out.println("say:" + world);
        String echo = sampleService.echo("echo hello");
        System.out.println("echo:" + echo);
    }

    public static void benchmarkTest() throws InterruptedException {
        final SampleService sampleService = new RpcProxy().getProxy(SampleService.class);
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
