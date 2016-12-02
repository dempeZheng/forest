package com.dempe.forest.example;

import com.dempe.forest.core.CompressType;
import com.dempe.forest.core.SerializeType;
import com.dempe.forest.core.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/11/28
 * Time: 19:17
 * To change this template use File | Settings | File Templates.
 */
@Action("sample")
public class SampleAction {

    @Autowired
    private SampleService sampleService;

     @Interceptor(id = "printInterceptor,metricInterceptor")
    @Rate(value = 1000000)
    @Export(uri = "hello", compressType = CompressType.compressNo, serializeType = SerializeType.fastjson, timeOut = 1000)
    public String hello(@HttpParam String word) {
        return sampleService.hello(word);
    }

    @Export
    public void noReplyMethod() {
        // do service

    }

}
