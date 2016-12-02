# Forest
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://github.com/weibocom/motan/blob/master/LICENSE)
[![Build Status](https://img.shields.io/travis/weibocom/motan/master.svg?label=Build)](https://travis-ci.org/weibocom/motan)

# Overview
Forest is a remote procedure call(RPC) framework for rapid development of high performance distributed services.

# Features
- Create distributed services without writing extra code.
- Provides cluster support and integrate with popular service discovery services like [Zookeeper][zookeeper].
- Supports advanced scheduling features like weighted load-balance, scheduling cross IDCs, etc.
- Optimization for high load scenarios, provides high availability in production environment.

# Quick Start

The quick start gives very basic example of running client and server on the same machine. For the detailed information about using and developing Motan, please jump to [Documents](#documents).

> The minimum requirements to run the quick start are:
>  * JDK 1.7 or above



1. Write Action.

    `src/main/java/com/dempe/forest/example/SampleAction.java`

    ```java
    @Action("sample")
	public class SampleAction {

	    private final static Logger LOGGER = LoggerFactory.getLogger(SampleAction.class);

	    @Autowired
	    private SampleService sampleService;

	    /**
	     * uri:服务路由uri
	     * compressType：压缩类型，目前支持:ompressNo, gizp, snappy;
	     * serializeType:序列化类型，目前支持kyro, fastjson, hession2;
	     * timeOut:客户端请求超时间
	     * group：服务线程组，通过group实现线程隔离
	     *
	     * @param word
	     * @return
	     */
	    @Interceptor(id = "printInterceptor,metricInterceptor")//拦截器，多个拦截器用逗号分隔
	    @Rate(value = 1000000)//服务限速
	    @Export(uri = "hello", compressType = CompressType.compressNo, serializeType = SerializeType.fastjson, timeOut = 1000, group = "sample")
	    public String hello(@HttpParam String word) {
	        return sampleService.hello(word);
	    }

	    @Export
	    public void noReplyMethod() {
	        // do service
	        LOGGER.info("----noReplyMethod---");
	    }
	}


    ```

2.config server use owner & start Server

``` java
@Config.Sources("classpath:server.properties")
public interface ServerConfig extends Config {

    // *********************system configuration*********************

    @Key("forest.port")
    @DefaultValue("9999")
    int port();

    @DefaultValue("true")
    boolean tcpNoDelay();

    @DefaultValue("true")
    boolean soKeepAlive();

    @DefaultValue("65535")
    int soBacklog();


    // StandardThreadExecutor 业务线程池配置

    @DefaultValue("20")
    int coreThread();

    @DefaultValue("200")
    int maxThreads();

    //
    @Key("http.port")
    @DefaultValue("8080")
    public int httpPort();

    @Key("http.backlog")
    @DefaultValue("50")
    int httpBacklog();

    @Key("zookeeper.connectString")
    @DefaultValue("")
    String zkConnectStr();

    @Key("zookeeper.basePath")
    @DefaultValue("forest")
    String zkBasePath();


}
```

   `src/main/java/com/dempe/forest/example/ServerMain.java`

``` java
public class ServerMain {

    public static void main(String[] args) throws InterruptedException {
        ApplicationContext context = new ClassPathXmlApplicationContext(new String[]{"application.xml"});
        AnnotationRouterMapping mapping = new AnnotationRouterMapping(context);
        ServerConfig config = ConfigFactory.create(ServerConfig.class);
        ForestExecutorGroup executorGroup = new ForestExecutorGroup(config, mapping.listGroup(), context);
        /**
         * 对于同一业务接口，可以同时暴露两种协议
         */
        // 启动rpc服务
        new NettyServer(mapping, config, executorGroup).doBind();
        // 启动http服务
        new HttpForestServer(mapping, config).start();

    }
}

```

3. Create and start RPC Client.

``` java
public class ClientMain {

    public final static ClientConfig config = ConfigFactory.create(ClientConfig.class);

    public static void main(String[] args) throws InterruptedException, IOException {
        benchMarkTest();
    }

    public static void benchMarkTest() throws InterruptedException {
        NettyClient client = new NettyClient(config);
        client.connect();
        final SampleAction sampleAction = Proxy.getCglibProxy(SampleAction.class, new ChannelPool(client));
        Stopwatch stopwatch = Stopwatch.createStarted();
        ExecutorService executorService = Executors.newCachedThreadPool();
        for (int i = 0; i < 20; i++) {
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    for (int i = 0; i < 1000000; i++) {
                        String hello = sampleAction.hello("hello====");
                        if (i % 1000 == 0) {
                            System.out.println(hello);
                        }
                    }
                }
            });
        }
        System.out.println("exeTime : " + stopwatch.stop().elapsed(TimeUnit.MILLISECONDS));
    }
}

```

Execute main function in Client will invoke the remote service and print response.

#Performance
8G win64 1k String 3w+

```
21:01:50.910 [pool-1-thread-1] INFO  c.d.forest.example.MetricInterceptor 39 - group:sample, uri:/sample/hello, current tps:31310, avgTime:0, maxTime:16, minTime:0
21:01:51.910 [pool-1-thread-1] INFO  c.d.forest.example.MetricInterceptor 39 - group:sample, uri:/sample/hello, current tps:32524, avgTime:0, maxTime:16, minTime:0
21:01:52.910 [pool-1-thread-1] INFO  c.d.forest.example.MetricInterceptor 39 - group:sample, uri:/sample/hello, current tps:32375, avgTime:0, maxTime:16, minTime:0
21:01:53.910 [pool-1-thread-1] INFO  c.d.forest.example.MetricInterceptor 39 - group:sample, uri:/sample/hello, current tps:32284, avgTime:0, maxTime:16, minTime:0
21:01:54.909 [pool-1-thread-1] INFO  c.d.forest.example.MetricInterceptor 39 - group:sample, uri:/sample/hello, current tps:32291, avgTime:0, maxTime:16, minTime:0
21:01:55.910 [pool-1-thread-1] INFO  c.d.forest.example.MetricInterceptor 39 - group:sample, uri:/sample/hello, current tps:31521, avgTime:0, maxTime:16, minTime:0
21:01:56.910 [pool-1-thread-1] INFO  c.d.forest.example.MetricInterceptor 39 - group:sample, uri:/sample/hello, current tps:31588, avgTime:0, maxTime:16, minTime:0
```


# Documents

* [Wiki](https://github.com/dempeZheng/forest/wiki)
* [Wiki(中文)](https://github.comdempeZheng/forest/wiki/zh_overview)

# Contributors



# License

Forest is released under the [Apache License 2.0](http://www.apache.org/licenses/LICENSE-2.0).



