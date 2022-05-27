package com.hj.platform.uac.auth.config;

import com.hj.platform.uac.auth.respository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author <a href="mailto:hjm0928@sina.cn">韩金明</a>
 */
@EnableWebFluxSecurity
@Slf4j
public class SecurityWebConfig {
    protected static final List<ServerWebExchangeMatcher> LOGIN_MATCH_LIST = new ArrayList<>(8);

    @Bean
    public SecurityWebFilterChain filterChain(ServerHttpSecurity httpSecurity,
                                              ReactiveAuthenticationManager authenticationManager){
        ServerWebExchangeMatcher formLoginMatcher = ServerWebExchangeMatchers
                .pathMatchers(HttpMethod.POST, "/login");
        ServerWebExchangeMatcher oauthMatcher = ServerWebExchangeMatchers
                .pathMatchers(HttpMethod.POST, "/oauth/login");
        LOGIN_MATCH_LIST.add(formLoginMatcher);
        LOGIN_MATCH_LIST.add(oauthMatcher);
        // token存储器定义
        httpSecurity.securityContextRepository(new PlatformSecurityContextRepository());
        // formLogin认证方式实现
        httpSecurity
                .formLogin()
                .requiresAuthenticationMatcher(formLoginMatcher)
                .authenticationManager(authenticationManager)
                .authenticationSuccessHandler(new PlatformAuthenticationSuccessHandler())
                .authenticationFailureHandler(new PlatformAuthenticationFailureHandler());
        // oauthLogin认证实现
//        httpSecurity
//                .oauth2Login()
//                .authenticationMatcher(oauthMatcher);
        // 其他登录方式禁用
        httpSecurity
                .httpBasic().disable()
                .csrf().disable()
                .logout().disable();
        // 统一配置
        httpSecurity
                .authorizeExchange()
                .matchers(LOGIN_MATCH_LIST.toArray(new ServerWebExchangeMatcher[0])).permitAll()
                .anyExchange().authenticated()
                .and()
                .exceptionHandling().accessDeniedHandler(new PlatformAccessDeniedHandler());

       return httpSecurity.build();
    }

    @Bean
    ReactiveUserDetailsService userDetailsService(@Lazy UserRepository userRepository){
        return username -> userRepository.getSysUserByUsername(username)
                .map(user -> new User(user.getUsername(), user.getCypher(), Collections.emptyList()));
    }

    @Bean
    ReactiveAuthenticationManager authenticationManager(ReactiveUserDetailsService userDetailsService,
                                                        PasswordEncoder passwordEncoder){
        return new PlatformAuthenticationManager(userDetailsService, passwordEncoder);
    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    ServerSecurityContextRepository contextRepository(){
        return new PlatformSecurityContextRepository();
    }

}
