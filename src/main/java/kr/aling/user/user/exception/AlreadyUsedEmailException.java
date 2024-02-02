package kr.aling.user.user.exception;

/**
 * 회원가입시 이메일이 중복일 때 발생하는 예외.
 *
 * @author : 여운석
 * @since : 1.0
 **/
public class AlreadyUsedEmailException extends RuntimeException {
    private static final String MESSAGE = "Already Used Email! email: ";
    public AlreadyUsedEmailException(String email) {
        super(MESSAGE + email);
    }
}
