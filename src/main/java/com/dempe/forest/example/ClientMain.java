package com.dempe.forest.example;

import com.dempe.forest.Constants;
import com.dempe.forest.client.proxy.JdkProxyFactory;
import com.dempe.forest.client.proxy.ReferInvocationHandler;
import com.dempe.forest.codec.Header;
import com.dempe.forest.codec.Message;
import com.dempe.forest.codec.RpcProtocolVersion;
import com.dempe.forest.codec.serialize.Hessian2Serialization;
import com.dempe.forest.core.ForestUtil;
import com.dempe.forest.core.CompressType;
import com.dempe.forest.core.MessageType;
import com.dempe.forest.core.SerializeType;
import com.dempe.forest.transport.NettyClient;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/11/29
 * Time: 14:13
 * To change this template use File | Settings | File Templates.
 */
public class ClientMain {

    public static void main(String[] args) throws InterruptedException, IOException {


        proxyTest();
    }

    public static void simpleTest() throws InterruptedException, IOException {
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
        Hessian2Serialization serialization = new Hessian2Serialization();
        Object[] params = new Object[]{"test"};
        byte[] tests = serialization.serialize(params);
        message.setPayload(tests);
        client.write(message);
    }

    public static void proxyTest() throws InterruptedException {
        NettyClient client = new NettyClient("127.0.0.1", 9999);
        client.connect();
        SampleActionInterface proxy = new JdkProxyFactory().getProxy(SampleActionInterface.class, new ReferInvocationHandler(client));
        proxy.hello("test");
    }
}
