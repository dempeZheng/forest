package com.yy.ent.action;


import com.yy.ent.model.User;
import com.yy.ent.mvc.anno.Action;
import com.yy.ent.mvc.anno.Inject;
import com.yy.ent.mvc.anno.Param;
import com.yy.ent.mvc.anno.Path;
import com.yy.ent.service.UserService;
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

    @Path
    public User getUserByUid(@Param String uid) {
        return userService.getUserByUid(uid);
    }
}
