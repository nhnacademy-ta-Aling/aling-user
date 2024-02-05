package kr.aling.user.mail.service.impl;

import kr.aling.user.mail.service.MailService;
import kr.aling.user.user.exception.UserEmailAlreadyUsedException;
import kr.aling.user.user.service.UserReadService;
import lombok.RequiredArgsConstructor;
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

    /**
     * {@inheritDoc}
     */
    @Override
    public int sendAuthNumber(String email) {
        if (userReadService.isExistsEmail(email)) {
            throw new UserEmailAlreadyUsedException(email);
        }

        int authNumber = (int) (Math.random() * (999_999 - 100_000 + 1)) + 100_000;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject(SUBJECT);
        message.setText(Integer.toString(authNumber));

        javaMailSender.send(message);

        return authNumber;
    }
}
