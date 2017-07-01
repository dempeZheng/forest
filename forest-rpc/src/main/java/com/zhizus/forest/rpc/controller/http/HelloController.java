package com.zhizus.forest.rpc.controller.http;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

/**
 * Created by Dempe on 2017/7/1 0001.
 */
@Controller
public class HelloController {

    @Autowired
    RestTemplate restTemplate;

    @RequestMapping("/hello")
    @ResponseBody
    public String hello() {
        return "hello";
    }

    @RequestMapping("/sayHello")
    @ResponseBody
    public String sayHello() {
        String objectResponseEntity = restTemplate.getForObject("http://localhost:8080/hello", String.class);
        return objectResponseEntity;
    }
}
