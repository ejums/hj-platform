package com.hj.platform.common.vo;

import com.hj.platform.common.util.JsonUtils;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.io.Serializable;

/**
 * 消息返回类
 * @author <a href="mailto:hjm0928@sina.cn">韩金明</a>
 */
@Data
public class HttpResult implements Serializable {
    /**
     * http状态码
     */
    private int statusCode;
    /**
     * 错误状态码
     */
    private String errCode;

    /**
     * 错误消息
     */
    private String errMsg;

    /**
     * 时间戳
     */
    private long timestamp;

    public HttpResult() {}

    public HttpResult(int statusCode, String errCode, String errMsg, long timestamp) {
        this.statusCode = statusCode;
        this.errCode = errCode;
        this.errMsg = errMsg;
        this.timestamp = timestamp;
    }

    public HttpResult(HttpStatus status){
        this.statusCode = status.value();
        this.errCode = String.valueOf(status.value());
        this.errMsg = status.toString();
        this.timestamp = System.currentTimeMillis();
    }

    public static HttpResult unauthorized(){
        return new HttpResult(HttpStatus.UNAUTHORIZED);
    }

    public static HttpResult noCredential(){
        return new HttpResult(HttpStatus.UNAUTHORIZED.value(), String.valueOf(HttpStatus.UNAUTHORIZED.value()),
                "credentials not found", System.currentTimeMillis());
    }

    public static HttpResult errorCredential(){
        return new HttpResult(HttpStatus.FORBIDDEN.value(), String.valueOf(HttpStatus.FORBIDDEN.value()),
                "error credentials", System.currentTimeMillis());
    }

    public static HttpResult forbidden(){
        return new HttpResult(HttpStatus.FORBIDDEN);
    }

    public static HttpResult notFound() {
        return new HttpResult(HttpStatus.NOT_FOUND);
    }

    public static HttpResult badRequest(){
        return new HttpResult(HttpStatus.BAD_REQUEST);
    }

    @Override
    public String toString(){
        return JsonUtils.stringify(this);
    }

}
