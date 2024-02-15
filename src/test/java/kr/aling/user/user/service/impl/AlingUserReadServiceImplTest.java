package kr.aling.user.user.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import kr.aling.user.user.dto.response.LoginInfoResponseDto;
import kr.aling.user.user.dto.response.LoginResponseDto;
import kr.aling.user.user.dto.resquest.LoginRequestDto;
import kr.aling.user.user.exception.UserNotFoundException;
import kr.aling.user.user.repository.UserReadRepository;
import kr.aling.user.user.service.UserReadService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

class AlingUserReadServiceImplTest {

    private UserReadService userReadService;

    private UserReadRepository userReadRepository;

    PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        userReadRepository = mock(UserReadRepository.class);
        passwordEncoder = mock(BCryptPasswordEncoder.class);

        userReadService = new UserReadServiceImpl(userReadRepository, passwordEncoder);
    }

    @Test
    @DisplayName("존재하는 이메일인지 확인 성공 - 존재함")
    void isExistsEmail_found() {
        // given
        String email = "test@aling.kr";

        when(userReadRepository.existsByEmail(email)).thenReturn(true);

        // when
        boolean isExists = userReadService.isExistsEmail(email);

        // then
        assertThat(isExists).isTrue();
    }

    @Test
    @DisplayName("존재하는 이메일인지 확인 성공 - 존재하지 않음")
    void isExistsEmail_notFound() {
        // given
        String email = "test_null@aling.kr";

        when(userReadRepository.existsByEmail(email)).thenReturn(false);

        // when
        boolean isExists = userReadService.isExistsEmail(email);

        // then
        assertThat(isExists).isFalse();
    }

    @Test
    @DisplayName("존재하는 회원번호인지 확인 성공 - 존재함")
    void isExistsUserNo_found() {
        // given
        Long userNo = 1L;

        when(userReadRepository.existsById(userNo)).thenReturn(true);

        // when
        boolean isExists = userReadService.isExistsUserNo(userNo);

        // then
        assertThat(isExists).isTrue();
    }

    @Test
    @DisplayName("존재하는 회원번호인지 확인 성공 - 존재하지 않음")
    void isExistsUserNo_notFound() {
        // given
        Long userNo = 99L;

        when(userReadRepository.existsById(userNo)).thenReturn(false);

        // when
        boolean isExists = userReadService.isExistsUserNo(userNo);

        // then
        assertThat(isExists).isFalse();
    }

    @Test
    @DisplayName("회원 로그인 성공")
    void login() {
        //given
        LoginInfoResponseDto infoResponse = new LoginInfoResponseDto(1L, "password");
        LoginRequestDto request = new LoginRequestDto("test@test.com", "password");
        LoginResponseDto response = new LoginResponseDto(infoResponse.getUserNo(), List.of("ROLE_TEST"));

        when(passwordEncoder.matches(any(), any())).thenReturn(Boolean.TRUE);
        when(userReadRepository.findByEmailForLogin(any())).thenReturn(Optional.of(infoResponse));
        when(userReadRepository.findRolesByUserNo(any())).thenReturn(response.getRole());

        //when
        LoginResponseDto expect = userReadService.login(request);

        //then
        assertThat(expect.getUserNo()).isEqualTo(infoResponse.getUserNo());
        assertThat(expect.getRole()).isEqualTo(response.getRole());
    }

    @Test
    @DisplayName("회원 로그인 실패 - 없는 이메일")
    void login_fail_NotFoundEmail() {
        //given
        LoginRequestDto request = new LoginRequestDto("test@test.com", "password");
        when(userReadRepository.findByEmailForLogin(any())).thenReturn(Optional.empty());

        //when, then
        assertThatThrownBy(() -> userReadService.login(request)).isInstanceOf(UserNotFoundException.class);
    }

    @Test
    @DisplayName("회원 로그인 실패 - 비밀번호 불일치")
    void login_fail_password() {
        //given
        LoginInfoResponseDto infoResponse = new LoginInfoResponseDto(1L, "password");
        LoginRequestDto request = new LoginRequestDto("test@test.com", "password");

        when(passwordEncoder.matches(any(), any())).thenReturn(Boolean.FALSE);
        when(userReadRepository.findByEmailForLogin(any())).thenReturn(Optional.of(infoResponse));

        //when, then
        assertThatThrownBy(() -> userReadService.login(request)).isInstanceOf(UserNotFoundException.class);
    }
}