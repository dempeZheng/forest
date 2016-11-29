package com.dempe.forest.example;

import com.dempe.forest.Constants;
import com.dempe.forest.codec.Header;
import com.dempe.forest.codec.Message;
import com.dempe.forest.codec.RpcProtocolVersion;
import com.dempe.forest.transport.NettyClient;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/11/29
 * Time: 14:13
 * To change this template use File | Settings | File Templates.
 */
public class ClientMain {

    public static void main(String[] args) throws InterruptedException {
        NettyClient client = new NettyClient("127.0.0.1", 9999);
        client.connect();
        Message message = new Message();
        Header header = new Header();
        header.setMessageID(1L);
        header.setMagic(Constants.MAGIC);
        header.setVersion(RpcProtocolVersion.VERSION_1.getVersion());
        header.setExtend((byte) 1);
        header.setUri("/sample/hello");
        message.setHeader(header);
        message.setPayload(new byte[0]);
        client.write(message);

    }
}
