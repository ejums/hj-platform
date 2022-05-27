package com.hj.platform.uac.auth.config;

import com.hj.platform.common.util.JsonUtils;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.server.authorization.ServerAccessDeniedHandler;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Map;

/**
 * @author <a href="mailto:hjm0928@sina.cn">韩金明</a>
 */
public class PlatformAccessDeniedHandler implements ServerAccessDeniedHandler {
    @Override
    public Mono<Void> handle(ServerWebExchange exchange, AccessDeniedException denied) {
        Map<String, Object> map = Map.of("status", 403, "msg", denied.getLocalizedMessage());
        ServerHttpResponse response = exchange.getResponse();
        return response.writeWith(Mono.just(response.bufferFactory().wrap(JsonUtils.toBytes(map))));
    }
}
