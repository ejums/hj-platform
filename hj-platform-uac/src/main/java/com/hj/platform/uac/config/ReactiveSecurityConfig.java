package com.hj.platform.uac.config;

import com.hj.platform.common.util.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.client.reactive.ClientHttpRequest;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.client.endpoint.WebClientReactiveAuthorizationCodeTokenResponseClient;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.RedirectServerAuthenticationSuccessHandler;
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler;
import org.springframework.security.web.server.savedrequest.CookieServerRequestCache;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.lang.reflect.Field;

/**
 * @author <a href="mailto:hjm0928@sina.cn">韩金明</a>
 */
@ConditionalOnMissingClass(value = "org.springframework.web.servlet.config.MvcNamespaceHandler")
@EnableWebFluxSecurity
@Slf4j
public class ReactiveSecurityConfig {

    private final StringRedisTemplate stringRedisTemplate;

    public ReactiveSecurityConfig(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity httpSecurity) {
        httpSecurity
                .authorizeExchange()
                .pathMatchers("/favicon.ico", "/login", "/js/**", "/css/**", "/oauth2/**", "/logout", "/oauth/**")
                .permitAll()
                .anyExchange().authenticated()
                .and()
                .csrf().disable()
                .cors().disable()
                .formLogin().loginPage("/auth/login")
                .authenticationManager(new PlatformAuthenticationManager())
                .and()
                .oauth2Login()
                .authenticationSuccessHandler(new ServerAuthenticationSuccessHandler() {
                    final RedirectServerAuthenticationSuccessHandler handler = new RedirectServerAuthenticationSuccessHandler();
                    @Override
                    public Mono<Void> onAuthenticationSuccess(WebFilterExchange filterExchange, Authentication authentication) {
                        handler.setRequestCache(new CookieServerRequestCache());
                        ServerWebExchange exchange = filterExchange.getExchange();
                        return exchange.getSession().checkpoint().map(session -> {
                            log.info("session Id:{}", session.getId());
                            String contextValue = JsonUtils.stringify(new SecurityContextImpl(authentication));
                            stringRedisTemplate.opsForValue().set(session.getId(), contextValue);
                            return Mono.just(session);
                        }).flatMap(v -> handler.onAuthenticationSuccess(filterExchange, authentication));
                    }
                });
        return httpSecurity.build();
    }

    @Bean
    public WebClientReactiveAuthorizationCodeTokenResponseClient responseClient(){
        return new WebClientReactiveAuthorizationCodeTokenResponseClient(){
            @Override
            public Mono<OAuth2AccessTokenResponse> getTokenResponse(OAuth2AuthorizationCodeGrantRequest grantRequest) {
                setWebClient(webClient());
                return super.getTokenResponse(grantRequest);
            }
        };
    }

    @Bean
    public WebClient webClient(){
        return WebClient.builder().filter((request, next) -> {
            String prefix = request.logPrefix();
            log.info("{}请求地址为：{} {}", prefix, request.method(), request.url());
            log.info("{}请求Header为：{}", prefix, JsonUtils.stringify(request.headers()));
            String body = parserBody(request.body());
            if (body != null){
                log.info("{}请求体:{}", prefix, body);
            }
            return next.exchange(request)
                    .flatMap(clientResponse -> clientResponse.bodyToMono(String.class)
                            .map(resp -> {
                                log.info("{}请求响应为[Time1](Map)：{}", prefix, resp);
                                return ClientResponse
                                        .create(clientResponse.statusCode())
                                        .body(resp)
                                        .headers(httpHeaders ->
                                                httpHeaders.addAll(clientResponse.headers().asHttpHeaders()))
                                        .cookies(cookie -> cookie.addAll(clientResponse.cookies()))
                                        .statusCode(clientResponse.statusCode())
                                        .build();
                            })
                    )
                    .flatMap(clientResponse -> clientResponse.bodyToMono(String.class)
                            .map(resp -> {
                                log.info("{}请求响应为[Time2](Map)：{}", prefix, resp);
                                return clientResponse;
                            })
                    )
                    .flatMap(clientResponse -> clientResponse.bodyToMono(String.class)
                            .map(resp -> {
                                log.info("{}请求响应为[Time3](Map)：{}", prefix, resp);
                                return clientResponse;
                            })
                    )
                    ;
        }).build();
    }

    @SuppressWarnings(value = {"java:S3011"})
    public String parserBody(BodyInserter<?, ? super ClientHttpRequest> bodyInserter){
        //noinspection rawtypes
        Class<? extends BodyInserter> clazz = bodyInserter.getClass();
        Field data;
        try {
            data = clazz.getDeclaredField("data");
            data.setAccessible(true);
            Object result = data.get(bodyInserter);
            data.setAccessible(false);
            return JsonUtils.stringify(result);
        } catch (Exception e) {
            // ignore error
        }
        return null;
    }
}
