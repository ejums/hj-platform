package com.hj.platform.uac;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.hj.platform.*.*")
public class HjPlatformUacApplication {

    public static void main(String[] args) {
        SpringApplication.run(HjPlatformUacApplication.class, args);
    }
}
