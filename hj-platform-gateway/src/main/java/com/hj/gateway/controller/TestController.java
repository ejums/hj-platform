package com.hj.gateway.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * @author <a href="mailto:hjm0928@sina.cn">韩金明</a>
 */
@RestController
public class TestController {
    @GetMapping
    public Mono<String> index(){
        return Mono.just("hello world, Gateway");
    }
}
