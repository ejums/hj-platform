package com.hj.platform.domain.entity.sys;

import com.hj.platform.domain.entity.BaseModelEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户信息表
 * @author <a href="mailto:hjm0928@sina.cn">韩金明</a>
 * @apiNote sys_user用户实体表
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Table
public class SysUser extends BaseModelEntity implements Serializable {
    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String cypher;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 是否需要修改密码
     */
    private Boolean needChangePassword;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 过期时间
     */
    private LocalDateTime expiredTime;

    /**
     * 是否过期
     */
    @Column
    private Boolean isExpired;
}