package com.hj.platform.uac.oauth;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author <a href="mailto:hjm0928@sina.cn">韩金明</a>
 */
@Data
public class OauthTokenReq {
    @JsonAlias("grant_type")
    private List<String> grantType;

    @JsonAlias("client_id")
    private List<String> clientId;

    @JsonAlias("client_secret")
    private List<String> clientSecret;

    private List<String> code;

    @JsonAlias("redirect_uri")
    private List<String> redirectUri;
}
