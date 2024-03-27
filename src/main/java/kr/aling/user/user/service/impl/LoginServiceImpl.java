package kr.aling.user.user.service.impl;

import java.util.Objects;
import kr.aling.user.common.annotation.ReadService;
import kr.aling.user.common.properties.OauthProperties;
import kr.aling.user.user.dto.request.LoginRequestDto;
import kr.aling.user.user.dto.request.OauthGithubAccessTokenRequestDto;
import kr.aling.user.user.dto.request.OauthGoogleAccessTokenRequestDto;
import kr.aling.user.user.dto.response.LoginInfoResponseDto;
import kr.aling.user.user.dto.response.LoginResponseDto;
import kr.aling.user.user.dto.response.OauthGithubAccessTokenResponseDto;
import kr.aling.user.user.dto.response.OauthGithubUserInfoResponseDto;
import kr.aling.user.user.dto.response.OauthGoogleAccessTokenResponseDto;
import kr.aling.user.user.dto.response.OauthGoogleUserInfoResponseDto;
import kr.aling.user.user.entity.AlingUser;
import kr.aling.user.user.exception.SocialAccessTokenNotExistsException;
import kr.aling.user.user.exception.SocialEmailNotFoundException;
import kr.aling.user.user.exception.UserNotFoundException;
import kr.aling.user.user.repository.UserReadRepository;
import kr.aling.user.user.service.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;

/**
 * 로그인 Service 구현체.
 *
 * @author 이수정
 * @since 1.0
 */
@ReadService
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {

    private static final String GITHUB_ACCESS_TOKEN_URL = "https://github.com/login/oauth/access_token";
    private static final String GITHUB_USER_INFO_URL = "https://api.github.com/user";
    private static final String GOOGLE_ACCESS_TOKEN_URL = "https://oauth2.googleapis.com/token";
    private static final String GOOGLE_USER_INFO_URL = "https://oauth2.googleapis.com/tokeninfo?id_token=";

    private final UserReadRepository userReadRepository;
    private final PasswordEncoder passwordEncoder;

    private final RestTemplate restTemplate;
    private final OauthProperties oauthProperties;

    /**
     * {@inheritDoc}
     */
    @Override
    public LoginResponseDto login(LoginRequestDto loginRequestDto) {
        LoginInfoResponseDto response = userReadRepository.findByEmailForLogin(loginRequestDto.getEmail())
                .orElseThrow(UserNotFoundException::new);

        if (passwordEncoder.matches(loginRequestDto.getPassword(), response.getPassword())) {
            return new LoginResponseDto(response.getUserNo(),
                    userReadRepository.findRolesByUserNo(response.getUserNo()));
        }

        throw new UserNotFoundException();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LoginResponseDto github(String code) {
        HttpHeaders atkHeaders = new HttpHeaders();
        atkHeaders.setContentType(MediaType.APPLICATION_JSON);

        OauthGithubAccessTokenRequestDto oauthGithubAccessTokenRequestDto = new OauthGithubAccessTokenRequestDto(
                oauthProperties.getGithubClientId(), oauthProperties.getGithubClientSecret(), code);

        OauthGithubAccessTokenResponseDto oauthGithubAccessTokenResponseDto = restTemplate.exchange(
                GITHUB_ACCESS_TOKEN_URL,
                HttpMethod.POST,
                new HttpEntity<>(oauthGithubAccessTokenRequestDto, atkHeaders),
                OauthGithubAccessTokenResponseDto.class).getBody();

        if (Objects.isNull(oauthGithubAccessTokenResponseDto)
                || Objects.isNull(oauthGithubAccessTokenResponseDto.getAccessToken())) {
            throw new SocialAccessTokenNotExistsException();
        }

        HttpHeaders userHeaders = new HttpHeaders();
        userHeaders.setBearerAuth(oauthGithubAccessTokenResponseDto.getAccessToken());

        OauthGithubUserInfoResponseDto oauthGithubUserInfoResponseDto = restTemplate.exchange(
                GITHUB_USER_INFO_URL,
                HttpMethod.GET,
                new HttpEntity<>(userHeaders),
                OauthGithubUserInfoResponseDto.class).getBody();

        if (Objects.isNull(oauthGithubUserInfoResponseDto)
                || Objects.isNull(oauthGithubUserInfoResponseDto.getEmail())) {
            throw new SocialEmailNotFoundException();
        }

        AlingUser user = userReadRepository.findByEmail(oauthGithubUserInfoResponseDto.getEmail())
                .orElseThrow(SocialEmailNotFoundException::new);
        return new LoginResponseDto(user.getUserNo(), userReadRepository.findRolesByUserNo(user.getUserNo()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LoginResponseDto google(String code) {
        HttpHeaders atkHeaders = new HttpHeaders();
        atkHeaders.setContentType(MediaType.APPLICATION_JSON);

        OauthGoogleAccessTokenRequestDto oauthGoogleAccessTokenRequestDto = new OauthGoogleAccessTokenRequestDto(
                oauthProperties.getGoogleClientId(), oauthProperties.getGoogleClientSecret(), code,
                oauthProperties.getGoogleGrantType(), oauthProperties.getGoogleRedirectUri());

        OauthGoogleAccessTokenResponseDto oauthGoogleAccessTokenResponseDto = restTemplate.exchange(
                GOOGLE_ACCESS_TOKEN_URL,
                HttpMethod.POST,
                new HttpEntity<>(oauthGoogleAccessTokenRequestDto, atkHeaders),
                OauthGoogleAccessTokenResponseDto.class).getBody();

        if (Objects.isNull(oauthGoogleAccessTokenResponseDto)
                || Objects.isNull(oauthGoogleAccessTokenResponseDto.getIdToken())) {
            throw new SocialAccessTokenNotExistsException();
        }

        OauthGoogleUserInfoResponseDto oauthGoogleUserInfoResponseDto = restTemplate.exchange(
                GOOGLE_USER_INFO_URL + oauthGoogleAccessTokenResponseDto.getIdToken(),
                HttpMethod.GET,
                HttpEntity.EMPTY,
                OauthGoogleUserInfoResponseDto.class).getBody();

        if (Objects.isNull(oauthGoogleUserInfoResponseDto)
                || Objects.isNull(oauthGoogleUserInfoResponseDto.getEmail())) {
            throw new SocialEmailNotFoundException();
        }

        AlingUser user = userReadRepository.findByEmail(oauthGoogleUserInfoResponseDto.getEmail())
                .orElseThrow(SocialEmailNotFoundException::new);
        return new LoginResponseDto(user.getUserNo(), userReadRepository.findRolesByUserNo(user.getUserNo()));
    }
}
