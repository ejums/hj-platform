package com.hj.platform.common.exception;

/**
 * @author <a href="mailto:hjm0928@sina.cn">韩金明</a>
 */
public class JsonSerializableException extends RuntimeException{
    public JsonSerializableException(String message, Throwable cause) {
        super(message, cause);
    }
}
