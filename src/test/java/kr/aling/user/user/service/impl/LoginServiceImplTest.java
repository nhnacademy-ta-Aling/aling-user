package kr.aling.user.user.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import kr.aling.user.common.properties.OauthProperties;
import kr.aling.user.user.dto.request.LoginRequestDto;
import kr.aling.user.user.dto.response.LoginInfoResponseDto;
import kr.aling.user.user.dto.response.LoginResponseDto;
import kr.aling.user.user.dto.response.OauthGithubAccessTokenResponseDto;
import kr.aling.user.user.dto.response.OauthGithubUserInfoResponseDto;
import kr.aling.user.user.dummy.UserDummy;
import kr.aling.user.user.entity.AlingUser;
import kr.aling.user.user.exception.GithubAccessTokenNotExistsException;
import kr.aling.user.user.exception.SocialEmailNotFoundException;
import kr.aling.user.user.exception.UserNotFoundException;
import kr.aling.user.user.repository.UserReadRepository;
import kr.aling.user.user.service.LoginService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

class LoginServiceImplTest {

    private LoginService loginService;

    private UserReadRepository userReadRepository;
    private PasswordEncoder passwordEncoder;

    private RestTemplate restTemplate;
    private OauthProperties oauthProperties;

    @BeforeEach
    void setUp() {
        userReadRepository = mock(UserReadRepository.class);
        passwordEncoder = mock(PasswordEncoder.class);

        restTemplate = mock(RestTemplate.class);
        oauthProperties = mock(OauthProperties.class);

        loginService = new LoginServiceImpl(userReadRepository, passwordEncoder, restTemplate, oauthProperties);
    }

    @Test
    @DisplayName("회원 로그인 성공")
    void login() {
        // given
        LoginInfoResponseDto infoResponse = new LoginInfoResponseDto(1L, "password");
        LoginRequestDto request = new LoginRequestDto("test@test.com", "password");
        LoginResponseDto response = new LoginResponseDto(infoResponse.getUserNo(), List.of("ROLE_TEST"));

        when(passwordEncoder.matches(any(), any())).thenReturn(Boolean.TRUE);
        when(userReadRepository.findByEmailForLogin(any())).thenReturn(Optional.of(infoResponse));
        when(userReadRepository.findRolesByUserNo(any())).thenReturn(response.getRoles());

        // when
        LoginResponseDto expect = loginService.login(request);

        // then
        assertThat(expect.getUserNo()).isEqualTo(infoResponse.getUserNo());
        assertThat(expect.getRoles()).isEqualTo(response.getRoles());
    }

    @Test
    @DisplayName("회원 로그인 실패 - 없는 이메일")
    void login_fail_NotFoundEmail() {
        // given
        LoginRequestDto request = new LoginRequestDto("test@test.com", "password");
        when(userReadRepository.findByEmailForLogin(any())).thenReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> loginService.login(request)).isInstanceOf(UserNotFoundException.class);
    }

    @Test
    @DisplayName("회원 로그인 실패 - 비밀번호 불일치")
    void login_fail_password() {
        // given
        LoginInfoResponseDto infoResponse = new LoginInfoResponseDto(1L, "password");
        LoginRequestDto request = new LoginRequestDto("test@test.com", "password");

        when(passwordEncoder.matches(any(), any())).thenReturn(Boolean.FALSE);
        when(userReadRepository.findByEmailForLogin(any())).thenReturn(Optional.of(infoResponse));

        // when, then
        assertThatThrownBy(() -> loginService.login(request)).isInstanceOf(UserNotFoundException.class);
    }

    @Test
    @DisplayName("깃허브 로그인 성공")
    void github() {
        // given
        final String code = "ABC123!@#";
        final long userNo = 1L;
        final List<String> roles = List.of("ROLE_TEST");

        AlingUser user = UserDummy.dummy();
        ReflectionTestUtils.setField(user, "userNo", userNo);

        final String accessTokenUrl = "https://github.com/login/oauth/access_token";
        final String userUrl = "https://api.github.com/user";

        OauthGithubAccessTokenResponseDto oauthGithubAccessTokenResponseDto = new OauthGithubAccessTokenResponseDto();
        ReflectionTestUtils.setField(oauthGithubAccessTokenResponseDto, "accessToken", "ABC");

        ResponseEntity accessTokenResponseEntity = mock(ResponseEntity.class);
        when(restTemplate.exchange(eq(accessTokenUrl), eq(HttpMethod.POST), any(), any(Class.class)))
                .thenReturn(accessTokenResponseEntity);
        when(accessTokenResponseEntity.getBody()).thenReturn(oauthGithubAccessTokenResponseDto);

        OauthGithubUserInfoResponseDto oauthGithubUserInfoResponseDto = new OauthGithubUserInfoResponseDto();
        ReflectionTestUtils.setField(oauthGithubUserInfoResponseDto, "email", user.getEmail());

        ResponseEntity userResponseEntity = mock(ResponseEntity.class);
        when(restTemplate.exchange(eq(userUrl), eq(HttpMethod.GET), any(), any(Class.class)))
                .thenReturn(userResponseEntity);
        when(userResponseEntity.getBody()).thenReturn(oauthGithubUserInfoResponseDto);

        Optional<AlingUser> opsUser = Optional.of(user);
        when(userReadRepository.findByEmail(anyString())).thenReturn(opsUser);

        when(userReadRepository.findRolesByUserNo(anyLong())).thenReturn(roles);

        // when
        LoginResponseDto loginResponseDto = loginService.github(code);

        // then
        assertThat(loginResponseDto).isNotNull();
        assertThat(loginResponseDto.getUserNo()).isEqualTo(userNo);
        assertThat(loginResponseDto.getRoles()).isEqualTo(roles);
    }

    @Test
    @DisplayName("깃허브 로그인 실패 - 깃허브 AccessToken 발급에 실패한 경우")
    void github_fail_issueAccessToken() {
        // given
        final String code = "ABC123!@#";
        final String accessTokenUrl = "https://github.com/login/oauth/access_token";

        OauthGithubAccessTokenResponseDto oauthGithubAccessTokenResponseDto = new OauthGithubAccessTokenResponseDto();

        ResponseEntity accessTokenResponseEntity = mock(ResponseEntity.class);
        when(restTemplate.exchange(eq(accessTokenUrl), eq(HttpMethod.POST), any(), any(Class.class)))
                .thenReturn(accessTokenResponseEntity);
        when(accessTokenResponseEntity.getBody()).thenReturn(oauthGithubAccessTokenResponseDto);

        // when, then
        assertThatThrownBy(() -> loginService.github(code))
                .isInstanceOf(GithubAccessTokenNotExistsException.class);

        verify(restTemplate, times(1)).exchange(anyString(), any(HttpMethod.class), any(), any(Class.class));
        verify(userReadRepository, never()).findByEmail(anyString());
        verify(userReadRepository, never()).findRolesByUserNo(anyLong());
    }

    @Test
    @DisplayName("깃허브 로그인 실패 - 깃허브 유저 정보 얻기에 실패한 경우")
    void github_fail_getUserInfo() {
        // given
        final String code = "ABC123!@#";
        final long userNo = 1L;

        AlingUser user = UserDummy.dummy();
        ReflectionTestUtils.setField(user, "userNo", userNo);

        final String accessTokenUrl = "https://github.com/login/oauth/access_token";
        final String userUrl = "https://api.github.com/user";

        OauthGithubAccessTokenResponseDto oauthGithubAccessTokenResponseDto = new OauthGithubAccessTokenResponseDto();
        ReflectionTestUtils.setField(oauthGithubAccessTokenResponseDto, "accessToken", "ABC");

        ResponseEntity accessTokenResponseEntity = mock(ResponseEntity.class);
        when(restTemplate.exchange(eq(accessTokenUrl), eq(HttpMethod.POST), any(), any(Class.class)))
                .thenReturn(accessTokenResponseEntity);
        when(accessTokenResponseEntity.getBody()).thenReturn(oauthGithubAccessTokenResponseDto);

        OauthGithubUserInfoResponseDto oauthGithubUserInfoResponseDto = new OauthGithubUserInfoResponseDto();

        ResponseEntity userResponseEntity = mock(ResponseEntity.class);
        when(restTemplate.exchange(eq(userUrl), eq(HttpMethod.GET), any(), any(Class.class)))
                .thenReturn(userResponseEntity);
        when(userResponseEntity.getBody()).thenReturn(oauthGithubUserInfoResponseDto);

        // when, then
        assertThatThrownBy(() -> loginService.github(code))
                .isInstanceOf(SocialEmailNotFoundException.class);

        verify(restTemplate, times(2)).exchange(anyString(), any(HttpMethod.class), any(), any(Class.class));
        verify(userReadRepository, never()).findByEmail(anyString());
        verify(userReadRepository, never()).findRolesByUserNo(anyLong());
    }

    @Test
    @DisplayName("깃허브 로그인 실패 - 얻어온 이메일이 DB상에 존재하지 않는 경우")
    void github_fail_emailNotFound() {
        // given
        final String code = "ABC123!@#";
        final long userNo = 1L;

        AlingUser user = UserDummy.dummy();
        ReflectionTestUtils.setField(user, "userNo", userNo);

        final String accessTokenUrl = "https://github.com/login/oauth/access_token";
        final String userUrl = "https://api.github.com/user";

        OauthGithubAccessTokenResponseDto oauthGithubAccessTokenResponseDto = new OauthGithubAccessTokenResponseDto();
        ReflectionTestUtils.setField(oauthGithubAccessTokenResponseDto, "accessToken", "ABC");

        ResponseEntity accessTokenResponseEntity = mock(ResponseEntity.class);
        when(restTemplate.exchange(eq(accessTokenUrl), eq(HttpMethod.POST), any(), any(Class.class)))
                .thenReturn(accessTokenResponseEntity);
        when(accessTokenResponseEntity.getBody()).thenReturn(oauthGithubAccessTokenResponseDto);

        OauthGithubUserInfoResponseDto oauthGithubUserInfoResponseDto = new OauthGithubUserInfoResponseDto();
        ReflectionTestUtils.setField(oauthGithubUserInfoResponseDto, "email", user.getEmail());

        ResponseEntity userResponseEntity = mock(ResponseEntity.class);
        when(restTemplate.exchange(eq(userUrl), eq(HttpMethod.GET), any(), any(Class.class)))
                .thenReturn(userResponseEntity);
        when(userResponseEntity.getBody()).thenReturn(oauthGithubUserInfoResponseDto);

        // when, then
        assertThatThrownBy(() -> loginService.github(code))
                .isInstanceOf(SocialEmailNotFoundException.class);

        verify(restTemplate, times(2)).exchange(anyString(), any(HttpMethod.class), any(), any(Class.class));
        verify(userReadRepository, times(1)).findByEmail(anyString());
        verify(userReadRepository, never()).findRolesByUserNo(anyLong());
    }
}