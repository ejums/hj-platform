package com.hj.platform.uac.oauth;

import lombok.Data;

/**
 * 登录请求接收器
 * @author <a href="mailto:hjm0928@sina.cn">韩金明</a>
 */
@Data
public class OauthLoginReq {
    /**
     * 用户名
     */
    private String username;
    /**
     * 密码
     */
    private String password;
}
