package com.zhizus.forest.demo.api;


import com.zhizus.forest.common.*;
import com.zhizus.forest.common.annotation.MethodProvider;
import com.zhizus.forest.common.annotation.ServiceProvider;

/**
 * Created by Dempe on 2016/12/7.
 */
@ServiceProvider(serviceName = "sampleService", haStrategyType = HaStrategyType.FAIL_FAST,
        loadBalanceType = LoadBalanceType.RANDOM, connectionTimeout = Constants.CONNECTION_TIMEOUT, port = 8888)
public interface SampleService {

    @MethodProvider(methodName = "say")
    String say(String str);

    @MethodProvider(methodName = "echo", serializeType = SerializeType.fastjson, compressType = CompressType.gizp)
    String echo(String msg);


}
