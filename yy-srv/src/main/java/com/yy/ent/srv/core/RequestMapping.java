package com.yy.ent.srv.core;

import com.yy.ent.srv.method.ActionMethod;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: zhengdaxia
 * Date: 15/10/17
 * Time: 上午10:41
 * To change this template use File | Settings | File Templates.
 */
public class RequestMapping {

    public Map<String, ActionMethod> mapping = new HashMap<String, ActionMethod>();

    /**
     * 扫描packet下面所有的映射，初始化mapping
     */
    public void initMapping() {

    }

    public ActionMethod tack(String uri) {
        return mapping.get(uri);
    }

}
