package com.yy.ent.srv;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2015/10/20
 * Time: 13:11
 * To change this template use File | Settings | File Templates.
 */
public class AppServerTest {

    public static void main(String[] args) {

        YYServer server = new YYServer();
        server.start(8888);

    }
}
