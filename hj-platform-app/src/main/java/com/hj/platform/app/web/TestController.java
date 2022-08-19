package com.hj.platform.app.web;

import com.hj.platform.common.util.JsonUtils;
import com.hj.platform.common.util.ReactiveSecurityHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.Resource;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.http.ZeroCopyHttpOutputMessage;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.*;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Optional;

/**
 * @author <a href="mailto:hjm0928@sina.cn">韩金明</a>
 */
@RestController
@Import(JsonUtils.class)
@Slf4j
public class TestController {

    @GetMapping(value = {"/", "/index"})
    public Mono<Map<String, Object>> index(String token){

        return ReactiveSecurityHolder.getUserInfo();
    }

    @PostMapping("/file/upload")
    public Flux<String> fileUpload(Flux<FilePart> file, ServerHttpRequest httpRequest){
        return  file.map(filePart -> filePart.filename());
    }

    @GetMapping(value = {"/file/{type:download|view}"})
    public Mono<Void> downloadFile(ServerHttpResponse response, @PathVariable String type){
        ZeroCopyHttpOutputMessage httpOutputMessage = (ZeroCopyHttpOutputMessage) response;
        String fileName = "C:\\Users\\hjm09\\Desktop\\s1.docx";
        File file = new File(fileName);
        String viewType = "attachment; ";
        MediaType mediaType = MediaType.APPLICATION_OCTET_STREAM;
        if ("view".equals(type)) {
           viewType = "inline; ";
           mediaType = MediaTypeFactory.getMediaType(file.getName()).orElse(mediaType);
        }
        httpOutputMessage.getHeaders().setContentType(mediaType);
        httpOutputMessage.getHeaders().add(HttpHeaders.CONTENT_DISPOSITION,
                viewType + "filename="+ URLEncoder.encode(file.getName(), StandardCharsets.UTF_8));
        return httpOutputMessage.writeWith(file, 0, file.length());
    }
}
