package com.yy.ent;

import com.yy.ent.srv.AppServer;
import com.yy.ent.srv.YYServer;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2015/10/22
 * Time: 10:45
 * To change this template use File | Settings | File Templates.
 */
public class SimpleServer {


    public static void main(String[] args) {
        AppServer appServer = new AppServer();
        appServer.start(8888);
    }
}
