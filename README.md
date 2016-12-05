# Forest
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://github.com/weibocom/motan/blob/master/LICENSE)
[![Build Status](https://img.shields.io/travis/weibocom/motan/master.svg?label=Build)](https://travis-ci.org/weibocom/motan)

# Overview
基于netty, spring,轻量的高性能分布式RPC服务框架。简单，易用，高效。

# Features
- 服务端支持多种序列化方式：fastjson，hession，kryo
- 服务端支持多种压缩方式：gzip，snappy
- 服务端可根据group进行线程隔离
- 支持spring容器
- 支持服务发现服务注册
- 支持多种负载均衡策略
- 支持基于Hystrix的容灾策略
- client内置连接池
- 基于netty实现，高性能，单机5w+
- 基于spring容器，简单易用

# Protocol

# Quick Start


## 1.定义接口

>通过注解`@ServiceProvider`暴露服务，通过`@MethodProvide`暴露方法默认配置，如：`压缩方式，序列化方式，客户端超时时间`

``` java

	@ServiceProvider(serviceName = "sampleService", port = 8888)
	public interface SampleService {

	    @MethodProvider(methodName = "say")
	    String say(String str);

	    @MethodProvider(methodName = "say2")
	    String say2(String str);

	    @MethodProvider(methodName = "echo")
	    String echo(String msg);

	    @MethodProvider(methodName = "hi",serializeType = SerializeType.fastjson, compressType = CompressType.gizp)
	    String hi(String msg);

	}


   ```

## 2.实现接口

>基于注解`@ServiceExport`发布服务，基于注解 `@MethodExport`发布方法，

``` java
@ServiceExport
public class SampleServiceImpl implements SampleService {

    @MethodExport
    @Rate(2)// 服务限流，每秒2个请求 可选
    @Interceptor("metricInterceptor")//添加统计拦截器，可选
    @Override
    public String say(String str) {
        return "say " + str;
    }

    @MethodExport
    @Override
    public String say2(String str) {
        return "say2 " + str;
    }

    @Interceptor("metricInterceptor")
    @MethodExport
    @Override
    public String echo(String msg) {
        return "echo " + msg;
    }

    @MethodExport
    @Override
    public String hi(String msg) {
        return "hi " + msg;
    }
}

```

## 3.服务端开发

### spring context 配置：

`application.xml`

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xmlns="http://www.springframework.org/schema/beans"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
        http://www.springframework.org/schema/beans/spring-beans.xsd
        http://www.springframework.org/schema/context
        http://www.springframework.org/schema/context/spring-context.xsd">

    <context:component-scan base-package="quickstart"/>

    <bean id="metricInterceptor" class="com.dempe.forest.core.interceptor.MetricInterceptor"/>

    <!--服务注册-->
    <bean id="namingService" class="com.dempe.forest.register.redis.RedisRegistryService">
        <constructor-arg>
            <bean class="com.dempe.forest.register.redis.RedisClient">
                <property name="redisServer" value="116.31.122.26"></property>
                <property name="port" value="6379"></property>
                <property name="testOnBorrow" value="true"></property>
                <property name="maxWait" value="2000"></property>
            </bean>
        </constructor-arg>
        <property name="administrator" value="true"></property>
        <property name="group" value="default/"></property>
        <property name="expirePeriod" value="3000"></property>
    </bean>


    <bean id="forestServer" class="com.dempe.forest.support.spring.ForestServerBean">
        <property name="registryCenterService" ref="namingService"></property>
    </bean>

</beans>
```

### Server开发

``` java
public class SampleServer {

    public static void main(String[] args) throws Exception {

        new ClassPathXmlApplicationContext(new String[]{"application.xml"});
    }
}
```

## 4.客户端开发

### spring配置

`application-client.xml`

> 可以使用api注解暴露的默认配置，也可以通过spring为每个方法定义配置


```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xmlns="http://www.springframework.org/schema/beans" xmlns:util="http://www.springframework.org/schema/util"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
        http://www.springframework.org/schema/beans/spring-beans.xsd
        http://www.springframework.org/schema/context
        http://www.springframework.org/schema/context/spring-context.xsd http://www.springframework.org/schema/util http://www.springframework.org/schema/util/spring-util.xsd">

    <context:component-scan base-package="quickstart"/>

    <bean id="methodConf" class="com.dempe.forest.MethodProviderConf">
        <property name="compressType">
            <util:constant static-field="com.dempe.forest.core.CompressType.compressNo"/>
        </property>
        <property name="serializeType">
            <util:constant static-field="com.dempe.forest.core.SerializeType.fastjson"/>
        </property>
        <property name="timeout" value="5000"></property>
    </bean>

    <bean id="sampleServiceProxy" class="com.dempe.forest.support.spring.ProxyFactoryBean">
        <property name="serviceInterface" value="quickstart.api.SampleService"></property>
        <!--methodConfMap如果不配置，则使用接口方法注解上面的配置-->
        <property name="methodConfMap">
            <map>
                <entry key="echo" value-ref="methodConf"></entry>
                <entry key="say" value-ref="methodConf"></entry>
            </map>
        </property>
    </bean>

</beans>
```

### 客户端开发

``` java

public class SpringSampleClient {

    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext(new String[]{"application-client.xml"});
        SampleService sampleServiceProxy = (SampleService) context.getBean("sampleServiceProxy");
        String hello = sampleServiceProxy.say("hello");
        System.out.println(hello);
    }

}

```

### Console输出

```
21:19:50.924 [pool-1-thread-1] INFO  c.d.f.c.i.MetricInterceptor 36 - group:def_group, methodName:/sampleService/echo, current tps:57904, avgTime:0, maxTime:5, minTime:0
21:19:51.924 [pool-1-thread-1] INFO  c.d.f.c.i.MetricInterceptor 36 - group:def_group, methodName:/sampleService/echo, current tps:60205, avgTime:0, maxTime:5, minTime:0
21:19:52.924 [pool-1-thread-1] INFO  c.d.f.c.i.MetricInterceptor 36 - group:def_group, methodName:/sampleService/echo, current tps:61398, avgTime:0, maxTime:5, minTime:0
21:19:53.925 [pool-1-thread-1] INFO  c.d.f.c.i.MetricInterceptor 36 - group:def_group, methodName:/sampleService/echo, current tps:59617, avgTime:0, maxTime:5, minTime:0
21:19:54.925 [pool-1-thread-1] INFO  c.d.f.c.i.MetricInterceptor 36 - group:def_group, methodName:/sampleService/echo, current tps:59147, avgTime:0, maxTime:5, minTime:0
21:19:55.926 [pool-1-thread-1] INFO  c.d.f.c.i.MetricInterceptor 36 - group:def_group, methodName:/sampleService/echo, current tps:56509, avgTime:0, maxTime:5, minTime:0
21:19:56.925 [pool-1-thread-1] INFO  c.d.f.c.i.MetricInterceptor 36 - group:def_group, methodName:/sampleService/echo, current tps:56032, avgTime:0, maxTime:5, minTime:0
```

[更多示例](https://github.com/dempeZheng/forest/tree/master/src/main/java/quickstart)


# Documents

* [Wiki](https://github.com/dempeZheng/forest/wiki)
* [Wiki(中文)](https://github.comdempeZheng/forest/wiki/zh_overview)

# TODO

- 服务注册发现
- 基于Hystrix的容灾策略
- client高可用
- 多语言协议支持

# License

Forest is released under the [Apache License 2.0](http://www.apache.org/licenses/LICENSE-2.0).




