package kr.aling.user.user.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import kr.aling.user.user.dto.request.CreateUserRequestDto;
import kr.aling.user.user.dto.response.CreateUserResponseDto;
import kr.aling.user.user.dummy.UserDummy;
import kr.aling.user.user.entity.AlingUser;
import kr.aling.user.user.exception.UserEmailAlreadyUsedException;
import kr.aling.user.user.repository.UserManageRepository;
import kr.aling.user.user.repository.UserReadRepository;
import kr.aling.user.user.service.UserManageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

class AlingUserManageServiceImplTest {

    private final String TMP_PASSWORD = "########";

    private UserManageService userManageService;

    private PasswordEncoder passwordEncoder;

    private UserReadRepository userReadRepository;
    private UserManageRepository userManageRepository;

    @BeforeEach
    void setUp() {
        passwordEncoder = mock(PasswordEncoder.class);

        userReadRepository = mock(UserReadRepository.class);
        userManageRepository = mock(UserManageRepository.class);

        userManageService = new UserManageServiceImpl(
                passwordEncoder,
                userReadRepository,
                userManageRepository
        );
    }

    @Test
    @DisplayName("회원 등록 성공")
    void registerUser() {
        // given
        AlingUser alingUser = UserDummy.dummyEncoder(new BCryptPasswordEncoder());

        CreateUserRequestDto requestDto =
                new CreateUserRequestDto(alingUser.getEmail(), TMP_PASSWORD, alingUser.getName());

        when(userReadRepository.existsByEmail(any())).thenReturn(Boolean.FALSE);
        when(userManageRepository.save(any())).thenReturn(alingUser);

        // when
        CreateUserResponseDto responseDto = userManageService.registerUser(requestDto);

        // then
        assertThat(responseDto).isNotNull();

        verify(userReadRepository, times(1)).existsByEmail(any());
        verify(userManageRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("회원 등록 실패 - 이미 존재하는 ID(Email)인 경우")
    void registerUser_alreadyExistsEmail() {
        // given
        AlingUser alingUser = UserDummy.dummyEncoder(new BCryptPasswordEncoder());

        CreateUserRequestDto requestDto =
                new CreateUserRequestDto(alingUser.getEmail(), TMP_PASSWORD, alingUser.getName());

        when(userReadRepository.existsByEmail(any())).thenReturn(Boolean.TRUE);

        // when
        // then
        assertThatThrownBy(() -> userManageService.registerUser(requestDto))
                .isInstanceOf(UserEmailAlreadyUsedException.class);

        verify(userReadRepository, times(1)).existsByEmail(any());
        verify(userManageRepository, never()).save(any());
    }
}