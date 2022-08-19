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
}
