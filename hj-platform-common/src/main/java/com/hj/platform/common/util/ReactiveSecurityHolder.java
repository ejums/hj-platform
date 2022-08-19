package com.hj.platform.common.util;


import com.hj.platform.common.contants.StringConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.autoconfigure.web.reactive.WebFluxRegistrations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.http.HttpCookie;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.result.method.RequestMappingInfo;
import org.springframework.web.reactive.result.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebSession;
import org.springframework.web.server.adapter.WebHttpHandlerBuilder;
import org.springframework.web.server.session.DefaultWebSessionManager;
import org.springframework.web.server.session.InMemoryWebSessionStore;
import org.springframework.web.server.session.WebSessionIdResolver;
import org.springframework.web.server.session.WebSessionManager;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Map;

/**
 * @author <a href="mailto:hjm0928@sina.cn">韩金明</a>
 */
@Component
@Slf4j
public class ReactiveSecurityHolder {

    private static ReactiveStringRedisTemplate stringRedisTemplate;

    private static final ThreadLocal<ServerWebExchange> CONTEXT_HOLDER = new InheritableThreadLocal<>();

    public static void setExchange(ServerWebExchange obj) {
        log.debug("setExchange in thread {}", Thread.currentThread().getId());
        CONTEXT_HOLDER.set(obj);
    }

    public static ServerWebExchange getExchange(){
        log.debug("getExchange in thread {}", Thread.currentThread().getId());
        return CONTEXT_HOLDER.get();
    }

    public static ServerHttpRequest getRequest(){
        ServerWebExchange exchange = getExchange();
        if (exchange == null) {
            return null;
        }
        return exchange.getRequest();
    }

    public static ServerHttpResponse getResponse(){
        ServerWebExchange exchange = getExchange();
        if (exchange == null) {
            return null;
        }
        return exchange.getResponse();
    }

    public static String getToken(){
        ServerWebExchange exchange = getExchange();
        if (exchange == null) {
            return null;
        }
        ServerHttpRequest request = exchange.getRequest();
        HttpCookie cookie = request.getCookies().getFirst(StringConstants.SESSION);
        if (cookie != null) {
            return cookie.getValue();
        }
        return null;
    }

    public static Mono<Map<String, Object>> getUserInfo(){
        String token = getToken();
        if (token != null) {
            return stringRedisTemplate.opsForValue()
                    .get(token)
                    .map(JsonUtils::parseObject);
        }
        return Mono.empty();
    }

    public static void clear(){
        CONTEXT_HOLDER.remove();
    }

    @Autowired
    @SuppressWarnings("java:S2696")
    public void setStringRedisTemplate(ReactiveStringRedisTemplate redisTemplate){
        ReactiveSecurityHolder.stringRedisTemplate = redisTemplate;
    }


    @Bean
    @Order(1)
    public WebFluxRegistrations getWebFluxRegistrations(){
        return new WebFluxRegistrations() {
            @Override
            public RequestMappingHandlerMapping getRequestMappingHandlerMapping() {
                return requestMappingHandlerMapping();
            }
        };
    }


    public RequestMappingHandlerMapping requestMappingHandlerMapping(){
        return new RequestMappingHandlerMapping(){
            @Override
            protected RequestMappingInfo getMatchingMapping(RequestMappingInfo info,
                                                            ServerWebExchange exchange) {
                ReactiveSecurityHolder.setExchange(exchange);
                return super.getMatchingMapping(info, exchange);
            }
        };
    }
}
