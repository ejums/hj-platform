package com.hj.platform.uac.oauth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author <a href="mailto:hjm0928@sina.cn">韩金明</a>
 */
@Data
public class OauthTokenResp {
    @JsonProperty(value = "access_token")
    private String accessToken;

    @JsonProperty(value = "token_type")
    private String tokenType;

    @JsonProperty(value = "expires_in")
    private Integer expiresIn;

    @JsonProperty(value = "refresh_token")
    private String refreshToken;

    @JsonProperty(value = "scope")
    private String scope;

    @JsonProperty(value = "created_at")
    private Long createdAt;
}
