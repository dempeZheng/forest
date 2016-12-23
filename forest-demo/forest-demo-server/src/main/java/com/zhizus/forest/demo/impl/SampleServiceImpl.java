package com.zhizus.forest.demo.impl;


import com.zhizus.forest.common.annotation.Interceptor;
import com.zhizus.forest.common.annotation.MethodExport;
import com.zhizus.forest.common.annotation.Rate;
import com.zhizus.forest.common.annotation.ServiceExport;
import com.zhizus.forest.demo.api.SampleService;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

/**
 * Created by Dempe on 2016/12/7.
 */
@Path("/sample")
@ServiceExport
public class SampleServiceImpl implements SampleService {

    /**
     * 支持jersey，可以通过配置打开，同时启动http服务
     *
     * @param str
     * @return
     */
    @Path("/hello/{str}")
    @GET
    @Produces("text/plain")

    @MethodExport
    @Rate(2)
    @Override
    public String say(@PathParam("str") String str) {
        return "say " + str;
    }

    @Interceptor("metricInterceptor")
    @MethodExport
    @Override
    public String echo(String msg) {
        return "echo>>> " + msg;
    }


}
