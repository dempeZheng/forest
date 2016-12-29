package com.zhizus.forest.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Dempe on 2016/12/29.
 */
@Controller
@RequestMapping("/")
public class HomeController {

    @RequestMapping("/index")
    public String index() {
        return "index";
    }
}
