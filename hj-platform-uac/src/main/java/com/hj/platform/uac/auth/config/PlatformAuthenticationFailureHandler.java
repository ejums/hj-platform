package com.hj.platform.uac.auth.config;

import com.hj.platform.common.util.JsonUtils;
import org.springframework.http.HttpCookie;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.ServerAuthenticationFailureHandler;
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler;
import reactor.core.publisher.Mono;

import java.util.Map;

/**
 * @author <a href="mailto:hjm0928@sina.cn">韩金明</a>
 */
public class PlatformAuthenticationFailureHandler implements ServerAuthenticationFailureHandler {
    @Override
    public Mono<Void> onAuthenticationFailure(WebFilterExchange webFilterExchange,
                                              AuthenticationException exception) {
        Map<String, Object> map = Map.of("status", 401, "msg", exception.getLocalizedMessage());
        ServerHttpResponse response = webFilterExchange.getExchange().getResponse();
        return response.writeWith(Mono.just(response.bufferFactory().wrap(JsonUtils.toBytes(map))));
    }
}
