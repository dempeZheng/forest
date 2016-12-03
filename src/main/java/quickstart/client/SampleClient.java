package quickstart.client;

import com.dempe.forest.ClientConfig;
import com.dempe.forest.client.proxy.JdkProxy;
import com.dempe.forest.transport.NettyClient;
import org.aeonbits.owner.ConfigFactory;
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
        benchmarkTest();
    }

    public static void test() throws InterruptedException {
        String say = getProxy(SampleService.class).say("hello");
        System.out.println(say);
    }

    public static void benchmarkTest() throws InterruptedException {
        final SampleService sampleService = getProxy(SampleService.class);
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

    public static <T> T getProxy(Class<T> clazz) throws InterruptedException {
        ClientConfig config = ConfigFactory.create(ClientConfig.class);
        NettyClient client = new NettyClient(config);
        client.connect();
        return new JdkProxy<>().getProxy(clazz, client);
    }
}
