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
    public Mono<?> index(){
        Mono.deferContextual(Mono::just).contextWrite(context -> context.put("sa", "sa-value"))
                .subscribe(context->{
            try {
                String value = new ObjectMapper().writeValueAsString(context);
                System.out.println(value);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        });
        Mono.deferContextual(Mono::just)
                .contextWrite(context -> {
                    context.size();
                    return context;
                })
                .subscribe(contextView -> {
                    System.out.println(contextView.size());
                });
        return Mono.just("hello world, Gateway");
    }
}
