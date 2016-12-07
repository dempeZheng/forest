package com.zhizus.forest.demo.impl;


import com.zhizus.forest.common.annotation.Interceptor;
import com.zhizus.forest.common.annotation.MethodExport;
import com.zhizus.forest.common.annotation.Rate;
import com.zhizus.forest.common.annotation.ServiceExport;
import com.zhizus.forest.demo.api.SampleService;

/**
 * Created by Dempe on 2016/12/7.
 */
@ServiceExport
public class SampleServiceImpl implements SampleService {

    @MethodExport
    @Rate(2)
    @Interceptor("metricInterceptor")
    @Override
    public String say(String str) {
        return "say " + str;
    }

    @Interceptor("metricInterceptor")
    @MethodExport
    @Override
    public String echo(String msg) {
        return "echo " + msg;
    }


}
