package com.hj.platform.common.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import com.hj.platform.common.contants.TypeReferences;
import com.hj.platform.common.exception.JsonSerializableException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author <a href="mailto:hjm0928@sina.cn">韩金</a>
 */
@Component
public class JsonUtils{
    private JsonUtils() throws InstantiationException {
        synchronized (this){
            if(JsonUtils.objectMapper != null) {
                throw new InstantiationException("JsonUtils.objectMapper不为空时，不允许被实例化");
            }
        }
    }

    private static ObjectMapper objectMapper;

    @Bean
    @SuppressWarnings("java:S2696")
    ObjectMapper objectMapper(){
        JsonUtils.objectMapper = new JsonMapper();
        JavaTimeModule timeModule = new JavaTimeModule();
        timeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateUtils.FORMAT_DATE_TIME));
        timeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateUtils.FORMAT_DATE_TIME));
        timeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(DateUtils.FORMAT_DATE));
        timeModule.addSerializer(LocalDate.class, new LocalDateSerializer(DateUtils.FORMAT_DATE));
        timeModule.addDeserializer(LocalTime.class, new LocalTimeDeserializer(DateUtils.FORMAT_TIME));
        timeModule.addSerializer(LocalTime.class, new LocalTimeSerializer(DateUtils.FORMAT_TIME));
        objectMapper.registerModule(timeModule);
        return objectMapper;
    }

    public static <T> T parseObject(Flux<DataBuffer> dataBufferFlux, TypeReference<T> reference){
        AtomicReference<String> atomicStrReference = new AtomicReference<>();
        dataBufferFlux.subscribe(dataBuffer -> {
            CharBuffer charBuffer = StandardCharsets.UTF_8.decode(dataBuffer.asByteBuffer());
            atomicStrReference.set(String.valueOf(charBuffer.array()));
            DataBufferUtils.release(dataBuffer);
        });
        dataBufferFlux.subscribe();
        return parseObject(atomicStrReference.get().getBytes(), reference);
    }

    public static Map<String, Object> parseObject(byte[] bytes){
        return parseObject(bytes, TypeReferences.STRING_OBJECT_MAP);
    }

    public static <T> T parseObject(byte[] bytes, JavaType type){
        try {
            return objectMapper.readValue(bytes, type);
        } catch (IOException e) {
            throw new JsonSerializableException("json反序列化失败", e);
        }
    }

    public static <T> T parseObject(byte[] bytes, TypeReference<T> reference){
        try {
            return objectMapper.readValue(bytes, reference);
        } catch (IOException e) {
            throw new JsonSerializableException("json反序列化失败", e);
        }
    }


    public static String stringify(Object obj){
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new JsonSerializableException("json", e);
        }
    }

    public static byte[] toBytes(Object obj){
        try {
            return objectMapper.writeValueAsBytes(obj);
        } catch (JsonProcessingException e) {
            throw new JsonSerializableException("json", e);
        }
    }
}
