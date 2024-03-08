package kr.aling.user.banduser.exception;

/**
 * 그룹 회원이 이미 존재 할 때 발생 하는 exception.
 *
 * @author 정유진
 * @since 1.0
 **/
public class BandUserAlreadyExistsException extends RuntimeException {
    public static final String MESSAGE = "Band user already exists.";

    public BandUserAlreadyExistsException() {
        super(MESSAGE);
    }
}
