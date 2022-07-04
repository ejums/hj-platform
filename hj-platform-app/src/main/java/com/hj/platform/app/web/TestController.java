package com.hj.platform.app.web;

import com.hj.platform.common.util.JsonUtils;
import com.hj.platform.common.util.ReactiveSecurityHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Import;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Map;

/**
 * @author <a href="mailto:hjm0928@sina.cn">韩金明</a>
 */
@RestController
@Import(JsonUtils.class)
@Slf4j
public class TestController {

    @GetMapping(value = {"/", "/index"})
    public Mono<Map<String, Object>> index(){
        return ReactiveSecurityHolder.getUserInfo();
    }
}
