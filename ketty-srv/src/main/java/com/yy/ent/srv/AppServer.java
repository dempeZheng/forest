package com.yy.ent.srv;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2015/10/22
 * Time: 13:04
 * To change this template use File | Settings | File Templates.
 */
public class AppServer extends KettyServer {

    private final static Logger LOGGER = LoggerFactory.getLogger(AppServer.class);

    public static void main(String[] args) throws Exception {
        LOGGER.info("______________start AppServer____________");
        new AppServer()
                .stopWithJVMShutdown()
                .initMVC()
                .start(8888);
    }
}
