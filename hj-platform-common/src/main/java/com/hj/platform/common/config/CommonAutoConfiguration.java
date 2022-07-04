package com.hj.platform.common.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.ComponentScan;

/**
 * common 自动加载器
 * @author <a href="mailto:hjm0928@sina.cn">韩金明</a>
 */

@ComponentScan(basePackages = {"com.hj.platform.common.config", "com.hj.platform.common.util"})
@ConfigurationPropertiesScan(basePackages = "com.hj.platform.common.properties")
@Slf4j
public class CommonAutoConfiguration{

    public CommonAutoConfiguration(){
        log.info("已加载hj-common相关包.....");
    }
}
