package com.hj.platform.app;

import com.hj.platform.common.config.CommonAutoConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Slf4j
public class HjPlatformAppApplication {
    public static void main(String[] args) {
        SpringApplication.run(HjPlatformAppApplication.class, args);
    }
}
