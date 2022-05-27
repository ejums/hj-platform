package com.hj.platform.domain.entity;


import com.hj.platform.domain.annoucement.LocalDateTimeFormat;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

/**
 * 基础实体类
 * @author <a href="mailto:hjm0928@sina.cn">韩金明</a>
 */
@Data
public class BaseModelEntity {
    @Id
    protected Long id;

    /**
     * 创建时间
     */
    @LocalDateTimeFormat
    protected LocalDateTime createTime;

    /**
     * 更新时间
     */
    @LocalDateTimeFormat
    protected LocalDateTime updateTime;

}
