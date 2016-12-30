package com.zhizus.forest.controller;

import com.alibaba.fastjson.JSON;
import com.zhizus.forest.common.MetaInfo;
import com.zhizus.forest.common.registry.AbstractServiceDiscovery;
import org.apache.curator.x.discovery.ServiceInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Dempe on 2016/12/29.
 */
@Controller
@RequestMapping("/metric")
public class MetricsController {
    @Autowired
    private AbstractServiceDiscovery<MetaInfo> discovery;

    @RequestMapping("/index")
    public ModelAndView index(ModelAndView modelAndView) throws Exception {
        Collection<String> names = discovery.queryForNames();
        ArrayList<String> attributeValue = new ArrayList<String>(names);
        modelAndView.addObject("names", attributeValue);
        modelAndView.setViewName("forest/metrics");
        return modelAndView;
    }

    @RequestMapping("/queryByName")
    @ResponseBody
    public String queryByName(@RequestParam String name) throws Exception {
        Collection<ServiceInstance<MetaInfo>> collection = discovery.queryForInstances(name);
        return JSON.toJSONString(collection);
    }
}
