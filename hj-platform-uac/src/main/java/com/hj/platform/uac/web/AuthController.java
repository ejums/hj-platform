package com.hj.platform.uac.web;

import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Map;

/**
 * @author <a href="mailto:hjm0928@sina.cn">韩金明</a>
 */
@RestController
public class AuthController {
    @GetMapping("/login/error")
    public Mono<?> login(){
        Map<String, String> map = Map.of("msg", "un logon", "code", "401");
        return Mono.just(map);
    }

    @GetMapping
    public Mono<?> index(){
        return Mono.just("login success");
    }


    @GetMapping("/user/info")
    public Mono<?> user(){
        Mono<SecurityContext> context = ReactiveSecurityContextHolder.getContext();
        return context;
    }
}
