package com.dempe.forest.example;

import com.dempe.forest.Constants;
import com.dempe.forest.ForestUtil;
import com.dempe.forest.client.ChannelPool;
import com.dempe.forest.client.proxy.Proxy;
import com.dempe.forest.codec.Header;
import com.dempe.forest.codec.Message;
import com.dempe.forest.codec.compress.Compress;
import com.dempe.forest.codec.serialize.Serialization;
import com.dempe.forest.core.CompressType;
import com.dempe.forest.core.ProtoVersion;
import com.dempe.forest.core.SerializeType;
import com.dempe.forest.ClientConfig;
import com.dempe.forest.transport.NettyClient;
import com.google.common.base.Stopwatch;
import org.aeonbits.owner.ConfigFactory;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/11/29
 * Time: 14:13
 * To change this template use File | Settings | File Templates.
 */
public class ClientMain {

    public final static ClientConfig config = ConfigFactory.create(ClientConfig.class);

    public static void main(String[] args) throws InterruptedException, IOException {
//        benchMarkTest();
        test();
    }


    public static void simpleTest() throws Exception {
        NettyClient client = new NettyClient(config);
        client.connect();
        Message message = new Message();
        Header header = new Header();
        header.setMessageID(1L);
        header.setMagic(Constants.MAGIC);
        header.setVersion(ProtoVersion.VERSION_1.getVersion());
        byte extend = ForestUtil.getExtend(SerializeType.hession2, CompressType.gizp);
        header.setExtend(extend);
        header.setUri("/sample/hello");
        message.setHeader(header);
        Serialization serialization = SerializeType.getSerializationByExtend(extend);
        Object[] params = new Object[]{"test"};
        byte[] tests = serialization.serialize(params);
        Compress compress = CompressType.getCompressTypeByValueByExtend(extend);
        message.setPayload(compress.compress(tests));
        new ChannelPool(client).write(message, 5000L);
    }

    public static void test() throws InterruptedException {
        NettyClient client = new NettyClient(config);
        client.connect();
        final SampleAction sampleAction = Proxy.getCglibProxy(SampleAction.class, new ChannelPool(client));
        Stopwatch stopwatch = Stopwatch.createStarted();
        sampleAction.noReplyMethod();
        System.out.println("exeTime : " + stopwatch.stop().elapsed(TimeUnit.MILLISECONDS));
    }


    public static void benchMarkTest() throws InterruptedException {
        NettyClient client = new NettyClient(config);
        client.connect();
        final SampleAction sampleAction = Proxy.getCglibProxy(SampleAction.class, new ChannelPool(client));
        Stopwatch stopwatch = Stopwatch.createStarted();
        ExecutorService executorService = Executors.newCachedThreadPool();
        for (int i = 0; i < 10; i++) {
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    for (int i = 0; i < 1000000; i++) {
                        String hello = sampleAction.hello("hello====");
                        if (i % 1000 == 0) {
                            System.out.println(hello);
                        }
                    }
                }
            });
        }
        System.out.println("exeTime : " + stopwatch.stop().elapsed(TimeUnit.MILLISECONDS));
    }
}
