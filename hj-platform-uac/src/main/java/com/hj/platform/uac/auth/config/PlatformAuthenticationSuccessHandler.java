package com.hj.platform.uac.auth.config;

import com.hj.platform.common.contants.StringConstants;
import com.hj.platform.common.util.JsonUtils;
import com.hj.platform.common.util.Md5Utils;
import com.hj.platform.common.util.ResponseHelper;
import com.hj.platform.common.vo.HttpResult;
import org.springframework.http.ResponseCookie;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler;
import reactor.core.publisher.Mono;

import java.util.Map;

/**
 * @author <a href="mailto:hjm0928@sina.cn">韩金明</a>
 */
public class PlatformAuthenticationSuccessHandler implements ServerAuthenticationSuccessHandler {

    @Override
    public Mono<Void> onAuthenticationSuccess(WebFilterExchange webFilterExchange, Authentication authentication) {
        String str = authentication.getPrincipal().toString() + System.currentTimeMillis();
        String token = Md5Utils.md5(str);
        webFilterExchange.getExchange().getResponse().getCookies()
                .add(StringConstants.TOKEN, ResponseCookie.from(StringConstants.TOKEN, token).path("/").httpOnly(true).build());
        Map<String, Object> map = Map.of(StringConstants.TOKEN, token, "status", 0);
        ServerHttpResponse response = webFilterExchange.getExchange().getResponse();
        return ResponseHelper.write(response, map);
    }
}
