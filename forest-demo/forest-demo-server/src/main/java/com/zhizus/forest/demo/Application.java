package com.zhizus.forest.demo;

import com.zhizus.forest.demo.impl.FooServiceImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Dempe on 2017/6/6 0006.
 */
@RestController
@EnableAutoConfiguration
@SpringBootApplication
public class Application {

    private ApplicationContext context;

    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(Application.class, args);
        FooServiceImpl bean = run.getBean(FooServiceImpl.class);
        System.out.println(bean.echo("hello"));

    }


}
