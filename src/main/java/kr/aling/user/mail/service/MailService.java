package kr.aling.user.mail.service;

import kr.aling.user.mail.dto.request.CheckAuthNumberMailRequestDto;

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
     * @author : 이수정
     * @since : 1.0
     */
    void sendAuthNumber(String email);

    /**
     * 인증번호를 받아 유효한지 검증합니다.
     *
     * @param requestDto 검증할 인증번호
     * @author : 이수정
     * @since : 1.0
     */
    void checkAuthNumber(CheckAuthNumberMailRequestDto requestDto);
}
