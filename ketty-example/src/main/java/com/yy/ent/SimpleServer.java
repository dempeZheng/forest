package com.yy.ent;


import com.yy.ent.srv.KettyServer;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2015/10/22
 * Time: 10:45
 * To change this template use File | Settings | File Templates.
 */
public class SimpleServer {


    public static void main(String[] args) throws Exception {
        //starHttpServer();
        startKettyServer();
    }

    public static void starHttpServer() throws Exception {
        new KettyServer.Builder()
                .initPackage("com.yy.ent")
                .setHttpProtocol()
                .host("localhost")
                .port(8888)
                .build()
                .start();
    }

    public static void startKettyServer() throws Exception {
        new KettyServer.Builder()
                .initPackage("com.yy.ent")
                .setKettyProtocol()
                .port(8888)
                .build()
                .start();
    }
}
