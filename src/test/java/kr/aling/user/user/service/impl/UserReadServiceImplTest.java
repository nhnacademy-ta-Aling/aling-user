package kr.aling.user.user.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import kr.aling.user.user.repository.UserReadRepository;
import kr.aling.user.user.service.UserReadService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UserReadServiceImplTest {

    private UserReadService userReadService;

    private UserReadRepository userReadRepository;

    @BeforeEach
    void setUp() {
        userReadRepository = mock(UserReadRepository.class);

        userReadService = new UserReadServiceImpl(userReadRepository);
    }

    @Test
    @DisplayName("존재하는 이메일인지 확인 성공 - 존재함")
    void existsEmail_found() {
        // given
        String email = "test@aling.kr";

        when(userReadRepository.existsByEmail(email)).thenReturn(true);

        // when
        boolean isExists = userReadService.existsEmail(email);

        // then
        assertThat(isExists).isTrue();
    }

    @Test
    @DisplayName("존재하는 이메일인지 확인 성공 - 존재하지 않음")
    void existsEmail_notFound() {
        // given
        String email = "test@aling.kr";

        when(userReadRepository.existsByEmail(email)).thenReturn(false);

        // when
        boolean isExists = userReadService.existsEmail(email);

        // then
        assertThat(isExists).isFalse();
    }
}