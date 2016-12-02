package com.dempe.forest.example;

import com.dempe.forest.Constants;
import com.dempe.forest.client.ChannelPool;
import com.dempe.forest.client.proxy.CglibProxy;
import com.dempe.forest.codec.Header;
import com.dempe.forest.codec.Message;
import com.dempe.forest.codec.RpcProtocolVersion;
import com.dempe.forest.codec.compress.Compress;
import com.dempe.forest.codec.serialize.Serialization;
import com.dempe.forest.core.CompressType;
import com.dempe.forest.core.ForestUtil;
import com.dempe.forest.core.MessageType;
import com.dempe.forest.core.SerializeType;
import com.dempe.forest.transport.NettyClient;
import com.google.common.base.Stopwatch;

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

    public static void main(String[] args) throws InterruptedException, IOException {
        cglibProxyTest();
    }

    public static void simpleTest() throws Exception {
        NettyClient client = new NettyClient("127.0.0.1", 9999);
        client.connect();
        Message message = new Message();
        Header header = new Header();
        header.setMessageID(1L);
        header.setMagic(Constants.MAGIC);
        header.setVersion(RpcProtocolVersion.VERSION_1.getVersion());
        byte extend = ForestUtil.getExtend(SerializeType.hession2, CompressType.gizp, MessageType.request);
        header.setExtend(extend);
        header.setUri("/sample/hello");
        message.setHeader(header);
        Serialization serialization = SerializeType.getSerializationByExtend(extend);
        Object[] params = new Object[]{"test"};
        byte[] tests = serialization.serialize(params);
        Compress compress = CompressType.getCompressTypeByValueByExtend(extend);
        message.setPayload(compress.compress(tests));
        new ChannelPool(client).getChannel().write(message, 5000L);
    }

    public static void cglibProxyTest() throws InterruptedException {
        NettyClient client = new NettyClient("127.0.0.1", 9999);
        client.connect();
        final SampleAction sampleAction = CglibProxy.getProxy(SampleAction.class, new ChannelPool(client));
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
//            System.out.println(hello);
                    }
                }
            });
        }


        System.out.println(stopwatch.stop().elapsed(TimeUnit.MILLISECONDS));


    }
}
