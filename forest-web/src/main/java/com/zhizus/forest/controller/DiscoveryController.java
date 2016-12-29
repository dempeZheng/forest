package com.zhizus.forest.controller;

import com.alibaba.fastjson.JSON;
import com.zhizus.forest.common.registry.AbstractServiceDiscovery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Collection;

/**
 * Created by Dempe on 2016/12/29.
 */
@Controller
@RequestMapping("/discovery")
public class DiscoveryController {

    @Autowired
    private AbstractServiceDiscovery discovery;

    @RequestMapping("query")
    @ResponseBody
    public String query() {
        return "{}";
    }

    @RequestMapping("queryByName")
    @ResponseBody
    public String queryByName(@RequestParam String name) throws Exception {
        Collection collection = discovery.queryForInstances(name);
        return JSON.toJSONString(collection);
    }

    @RequestMapping("updateServiceInstance")
    @ResponseBody
    public String updateServiceInstance() {
        return "{}";

    }

}
