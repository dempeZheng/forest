package com.dempe.forest.example;

import com.dempe.forest.core.annotation.Action;
import com.dempe.forest.core.annotation.Param;
import com.dempe.forest.core.annotation.URI;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/11/28
 * Time: 19:17
 * To change this template use File | Settings | File Templates.
 */
@Action("sample")
public class SampleAction {

    @URI("hello")
    public String hello(@Param String word) {
        return "hello>>>" + word;
    }

}
