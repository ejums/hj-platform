package com.hj.platform.uac.auth.config;

import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import reactor.core.publisher.Mono;

import java.util.Collections;

/**
 * @author <a href="mailto:hjm0928@sina.cn">韩金明</a>
 */
public class PlatformAuthenticationManager implements ReactiveAuthenticationManager {
    private final ReactiveUserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    public PlatformAuthenticationManager(ReactiveUserDetailsService userDetailsService,
                                         PasswordEncoder passwordEncoder) {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        Object principal = authentication.getPrincipal();
        Object credentials = authentication.getCredentials();
        if (principal == null){
            throw new AuthenticationServiceException("username is not assign");
        }
        Mono<UserDetails> userDetail = userDetailsService.findByUsername(principal.toString());
        return userDetail
                .switchIfEmpty(Mono.error(new AuthenticationServiceException("user not exists")))
                .filter(userDetails -> passwordEncoder.matches(credentials.toString(), userDetails.getPassword()))
                .switchIfEmpty(Mono.error(new AuthenticationServiceException("username or password not matchong")))
                .map(userDetails -> UsernamePasswordAuthenticationToken.authenticated(userDetails.getUsername(),
                        userDetails.getPassword(), Collections.emptyList()));
    }
}
