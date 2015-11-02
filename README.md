##Ketty
>基于netty实现的rpc框架，提供性能监控，日志分析，动态扩展的功能。

###ketty-srv模块
>基于netty实现支持自定义协议扩展的Nio MVC高性能业务框架

####协议
- Http
- Websckoket
- KettyRequest
- KettyResponse



####基于注解的 mvc

- @Inject注入
- @Path 路径支持
- @Param参数自动注入value

``` java
@Action
public class SimpleAction {

    @Inject
    private UserService userService;

    @Path
    public User getUserByUid(@Param String uid) {
        return userService.getUserByUid(uid);
    }
}

public class SimpleServer {

    public static void main(String[] args) throws Exception {
        new AppServer()
                .stopWithJVMShutdown()
                .initMVC()
                .start(8888);
    }
}

public class Test {

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

####TODO 

- 支持自定义协议扩展
- 添加拦截器的支持
- 安全验证
- 性能优化
- HttpJettyServer协议的实现
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



