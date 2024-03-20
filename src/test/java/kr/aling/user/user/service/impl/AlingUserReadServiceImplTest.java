package kr.aling.user.user.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;
import kr.aling.user.user.dummy.UserDummy;
import kr.aling.user.user.entity.AlingUser;
import kr.aling.user.user.exception.UserNotFoundException;
import kr.aling.user.user.repository.UserReadRepository;
import kr.aling.user.user.service.UserReadService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

class AlingUserReadServiceImplTest {

    private UserReadService userReadService;

    private UserReadRepository userReadRepository;

    @BeforeEach
    void setUp() {
        userReadRepository = mock(UserReadRepository.class);

        userReadService = new UserReadServiceImpl(userReadRepository);
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
    @DisplayName("회원 조회 성공")
    void getAlingUserByUserNo() {
        // given
        Long userNo = 1L;
        AlingUser dummy = UserDummy.dummy();

        ReflectionTestUtils.setField(dummy, "userNo", userNo);

        when(userReadRepository.findById(anyLong())).thenReturn(Optional.of(dummy));

        // when
        AlingUser alingUser = userReadService.getAlingUserByUserNo(userNo);

        // then
        assertThat(alingUser).isNotNull();
        assertThat(alingUser.getUserNo()).isEqualTo(userNo);
        assertThat(alingUser.getEmail()).isEqualTo(dummy.getEmail());
        assertThat(alingUser.getPassword()).isEqualTo(dummy.getPassword());
        assertThat(alingUser.getName()).isEqualTo(dummy.getName());
    }

    @Test
    @DisplayName("회원 조회 실패 - 존재하지 않음")
    void getAlingUserByUserNo_notFound() {
        // given
        Long userNo = 99L;

        when(userReadRepository.findById(userNo)).thenReturn(Optional.empty());

        // when
        assertThatThrownBy(() -> userReadService.getAlingUserByUserNo(userNo))
                .isInstanceOf(UserNotFoundException.class);
    }
}