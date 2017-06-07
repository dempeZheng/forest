package com.zhizus.forest.demo.impl;


import com.zhizus.forest.common.annotation.ForestService;
import com.zhizus.forest.demo.api.FooService;

/**
 * Created by Dempe on 2016/12/7.
 */
@ForestService("fooService")
public class FooServiceImpl implements FooService {

    @Override
    public String echo(String msg) {
        return "echo>>> " + msg;
    }


}
