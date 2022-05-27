package com.hj.platform.app.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author <a href="mailto:hjm0928@sina.cn">韩金明</a>
 */
@RestController
public class TestController {
    @GetMapping
    public String index(){
        return "hello, welcome";
    }
}
