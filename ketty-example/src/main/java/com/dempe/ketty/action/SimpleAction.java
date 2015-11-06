package com.dempe.ketty.action;


import com.dempe.ketty.model.User;
import com.dempe.ketty.mvc.anno.*;
import com.dempe.ketty.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2015/10/22
 * Time: 10:43
 * To change this template use File | Settings | File Templates.
 */
@Action
public class SimpleAction {

    public static final Logger LOGGER = LoggerFactory.getLogger(SimpleAction.class);

    @Inject
    private UserService userService;

    @Path
    public String hello() {
        return "hello yy-rpc";
    }

    // 限制每秒处理2个请求
    @Rate(value = 2)
    @Interceptor(id = "echoInterceptor,permissionInterceptor")
    @Path
    public User getUserByUid(@Param String uid) {
        return userService.getUserByUid(uid);
    }
}
