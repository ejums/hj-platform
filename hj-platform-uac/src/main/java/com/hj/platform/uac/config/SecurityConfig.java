package com.hj.platform.uac.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hj.platform.common.config.CommonAutoConfiguration;
import com.hj.platform.common.util.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.RedirectServerAuthenticationSuccessHandler;
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler;
import org.springframework.security.web.server.savedrequest.CookieServerRequestCache;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import reactor.core.publisher.Mono;

/**
 * @author <a href="mailto:hjm0928@sina.cn">韩金明</a>
 */
@EnableWebFluxSecurity
@Slf4j
public class SecurityConfig {

    private final StringRedisTemplate stringRedisTemplate;

    public SecurityConfig(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity httpSecurity) {
        httpSecurity
                .authorizeExchange()
                .pathMatchers("/favicon.ico", "/login", "/oauth2/**", "/logout", "/oauth/**")
                .permitAll()
                .anyExchange().authenticated()
                .and()
                .csrf().disable()
                .cors().disable()
                .oauth2Login().authenticationSuccessHandler(new ServerAuthenticationSuccessHandler() {
                    final RedirectServerAuthenticationSuccessHandler handler = new RedirectServerAuthenticationSuccessHandler();
                    @Override
                    public Mono<Void> onAuthenticationSuccess(WebFilterExchange filterExchange, Authentication authentication) {
                        handler.setRequestCache(new CookieServerRequestCache());
                        ServerWebExchange exchange = filterExchange.getExchange();
                        return exchange.getSession().map(session -> {
                            log.info("session Id:{}", session.getId());
                            String contextValue = JsonUtils.stringify(new SecurityContextImpl(authentication));
                            stringRedisTemplate.opsForValue().set(session.getId(), contextValue);
                            return Mono.just(session);
                        }).flatMap(v->handler.onAuthenticationSuccess(filterExchange, authentication));
                    }
                });
        return httpSecurity.build();
    }
}
