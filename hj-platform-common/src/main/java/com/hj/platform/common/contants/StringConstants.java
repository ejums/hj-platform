package com.hj.platform.common.contants;

/**
 * @author <a href="mailto:hjm0928@sina.cn">韩金明</a>
 */
public class StringConstants {

    private StringConstants() throws InstantiationException {
        throw ExceptionConstants.UTIL_CLASS_UN_INITIALIZED;
    }

    public static final String SESSION = "SESSION";
    public static final String TOKEN = "token";
    public static final String USERNAME = "username";
    public static final String BEARER = "bearer";
    public static final String OK = "ok";
}
