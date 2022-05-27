package com.hj.platform.uac.controller;

import com.hj.platform.common.util.JsonUtils;
import com.hj.platform.domain.entity.sys.SysUser;
import com.hj.platform.uac.auth.respository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.adapter.HttpWebHandlerAdapter;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

/**
 * @author <a href="mailto:hjm0928@sina.cn">韩金明</a>
 */
@RestController
@Slf4j
public class TestController {

    private final UserRepository userRepository;

    public TestController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping
    public Mono<String> hello(){
        return ReactiveSecurityContextHolder.getContext()
                .map(v -> "你好, " + v.getAuthentication().getPrincipal());
    }

    @GetMapping("/api/user/list")
    public Flux<SysUser> userList(){
        return userRepository.queryAllBy().doOnNext(sysUser -> log.info(JsonUtils.stringify(sysUser)));
    }

    @GetMapping("/login")
    public Mono<Map<String, Object>> unLogin(){
        return Mono.just(Map.of("status", 401, "msg", "Not logged in"));
    }
}
