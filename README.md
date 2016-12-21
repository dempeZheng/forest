# Forest
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://github.com/dempeZheng/forest/blob/master/LICENSE)
[![Build Status](https://img.shields.io/travis/dempeZheng/forest/master.svg?label=Build)](https://travis-ci.org/dempeZheng/forest)

# Overview
基于netty轻量的高性能分布式RPC服务框架。简单，易用，高效。

# Features
- 服务端支持多种序列化方式：fastjson，hession，kryo
- 服务端支持多种压缩方式：gzip，snappy
- 支持注解配置，也支持spring xml配置
- 支持服务发现服务注册
- client端支持多种负载均衡策略和容灾策略
- client内置连接池
- 基于netty 4.x版本实现，高性能（win 8cpu单机8w+）


# Protocol

<style type="text/css">
.tg  {border-collapse:collapse;border-spacing:0;border:none;}
.tg td{font-family:Arial, sans-serif;font-size:14px;padding:10px 5px;border-style:solid;border-width:0px;overflow:hidden;word-break:normal;}
.tg th{font-family:Arial, sans-serif;font-size:14px;font-weight:normal;padding:10px 5px;border-style:solid;border-width:0px;overflow:hidden;word-break:normal;}
.tg .tg-yw4l{vertical-align:top}
</style>
<table class="tg">
  <tr>
    <th class="tg-031e">short</th>
    <th class="tg-031e">byte</th>
    <th class="tg-yw4l">byte</th>
    <th class="tg-yw4l"></th>
    <th class="tg-yw4l"></th>
    <th class="tg-yw4l"></th>
    <th class="tg-031e"></th>
    <th class="tg-yw4l"></th>
    <th class="tg-031e">long</th>
    <th class="tg-yw4l"></th>
    <th class="tg-yw4l"></th>
  </tr>
  <tr>
    <td class="tg-031e">magic</td>
    <td class="tg-031e">version</td>
    <td class="tg-yw4l">extend</td>
    <td class="tg-yw4l"></td>
    <td class="tg-yw4l"></td>
    <td class="tg-yw4l"></td>
    <td class="tg-031e"></td>
    <td class="tg-yw4l"></td>
    <td class="tg-031e">messageID</td>
    <td class="tg-yw4l"></td>
    <td class="tg-yw4l"></td>
  </tr>
  <tr>
    <td class="tg-031e"></td>
    <td class="tg-031e"></td>
    <td class="tg-yw4l"></td>
    <td class="tg-yw4l"></td>
    <td class="tg-yw4l"></td>
    <td class="tg-yw4l"></td>
    <td class="tg-031e"></td>
    <td class="tg-yw4l"></td>
    <td class="tg-031e"></td>
    <td class="tg-yw4l"></td>
    <td class="tg-yw4l"></td>
  </tr>
  <tr>
    <td class="tg-031e"></td>
    <td class="tg-031e"></td>
    <td class="tg-yw4l"></td>
    <td class="tg-yw4l"></td>
    <td class="tg-yw4l"></td>
    <td class="tg-yw4l"></td>
    <td class="tg-031e"></td>
    <td class="tg-yw4l"></td>
    <td class="tg-031e"></td>
    <td class="tg-yw4l"></td>
    <td class="tg-yw4l"></td>
  </tr>
</table>

# Quick Start


Add dependencies to pom.

``` xml
<dependency>
    <groupId>com.zhizus</groupId>
    <artifactId>forest-rpc</artifactId>
    <version>0.0.1</version>
</dependency>

<dependency>
    <groupId>com.zhizus</groupId>
    <artifactId>forest-common</artifactId>
    <version>0.0.1</version>
</dependency>
```


## 1.定义接口

>通过注解`@ServiceProvider`暴露服务，通过`@MethodProvide`暴露方法默认配置，如：`压缩方式，序列化方式，客户端超时时间`

``` java
@ServiceProvider
public interface SampleService {
    @MethodProvider
    String say(String str);
}
 ```

## 2.实现接口

>基于注解`@ServiceExport`发布服务，基于注解 `@MethodExport`发布方法，

``` java
@ServiceExport
public class SampleServiceImpl implements SampleService {

    @MethodExport
    @Override
    public String say(String str) {
	return "say " + str;
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

```java
SampleService sampleService = Forest.from(SampleService.class);
String result = sampleService.say("hello");
```

### Console输出

```
23:10:10.295 [pool-1-thread-1] INFO MetricInterceptor 34 - methodName:/sampleService/say, current tps:83342, avgTime:0, maxTime:63, minTime:0
23:10:11.298 [pool-1-thread-1] INFO MetricInterceptor 34 - methodName:/sampleService/say, current tps:86271, avgTime:0, maxTime:63, minTime:0
23:10:12.295 [pool-1-thread-1] INFO MetricInterceptor 34 - methodName:/sampleService/say, current tps:86063, avgTime:0, maxTime:63, minTime:0
23:10:13.295 [pool-1-thread-1] INFO MetricInterceptor 34 - methodName:/sampleService/say, current tps:84305, avgTime:0, maxTime:63, minTime:0
```

[更多示例](https://github.com/dempeZheng/forestRPC/tree/master/forest-demo)


# Documents

* [Wiki(中文)](https://github.com/dempeZheng/forestRPC)

# TODO

- 服务降级功能
- http服务支持
- 跨语言协议支持
- 服务治理管理后台

# License

Forest is released under the [Apache License 2.0](http://www.apache.org/licenses/LICENSE-2.0).





