package kr.aling.user.mail.service;

/**
 * 이메일 인증을 위한 Service interface.
 *
 * @author : 이수정
 * @since : 1.0
 */
public interface MailService {

    /**
     * 인증번호를 보내고 생성된 인증번호를 반환합니다.
     *
     * @param email 인증번호를 보낼 이메일
     * @return 생성된 인증번호
     * @author : 이수정
     * @since : 1.0
     */
    int sendAuthNumber(String email);
}
