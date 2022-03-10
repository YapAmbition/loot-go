package com.nikfce.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author shenzhencheng 2022/3/1
 */
@RestController
@RequestMapping("/index")
public class IndexController {

    @RequestMapping("/index")
    public Object index() {
        return "hello";
    }

}
