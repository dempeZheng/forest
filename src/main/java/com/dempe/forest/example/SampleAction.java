package com.dempe.forest.example;

import com.dempe.forest.core.annotation.Action;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/11/28
 * Time: 19:17
 * To change this template use File | Settings | File Templates.
 */
@Action("sample")
public class SampleAction {

    public void hello() {
        System.out.println("-------------");
    }

}
