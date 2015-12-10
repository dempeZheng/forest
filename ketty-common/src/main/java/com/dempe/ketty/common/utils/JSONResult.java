package com.dempe.ketty.common.utils;

import com.alibaba.fastjson.JSONObject;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2015/12/10
 * Time: 14:51
 * To change this template use File | Settings | File Templates.
 */
public class JSONResult extends JSONObject {
    public JSONResult() {
        put("result", 0);
    }

    public void putResult(int statusCode) {
        put("result", statusCode);

    }

}
