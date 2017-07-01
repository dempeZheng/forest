package com.zhizus.forest.rpc;

import com.zhizus.forest.rpc.sample.gen.Sample;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.THttpClient;
import org.apache.thrift.transport.TTransportException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * Created by Dempe on 2017/7/1 0001.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class HttpThriftTester {

    private THttpClient transport;
    private Sample.Client sampleClient;

    public HttpThriftTester() throws TTransportException {
        transport = new THttpClient("http://localhost:8080/sample");
//        transport.setCustomHeader("protocol", "json");
        TProtocol protocol = new TBinaryProtocol(transport);
        sampleClient = new Sample.Client(protocol);
    }

    @Test
    public void helloTest() throws TException {
        String hello = sampleClient.hello("hello");
        System.out.println(hello);
    }

}
