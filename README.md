##Jetty
>基于netty实现的rpc框架，提供性能监控，日志分析，动态扩展的功能。

###jetty-srv模块
>基于netty实现支持自定义协议扩展的Nio MVC高性能业务框架

####协议
- Http
- Websckoket
- JettyRequest
- JettyResponse



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
```

####TODO 

- 支持自定义协议扩展
- 添加拦截器的支持
- 安全验证
- 性能优化
- HttpJettyServer协议的实现
- WebSocketJettyServer的实现

###jetty-client模块
>JettyServer高可用NIO客户端

####High availability
支持多个节点，节点不可用自动移除

#### Client pool
支持连接池

#### 断链自动重连

#### 自动维护心跳

###jetty-codec模块
>编解码框架

####JettyRequest

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

####JettyResponse

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

###jetty-router模块
>服务代理模块，提供路由分发功能

###jetty-monitor模块
>性能监控
>报警

###jetty-analysis模块
>接口统计分析
>智能推荐



