package com.hj.platform.uac.oauth;

import com.hj.platform.common.util.StringHelper;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.util.StringUtils;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author <a href="mailto:hjm0928@sina.cn">韩金明</a>
 */
public class UrlUtils {
    public static Map<String, Object> urlParamEncoded(String params){
        if (!StringUtils.hasText(params)) {
            return Collections.emptyMap();
        }
        Map<String, Object> map = new HashMap<>();
        String[] paramSplitArr = params.split("&");
        for (String paramSplit : paramSplitArr) {
            if(StringUtils.hasText(paramSplit)){
                String[] paramKvSplit = paramSplit.split("=");
                if(paramKvSplit.length == 2 && StringUtils.hasText(paramKvSplit[0])){
                    String key = paramKvSplit[0];
                    if(paramKvSplit[1].contains(",")){
                        String[] paramValueSplit = paramKvSplit[1].split(",");
                        List<String> valueList = Stream.of(paramValueSplit)
                                .map(value -> URLDecoder.decode(value, StandardCharsets.UTF_8))
                                .toList();
                        map.put(key, valueList);
                    } else {
                        map.put(key, URLDecoder.decode(paramKvSplit[1], StandardCharsets.UTF_8));
                    }
                }
            }
        }
        return map;
    }

    public static <T> T urlParamConvert(String params, Class<T> clazz){
        Map<String, Object> map = urlParamEncoded(params);
        T instance;
        try {
            instance = clazz.getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException
                 | NoSuchMethodException e) {
            throw new BeanInitializationException("初始化失败", e);
        }
        // method group
        Map<String, List<Class<?>>> methodGroup = Arrays.stream(clazz.getMethods())
                .filter(v -> v.getName().startsWith("set"))
                .filter(v -> v.getParameterTypes().length == 1)
                .collect(Collectors.groupingBy(Method::getName,
                        Collectors.mapping(v -> v.getParameterTypes()[0], Collectors.toList())));
        try {
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                if(entry.getValue() != null) {
                    Class<?> valueClazz = entry.getValue().getClass();
                    String methodName = StringHelper.lineToHump("set_" + entry.getKey());
                    List<Class<?>> paramTypeClazzList = methodGroup.get(methodName);
                    for (Class<?> paramTypeClazz : paramTypeClazzList) {
                        // base
                        if (valueClazz.isAssignableFrom(paramTypeClazz)){
                            Method method = clazz.getMethod(methodName, paramTypeClazz);
                            method.invoke(instance, entry.getValue());
                        }
                        // arr
                        Object objArr = Array.newInstance(valueClazz, 1);
                        if (objArr.getClass().isAssignableFrom(paramTypeClazz)) {
                            Method method = clazz.getMethod(methodName, objArr.getClass());
                            method.invoke(instance, entry.getValue());
                        }
                        // list
                        if (Collection.class.isAssignableFrom(paramTypeClazz)) {
                            Method method = clazz.getMethod(methodName, paramTypeClazz);
                            Method addMethod = paramTypeClazz.getMethod("of", Object.class);
                            boolean accessible = addMethod.trySetAccessible();
                            addMethod.setAccessible(true);
                            Object obj = addMethod.invoke(null, entry.getValue());
                            addMethod.setAccessible(accessible);
                            method.invoke(instance, obj);
                        }
                    }
                }
            }
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        return instance;
    }
}
