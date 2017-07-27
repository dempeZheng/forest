package com.zhizus.forest.rpc.service;

import com.zhizus.forest.rpc.annotation.Inject;
import com.zhizus.forest.rpc.sample.gen.Sample;
import org.apache.thrift.TException;
import org.springframework.stereotype.Service;

/**
 * Created by Dempe on 2017/7/27 0027.
 */
@Service
public class TClientService {

    @Inject
    Sample.Client client;

    public String hello() throws TException {
        return client.hello("test");
    }


}
