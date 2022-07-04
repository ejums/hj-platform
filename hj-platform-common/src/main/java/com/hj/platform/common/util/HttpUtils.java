package com.hj.platform.common.util;

import io.netty.handler.codec.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * @author <a href="mailto:hjm0928@sina.cn">韩金明</a>
 */
public class HttpUtils {

    protected HttpUtils() throws InstantiationException {}

    public static WebClient getWebClient(){
        return WebClient.builder().build();
    }

    public static <T> Mono<T> exchange(String url, HttpMethod httpMethod, HttpHeaders headers, Object body, Class<T> clazz){
        WebClient.RequestBodySpec spec = getWebClient()
                .method(httpMethod)
                .uri(url);
        if (headers != null){
            spec.headers(httpHeaders -> headers.forEach(entry -> httpHeaders.add(entry.getKey(), entry.getValue())));
        }
        if (body != null){
            spec.body(BodyInserters.fromValue(body));
        }
        return spec.retrieve()
                .bodyToMono(clazz);
    }

    public static <T> T exchangeObject(String url, HttpMethod httpMethod, HttpHeaders headers, Object body, Class<T> clazz){
        return exchange(url, httpMethod, headers, body, clazz).block();
    }

}
