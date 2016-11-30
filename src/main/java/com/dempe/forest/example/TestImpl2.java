package com.dempe.forest.example;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/11/30
 * Time: 17:33
 * To change this template use File | Settings | File Templates.
 */
public class TestImpl2 implements Test {
    public String say(String msg) {
        String result = "TestImpl2>>>>>>>>" + msg;
        System.out.println(result);
        return msg;
    }
}
