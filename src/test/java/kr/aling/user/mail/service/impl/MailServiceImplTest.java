package kr.aling.user.mail.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import kr.aling.user.mail.service.MailService;
import kr.aling.user.user.exception.UserEmailAlreadyUsedException;
import kr.aling.user.user.service.UserReadService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

class MailServiceImplTest {

    private MailService mailService;

    private UserReadService userReadService;

    private JavaMailSender javaMailSender;

    @BeforeEach
    void setUp() {
        userReadService = mock(UserReadService.class);

        javaMailSender = mock(JavaMailSender.class);

        mailService = new MailServiceImpl(
                userReadService,
                javaMailSender
        );
    }

    @Test
    @DisplayName("인증번호 송신 성공")
    void sendAuthNumber() {
        // given
        String email = "test@aling.kr";

        when(userReadService.existsEmail(any())).thenReturn(false);

        // when
        int authNumber = mailService.sendAuthNumber(email);

        // then
        assertThat(authNumber).isBetween(100_000, 999_999);

        verify(userReadService, times(1)).existsEmail(any());
        verify(javaMailSender, times(1)).send((SimpleMailMessage) any());
    }

    @Test
    @DisplayName("인증번호 송신 실패 - 이미 존재하는 이메일인 경우")
    void sendAuthNumber_alreadyExistsEmail() {
        // given
        String email = "test@aling.kr";

        when(userReadService.existsEmail(any())).thenReturn(true);

        // when
        // then
        assertThatThrownBy(() -> mailService.sendAuthNumber(email)).isInstanceOf(UserEmailAlreadyUsedException.class);

        verify(userReadService, times(1)).existsEmail(any());
        verify(javaMailSender, never()).send((SimpleMailMessage) any());
    }
}