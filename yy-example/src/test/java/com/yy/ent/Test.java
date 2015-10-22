package com.yy.ent;

import com.yy.ent.action.SimpleAction;
import com.yy.ent.mvc.ioc.BeanFactory;
import com.yy.ent.mvc.ioc.Cherry;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2015/10/22
 * Time: 11:37
 * To change this template use File | Settings | File Templates.
 */
public class Test {
    public static void main(String[] args) throws Exception {
        Cherry cherry = new Cherry();
        cherry.init();
        SimpleAction simpleAction = BeanFactory.getBean(SimpleAction.class.getName());
        System.out.println(simpleAction.getUserByUid("12345677"));
    }
}
