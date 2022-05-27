package com.hj.platform.uac;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import lombok.Data;

import java.io.IOException;


public class StressTest {

    @Data
    static class C{
        private String value;
    }

    @Data
    static class B<T> {
        private Integer age;
        private T info;
    }

    @Data
    static class A<T, E> {
        private String name;

        private T t1;

        private E t2;
    }

    public static void main(String[] args) throws IOException {
        A<B<C>, C> a = new A<>();
        a.name = "aObject";
        a.t1 = new B<>();
        a.t2 = new C();
        a.t1.info = new C();
        a.t2.value = "32";
        a.t1.age = 3;
        a.t1.info.value = "33";
        ObjectMapper mapper = new ObjectMapper();
        String s = mapper.writeValueAsString(a);
        JavaType cType = TypeFactory.defaultInstance().constructType(C.class);
        JavaType bType = TypeFactory.defaultInstance().constructParametricType(B.class, C.class);
        JavaType aType = TypeFactory.defaultInstance().constructParametricType(A.class, bType, cType);
        A<B<C>,C> o = mapper.readValue(s, aType);
        System.out.println(s);
    }
}
