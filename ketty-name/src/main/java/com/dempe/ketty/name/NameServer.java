package com.dempe.ketty.name;

import com.dempe.ketty.srv.KettyServer;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2015/12/10
 * Time: 14:34
 * To change this template use File | Settings | File Templates.
 */
public class NameServer {

    public static void main(String[] args) throws Exception {
        new KettyServer.Builder()
                .initPackage("com.dempe.ketty")
                .setKettyProtocol()
                .port(6666)
                .starReport(true)
                .build()
                .start();
    }
}
