package kr.aling.user.mail.exception;

/**
 * 유효하지 않은 인증번호인 경우 발생하는 Exception.
 *
 * @author : 이수정
 * @since : 1.0
 */
public class MailAuthNumberInvalidException extends RuntimeException {

    public MailAuthNumberInvalidException(String authNumber) {
        super("입력된 값이 유효하지 않습니다. - " + authNumber);
    }
}
