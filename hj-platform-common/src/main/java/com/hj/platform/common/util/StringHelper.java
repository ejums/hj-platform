package com.hj.platform.common.util;

import com.hj.platform.common.contants.ExceptionConstants;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * String帮助
 * @author <a href="mailto:hjm0928@sina.cn">韩金明</a>
 */
public class StringHelper {
    private static final Pattern LINE_PATTERN = Pattern.compile("_(\\w)");
    private static final Pattern HUMP_PATTERN = Pattern.compile("[A-Z]");

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

    /**
     * 驼峰转下划线,最后转为大写
     * @param str str
     * @return linedStr
     */
    public static String humpToLine(String str) {
        Matcher matcher = HUMP_PATTERN.matcher(str);
        StringBuilder sb = new StringBuilder();
        while (matcher.find()) {
            matcher.appendReplacement(sb, "_" + matcher.group(0).toUpperCase());
        }
        matcher.appendTail(sb);
        return sb.toString().toUpperCase();
    }

    /**
     * 下划线转驼峰,正常输出
     * @param str str
     * @return humpedStr
     */
    public static String lineToHump(String str) {
        Matcher matcher = LINE_PATTERN.matcher(str);
        StringBuilder sb = new StringBuilder();
        while (matcher.find()) {
            matcher.appendReplacement(sb, matcher.group(1).toUpperCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    public static String firstUpCase(String str){
        char c = str.charAt(0);
        return String.valueOf(c).toUpperCase() + str.substring(1);
    }
}
