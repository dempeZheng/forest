package com.yy.ent.srv.action;

import com.yy.ent.ioc.Action;
import com.yy.ent.ioc.Param;
import com.yy.ent.ioc.Path;

import java.util.UUID;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2015/10/20
 * Time: 19:54
 * To change this template use File | Settings | File Templates.
 */
@Action
public class SimpleAction {

    @Path(value = "test")
    public String test(@Param String name) {
        System.out.println("----------------" + name);
        return "hello" + UUID.randomUUID().toString();

    }
}


