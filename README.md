# Forest
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://github.com/dempeZheng/forestRPC/blob/master/LICENSE)
[![Build Status](https://img.shields.io/travis/dempeZheng/forestRPC/master.svg?label=Build)](https://travis-ci.org/dempeZheng/forestRPC)

# Overview
基于netty, spring,轻量的高性能分布式RPC服务框架。简单，易用，高效。

# Features
- 服务端支持多种序列化方式：fastjson，hession，kryo
- 服务端支持多种压缩方式：gzip，snappy
- 服务端可根据group进行线程隔离，支持基于spring对不同的group配置不同的业务线程池
- 支持注解配置，也支持spring xml配置
- 支持服务发现服务注册
- client端支持多种负载均衡策略和容灾策略
- client内置连接池
- 基于netty 4.x版本实现，高性能（win 8cpu单机8w+）


# Protocol

# Quick Start

## 1.定义接口

>通过注解`@ServiceProvider`暴露服务，通过`@MethodProvide`暴露方法默认配置，如：`压缩方式，序列化方式，客户端超时时间`

``` java
@ServiceProvider(serviceName = "sampleService", haStrategyType = HaStrategyType.FAIL_FAST,
	loadBalanceType = LoadBalanceType.RANDOM, connectionTimeout = Constants.CONNECTION_TIMEOUT, port = 8888)
public interface SampleService {

    @MethodProvider(methodName = "say")
    String say(String str);

    @MethodProvider(methodName = "echo", serializeType = SerializeType.fastjson, compressType = CompressType.gzip)
    String echo(String msg);

}
 ```

## 2.实现接口

>基于注解`@ServiceExport`发布服务，基于注解 `@MethodExport`发布方法，

``` java
@ServiceExport
public class SampleServiceImpl implements SampleService {

    @MethodExport
    @Rate(2)
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

    <context:component-scan base-package="com.zhizus.forest.demo"/>
    <bean id="metricInterceptor" class="com.zhizus.forest.core.interceptor.MetricInterceptor"/>
    <bean id="forestServer" class="com.zhizus.forest.support.spring.ForestServerBean"/>

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

###基于spring配置

`application-client.xml`

> 可以使用api注解暴露的默认配置，也可以通过spring为每个方法定义配置

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xmlns="http://www.springframework.org/schema/beans" xmlns:util="http://www.springframework.org/schema/util"
       xmlns:aop="http://www.springframework.org/schema/aop"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
	http://www.springframework.org/schema/beans/spring-beans.xsd
	http://www.springframework.org/schema/context
	http://www.springframework.org/schema/context/spring-context.xsd http://www.springframework.org/schema/util http://www.springframework.org/schema/util/spring-util.xsd http://www.springframework.org/schema/aop http://www.springframework.org/schema/aop/spring-aop.xsd">

    <context:component-scan base-package="com.zhizus.forest.demo.client"/>
    <bean id="methodConfig" class="com.zhizus.forest.common.config.MethodConfig">
	<property name="compressType">
	    <util:constant static-field="None"/>
	</property>
	<property name="serializeType">
	    <util:constant static-field="Fastjson"/>
	</property>
	<property name="timeout" value="5000"></property>
    </bean>
    <bean id="sampleServiceProxy" class="com.zhizus.forest.support.spring.ForestProxyFactoryBean">
	<property name="serviceInterface" value="com.zhizus.forest.demo.api.SampleService"></property>
	<!--methodConfMap如果不配置，则使用接口方法注解上面的配置-->
	<property name="methodConfigMap">
	    <map>
		<entry key="echo" value-ref="methodConfig"></entry>
		<entry key="say" value-ref="methodConfig"></entry>
	    </map>
	</property>
    </bean>
</beans>
```

``` java
public class SampleClient {
    public static void main(String[] args) throws InterruptedException {
	ApplicationContext context = new ClassPathXmlApplicationContext(new String[]{"application-client.xml"});
	SampleService bean = (SampleService) context.getBean("sampleServiceProxy");
	String test = bean.say("hello");
	System.out.println(test);
    }
}
```

### 基于默认注解

```java
SampleService sampleService = Forest.from(SampleService.class);
```

### 基于代码自定义配置

``` java
SampleService sampleService = Forest.from(SampleService.class, ServiceConfig.Builder.newBuilder()
			.withMethodConfig("say", MethodConfig.Builder.newBuilder()
				.withCompressType(CompressType.none)
				.withSerializeType(SerializeType.fastjson)
				.build())
			.withMethodConfig("echo", MethodConfig.Builder.newBuilder()
				.withCompressType(CompressType.none)
				.build())
			.build());
```

### Console输出

```
23:10:10.295 [pool-1-thread-1] INFO MetricInterceptor 34 - methodName:/sampleService/echo, current tps:83342, avgTime:0, maxTime:63, minTime:0 
23:10:11.298 [pool-1-thread-1] INFO MetricInterceptor 34 - methodName:/sampleService/echo, current tps:86271, avgTime:0, maxTime:63, minTime:0 
23:10:12.295 [pool-1-thread-1] INFO MetricInterceptor 34 - methodName:/sampleService/echo, current tps:86063, avgTime:0, maxTime:63, minTime:0 
23:10:13.295 [pool-1-thread-1] INFO MetricInterceptor 34 - methodName:/sampleService/echo, current tps:84305, avgTime:0, maxTime:63, minTime:0 
```

[更多示例](https://github.com/dempeZheng/forestRPC/tree/master/forest-demo)


# Documents

* [Wiki(中文)](https://github.comdempeZheng/forestRPC)

# TODO

- 服务降级功能
- http服务支持
- 跨语言协议支持
- 服务治理管理后台

# License

Forest is released under the [Apache License 2.0](http://www.apache.org/licenses/LICENSE-2.0).




