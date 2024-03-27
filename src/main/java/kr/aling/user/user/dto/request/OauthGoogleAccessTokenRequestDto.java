package kr.aling.user.user.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Google AccessToken 요청 내용을 담는 Dto.
 *
 * @author 이수정
 * @since 1.0
 */
@Getter
@AllArgsConstructor
public class OauthGoogleAccessTokenRequestDto {

    @JsonProperty(value = "client_id")
    private final String clientId;
    @JsonProperty(value = "client_secret")
    private final String clientSecret;
    private final String code;
    @JsonProperty(value = "grant_type")
    private final String grantType;
    @JsonProperty(value = "redirect_uri")
    private final String redirectUri;
}
