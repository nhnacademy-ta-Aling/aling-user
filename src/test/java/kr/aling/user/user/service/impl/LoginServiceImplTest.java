package kr.aling.user.user.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import kr.aling.user.user.dto.request.LoginRequestDto;
import kr.aling.user.user.dto.response.LoginInfoResponseDto;
import kr.aling.user.user.dto.response.LoginResponseDto;
import kr.aling.user.user.exception.UserNotFoundException;
import kr.aling.user.user.repository.UserReadRepository;
import kr.aling.user.user.service.LoginService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

class LoginServiceImplTest {

    private LoginService loginService;

    private UserReadRepository userReadRepository;

    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        userReadRepository = mock(UserReadRepository.class);
        passwordEncoder = mock(PasswordEncoder.class);

        loginService = new LoginServiceImpl(userReadRepository, passwordEncoder);
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
}