##Ketty
>基于netty实现的服务端Nio MVC业务开发平台，提供性能监控，日志分析，动态扩展的功能。

###ketty-srv模块
>基于netty实现支持自定义协议扩展的Nio MVC高性能业务框架

####协议
- Http
- WebSocket
- Ketty(自定义私有协议)


####基于注解的 mvc

- @Inject注入
- @Path 路径支持
- @Param参数自动注入value

``` java
@Action
public class SimpleAction {

    @Inject
    private UserService userService;

	@Interceptor(id = "echoInterceptor")
    @Path
    public User getUserByUid(@Param String uid) {
        return userService.getUserByUid(uid);
    }
}

```

####拦截器 example
``` java
@Around
public class EchoInterceptor extends BaseInterceptor {

    private final static Logger LOGGER = LoggerFactory.getLogger(EchoInterceptor.class);

    @Override
    public boolean before() {
        LOGGER.info("==============EchoInterceptor before=========");
        return true;
    }

    @Override
    public boolean after() {
        LOGGER.info("==============EchoInterceptor after=========");
        return true;
    }
}
```

#### KettyServer example
``` java
// nio mvc 业务server启动类example
new KettyServer.Builder()
                .tcpNoDelay(true)
                .soKeepAlive(true)
                .setHttpProtocol()
                .host("localhost")
                .port(8888)
                .build()
                .start();

// 测试jetty客户端
public class JettClientTest {
	public static ClientSender clientSender = new ClientSender("localhost", 8888);
	public static void main(String[] args) throws Exception {
		KettyRequest request = new KettyRequest();
		request.setUri("/simpleAction/getUserByUid");
		JSONObject params = new JSONObject();
		params.put("uid", "12345677");
		request.setParameter(params);
		String result = clientSender.sendAndWait(request);
		System.out.println("result : " + result);
	}
}
```

#### HttpServer example
``` java
// nio mvc 业务server启动类example
public class SimpleServer {
    public static void main(String[] args) throws Exception {
         new KettyServer.Builder()
                        .setKettyProtocol()
                        .port(8888)
                        .build()
                        .start();
    }
}

```
####TODO 

- 支持自定义协议扩展
- 安全验证
- 性能优化
- WebSocketJettyServer的实现

###ketty-client模块
>KettyServer高可用NIO客户端

####High availability
支持多个节点，节点不可用自动移除

#### Client pool
支持连接池

#### 断链自动重连

#### 自动维护心跳

###ketty-codec模块
>编解码框架

####KettyRequest

<table>
<tr bgcolor="#DCDCDC">
	<th colspan="5" width="50%">header</th>
	<th>body</th>
</tr>
<tr>	
	<td>size</td>
	<td>len</td>
	<td>uri</td>
	<td>msgId</td>
	<td>paramsMap</td>
	<td>body</td>
</tr>
<tr>	
	<td>short(2byte)</td>
	<td>short(2byte)</td>
	<td>string</td>
	<td>int(4byte)</td>
	<td>map</td>
	<td>JSONString</td>
</tr>
</table>

####KettyResponse

<table>
<tr bgcolor="#DCDCDC">
	<th colspan="4" width="50%">header</th>
	<th>body</th>
</tr>
<tr>	
	<td>size</td>
	<td>len</td>
	<td>msgId(消息id)</td>
	<td>resCode(消息返回码)</td>
	<td>body</td>
</tr>
<tr>	
	<td>short(2byte)</td>
	<td>short(2byte)</td>
	<td>int(4byte)</td>
	<td>short(2byte)</td>
	<td>JSONString</td>
</tr>
</table>

###ketty-router模块
>服务代理模块，提供路由分发功能

###ketty-monitor模块
>性能监控
>报警

###ketty-analysis模块
>接口统计分析
>智能推荐

[READ MORE](http://zhizus.com)



