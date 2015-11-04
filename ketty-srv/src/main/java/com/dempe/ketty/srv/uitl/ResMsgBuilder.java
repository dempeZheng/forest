package com.dempe.ketty.srv.uitl;

import com.alibaba.fastjson.JSONObject;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2015/11/4
 * Time: 15:41
 * To change this template use File | Settings | File Templates.
 */
public class ResMsgBuilder {

    public final static String NO_PERMISSION_MSG;

    static {
        JSONObject msg = new JSONObject();
        msg.put("msg", "you have no permission");

        NO_PERMISSION_MSG = msg.toJSONString();
    }

    public static String getNoPermissionMsg() {
        return NO_PERMISSION_MSG;
    }
}
