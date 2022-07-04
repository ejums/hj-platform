package com.hj.platform.common.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author <a href="mailto:hjm0928@sina.cn">韩金明</a>
 */
@Data
public class UserInfo {
    /**
     * 用户Id
     */
    private String id;
    /**
     * 用户名
     */
    private String username;
    /**
     * 昵称
     */
    private String nickname;
    /**
     * 用户来源
     */
    private String userSource;
    /**
     * 头像地址
     */
    private String avatarUrl;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 电话号码
     */
    private String phone;
}
