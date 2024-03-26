package kr.aling.user.user.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Github AccessToken 요청 후 받아오는 Dto.
 *
 * @author 이수정
 * @since 1.0
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class OauthGithubAccessTokenResponseDto {

    @JsonProperty(value = "access_token")
    private String accessToken;
    private String scope;
    @JsonProperty(value = "token_type")
    private String tokenType;
}
