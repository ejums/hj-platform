package com.hj.platform.common.util;

import com.hj.platform.common.contants.ExceptionConstants;

/**
 * String帮助
 * @author <a href="mailto:hjm0928@sina.cn">韩金明</a>
 */
public class StringHelper {
    private StringHelper() throws InstantiationException {
        throw ExceptionConstants.UTIL_CLASS_UN_INITIALIZED;
    }
    /**
     * 返回非空字符
     * @param src src str
     * @param otherwise return if src str is null
     * @return str:String
     */
    public static String nonOrElse(String src, String otherwise) {
        return src == null || src.isEmpty() || src.isBlank() ? otherwise : src;
    }

    public static String nonOrEmpty(String src){
        return src == null ? "" : src;
    }
}
