package com.yy.ent;

import com.yy.ent.srv.AppServer;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2015/10/22
 * Time: 10:45
 * To change this template use File | Settings | File Templates.
 */
public class SimpleServer {


    public static void main(String[] args) throws Exception {
        new AppServer()
                .stopWithJVMShutdown()
                .initMVC()
                .start(8888);
    }
}
