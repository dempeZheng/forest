package com.dempe.forest.example;

import com.dempe.forest.core.CompressType;
import com.dempe.forest.core.SerializeType;
import com.dempe.forest.core.annotation.Action;
import com.dempe.forest.core.annotation.Export;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/11/30
 * Time: 17:32
 * To change this template use File | Settings | File Templates.
 */
@Action("sample")
public class TestImpl1 implements Test{


    @Export(uri = "hello", compressType = CompressType.gizp, serializeType = SerializeType.kyro)
    public String say(String msg) {
        String result = ">>>>>>>>"+msg;
        System.out.println(result);
        return msg;
    }
}
