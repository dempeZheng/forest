package com.yy.ent;


import com.yy.ent.srv.KettyServer;
import com.yy.ent.srv.uitl.ServerType;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2015/10/22
 * Time: 10:45
 * To change this template use File | Settings | File Templates.
 */
public class SimpleServer {


    public static void main(String[] args) throws Exception {

        starHttpServer();
    }

    public static void starHttpServer() throws Exception {
        new KettyServer(ServerType.HTTP_SERVER)
                .stopWithJVMShutdown()
                .initMVC()
                .start(8888);
    }

    public static void startDefServer() throws Exception {
        new KettyServer(ServerType.HTTP_SERVER)
                .stopWithJVMShutdown()
                .initMVC()
                .start(8888);
    }
}
