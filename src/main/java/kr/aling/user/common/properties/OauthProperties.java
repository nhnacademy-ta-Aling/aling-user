package kr.aling.user.common.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Oauth2 프로퍼티를 저장하는 Properties class.
 *
 * @author 이수정
 * @since 1.0
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "aling.oauth2")
public class OauthProperties {

    private String githubClientId;
    private String githubClientSecret;

    private String googleClientId;
    private String googleClientSecret;
    private String googleGrantType;
    private String googleRedirectUri;
}
