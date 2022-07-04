package com.hj.platform.common.util;

import com.hj.platform.common.vo.UserInfo;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * @author <a href="mailto:hjm0928@sina.cn">韩金明</a>
 */
@Component
public class UserUtils {

    protected UserUtils() throws InstantiationException {}

    @SuppressWarnings("java:S1192")
    public static Mono<UserInfo> currentUser(){
        return readUser().mapNotNull(UserUtils::parserUser);
    }

    private static UserInfo parserUser(Map<String, Object> map) {
        Object registrationId = map.get("authorizedClientRegistrationId");
        if(registrationId != null) {
            String regId = (String) registrationId;
            return switch (regId) {
                case "gitee" -> parserGiteeUser(map);
                case "github" -> parserGithubUser(map);
                default -> null;
            };
        }
        return null;
    }

    private static UserInfo parserGithubUser(Map<String, Object> map) {
        UserInfo userInfo = new UserInfo();
        userInfo.setId((String) map.get("id"));
        userInfo.setUsername((String) map.get("login"));
        userInfo.setNickname(userInfo.getUsername());
        userInfo.setAvatarUrl((String) map.get("avatar_url"));
        userInfo.setEmail((String) map.get("email"));
        userInfo.setUserSource("github");
        userInfo.setCreateTime(LocalDateTime.parse((String) map.get("created_at")));
        return userInfo;
    }

    private static UserInfo parserGiteeUser(Map<String, Object> map) {
        UserInfo userInfo = new UserInfo();
        userInfo.setId((String) map.get("id"));
        userInfo.setUsername((String) map.get("login"));
        userInfo.setNickname((String) map.get("name"));
        userInfo.setAvatarUrl((String) map.get("avatar_url"));
        userInfo.setEmail((String) map.get("email"));
        userInfo.setUserSource("gitee");
        userInfo.setCreateTime(LocalDateTime.parse((String) map.get("created_at")));
        return userInfo;
    }


    public static Mono<Map<String, Object>> readUser(){
        return ReactiveSecurityHolder.getUserInfo();
    }
}
