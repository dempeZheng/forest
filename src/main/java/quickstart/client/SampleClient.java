package quickstart.client;

import com.dempe.forest.RefConfMapping;
import com.dempe.forest.client.proxy.ReferConfig;
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
        test();
    }

    public static void test() throws InterruptedException {
        SampleService sampleService = new RpcProxy()
                .registerReferConfig(ReferConfig.makeReferConfig()
                        .setMethodName("say")
                        .setCompressType(CompressType.gizp)
                        .setSerializeType(SerializeType.fastjson)
                        .setTimeout(5000))
                .registerReferConfig(ReferConfig.makeReferConfig()
                        .setMethodName("echo")
                        .setCompressType(CompressType.compressNo)
                        .setSerializeType(SerializeType.hession2)
                        .setTimeout(2000))
                .getProxy(SampleService.class);
        String world = sampleService.say("world");
        System.out.println("say:" + world);
        String echo = sampleService.echo("echo hello");
        System.out.println("echo:" + echo);
    }

    public static void benchmarkTest() throws InterruptedException {
        final SampleService sampleService = new RpcProxy()
                .setRefConfMapping(new RefConfMapping())
                .getProxy(SampleService.class);
        ExecutorService executorService = Executors.newCachedThreadPool();
        for (int i = 0; i < 20; i++) {
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    for (int i = 0; i < 1000000; i++) {
                        String say = sampleService.say("hello");
                    }
                }
            });
        }
    }


}
