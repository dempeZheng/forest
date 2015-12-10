package com.dempe.ketty.name;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2015/12/10
 * Time: 14:45
 * To change this template use File | Settings | File Templates.
 */
public class IDMaker {

    public static String buildID(String name, String host, int port) {
        return new StringBuffer(name).append("|")
                .append(host)
                .append(port).toString();
    }
}
