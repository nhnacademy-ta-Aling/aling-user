package kr.aling.user.banduser.exception;

/**
 * 그룹 회원을 찾을 수 없을 때 발생 exception.
 *
 * @author : 정유진
 * @since : 1.0
 **/
public class BandUserNotFoundException extends RuntimeException {
    public static final String MESSAGE = "Band User not found!";

    public BandUserNotFoundException() {
        super(MESSAGE);
    }
}
