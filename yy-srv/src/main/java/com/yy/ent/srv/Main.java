package com.yy.ent.srv;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2015/10/15
 * Time: 18:21
 * To change this template use File | Settings | File Templates.
 */
public class Main {
    public static void main(String[] args) {
        new YYServer().start(8888);
        new YYServer().start(9999);
    }
}
