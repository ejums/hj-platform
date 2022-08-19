package com.hj.platform.common.util;

import com.hj.platform.common.vo.HttpResult;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.server.reactive.ServerHttpResponse;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

/**
 * 响应处理类
 * @author <a href="mailto:hjm0928@sina.cn">韩金明</a>
 */
public class ResponseHelper {

    private ResponseHelper(){}

    public static Mono<Void> write(ServerHttpResponse response, byte[] bytes){
        DataBufferFactory bufferFactory = response.bufferFactory();
        DataBuffer wrap = bufferFactory.wrap(bytes);
        return response.writeWith(Mono.just(wrap));
    }

    public static Mono<Void> write(ServerHttpResponse response, HttpResult result){
        return write(response, result.toString());
    }

    public static Mono<Void> write(ServerHttpResponse response, String str){
        return write(response, str.getBytes(StandardCharsets.UTF_8));
    }

    public static Mono<Void> write(ServerHttpResponse response, Object object){
        return write(response, JsonUtils.toBytes(object));
    }
}
