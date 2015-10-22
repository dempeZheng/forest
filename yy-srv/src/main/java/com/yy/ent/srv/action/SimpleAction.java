package com.yy.ent.srv.action;

import com.yy.ent.mvc.anno.Action;
import com.yy.ent.mvc.anno.Param;
import com.yy.ent.mvc.anno.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleAction.class);

    @Path(value = "test")
    public String test(@Param String name) {
        return "hello" + UUID.randomUUID().toString();

    }
}


