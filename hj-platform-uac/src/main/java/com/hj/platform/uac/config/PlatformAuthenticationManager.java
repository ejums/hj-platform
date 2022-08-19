package com.hj.platform.uac.config;

import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import reactor.core.publisher.Mono;

import java.util.Collections;

/**
 * @author <a href="mailto:hjm0928@sina.cn">韩金明</a>
 */
public class PlatformAuthenticationManager implements ReactiveAuthenticationManager {
    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        return Mono.just(UsernamePasswordAuthenticationToken.authenticated(authentication.getPrincipal(),
                authentication.getCredentials(), Collections.emptyList()));
    }
}
