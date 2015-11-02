package com.yy.ent.ioc;

import com.yy.ent.mvc.ioc.KettyIOC;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2015/10/22
 * Time: 11:27
 * To change this template use File | Settings | File Templates.
 */
public class Test {
    public static void main(String[] args) throws Exception {
        KettyIOC cherry = new KettyIOC("E:\\IDEAProject\\yy-rpc\\yy-ioc\\src\\main\\resources\\ketty.xml");
        cherry.init();

    }
}
