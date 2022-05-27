

package com.hj.platform.common.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.hj.platform.common.contants.TypeReferences;
import io.netty.buffer.PooledByteBufAllocator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.core.io.buffer.NettyDataBufferFactory;
import reactor.core.publisher.Flux;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class JsonUtilsTest {

    private final Map<String, String> map = Map.of("k1", "v1", "k2", "v2");
    private final String str = "{\"k1\":\"v1\",\"k2\":\"v2\"}";

    @BeforeEach
    void setUp() throws Exception {
        ObjectMapper objectMapper = JsonMapper.builder()
                .configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true)
                .build();
        JsonUtils.bindObjectMapper(objectMapper);
    }

    @Test
    void testSetObjectMapper() throws Exception {
        String actualResult = JsonUtils.stringify(map);
        assertThat(actualResult).isEqualTo(str);
    }

    @Test
    void testStringify() throws Exception {
        assertThat(JsonUtils.stringify(map)).isEqualTo(str);
    }

    @Test
    void testParseObject1() throws Exception {
        // Setup
        NettyDataBufferFactory factory = new NettyDataBufferFactory(PooledByteBufAllocator.DEFAULT);
        DataBuffer dataBuffer = factory.wrap(str.getBytes());
        final Flux<DataBuffer> dataBufferFlux = Flux.just(dataBuffer);

        // Run the test
        final Map<String, String> actualResult = JsonUtils.parseObject(dataBufferFlux, TypeReferences.STRING_STRING_MAP);

        // Verify the results
        assertThat(actualResult).isEqualTo(map);
    }
}

