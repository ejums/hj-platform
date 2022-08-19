package com.hj.platform.uac.oauth;

import com.hj.platform.common.contants.StringConstants;
import com.hj.platform.common.util.ReactiveSecurityHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.ResponseCookie;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.server.context.WebSessionServerSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebSession;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 *
 * @author <a href="mailto:hjm0928@sina.cn">韩金明</a>
 */
@Controller
@Slf4j
public class OauthController {

    private final OAuth2ClientProperties clientProperties;
    private final UserRepository userRepository;
    private final DatabaseClient databaseClient;

    public OauthController(OAuth2ClientProperties clientProperties, UserRepository userRepository, DatabaseClient databaseClient) {
        this.clientProperties = clientProperties;
        this.userRepository = userRepository;
        this.databaseClient = databaseClient;
    }

    @GetMapping("/static/{name}.json")
    public Mono<String> staticFile(@PathVariable String name){
        return Mono.just("static/" + name + ".json");
    }

    @GetMapping("/oauth/sql2")
    @ResponseBody
    public Flux<Map<String, Object>> asertTest(String name){
        log.info("sql name={}", name);
        return databaseClient.sql("select * from sys_user where username='" + name + "'").fetch().all();
    }

    @PostMapping("/oauth/token")
    @ResponseBody
    public Mono<OauthTokenResp> oauth2Token(ServerHttpRequest request){
        return Mono.from(request.getBody().map(buffer -> {
            byte[] bytes = new byte[buffer.readableByteCount()];
            buffer.read(bytes);
            DataBufferUtils.release(buffer);
            String strBody = new String(bytes, StandardCharsets.UTF_8);
            OauthTokenReq req = UrlUtils.urlParamConvert(strBody, OauthTokenReq.class);
            OauthTokenResp resp = new OauthTokenResp();
            resp.setAccessToken("726f09ba53905f827e86714b468b47fd");
            resp.setTokenType("bearer");
            resp.setExpiresIn(86400);
            resp.setRefreshToken("75f71dfb4816966fe894319cb7423d89261065370a665a702511fb70eb636b6c");
            resp.setScope("user_info emails");
            resp.setCreatedAt(1656925520L);
            return resp;
        }));
    }

    @GetMapping("/oauth/channel")
    @ResponseBody
    public Flux<Object> getLoginWay(){
        return Flux.fromIterable(clientProperties.getRegistration().entrySet())
                .map(entry -> Map.of("clientName", entry.getValue().getClientName(),
                        "clientKey", entry.getKey()));
    }

    @GetMapping(value = {"/js/{name}", "/css/{name}"})
    public Mono<String> staticMapping(@PathVariable String name){
        if(ReactiveSecurityHolder.getRequest().getPath().toString().startsWith("/js")) {
            return Mono.just("/static/js/" + name);
        } else {
            return Mono.just("/static/css/" + name);
        }
    }

    /**
     * 登录页跳转
     * @return 登录页页面
     */
    @GetMapping(value = {"/auth/login", "/login"})
    public Mono<String> loginPage(){
        return Mono.just("/login.html");
    }

    @PostMapping("/oauth/login")
    @ResponseBody
    public Mono<String> oauthLogin(@RequestBody OauthLoginReq req, ServerWebExchange exchange){
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(req.getUsername(),
                req.getPassword());
        SecurityContextImpl securityContext = new SecurityContextImpl();
        securityContext.setAuthentication(token);
        ReactiveSecurityContextHolder.withAuthentication(token);
        return Mono.just(exchange)
                .flatMap(ServerWebExchange::getSession)
                .doOnSuccess(session -> {
                    exchange.getResponse().addCookie(
                            ResponseCookie.fromClientResponse(StringConstants.SESSION, session.getId()).build());
                    String attrName = WebSessionServerSecurityContextRepository.DEFAULT_SPRING_SECURITY_CONTEXT_ATTR_NAME;
                    session.getAttributes().put(attrName, securityContext);
                })
                .map(WebSession::getId);
    }

    @RequestMapping("/oauth/user")
    @ResponseBody
    public Mono<UserInfoResp> getUserInfo(@RequestHeader String authorization){
        UserInfoResp resp = new UserInfoResp();
        resp.setId(3278787);
        resp.setLogin("login");
        resp.setEmail("hjm0928@sina.cn");
        resp.setName("name");
        resp.setAvatarUrl("https://gitee.com/assets/no_portrait.png");
        return Mono.just(resp);
    }
}
