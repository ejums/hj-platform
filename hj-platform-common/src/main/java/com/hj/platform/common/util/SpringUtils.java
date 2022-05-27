package com.hj.platform.common.util;

import com.sun.nio.sctp.IllegalUnbindException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author <a href="mailto:hjm0928@sina.cn">韩金明</a>
 */
@Component
@Slf4j
public class SpringUtils implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    public SpringUtils() throws InstantiationException {
        synchronized (this){
            if (SpringUtils.applicationContext != null) {
                throw new InstantiationException("SpringUtils不为空时，不允许实例化该对象");
            }
        }

    }

    @Override
    @SuppressWarnings("java:S2696")
    public void setApplicationContext(ApplicationContext applicationContext) {
        if(SpringUtils.applicationContext != null){
            throw new IllegalUnbindException("参数SpringUtils.applicationContext不允许被重复赋值");
        }
        SpringUtils.applicationContext = applicationContext;
    }

    public static <T> T getBean(Class<T> clazz) {
        return applicationContext.getBean(clazz);
    }

    public static <T> Map<String, T> getBeanMap(Class<T> clazz) {
        return applicationContext.getBeansOfType(clazz);
    }
}
