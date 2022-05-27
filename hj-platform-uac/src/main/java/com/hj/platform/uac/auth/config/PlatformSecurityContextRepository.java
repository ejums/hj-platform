package com.hj.platform.uac.auth.config;

import com.hj.platform.common.contants.StringConstants;
import com.hj.platform.common.util.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author <a href="mailto:hjm0928@sina.cn">韩金明</a>
 */
@Component
@Slf4j
public class PlatformSecurityContextRepository implements ServerSecurityContextRepository {

    private static final Map<String, SecurityContext> CONTEXT_MAP = new ConcurrentHashMap<>();

    @Override
    public Mono<Void> save(ServerWebExchange exchange, SecurityContext context) {
        return parserKey(exchange)
                .flatMap(key -> Mono.justOrEmpty(CONTEXT_MAP.put(key, context)))
                .then(Mono.empty());
    }

    @Override
    public Mono<SecurityContext> load(ServerWebExchange exchange) {
        return parserKey(exchange).flatMap(key -> Mono.justOrEmpty(CONTEXT_MAP.get(key)));
    }

    private Mono<String> parserKey(ServerWebExchange exchange){
        return Mono.from(Flux.fromIterable(SecurityWebConfig.LOGIN_MATCH_LIST)
                .flatMap(matcher -> matcher.matches(exchange))
                .filter(ServerWebExchangeMatcher.MatchResult::isMatch)
                .flatMap(matchResult -> loadResponseToken(exchange))
                .switchIfEmpty(loadRequestToken(exchange)));
    }

    private Mono<String> loadResponseToken(ServerWebExchange exchange) {
        return Mono.justOrEmpty(exchange.getResponse().getCookies().getFirst(StringConstants.TOKEN))
                .filter(Objects::nonNull)
                .map(HttpCookie::getValue)
                .switchIfEmpty(Mono.empty());
    }

    private Mono<String> loadRequestToken(@NonNull ServerWebExchange exchange){
        return Mono
                .justOrEmpty(exchange.getRequest().getCookies().getFirst(StringConstants.TOKEN))
                .filter(Objects::nonNull)
                .flatMap(httpCookie -> Mono.just(httpCookie.getValue()))
                .switchIfEmpty(loadHeader(exchange.getRequest().getHeaders()))
                .filter(key -> key != null && CONTEXT_MAP.containsKey(key))
                .switchIfEmpty(authenticationFailed(exchange).then(Mono.empty()))
                .flatMap(Mono::just);
    }

    private Mono<Void> authenticationFailed(ServerWebExchange exchange) {
        ServerHttpResponse response = exchange.getResponse();
        Map<String, Object> map = Map.of("status", 401, "msg", "Not logged in");
        log.info(JsonUtils.stringify(map));
        return response.writeWith(Mono.just(response.bufferFactory().wrap(JsonUtils.toBytes(map))));
    }

    private Mono<String> loadHeader(HttpHeaders headers) {
        return Mono.justOrEmpty(headers.getFirst("Authorization"))
                .map(s -> s.split(" "))
                .filter(arr -> arr.length > 1)
                .map(arr -> arr[1]);
    }
}
