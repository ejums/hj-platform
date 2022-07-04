package com.hj.platform.common.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.hj.platform.common.contants.StringConstants;
import com.hj.platform.common.util.JsonUtils;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.io.Serializable;

/**
 * 消息返回类
 * @author <a href="mailto:hjm0928@sina.cn">韩金明</a>
 */
@Data
@JsonInclude(value = JsonInclude.Include.NON_NULL)
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
     * 响应消息
     */
    private String msg;

    @SuppressWarnings("java:S1948")
    private Object data;

    /**
     * 时间戳
     */
    private long timestamp;

    public HttpResult() {}

    public HttpResult(int statusCode, String errCode, String msg, long timestamp) {
        this.statusCode = statusCode;
        this.errCode = errCode;
        this.msg = msg;
        this.timestamp = timestamp;
    }

    public HttpResult(HttpStatus status){
        this.statusCode = status.value();
        this.errCode = String.valueOf(status.value());
        this.msg = status.toString();
        this.timestamp = System.currentTimeMillis();
    }

    public static HttpResult success() {
        return new HttpResult(200, "0", "success", System.currentTimeMillis());
    }

    public static HttpResult success(String msg) {
        return new HttpResult(200, "0", msg, System.currentTimeMillis());
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

    public static HttpResult errMsg(String errMsg){
        return new HttpResult(500, "500", errMsg, System.currentTimeMillis());
    }

    public static HttpResult data(Object data){
        HttpResult result = new HttpResult(200, "0", StringConstants.OK, System.currentTimeMillis());
        result.setData(data);
        return result;
    }

    public static HttpResult errMsg(String errCode, String errMsg){
        return new HttpResult(500, errCode, errMsg, System.currentTimeMillis());
    }

    @Override
    public String toString(){
        return JsonUtils.stringify(this);
    }

}
