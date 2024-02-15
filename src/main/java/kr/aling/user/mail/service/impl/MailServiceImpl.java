package kr.aling.user.mail.service.impl;

import java.util.Objects;
import java.util.concurrent.TimeUnit;
import kr.aling.user.mail.dto.request.CheckAuthNumberMailRequestDto;
import kr.aling.user.mail.exception.MailAuthNumberInvalidException;
import kr.aling.user.mail.service.MailService;
import kr.aling.user.user.exception.UserEmailAlreadyUsedException;
import kr.aling.user.user.service.UserReadService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * 이메일 인증을 위한 Service 구현체.
 *
 * @author : 이수정
 * @since : 1.0
 */
@RequiredArgsConstructor
@Service
public class MailServiceImpl implements MailService {

    private static final String SUBJECT = "Aling 이메일 인증번호";

    private final UserReadService userReadService;

    private final JavaMailSender javaMailSender;
    private final RedisTemplate<String, Object> redisTemplate;

    /**
     * {@inheritDoc}
     */
    @Override
    public void sendAuthNumber(String email) {
        if (userReadService.isExistsEmail(email)) {
            throw new UserEmailAlreadyUsedException(email);
        }

        String authNumber = Integer.toString((int) (Math.random() * (999_999 - 100_000 + 1)) + 100_000);

        redisTemplate.opsForValue().set(email, authNumber, 3, TimeUnit.MINUTES);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject(SUBJECT);
        message.setText(authNumber);

        javaMailSender.send(message);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void checkAuthNumber(CheckAuthNumberMailRequestDto requestDto) {
        String authNumber = Objects.requireNonNull(redisTemplate.opsForValue().get(requestDto.getEmail())).toString();
        if (!authNumber.equals(requestDto.getAuthNumber())) {
            throw new MailAuthNumberInvalidException(requestDto.getAuthNumber());
        }
    }
}
