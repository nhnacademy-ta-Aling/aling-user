package kr.aling.user.mail.service.impl;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.concurrent.TimeUnit;
import kr.aling.user.mail.dto.request.CheckAuthNumberMailRequestDto;
import kr.aling.user.mail.exception.MailAuthNumberInvalidException;
import kr.aling.user.mail.service.MailService;
import kr.aling.user.user.exception.UserEmailAlreadyUsedException;
import kr.aling.user.user.service.UserReadService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

class MailServiceImplTest {

    private MailService mailService;

    private UserReadService userReadService;

    private JavaMailSender javaMailSender;
    private RedisTemplate<String, Object> redisTemplate;

    @BeforeEach
    void setUp() {
        userReadService = mock(UserReadService.class);

        javaMailSender = mock(JavaMailSender.class);
        redisTemplate = mock(RedisTemplate.class);

        mailService = new MailServiceImpl(
                userReadService,
                javaMailSender,
                redisTemplate
        );
    }

    @Test
    @DisplayName("인증번호 송신 성공")
    void sendAuthNumber() {
        // given
        String email = "test@aling.kr";
        ValueOperations<String, Object> valueOps = mock(ValueOperations.class);

        when(userReadService.isExistsEmail(any())).thenReturn(false);
        when(redisTemplate.opsForValue()).thenReturn(valueOps);
        doNothing().when(valueOps).set(anyString(), anyString(), anyLong(), any(TimeUnit.class));

        // when
        mailService.sendAuthNumber(email);

        // then
        verify(userReadService, times(1)).isExistsEmail(any());
        verify(redisTemplate, times(1)).opsForValue();
        verify(valueOps, times(1)).set(anyString(), anyString(), anyLong(), any(TimeUnit.class));
        verify(javaMailSender, times(1)).send((SimpleMailMessage) any());
    }

    @Test
    @DisplayName("인증번호 송신 실패 - 이미 존재하는 이메일인 경우")
    void sendAuthNumber_alreadyExistsEmail() {
        // given
        String email = "test@aling.kr";

        when(userReadService.isExistsEmail(any())).thenReturn(true);

        // when
        // then
        assertThatThrownBy(() -> mailService.sendAuthNumber(email)).isInstanceOf(UserEmailAlreadyUsedException.class);

        verify(userReadService, times(1)).isExistsEmail(any());
        verify(javaMailSender, never()).send((SimpleMailMessage) any());
    }

    @Test
    @DisplayName("인증번호 검증 성공")
    void checkAuthNumber() {
        // given
        CheckAuthNumberMailRequestDto requestDto = new CheckAuthNumberMailRequestDto("test@aling.kr", "999999");
        ValueOperations<String, Object> valueOps = mock(ValueOperations.class);

        when(redisTemplate.opsForValue()).thenReturn(valueOps);
        when(valueOps.get(anyString())).thenReturn("999999");

        // when
        mailService.checkAuthNumber(requestDto);

        // then
        verify(redisTemplate, times(1)).opsForValue();
        verify(valueOps, times(1)).get(requestDto.getEmail());
    }

    @Test
    @DisplayName("인증번호 검증 실패")
    void checkAuthNumber_invalidMailAuthNumber() {
        // given
        CheckAuthNumberMailRequestDto requestDto = new CheckAuthNumberMailRequestDto("test@aling.kr", "999999");
        ValueOperations<String, Object> valueOps = mock(ValueOperations.class);

        when(redisTemplate.opsForValue()).thenReturn(valueOps);
        when(valueOps.get(anyString())).thenReturn("000000");

        // when
        // then
        assertThatThrownBy(() -> mailService.checkAuthNumber(requestDto)).isInstanceOf(MailAuthNumberInvalidException.class);

        verify(redisTemplate, times(1)).opsForValue();
        verify(valueOps, times(1)).get(requestDto.getEmail());
    }
}