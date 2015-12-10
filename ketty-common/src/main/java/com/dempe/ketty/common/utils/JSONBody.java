package com.dempe.ketty.common.utils;

import com.alibaba.fastjson.JSONObject;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2015/12/10
 * Time: 17:01
 * To change this template use File | Settings | File Templates.
 */
public class JSONBody extends JSONObject {

    public JSONBody set(String key,Object value){
        put(key,value);
        return this;
    }


}
