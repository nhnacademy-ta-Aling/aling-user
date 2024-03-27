package kr.aling.user.user.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Github AccessToken 요청 내용을 담는 Dto.
 *
 * @author 이수정
 * @since 1.0
 */
@Getter
@AllArgsConstructor
public class OauthGithubAccessTokenRequestDto {

    @JsonProperty(value = "client_id")
    private final String clientId;
    @JsonProperty(value = "client_secret")
    private final String clientSecret;
    private final String code;
}
