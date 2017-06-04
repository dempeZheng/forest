package com.zhizus.forest.demo.client;

import com.zhizus.forest.common.annotation.FeignClient;
import com.zhizus.forest.demo.api.FooService;
import org.springframework.stereotype.Component;

@Component
public class SampleClient {

    @FeignClient
    FooService fooService;


    public String echo(String msg) {
        String echo = fooService.echo(msg);
        return echo;
    }

    public static void main(String[] args) throws Exception {

    }


}
