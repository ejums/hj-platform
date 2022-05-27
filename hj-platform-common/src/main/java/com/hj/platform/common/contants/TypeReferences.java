package com.hj.platform.common.contants;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.type.TypeFactory;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author <a href="mailto:hjm0928@sina.cn">韩金明</a>
 */
public class TypeReferences {
    private TypeReferences(){}

    /**
     * Map<String, Object>转换type
     */
    public static final TypeReference<Map<String, Object>> STRING_OBJECT_MAP = new TypeReference<>() {};
    /**
     * Map<String, String>转换type
     */
    public static final TypeReference<Map<String, String>> STRING_STRING_MAP = new TypeReference<>() {};

    public static <T> Type getListType(Class<T> clazz){
        return TypeFactory.defaultInstance().constructCollectionType(List.class, clazz);
    }

    public static <T> Type getSetType(Class<T> clazz){
        return TypeFactory.defaultInstance().constructCollectionType(Set.class, clazz);
    }

    public static <T> Type monolayerType(Class<T> outClass, Class<?>... innerClasses){
        return TypeFactory.defaultInstance().constructParametricType(outClass, innerClasses);
    }
}
