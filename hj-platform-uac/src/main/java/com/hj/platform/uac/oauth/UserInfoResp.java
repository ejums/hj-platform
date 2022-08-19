package com.hj.platform.uac.oauth;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author <a href="mailto:hjm0928@sina.cn">韩金明</a>
 */
@Data
public class UserInfoResp {
    private Integer id;
    private String login;
    private String name;
    private String email;
    private String avatarUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
